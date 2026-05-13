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
                .orElseThrow(() -> new RuntimeException("Product not found"));
        LicenseType type = typeRepo.findByName(typeName)
                .orElseThrow(() -> new RuntimeException("License type not found"));
        User owner = userRepo.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        License license = new License();
        license.setCode(generateLicenseCode());
        license.setProduct(product);
        license.setType(type);
        license.setOwner(owner);          // только владелец
        license.setUser(null);            // пока не активирована
        license.setDeviceCount(deviceCount);
        license.setEndingDate(endingDate); // может быть задана вручную (если передали), но при активации пересчитается
        license.setBlocked(false);
        license.setDescription(description);
        license.setCreatedAt(LocalDateTime.now());
        license.setUpdatedAt(LocalDateTime.now());

        License saved = licenseRepo.save(license);
        addHistory(saved, "CREATED", ownerId, "License created");
        return saved;
    }

    /**
     * 2. Активация лицензии пользователем на конкретном устройстве.
     */
    @Transactional
    public DeviceLicense activateLicense(String licenseCode, Long userId, String deviceIdentifier) {
        License license = licenseRepo.findByCode(licenseCode)
                .orElseThrow(() -> new RuntimeException("License not found"));

        if (license.getBlocked()) {
            throw new RuntimeException("License is blocked");
        }

        // Проверка, что лицензия принадлежит этому пользователю (владельцу)
        if (!license.getOwner().getId().equals(userId)) {
            throw new RuntimeException("License does not belong to this user");
        }

        User user = userRepo.findById(userId).orElseThrow();

        Device device = deviceRepo.findByDeviceIdentifier(deviceIdentifier)
                .orElseGet(() -> {
                    Device newDevice = new Device();
                    newDevice.setDeviceIdentifier(deviceIdentifier);
                    newDevice.setCreatedAt(LocalDateTime.now());
                    return deviceRepo.save(newDevice);
                });

        // Проверка лимита устройств
        long activeCount = activationRepo.countByLicenseAndStatus(license, "ACTIVE");
        if (activeCount >= license.getDeviceCount()) {
            throw new RuntimeException("Device limit reached");
        }

        // Проверка, что на этом устройстве уже нет активной активации
        if (activationRepo.existsByLicenseAndDeviceAndStatus(license, device, "ACTIVE")) {
            throw new RuntimeException("License already active on this device");
        }

        // Логика первой активации
        boolean isFirstActivation = (license.getUser() == null);
        if (isFirstActivation) {
            // Устанавливаем пользователя, активировавшего лицензию
            license.setUser(user);
            license.setFirstActivationDate(LocalDate.now());

            // Вычисляем дату окончания: дата первой активации + срок типа лицензии
            int durationDays = license.getType().getDefaultDurationInDays(); // из license_type
            license.setEndingDate(LocalDate.now().plusDays(durationDays));

            licenseRepo.save(license);
        }

        // Создаём запись активации на устройстве
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
        // Проверка, что лицензия активирована на этого пользователя (user_id == userId)
        if (license.getUser() == null || !license.getUser().getId().equals(userId)) return false;

        Device device = deviceRepo.findByDeviceIdentifier(deviceIdentifier).orElse(null);
        if (device == null) return false;

        Optional<DeviceLicense> activation = activationRepo
                .findByLicenseAndUserAndDeviceAndStatus(license, userRepo.findById(userId).orElse(null), device, "ACTIVE");
        return activation.isPresent();
    }

    /**
     * 4. Продление срока действия лицензии (только для администратора).
     */
    @Transactional
    public License extendLicense(String licenseCode, int additionalDays) {
        License license = licenseRepo.findByCode(licenseCode)
                .orElseThrow(() -> new RuntimeException("License not found"));
        LocalDate newEnding = license.getEndingDate().plusDays(additionalDays);
        license.setEndingDate(newEnding);
        license.setUpdatedAt(LocalDateTime.now());
        License saved = licenseRepo.save(license);
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