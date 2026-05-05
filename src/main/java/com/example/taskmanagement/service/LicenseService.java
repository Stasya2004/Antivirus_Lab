package com.example.taskmanagement.service;

import com.example.taskmanagement.model.*;
import com.example.taskmanagement.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LicenseService {

    private final LicenseRepository licenseRepo;
    private final DeviceRepository deviceRepo;
    private final LicenseActivationRepository activationRepo;
    private final UserRepository userRepo;

    public LicenseService(LicenseRepository licenseRepo,
                          DeviceRepository deviceRepo,
                          LicenseActivationRepository activationRepo,
                          UserRepository userRepo) {
        this.licenseRepo = licenseRepo;
        this.deviceRepo = deviceRepo;
        this.activationRepo = activationRepo;
        this.userRepo = userRepo;
    }

    // ---------- 1. Создание лицензии (только админ) ----------
    @Transactional
    public License createLicense(String productName, String licenseType,
                                 Integer maxDevices, LocalDate expirationDate) {
        License license = new License();
        license.setLicenseKey(generateLicenseKey());
        license.setProductName(productName);
        license.setLicenseType(licenseType);
        license.setMaxDevices(maxDevices);
        license.setExpirationDate(expirationDate);
        license.setCreatedAt(LocalDateTime.now());
        license.setUpdatedAt(LocalDateTime.now());
        return licenseRepo.save(license);
    }

    private String generateLicenseKey() {
        return "LIC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // ---------- 2. Активация лицензии ----------
    @Transactional
    public LicenseActivation activateLicense(String licenseKey, Long userId, String deviceId) {
        // 1. Найти лицензию
        License license = licenseRepo.findByLicenseKey(licenseKey)
                .orElseThrow(() -> new RuntimeException("License not found"));

        // 2. Проверить блокировку и срок действия
        if (license.getIsBlocked()) {
            throw new RuntimeException("License is blocked");
        }
        if (license.getExpirationDate() != null && license.getExpirationDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("License expired");
        }

        // 3. Найти или создать устройство
        Device device = deviceRepo.findByDeviceIdentifier(deviceId)
                .orElseGet(() -> {
                    Device newDevice = new Device();
                    newDevice.setDeviceIdentifier(deviceId);
                    newDevice.setCreatedAt(LocalDateTime.now());
                    return deviceRepo.save(newDevice);
                });

        // 4. Найти пользователя
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 5. Проверка дублирования активации (текущий пользователь + устройство + лицензия)
        Optional<LicenseActivation> existingActivation = activationRepo
                .findByLicenseAndUserAndDeviceAndStatus(license, user, device, "ACTIVE");
        if (existingActivation.isPresent()) {
            throw new RuntimeException("License already activated for this user and device");
        }

        // 6. Проверить лимит устройств (общее количество активных активаций по лицензии)
        long activeCount = activationRepo.countByLicenseAndStatus(license, "ACTIVE");
        if (activeCount >= license.getMaxDevices()) {
            throw new RuntimeException("Max devices limit reached");
        }

        // 7. Если лицензия активируется впервые – установить дату активации
        if (license.getActivationDate() == null) {
            license.setActivationDate(LocalDate.now());
            licenseRepo.save(license);
        }

        // 8. Создать запись активации
        LicenseActivation activation = new LicenseActivation();
        activation.setLicense(license);
        activation.setUser(user);
        activation.setDevice(device);
        activation.setActivatedAt(LocalDateTime.now());
        activation.setActivatedUntil(license.getExpirationDate());
        activation.setStatus("ACTIVE");
        return activationRepo.save(activation);
    }

    // ---------- 3. Проверка лицензии ----------
    @Transactional(readOnly = true)
    public boolean checkLicense(String licenseKey, Long userId, String deviceId) {
        // 1. Найти лицензию
        License license = licenseRepo.findByLicenseKey(licenseKey).orElse(null);
        if (license == null || license.getIsBlocked()) return false;
        if (license.getExpirationDate() != null && license.getExpirationDate().isBefore(LocalDate.now())) {
            return false;
        }

        // 2. Найти устройство
        Device device = deviceRepo.findByDeviceIdentifier(deviceId).orElse(null);
        if (device == null) return false;

        // 3. Найти пользователя
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) return false;

        // 4. Найти активную активацию
        Optional<LicenseActivation> activationOpt = activationRepo
                .findByLicenseAndUserAndDeviceAndStatus(license, user, device, "ACTIVE");
        if (activationOpt.isEmpty()) return false;

        LicenseActivation activation = activationOpt.get();

        // 5. Дополнительная проверка: активация не должна быть просрочена
        if (activation.getActivatedUntil() != null &&
                activation.getActivatedUntil().isBefore(LocalDate.now())) {
            return false;
        }
        return true;
    }

    // ---------- 4. Продление лицензии (админ) ----------
    @Transactional
    public License extendLicense(String licenseKey, int additionalDays) {
        License license = licenseRepo.findByLicenseKey(licenseKey)
                .orElseThrow(() -> new RuntimeException("License not found"));

        LocalDate newExpiration = license.getExpirationDate().plusDays(additionalDays);
        license.setExpirationDate(newExpiration);
        license.setUpdatedAt(LocalDateTime.now());

        // Обновить дату "действительна до" у всех активных активаций этой лицензии
        List<LicenseActivation> activeActivations = activationRepo.findByLicenseAndStatus(license, "ACTIVE");
        for (LicenseActivation act : activeActivations) {
            act.setActivatedUntil(newExpiration);
            activationRepo.save(act);
        }

        return licenseRepo.save(license);
    }

    // ---------- 5. Найти активную активацию по пользователю и устройству (для выдачи тикета) ----------
    @Transactional(readOnly = true)
    public Optional<LicenseActivation> findActiveActivation(Long userId, String deviceId) {
        return activationRepo.findByUser_IdAndDevice_DeviceIdentifierAndStatus(userId, deviceId, "ACTIVE");
    }
}