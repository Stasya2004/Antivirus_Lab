package com.example.taskmanagement.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO для передачи информации о лицензии клиенту.
 * Содержит все необходимые поля для проверки лицензии на стороне клиента.
 */
public class Ticket {
    private LocalDateTime serverCurrentTime;   // Текущая дата сервера
    private Long ticketLifetimeSeconds;         // Время жизни тикета (в секундах)
    private LocalDate licenseActivationDate;    // Дата активации лицензии
    private LocalDate licenseExpirationDate;    // Дата истечения лицензии
    private Long userId;                        // Идентификатор пользователя
    private String deviceId;                    // Идентификатор устройства
    private Boolean isLicenseBlocked;           // Флаг блокировки лицензии

    // Конструктор по умолчанию (необходим для десериализации JSON)
    public Ticket() {}

    // Конструктор со всеми полями (удобен для создания)
    public Ticket(LocalDateTime serverCurrentTime, Long ticketLifetimeSeconds,
                  LocalDate licenseActivationDate, LocalDate licenseExpirationDate,
                  Long userId, String deviceId, Boolean isLicenseBlocked) {
        this.serverCurrentTime = serverCurrentTime;
        this.ticketLifetimeSeconds = ticketLifetimeSeconds;
        this.licenseActivationDate = licenseActivationDate;
        this.licenseExpirationDate = licenseExpirationDate;
        this.userId = userId;
        this.deviceId = deviceId;
        this.isLicenseBlocked = isLicenseBlocked;
    }

    // Геттеры и сеттеры
    public LocalDateTime getServerCurrentTime() {
        return serverCurrentTime;
    }

    public void setServerCurrentTime(LocalDateTime serverCurrentTime) {
        this.serverCurrentTime = serverCurrentTime;
    }

    public Long getTicketLifetimeSeconds() {
        return ticketLifetimeSeconds;
    }

    public void setTicketLifetimeSeconds(Long ticketLifetimeSeconds) {
        this.ticketLifetimeSeconds = ticketLifetimeSeconds;
    }

    public LocalDate getLicenseActivationDate() {
        return licenseActivationDate;
    }

    public void setLicenseActivationDate(LocalDate licenseActivationDate) {
        this.licenseActivationDate = licenseActivationDate;
    }

    public LocalDate getLicenseExpirationDate() {
        return licenseExpirationDate;
    }

    public void setLicenseExpirationDate(LocalDate licenseExpirationDate) {
        this.licenseExpirationDate = licenseExpirationDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Boolean getIsLicenseBlocked() {
        return isLicenseBlocked;
    }

    public void setIsLicenseBlocked(Boolean isLicenseBlocked) {
        this.isLicenseBlocked = isLicenseBlocked;
    }

    // Опционально: метод для проверки, не истёк ли тикет (можно использовать на клиенте)
    public boolean isExpired() {
        if (serverCurrentTime == null || ticketLifetimeSeconds == null) return true;
        return serverCurrentTime.plusSeconds(ticketLifetimeSeconds).isBefore(LocalDateTime.now());
    }
}