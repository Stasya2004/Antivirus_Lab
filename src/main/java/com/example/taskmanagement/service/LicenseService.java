package com.example.taskmanagement.service;

import com.example.taskmanagement.model.*;
import com.example.taskmanagement.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class LicenseService {

    private final LicenseRepository licenseRepo;
    private final ProductRepository productRepo;
    private final LicenseTypeRepository typeRepo;
    private final DeviceRepository deviceRepo;
    private final DeviceLicenseRepository activationRepo;
    private final UserRepository userRepo;
    private final LicenseHistoryRepository historyRepo;

    public LicenseService(LicenseRepository licenseRepo,
                          ProductRepository productRepo,
                          LicenseTypeRepository typeRepo,
                          DeviceRepository deviceRepo,
                          DeviceLicenseRepository activationRepo,
                          UserRepository userRepo,
                          LicenseHistoryRepository historyRepo) {
        this.licenseRepo = licenseRepo;
        this.productRepo = productRepo;
        this.typeRepo = typeRepo;
        this.deviceRepo = deviceRepo;
        this.activationRepo = activationRepo;
        this.userRepo = userRepo;
        this.historyRepo = historyRepo;
    }

    /**
     * 1. Создание новой лицензии (только для администратора).
     */
    @Transactional
    public License createLicense(String productName, String typeName, Long ownerId,
                                 Integer deviceCount, LocalDate endingDate, String description) {
        Product product = productRepo.findByName(productName)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productName));
        LicenseType type = typeRepo.findByName(typeName)
                .orElseThrow(() -> new RuntimeException("License type not found: " + typeName));

        License license = new License();
        license.setCode(generateLicenseCode());
        license.setProduct(product);
        license.setType(type);
        license.setOwnerId(ownerId);
        license.setDeviceCount(deviceCount);
        license.setEndingDate(endingDate);
        license.setBlocked(false);
        license.setDescription(description);
        license.setCreatedAt(LocalDateTime.now());
        license.setUpdatedAt(LocalDateTime.now());

        License saved = licenseRepo.save(license);
        addHistory(saved, "CREATED", ownerId, "License created with code " + saved.getCode());
        return saved;
    }

    /**
     * 2. Активация лицензии пользователем на конкретном устройстве.
     */
    @Transactional
    public DeviceLicense activateLicense(String licenseCode, Long userId, String deviceIdentifier) {
        License license = licenseRepo.findByCode(licenseCode)
                .orElseThrow(() -> new RuntimeException("License not found: " + licenseCode));

        if (license.getBlocked()) {
            throw new RuntimeException("License is blocked");
        }
        if (license.getEndingDate() != null && license.getEndingDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("License has expired");
        }
        if (!license.getOwnerId().equals(userId)) {
            throw new RuntimeException("License does not belong to this user");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        Device device = deviceRepo.findByDeviceIdentifier(deviceIdentifier)
                .orElseGet(() -> {
                    Device newDevice = new Device();
                    newDevice.setDeviceIdentifier(deviceIdentifier);
                    newDevice.setCreatedAt(LocalDateTime.now());
                    return deviceRepo.save(newDevice);
                });

        long activeCount = activationRepo.countByLicenseAndStatus(license, "ACTIVE");
        if (activeCount >= license.getDeviceCount()) {
            throw new RuntimeException("Maximum number of devices (" + license.getDeviceCount() + ") reached");
        }

        if (activationRepo.existsByLicenseAndDeviceAndStatus(license, device, "ACTIVE")) {
            throw new RuntimeException("License already active on this device");
        }

        if (license.getFirstActivationDate() == null) {
            license.setFirstActivationDate(LocalDate.now());
            licenseRepo.save(license);
        }

        DeviceLicense activation = new DeviceLicense();
        activation.setLicense(license);
        activation.setUser(user);
        activation.setDevice(device);
        activation.setStatus("ACTIVE");
        activation.setChangeDate(LocalDateTime.now());
        activation.setDescription("Activated from device " + deviceIdentifier);

        DeviceLicense saved = activationRepo.save(activation);
        addHistory(license, "ACTIVATED", userId, "Activated on device " + deviceIdentifier);
        return saved;
    }

    /**
     * 3. Проверка, активна ли лицензия для данного пользователя и устройства.
     */
    @Transactional(readOnly = true)
    public boolean checkLicense(String licenseCode, Long userId, String deviceIdentifier) {
        License license = licenseRepo.findByCode(licenseCode).orElse(null);
        if (license == null) return false;
        if (license.getBlocked()) return false;
        if (license.getEndingDate() != null && license.getEndingDate().isBefore(LocalDate.now()))
            return false;
        if (!license.getOwnerId().equals(userId)) return false;

        Device device = deviceRepo.findByDeviceIdentifier(deviceIdentifier).orElse(null);
        if (device == null) return false;

        Optional<DeviceLicense> activation = activationRepo
                .findByLicenseAndUserAndDeviceAndStatus(license,
                        userRepo.findById(userId).orElse(null), device, "ACTIVE");
        return activation.isPresent();
    }

    /**
     * 4. Продление срока действия лицензии (только для администратора).
     */
    @Transactional
    public License extendLicense(String licenseCode, int additionalDays) {
        License license = licenseRepo.findByCode(licenseCode)
                .orElseThrow(() -> new RuntimeException("License not found: " + licenseCode));

        LocalDate newEnding = license.getEndingDate().plusDays(additionalDays);
        license.setEndingDate(newEnding);
        license.setUpdatedAt(LocalDateTime.now());
        License saved = licenseRepo.save(license);

        // Обновлять device_license не требуется – проверка идёт по license.ending_date
        addHistory(license, "EXTENDED", null,
                String.format("Extended by %d days, new ending date %s", additionalDays, newEnding));
        return saved;
    }

    /**
     * 5. Поиск активной активации для формирования тикета.
     */
    @Transactional(readOnly = true)
    public Optional<DeviceLicense> findActiveActivation(Long userId, String deviceId) {
        return activationRepo.findByUser_IdAndDevice_DeviceIdentifierAndStatus(userId, deviceId, "ACTIVE");
    }

    // ------------------------------------------------------------------------
    // Вспомогательные методы
    // ------------------------------------------------------------------------
    private String generateLicenseCode() {
        return "LIC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void addHistory(License license, String status, Long userId, String description) {
        LicenseHistory history = new LicenseHistory();
        history.setLicense(license);
        history.setUserId(userId);
        history.setStatus(status);
        history.setChangeDate(LocalDateTime.now());
        history.setDescription(description);
        historyRepo.save(history);
    }
}