package com.example.taskmanagement.dto;

import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data
public class SignatureHistoryResponse {
    private Long historyId;
    private UUID signatureId;
    private Instant versionCreatedAt;
    private String threatName;
    private String firstBytesHex;
    private String remainderHashHex;
    private Long remainderLength;
    private String fileType;
    private Long offsetStart;
    private Long offsetEnd;
    private Instant updatedAt;
    private String status;
    // digitalSignatureBase64 не возвращаем, если клиенту не нужно
}