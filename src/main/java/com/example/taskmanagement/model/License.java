package com.example.taskmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "license")
public class License {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String code;

    // Связь с продуктом
    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties({"licenses", "hibernateLazyInitializer"})
    private Product product;

    // Связь с типом лицензии
    @ManyToOne
    @JoinColumn(name = "type_id")
    @JsonIgnoreProperties({"licenses", "hibernateLazyInitializer"})
    private LicenseType type;

    // Владелец лицензии (owner_id)
    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnoreProperties({"deviceLicenses", "licenses", "password", "hibernateLazyInitializer"})
    private User owner;

    // Пользователь, который активировал лицензию (user_id)
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"deviceLicenses", "licenses", "password", "hibernateLazyInitializer"})
    private User user;

    @Column(name = "first_activation_date")
    private LocalDate firstActivationDate;

    @Column(name = "ending_date")
    private LocalDate endingDate;

    private Boolean blocked = false;

    @Column(name = "device_count")
    private Integer deviceCount = 1;

    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Связь с активациями – не сериализуем, чтобы разорвать цикл
    @OneToMany(mappedBy = "license", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<DeviceLicense> activations = new ArrayList<>();

    // Конструкторы
    public License() {}

    public License(String code, Product product, LicenseType type, User owner,
                   LocalDate endingDate, Integer deviceCount, String description) {
        this.code = code;
        this.product = product;
        this.type = type;
        this.owner = owner;
        this.endingDate = endingDate;
        this.deviceCount = deviceCount;
        this.description = description;
        this.blocked = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public LicenseType getType() { return type; }
    public void setType(LicenseType type) { this.type = type; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDate getFirstActivationDate() { return firstActivationDate; }
    public void setFirstActivationDate(LocalDate firstActivationDate) { this.firstActivationDate = firstActivationDate; }

    public LocalDate getEndingDate() { return endingDate; }
    public void setEndingDate(LocalDate endingDate) { this.endingDate = endingDate; }

    public Boolean getBlocked() { return blocked; }
    public void setBlocked(Boolean blocked) { this.blocked = blocked; }

    public Integer getDeviceCount() { return deviceCount; }
    public void setDeviceCount(Integer deviceCount) { this.deviceCount = deviceCount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<DeviceLicense> getActivations() { return activations; }
    public void setActivations(List<DeviceLicense> activations) { this.activations = activations; }

    // equals/hashCode по id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof License)) return false;
        License license = (License) o;
        return id != null && id.equals(license.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}