package com.example.taskmanagement.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignatureRequest {
    @NotBlank(message = "threatName cannot be blank")
    private String threatName;

    @NotBlank(message = "firstBytesHex cannot be blank")
    @Pattern(regexp = "^[0-9A-Fa-f]+$", message = "firstBytesHex must be hex string")
    private String firstBytesHex;

    @NotBlank(message = "remainderHashHex cannot be blank")
    @Pattern(regexp = "^[0-9A-Fa-f]+$", message = "remainderHashHex must be hex string")
    private String remainderHashHex;

    @NotNull(message = "remainderLength is required")
    @Min(value = 0, message = "remainderLength must be >= 0")
    private Long remainderLength;

    @NotBlank(message = "fileType cannot be blank")
    private String fileType;

    @NotNull(message = "offsetStart is required")
    @Min(value = 0, message = "offsetStart must be >= 0")
    private Long offsetStart;

    @NotNull(message = "offsetEnd is required")
    @Min(value = 0, message = "offsetEnd must be >= 0")
    private Long offsetEnd;

    // Валидация cross-field: offsetEnd >= offsetStart
    @AssertTrue(message = "offsetEnd must be >= offsetStart")
    public boolean isOffsetsValid() {
        if (offsetStart == null || offsetEnd == null) return true;
        return offsetEnd >= offsetStart;
    }
}