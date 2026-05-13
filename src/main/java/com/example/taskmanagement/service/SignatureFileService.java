package com.example.taskmanagement.service;

import com.example.taskmanagement.dto.SignatureRequest;
import com.example.taskmanagement.dto.SignatureResponse;
import com.example.taskmanagement.model.Signature;
import com.example.taskmanagement.model.SignatureStatus;
import com.example.taskmanagement.repository.SignatureRepository;
import com.example.taskmanagement.signature.SignatureCanonicalizer;
import com.example.taskmanagement.signature.SigningService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

@Service
public class SignatureFileService {

    private final SignatureRepository signatureRepository;
    private final MinioService minioService;
    private final SigningService signingService;

    public SignatureFileService(SignatureRepository signatureRepository,
                                MinioService minioService,
                                SigningService signingService) {
        this.signatureRepository = signatureRepository;
        this.minioService = minioService;
        this.signingService = signingService;
    }

    @Transactional
    public SignatureResponse uploadAndSaveSignature(MultipartFile file) throws Exception {
        byte[] fileData = file.getBytes();
        String originalFilename = file.getOriginalFilename();

        // 1. Вычисляем сигнатуру из файла
        SignatureRequest request = computeSignatureFromFile(fileData, originalFilename);

        // 2. Создаём новую сигнатуру в БД
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

        // подпись сигнатуры
        byte[] canonical = SignatureCanonicalizer.canonicalize(request, SignatureStatus.ACTUAL);
        String digitalSignature = signingService.sign(canonical);
        signature.setDigitalSignatureBase64(digitalSignature);

        // 3. Сохраняем файл в MinIO
        String objectName = "signatures/" + signature.getId().toString() + ".bin";
        try (InputStream is = file.getInputStream()) {
            minioService.uploadFile(objectName, is, file.getSize(), file.getContentType());
        }
        signature.setMinioObjectName(objectName);

        Signature saved = signatureRepository.save(signature);
        return toResponse(saved);
    }

    private SignatureRequest computeSignatureFromFile(byte[] fileData, String originalFilename) throws Exception {
        long fileSize = fileData.length;
        int firstBytesLen = Math.min(8, (int) fileSize);
        byte[] firstBytes = Arrays.copyOf(fileData, firstBytesLen);
        String firstBytesHex = bytesToHex(firstBytes);

        long remainderLength = fileSize - firstBytesLen;
        byte[] remainder = Arrays.copyOfRange(fileData, firstBytesLen, (int) fileSize);
        String remainderHashHex;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        if (remainderLength > 0) {
            byte[] hash = digest.digest(remainder);
            remainderHashHex = bytesToHex(hash);
        } else {
            // хеш пустого массива
            digest.update(new byte[0]);
            remainderHashHex = bytesToHex(digest.digest());
        }

        String fileType = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) fileType = originalFilename.substring(dotIndex + 1).toLowerCase();

        SignatureRequest request = new SignatureRequest();
        request.setThreatName(originalFilename);
        request.setFirstBytesHex(firstBytesHex);
        request.setRemainderHashHex(remainderHashHex);
        request.setRemainderLength(remainderLength);
        request.setFileType(fileType);
        request.setOffsetStart(0L);
        request.setOffsetEnd(fileSize - 1);
        return request;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

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
}