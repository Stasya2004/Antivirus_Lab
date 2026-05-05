package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.Ticket;
import com.example.taskmanagement.dto.TicketResponse;
import com.example.taskmanagement.model.License;
import com.example.taskmanagement.model.LicenseActivation;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.security.TicketSigner;
import com.example.taskmanagement.service.LicenseService;
import com.example.taskmanagement.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/licenses")
public class LicenseController {

    private final LicenseService licenseService;
    private final UserService userService;
    private final TicketSigner ticketSigner;

    public LicenseController(LicenseService licenseService,
                             UserService userService,
                             TicketSigner ticketSigner) {
        this.licenseService = licenseService;
        this.userService = userService;
        this.ticketSigner = ticketSigner;
    }

    // 1. Создание лицензии (только для администратора)
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public License createLicense(@RequestBody Map<String, Object> body) {
        String productName = (String) body.get("productName");
        String licenseType = (String) body.get("licenseType");
        Integer maxDevices = (Integer) body.get("maxDevices");
        String expirationDateStr = (String) body.get("expirationDate");
        LocalDate expirationDate = LocalDate.parse(expirationDateStr);
        return licenseService.createLicense(productName, licenseType, maxDevices, expirationDate);
    }

    // 2. Активация лицензии пользователем
    @PostMapping("/activate")
    public LicenseActivation activateLicense(@RequestBody Map<String, Object> body) {
        String licenseKey = (String) body.get("licenseKey");
        Long userId = Long.valueOf(body.get("userId").toString());
        String deviceId = (String) body.get("deviceId");
        return licenseService.activateLicense(licenseKey, userId, deviceId);
    }

    // 3. Проверка статуса лицензии
    @GetMapping("/check")
    public boolean checkLicense(@RequestParam String licenseKey,
                                @RequestParam Long userId,
                                @RequestParam String deviceId) {
        return licenseService.checkLicense(licenseKey, userId, deviceId);
    }

    // 4. Продление лицензии (админ)
    @PostMapping("/extend")
    @PreAuthorize("hasRole('ADMIN')")
    public License extendLicense(@RequestBody Map<String, Object> body) {
        String licenseKey = (String) body.get("licenseKey");
        int additionalDays = (int) body.get("additionalDays");
        return licenseService.extendLicense(licenseKey, additionalDays);
    }

    // 5. Получение подписанного тикета для клиента (лицензия должна быть активной)
    @PostMapping("/ticket")
    public TicketResponse getTicket(Authentication authentication,
                                    @RequestParam String deviceId) throws Exception {
        // Получаем текущего пользователя из токена
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Ищем активную активацию для этого пользователя и устройства
        LicenseActivation activation = licenseService.findActiveActivation(user.getId(), deviceId)
                .orElseThrow(() -> new RuntimeException("No active license for this user and device"));

        License license = activation.getLicense();

        // Формируем Ticket
        Ticket ticket = new Ticket();
        ticket.setServerCurrentTime(LocalDateTime.now());
        ticket.setTicketLifetimeSeconds(3600L);          // 1 час
        ticket.setLicenseActivationDate(license.getActivationDate());
        ticket.setLicenseExpirationDate(license.getExpirationDate());
        ticket.setUserId(user.getId());
        ticket.setDeviceId(deviceId);
        ticket.setIsLicenseBlocked(license.getIsBlocked());

        // Подписываем
        String signature = ticketSigner.sign(ticket);

        return new TicketResponse(ticket, signature);
    }
}