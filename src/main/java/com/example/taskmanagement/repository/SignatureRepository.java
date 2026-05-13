package com.example.taskmanagement.repository;

import com.example.taskmanagement.model.Signature;
import com.example.taskmanagement.model.SignatureStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SignatureRepository extends JpaRepository<Signature, UUID> {
    List<Signature> findByStatus(SignatureStatus status);
    List<Signature> findByUpdatedAtAfterAndStatusIn(Instant since, Collection<SignatureStatus> statuses);
    Optional<Signature> findByIdAndStatus(UUID id, SignatureStatus status);
}