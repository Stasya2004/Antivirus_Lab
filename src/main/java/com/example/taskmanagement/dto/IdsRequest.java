package com.example.taskmanagement.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
public class IdsRequest {
    @NotNull(message = "ids list cannot be null")
    private List<UUID> ids;
}