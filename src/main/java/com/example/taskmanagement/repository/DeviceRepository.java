package com.example.taskmanagement.repository;

import com.example.taskmanagement.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByDeviceIdentifier(String deviceIdentifier);
}