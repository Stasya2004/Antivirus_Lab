package com.example.taskmanagement.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "license_audit")
public class LicenseAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "license_id")
    private License license;

    @Column(name = "action", nullable = false, length = 50)
    private String action;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "device_id")
    private Long deviceId;

    @Column(name = "performed_at")
    private LocalDateTime performedAt;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    // Конструктор по умолчанию
    public LicenseAudit() {}

    // Конструктор для удобного заполнения
    public LicenseAudit(License license, String action, Long userId, Long deviceId,
                        LocalDateTime performedAt, String details) {
        this.license = license;
        this.action = action;
        this.userId = userId;
        this.deviceId = deviceId;
        this.performedAt = performedAt;
        this.details = details;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public LocalDateTime getPerformedAt() {
        return performedAt;
    }

    public void setPerformedAt(LocalDateTime performedAt) {
        this.performedAt = performedAt;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}