package com.example.taskmanagement.repository;

import com.example.taskmanagement.model.SignatureAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SignatureAuditRepository extends JpaRepository<SignatureAudit, Long> {
    List<SignatureAudit> findBySignatureIdOrderByChangedAtDesc(UUID signatureId);
}