package com.example.taskmanagement.dto;

import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data
public class SignatureResponse {
    private UUID id;
    private String threatName;
    private String firstBytesHex;
    private String remainderHashHex;
    private Long remainderLength;
    private String fileType;
    private Long offsetStart;
    private Long offsetEnd;
    private Instant updatedAt;
    private String status;   // "ACTUAL" или "DELETED"

    // digitalSignatureBase64 не возвращаем

}