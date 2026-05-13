package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.*;
import com.example.taskmanagement.service.SignatureService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/signatures")
public class SignatureController {

    private final SignatureService signatureService;

    public SignatureController(SignatureService signatureService) {
        this.signatureService = signatureService;
    }

    // 6.1 Получение всей базы (USER и ADMIN)
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<SignatureResponse> getAll() {
        return signatureService.getAllSignatures();
    }

    // 6.2 Инкремент
    @GetMapping("/increment")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<SignatureResponse> getIncrement(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant since) {
        return signatureService.getIncrement(since);
    }

    // 6.3 Получение по списку UUID
    @PostMapping("/list")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<SignatureResponse> getByIds(@RequestBody @Valid IdsRequest idsRequest) {
        return signatureService.getSignaturesByIds(idsRequest.getIds());
    }

    // 6.4 Создание (только ADMIN)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public SignatureResponse create(@RequestBody @Valid SignatureRequest request) {
        return signatureService.createSignature(request);
    }

    // 6.5 Обновление (только ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public SignatureResponse update(@PathVariable UUID id, @RequestBody @Valid SignatureRequest request) {
        return signatureService.updateSignature(id, request);
    }

    // 6.6 Удаление (только ADMIN)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable UUID id) {
        signatureService.deleteSignature(id);
    }

    // 6.7 История (только ADMIN)
    @GetMapping("/{id}/history")
    @PreAuthorize("hasRole('ADMIN')")
    public List<SignatureHistoryResponse> getHistory(@PathVariable UUID id) {
        return signatureService.getHistory(id);
    }

    // 6.8 Аудит (только ADMIN)
    @GetMapping("/{id}/audit")
    @PreAuthorize("hasRole('ADMIN')")
    public List<SignatureAuditResponse> getAudit(@PathVariable UUID id) {
        return signatureService.getAudit(id);
    }
}