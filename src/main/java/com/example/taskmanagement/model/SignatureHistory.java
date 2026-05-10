package com.example.taskmanagement.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "signatures_history")
public class SignatureHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @Column(name = "signature_id", nullable = false)
    private UUID signatureId;

    @Column(name = "version_created_at", nullable = false)
    private Instant versionCreatedAt;

    @Column(name = "threat_name", nullable = false)
    private String threatName;

    @Column(name = "first_bytes_hex", nullable = false, length = 500)
    private String firstBytesHex;

    @Column(name = "remainder_hash_hex", nullable = false, length = 64)
    private String remainderHashHex;

    @Column(name = "remainder_length", nullable = false)
    private Long remainderLength;

    @Column(name = "file_type", nullable = false, length = 100)
    private String fileType;

    @Column(name = "offset_start", nullable = false)
    private Long offsetStart;

    @Column(name = "offset_end", nullable = false)
    private Long offsetEnd;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SignatureStatus status;

    @Column(name = "digital_signature_base64", nullable = false, columnDefinition = "TEXT")
    private String digitalSignatureBase64;

    // Конструкторы, геттеры, сеттеры (аналогично Signature)
    public SignatureHistory() {}

    // Геттеры и сеттеры опущены для краткости, но они должны быть
    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    public UUID getSignatureId() {
        return signatureId;
    }

    public void setSignatureId(UUID signatureId) {
        this.signatureId = signatureId;
    }

    public Instant getVersionCreatedAt() {
        return versionCreatedAt;
    }

    public void setVersionCreatedAt(Instant versionCreatedAt) {
        this.versionCreatedAt = versionCreatedAt;
    }

    public String getThreatName() {
        return threatName;
    }

    public void setThreatName(String threatName) {
        this.threatName = threatName;
    }

    public String getFirstBytesHex() {
        return firstBytesHex;
    }

    public void setFirstBytesHex(String firstBytesHex) {
        this.firstBytesHex = firstBytesHex;
    }

    public String getRemainderHashHex() {
        return remainderHashHex;
    }

    public void setRemainderHashHex(String remainderHashHex) {
        this.remainderHashHex = remainderHashHex;
    }

    public Long getRemainderLength() {
        return remainderLength;
    }

    public void setRemainderLength(Long remainderLength) {
        this.remainderLength = remainderLength;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getOffsetStart() {
        return offsetStart;
    }

    public void setOffsetStart(Long offsetStart) {
        this.offsetStart = offsetStart;
    }

    public Long getOffsetEnd() {
        return offsetEnd;
    }

    public void setOffsetEnd(Long offsetEnd) {
        this.offsetEnd = offsetEnd;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public SignatureStatus getStatus() {
        return status;
    }

    public void setStatus(SignatureStatus status) {
        this.status = status;
    }

    public String getDigitalSignatureBase64() {
        return digitalSignatureBase64;
    }

    public void setDigitalSignatureBase64(String digitalSignatureBase64) {
        this.digitalSignatureBase64 = digitalSignatureBase64;
    }
}