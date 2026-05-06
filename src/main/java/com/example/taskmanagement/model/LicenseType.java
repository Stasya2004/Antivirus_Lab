package com.example.taskmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "license_type")
public class LicenseType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;                          // название типа (TRIAL, PAID, SUBSCRIPTION)

    @Column(name = "default_duration_in_days")
    private Integer defaultDurationInDays;        // срок действия по умолчанию в днях

    private String description;                   // описание типа лицензии

    @OneToMany(mappedBy = "type")
    @JsonIgnore
    private List<License> licenses = new ArrayList<>(); // лицензии этого типа

    // Конструкторы
    public LicenseType() {
    }

    public LicenseType(String name, Integer defaultDurationInDays, String description) {
        this.name = name;
        this.defaultDurationInDays = defaultDurationInDays;
        this.description = description;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDefaultDurationInDays() {
        return defaultDurationInDays;
    }

    public void setDefaultDurationInDays(Integer defaultDurationInDays) {
        this.defaultDurationInDays = defaultDurationInDays;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<License> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<License> licenses) {
        this.licenses = licenses;
    }

    // equals/hashCode по id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LicenseType)) return false;
        LicenseType that = (LicenseType) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}