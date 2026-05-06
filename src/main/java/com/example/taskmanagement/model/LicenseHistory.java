package com.example.taskmanagement.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "license_history")
public class LicenseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "license_id")
    private License license;          // лицензия, к которой относится запись

    @Column(name = "user_id")
    private Long userId;              // пользователь, выполнивший действие (админ или владелец)

    @Column(nullable = false)
    private String status;            // новое состояние (CREATED, ACTIVATED, EXTENDED, BLOCKED, etc.)

    @Column(name = "change_date")
    private LocalDateTime changeDate; // дата изменения

    @Column(columnDefinition = "TEXT")
    private String description;       // детали операции

    // Конструкторы
    public LicenseHistory() {
    }

    public LicenseHistory(License license, Long userId, String status,
                          LocalDateTime changeDate, String description) {
        this.license = license;
        this.userId = userId;
        this.status = status;
        this.changeDate = changeDate;
        this.description = description;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(LocalDateTime changeDate) {
        this.changeDate = changeDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // equals/hashCode по id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LicenseHistory)) return false;
        LicenseHistory that = (LicenseHistory) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}