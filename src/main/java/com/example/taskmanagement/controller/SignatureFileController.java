package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.IdsRequest;
import com.example.taskmanagement.dto.SignatureResponse;
import com.example.taskmanagement.model.Signature;
import com.example.taskmanagement.repository.SignatureRepository;
import com.example.taskmanagement.service.MinioService;
import com.example.taskmanagement.service.SignatureFileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/signatures/files")
public class SignatureFileController {

    private final SignatureFileService signatureFileService;
    private final SignatureRepository signatureRepository;
    private final MinioService minioService;

    public SignatureFileController(SignatureFileService signatureFileService,
                                   SignatureRepository signatureRepository,
                                   MinioService minioService) {
        this.signatureFileService = signatureFileService;
        this.signatureRepository = signatureRepository;
        this.minioService = minioService;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SignatureResponse> uploadSignature(@RequestParam("file") MultipartFile file) throws Exception {
        SignatureResponse response = signatureFileService.uploadAndSaveSignature(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/pre-signed-urls")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<UUID, String>> getPreSignedUrls(@RequestBody @Valid IdsRequest idsRequest) throws Exception {
        Map<UUID, String> result = new HashMap<>();
        List<Signature> signatures = signatureRepository.findAllById(idsRequest.getIds());
        for (Signature sig : signatures) {
            if (sig.getMinioObjectName() != null && !sig.getMinioObjectName().isEmpty()) {
                String url = minioService.generatePresignedUrl(sig.getMinioObjectName(), 5); // 5 минут
                result.put(sig.getId(), url);
            } else {
                result.put(sig.getId(), null);
            }
        }
        return ResponseEntity.ok(result);
    }
}