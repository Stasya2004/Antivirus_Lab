package com.example.taskmanagement.repository;

import com.example.taskmanagement.model.SignatureHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SignatureHistoryRepository extends JpaRepository<SignatureHistory, Long> {
    List<SignatureHistory> findBySignatureIdOrderByVersionCreatedAtDesc(UUID signatureId);
}