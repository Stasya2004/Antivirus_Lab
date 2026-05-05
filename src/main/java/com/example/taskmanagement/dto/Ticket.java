package com.example.taskmanagement.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Data
public class Ticket {
    private LocalDateTime serverCurrentTime;
    private Long ticketLifetimeSeconds; // например, 3600
    private LocalDate licenseActivationDate;
    private LocalDate licenseExpirationDate;
    private Long userId;
    private String deviceId;
    private Boolean isLicenseBlocked;
}