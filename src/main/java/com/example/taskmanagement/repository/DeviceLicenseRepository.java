package com.example.taskmanagement.repository;

import com.example.taskmanagement.model.Device;
import com.example.taskmanagement.model.DeviceLicense;
import com.example.taskmanagement.model.License;
import com.example.taskmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceLicenseRepository extends JpaRepository<DeviceLicense, Long> {

    /**
     * Подсчёт количества активаций лицензии с заданным статусом.
     * Используется для проверки лимита устройств.
     */
    long countByLicenseAndStatus(License license, String status);

    /**
     * Поиск всех активаций лицензии с определённым статусом.
     * Нужен, например, при продлении лицензии для обновления срока у всех активных активаций.
     */
    List<DeviceLicense> findByLicenseAndStatus(License license, String status);

    /**
     * Поиск конкретной активации по лицензии, пользователю, устройству и статусу.
     * Используется для проверки, активирована ли уже лицензия для данной связки.
     */
    Optional<DeviceLicense> findByLicenseAndUserAndDeviceAndStatus(License license,
                                                                   User user,
                                                                   Device device,
                                                                   String status);

    /**
     * Поиск активной активации по идентификатору пользователя и идентификатору устройства.
     * Используется в эндпоинте /ticket.
     */
    Optional<DeviceLicense> findByUser_IdAndDevice_DeviceIdentifierAndStatus(Long userId,
                                                                             String deviceId,
                                                                             String status);

    /**
     * Проверка существования активной активации для конкретной лицензии и устройства.
     * Используется для предотвращения повторной активации на одном устройстве.
     */
    boolean existsByLicenseAndDeviceAndStatus(License license, Device device, String status);
}