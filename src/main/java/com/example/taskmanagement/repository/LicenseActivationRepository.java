package com.example.taskmanagement.repository;

import com.example.taskmanagement.model.Device;
import com.example.taskmanagement.model.License;
import com.example.taskmanagement.model.LicenseActivation;
import com.example.taskmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LicenseActivationRepository extends JpaRepository<LicenseActivation, Long> {

    // Подсчёт активных активаций для лицензии
    long countByLicenseAndStatus(License license, String status);

    // Поиск всех активаций лицензии с определённым статусом (нужен для продления)
    List<LicenseActivation> findByLicenseAndStatus(License license, String status);

    // Поиск конкретной активации по лицензии, пользователю, устройству и статусу
    Optional<LicenseActivation> findByLicenseAndUserAndDeviceAndStatus(License license, User user, Device device, String status);

    // Поиск активной активации по пользователю и идентификатору устройства
    Optional<LicenseActivation> findByUser_IdAndDevice_DeviceIdentifierAndStatus(Long userId, String deviceId, String status);
}