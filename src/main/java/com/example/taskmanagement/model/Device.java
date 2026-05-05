package com.example.taskmanagement.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_identifier", unique = true, nullable = false, length = 255)
    private String deviceIdentifier;

    @Column(name = "device_name", length = 255)
    private String deviceName;

    @Column(name = "os", length = 100)
    private String os;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Конструктор по умолчанию (обязателен для JPA)
    public Device() {}

    // Конструктор с полями (удобен для тестов)
    public Device(String deviceIdentifier, String deviceName, String os, LocalDateTime createdAt) {
        this.deviceIdentifier = deviceIdentifier;
        this.deviceName = deviceName;
        this.os = os;
        this.createdAt = createdAt;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}