package com.example.taskmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "device")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_identifier", unique = true, nullable = false, length = 255)
    private String deviceIdentifier;

    @Column(name = "device_name", length = 255)
    private String deviceName;

    @Column(length = 100)
    private String os;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Связь с активациями – игнорируем при сериализации, чтобы разорвать цикл Device -> DeviceLicense -> Device
    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<DeviceLicense> deviceLicenses = new ArrayList<>();

    // Конструкторы
    public Device() {}

    public Device(String deviceIdentifier, String deviceName, String os, LocalDateTime createdAt) {
        this.deviceIdentifier = deviceIdentifier;
        this.deviceName = deviceName;
        this.os = os;
        this.createdAt = createdAt;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDeviceIdentifier() { return deviceIdentifier; }
    public void setDeviceIdentifier(String deviceIdentifier) { this.deviceIdentifier = deviceIdentifier; }

    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }

    public String getOs() { return os; }
    public void setOs(String os) { this.os = os; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<DeviceLicense> getDeviceLicenses() { return deviceLicenses; }
    public void setDeviceLicenses(List<DeviceLicense> deviceLicenses) { this.deviceLicenses = deviceLicenses; }

    // equals/hashCode по id
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