package com.example.taskmanagement.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "signatures")
public class Signature {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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
    @Column(name = "minio_object_name")
    private String minioObjectName;



    // Конструкторы, геттеры и сеттеры
    public Signature() {}

    public Signature(String threatName, String firstBytesHex, String remainderHashHex,
                     Long remainderLength, String fileType, Long offsetStart,
                     Long offsetEnd, Instant updatedAt, SignatureStatus status,
                     String digitalSignatureBase64) {
        this.threatName = threatName;
        this.firstBytesHex = firstBytesHex;
        this.remainderHashHex = remainderHashHex;
        this.remainderLength = remainderLength;
        this.fileType = fileType;
        this.offsetStart = offsetStart;
        this.offsetEnd = offsetEnd;
        this.updatedAt = updatedAt;
        this.status = status;
        this.digitalSignatureBase64 = digitalSignatureBase64;
    }

    // Геттеры и сеттеры (сгенерируйте в IDE)
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getThreatName() { return threatName; }
    public void setThreatName(String threatName) { this.threatName = threatName; }
    public String getFirstBytesHex() { return firstBytesHex; }
    public void setFirstBytesHex(String firstBytesHex) { this.firstBytesHex = firstBytesHex; }
    public String getRemainderHashHex() { return remainderHashHex; }
    public void setRemainderHashHex(String remainderHashHex) { this.remainderHashHex = remainderHashHex; }
    public Long getRemainderLength() { return remainderLength; }
    public void setRemainderLength(Long remainderLength) { this.remainderLength = remainderLength; }
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public Long getOffsetStart() { return offsetStart; }
    public void setOffsetStart(Long offsetStart) { this.offsetStart = offsetStart; }
    public Long getOffsetEnd() { return offsetEnd; }
    public void setOffsetEnd(Long offsetEnd) { this.offsetEnd = offsetEnd; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public SignatureStatus getStatus() { return status; }
    public void setStatus(SignatureStatus status) { this.status = status; }
    public String getDigitalSignatureBase64() { return digitalSignatureBase64; }
    public void setDigitalSignatureBase64(String digitalSignatureBase64) { this.digitalSignatureBase64 = digitalSignatureBase64; }

    public String getMinioObjectName() { return minioObjectName; }
    public void setMinioObjectName(String minioObjectName) { this.minioObjectName = minioObjectName; }
}