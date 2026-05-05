package com.example.taskmanagement.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "license_activations")
public class LicenseActivation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "license_id", nullable = false)
    private License license;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(name = "activated_at")
    private LocalDateTime activatedAt;

    @Column(name = "activated_until")
    private LocalDate activatedUntil;

    @Column(name = "status", length = 50)
    private String status = "ACTIVE"; // ACTIVE, REVOKED, EXPIRED

    // Конструктор по умолчанию
    public LicenseActivation() {}

    // Конструктор с основными полями
    public LicenseActivation(License license, User user, Device device,
                             LocalDateTime activatedAt, LocalDate activatedUntil, String status) {
        this.license = license;
        this.user = user;
        this.device = device;
        this.activatedAt = activatedAt;
        this.activatedUntil = activatedUntil;
        this.status = status;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public LocalDateTime getActivatedAt() {
        return activatedAt;
    }

    public void setActivatedAt(LocalDateTime activatedAt) {
        this.activatedAt = activatedAt;
    }

    public LocalDate getActivatedUntil() {
        return activatedUntil;
    }

    public void setActivatedUntil(LocalDate activatedUntil) {
        this.activatedUntil = activatedUntil;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}