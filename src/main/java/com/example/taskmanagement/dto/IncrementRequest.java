package com.example.taskmanagement.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class IncrementRequest {
    private Instant since;   // можно не делать отдельный DTO, использовать @RequestParam
}