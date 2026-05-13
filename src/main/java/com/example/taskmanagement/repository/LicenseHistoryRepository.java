package com.example.taskmanagement.repository;

import com.example.taskmanagement.model.LicenseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicenseHistoryRepository extends JpaRepository<LicenseHistory, Long> {
    // Можно добавить методы для выборки истории по лицензии или пользователю при необходимости
}