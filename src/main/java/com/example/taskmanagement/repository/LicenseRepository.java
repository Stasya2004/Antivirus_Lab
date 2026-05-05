package com.example.taskmanagement.repository;

import com.example.taskmanagement.model.License;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LicenseRepository extends JpaRepository<License, Long> {
    Optional<License> findByLicenseKey(String licenseKey);
}
