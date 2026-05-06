package com.example.taskmanagement.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "device")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_identifier", unique = true, nullable = false, length = 255)
    private String deviceIdentifier;   // уникальный идентификатор устройства (например, MAC-адрес или UUID)

    @Column(name = "device_name", length = 255)
    private String deviceName;         // название устройства (например, "iPhone 12")

    @Column(length = 100)
    private String os;                 // операционная система (iOS, Android, Windows и т.д.)

    @Column(name = "created_at")
    private LocalDateTime createdAt;   // дата регистрации устройства

    // Связь с активациями лицензий (одно устройство может быть связано с несколькими активациями)
    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DeviceLicense> deviceLicenses = new ArrayList<>();

    // Конструкторы
    public Device() {
    }

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

    public List<DeviceLicense> getDeviceLicenses() {
        return deviceLicenses;
    }

    public void setDeviceLicenses(List<DeviceLicense> deviceLicenses) {
        this.deviceLicenses = deviceLicenses;
    }

    // equals и hashCode (по id)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Device)) return false;
        Device device = (Device) o;
        return id != null && id.equals(device.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}