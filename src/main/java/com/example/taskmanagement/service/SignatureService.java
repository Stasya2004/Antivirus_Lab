package com.example.taskmanagement.service;

import com.example.taskmanagement.dto.SignatureRequest;
import com.example.taskmanagement.dto.SignatureResponse;
import com.example.taskmanagement.dto.SignatureHistoryResponse;
import com.example.taskmanagement.dto.SignatureAuditResponse;
import com.example.taskmanagement.model.*;
import com.example.taskmanagement.repository.*;
import com.example.taskmanagement.signature.SignatureCanonicalizer;
import com.example.taskmanagement.signature.SigningService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.PrivateKey;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SignatureService {

    private final SignatureRepository signatureRepository;
    private final SignatureHistoryRepository historyRepository;
    private final SignatureAuditRepository auditRepository;
    private final SigningService signingService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SignatureService(SignatureRepository signatureRepository,
                            SignatureHistoryRepository historyRepository,
                            SignatureAuditRepository auditRepository,
                            SigningService signingService) {
        this.signatureRepository = signatureRepository;
        this.historyRepository = historyRepository;
        this.auditRepository = auditRepository;
        this.signingService = signingService;
    }



    // 6.1 Получение всей базы (только ACTUAL)
    @Transactional(readOnly = true)
    public List<SignatureResponse> getAllSignatures() {
        return signatureRepository.findByStatus(SignatureStatus.ACTUAL)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // 6.2 Инкремент (updatedAt > since, включая DELETED)
    @Transactional(readOnly = true)
    public List<SignatureResponse> getIncrement(Instant since) {
        List<SignatureStatus> statuses = List.of(SignatureStatus.ACTUAL, SignatureStatus.DELETED);
        return signatureRepository.findByUpdatedAtAfterAndStatusIn(since, statuses)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // 6.3 Получение по списку UUID
    @Transactional(readOnly = true)
    public List<SignatureResponse> getSignaturesByIds(List<UUID> ids) {
        return signatureRepository.findAllById(ids)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // 6.4 Создание
    @Transactional
    public SignatureResponse createSignature(SignatureRequest request) {
        validateRequest(request); // кастомная валидация cross-field

        Signature signature = new Signature();
        signature.setId(UUID.randomUUID());
        signature.setThreatName(request.getThreatName());
        signature.setFirstBytesHex(request.getFirstBytesHex());
        signature.setRemainderHashHex(request.getRemainderHashHex());
        signature.setRemainderLength(request.getRemainderLength());
        signature.setFileType(request.getFileType());
        signature.setOffsetStart(request.getOffsetStart());
        signature.setOffsetEnd(request.getOffsetEnd());
        signature.setStatus(SignatureStatus.ACTUAL);
        Instant now = Instant.now();
        signature.setUpdatedAt(now);

        // подпись
        try {
            byte[] canonical = SignatureCanonicalizer.canonicalize(request, SignatureStatus.ACTUAL);
            String digitalSignature = signingService.sign(canonical);
            signature.setDigitalSignatureBase64(digitalSignature);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate digital signature", e);
        }

        Signature saved = signatureRepository.save(signature);

        // аудит
        createAudit(saved.getId(), getCurrentUsername(), now, null, "Создание сигнатуры");

        return toResponse(saved);
    }

    // 6.5 Обновление
    @Transactional
    public SignatureResponse updateSignature(UUID id, SignatureRequest request) {
        Signature existing = signatureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Signature not found"));
        if (existing.getStatus() == SignatureStatus.DELETED) {
            throw new RuntimeException("Cannot update deleted signature");
        }

        // Сохраняем текущую версию в историю
        saveToHistory(existing);

        // Определяем изменённые поля
        List<String> changedFields = detectChanges(existing, request);

        // Обновляем поля
        existing.setThreatName(request.getThreatName());
        existing.setFirstBytesHex(request.getFirstBytesHex());
        existing.setRemainderHashHex(request.getRemainderHashHex());
        existing.setRemainderLength(request.getRemainderLength());
        existing.setFileType(request.getFileType());
        existing.setOffsetStart(request.getOffsetStart());
        existing.setOffsetEnd(request.getOffsetEnd());
        // статус остаётся ACTUAL
        Instant now = Instant.now();
        existing.setUpdatedAt(now);

        // Пересчёт подписи
        try {
            byte[] canonical = SignatureCanonicalizer.canonicalize(request, SignatureStatus.ACTUAL);
            String newSignature = signingService.sign(canonical);
            existing.setDigitalSignatureBase64(newSignature);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate digital signature", e);
        }

        Signature updated = signatureRepository.save(existing);

        // Аудит с fieldsChanged
        String fieldsChangedJson = null;
        if (!changedFields.isEmpty()) {
            try {
                Map<String, List<String>> map = Map.of("changed", changedFields);
                fieldsChangedJson = objectMapper.writeValueAsString(map);
            } catch (JsonProcessingException e) {
                fieldsChangedJson = "{\"changed\": []}";
            }
        }
        createAudit(updated.getId(), getCurrentUsername(), now, fieldsChangedJson, "Обновление сигнатуры");

        return toResponse(updated);
    }

    // 6.6 Логическое удаление
    @Transactional
    public void deleteSignature(UUID id) {
        Signature existing = signatureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Signature not found"));
        if (existing.getStatus() == SignatureStatus.DELETED) {
            throw new RuntimeException("Signature already deleted");
        }

        // Сохраняем в историю
        saveToHistory(existing);

        // Меняем статус
        existing.setStatus(SignatureStatus.DELETED);
        Instant now = Instant.now();
        existing.setUpdatedAt(now);

        // Пересчёт подписи с новым статусом
        try {
            byte[] canonical = SignatureCanonicalizer.canonicalize(existing);
            String newSignature = signingService.sign(canonical);
            existing.setDigitalSignatureBase64(newSignature);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate digital signature", e);
        }

        signatureRepository.save(existing);

        // Аудит
        createAudit(existing.getId(), getCurrentUsername(), now, null, "Логическое удаление сигнатуры");
    }

    // 6.7 История
    @Transactional(readOnly = true)
    public List<SignatureHistoryResponse> getHistory(UUID signatureId) {
        return historyRepository.findBySignatureIdOrderByVersionCreatedAtDesc(signatureId)
                .stream()
                .map(this::toHistoryResponse)
                .collect(Collectors.toList());
    }

    // 6.8 Аудит
    @Transactional(readOnly = true)
    public List<SignatureAuditResponse> getAudit(UUID signatureId) {
        return auditRepository.findBySignatureIdOrderByChangedAtDesc(signatureId)
                .stream()
                .map(this::toAuditResponse)
                .collect(Collectors.toList());
    }

    // ==================== Приватные вспомогательные методы ====================

    private void validateRequest(SignatureRequest request) {
        // Дополнительные проверки, если нужно
        if (request.getOffsetEnd() < request.getOffsetStart()) {
            throw new IllegalArgumentException("offsetEnd must be >= offsetStart");
        }
        // Длина firstBytesHex должна быть чётной (поскольку hex)
        if (request.getFirstBytesHex().length() % 2 != 0) {
            throw new IllegalArgumentException("firstBytesHex must have even length");
        }
    }

    private void saveToHistory(Signature signature) {
        SignatureHistory history = new SignatureHistory();
        history.setSignatureId(signature.getId());
        history.setVersionCreatedAt(Instant.now());
        history.setThreatName(signature.getThreatName());
        history.setFirstBytesHex(signature.getFirstBytesHex());
        history.setRemainderHashHex(signature.getRemainderHashHex());
        history.setRemainderLength(signature.getRemainderLength());
        history.setFileType(signature.getFileType());
        history.setOffsetStart(signature.getOffsetStart());
        history.setOffsetEnd(signature.getOffsetEnd());
        history.setUpdatedAt(signature.getUpdatedAt());
        history.setStatus(signature.getStatus());
        history.setDigitalSignatureBase64(signature.getDigitalSignatureBase64());
        historyRepository.save(history);
    }

    private void createAudit(UUID signatureId, String changedBy, Instant changedAt, String fieldsChanged, String description) {
        SignatureAudit audit = new SignatureAudit();
        audit.setSignatureId(signatureId);
        audit.setChangedBy(changedBy);
        audit.setChangedAt(changedAt);
        audit.setFieldsChanged(fieldsChanged);
        audit.setDescription(description);
        auditRepository.save(audit);
    }

    private List<String> detectChanges(Signature original, SignatureRequest request) {
        List<String> changes = new ArrayList<>();
        if (!original.getThreatName().equals(request.getThreatName())) changes.add("threatName");
        if (!original.getFirstBytesHex().equals(request.getFirstBytesHex())) changes.add("firstBytesHex");
        if (!original.getRemainderHashHex().equals(request.getRemainderHashHex())) changes.add("remainderHashHex");
        if (!original.getRemainderLength().equals(request.getRemainderLength())) changes.add("remainderLength");
        if (!original.getFileType().equals(request.getFileType())) changes.add("fileType");
        if (!original.getOffsetStart().equals(request.getOffsetStart())) changes.add("offsetStart");
        if (!original.getOffsetEnd().equals(request.getOffsetEnd())) changes.add("offsetEnd");
        return changes;
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "system";
    }

    // Маппинг в DTO
    private SignatureResponse toResponse(Signature signature) {
        SignatureResponse resp = new SignatureResponse();
        resp.setId(signature.getId());
        resp.setThreatName(signature.getThreatName());
        resp.setFirstBytesHex(signature.getFirstBytesHex());
        resp.setRemainderHashHex(signature.getRemainderHashHex());
        resp.setRemainderLength(signature.getRemainderLength());
        resp.setFileType(signature.getFileType());
        resp.setOffsetStart(signature.getOffsetStart());
        resp.setOffsetEnd(signature.getOffsetEnd());
        resp.setUpdatedAt(signature.getUpdatedAt());
        resp.setStatus(signature.getStatus().name());
        return resp;
    }

    private SignatureHistoryResponse toHistoryResponse(SignatureHistory history) {
        SignatureHistoryResponse resp = new SignatureHistoryResponse();
        resp.setHistoryId(history.getHistoryId());
        resp.setSignatureId(history.getSignatureId());
        resp.setVersionCreatedAt(history.getVersionCreatedAt());
        resp.setThreatName(history.getThreatName());
        resp.setFirstBytesHex(history.getFirstBytesHex());
        resp.setRemainderHashHex(history.getRemainderHashHex());
        resp.setRemainderLength(history.getRemainderLength());
        resp.setFileType(history.getFileType());
        resp.setOffsetStart(history.getOffsetStart());
        resp.setOffsetEnd(history.getOffsetEnd());
        resp.setUpdatedAt(history.getUpdatedAt());
        resp.setStatus(history.getStatus().name());
        return resp;
    }

    private SignatureAuditResponse toAuditResponse(SignatureAudit audit) {
        SignatureAuditResponse resp = new SignatureAuditResponse();
        resp.setAuditId(audit.getAuditId());
        resp.setSignatureId(audit.getSignatureId());
        resp.setChangedBy(audit.getChangedBy());
        resp.setChangedAt(audit.getChangedAt());
        resp.setFieldsChanged(audit.getFieldsChanged());
        resp.setDescription(audit.getDescription());
        return resp;
    }
}