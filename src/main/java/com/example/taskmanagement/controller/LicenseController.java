package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.Ticket;
import com.example.taskmanagement.dto.TicketResponse;
import com.example.taskmanagement.model.DeviceLicense;
import com.example.taskmanagement.model.License;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.security.TicketSigner;
import com.example.taskmanagement.service.LicenseService;
import com.example.taskmanagement.service.UserService;
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

    /**
     * Создание новой лицензии (только для администратора).
     * Ожидает JSON:
     * {
     *   "productName": "ProductName",
     *   "licenseType": "TRIAL",
     *   "ownerId": 1,
     *   "deviceCount": 3,
     *   "expirationDate": "2025-12-31",
     *   "description": "Some description"
     * }
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public License createLicense(@RequestBody Map<String, Object> body) {
        String productName = (String) body.get("productName");
        String typeName = (String) body.get("licenseType");
        Long ownerId = Long.valueOf(body.get("ownerId").toString());
        Integer deviceCount = (Integer) body.get("deviceCount");
        LocalDate endingDate = LocalDate.parse((String) body.get("expirationDate"));
        String description = (String) body.get("description");

        return licenseService.createLicense(productName, typeName, ownerId,
                deviceCount, endingDate, description);
    }

    /**
     * Активация лицензии пользователем.
     * Ожидает JSON:
     * {
     *   "licenseCode": "LIC-ABC123",
     *   "userId": 1,
     *   "deviceId": "device-identifier"
     * }
     */
    @PostMapping("/activate")
    public DeviceLicense activateLicense(Authentication authentication,
                                         @RequestBody Map<String, Object> body) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Long userId = user.getId();   // вместо чтения из body

        String licenseCode = (String) body.get("licenseCode");
        String deviceId = (String) body.get("deviceId");
        return licenseService.activateLicense(licenseCode, userId, deviceId);
    }

    /**
     * Проверка статуса лицензии для заданных пользователя и устройства.
     * Параметры запроса: licenseCode, userId, deviceId
     * Возвращает true, если лицензия активна и валидна.
     */
    @GetMapping("/check")
    public boolean checkLicense(@RequestParam String licenseCode,
                                @RequestParam Long userId,
                                @RequestParam String deviceId) {
        return licenseService.checkLicense(licenseCode, userId, deviceId);
    }

    /**
     * Продление срока действия лицензии (только для администратора).
     * Ожидает JSON:
     * {
     *   "licenseCode": "LIC-ABC123",
     *   "additionalDays": 30
     * }
     */
    @PostMapping("/extend")
    @PreAuthorize("hasRole('ADMIN')")
    public License extendLicense(@RequestBody Map<String, Object> body) {
        String licenseCode = (String) body.get("licenseCode");
        int additionalDays = (int) body.get("additionalDays");
        return licenseService.extendLicense(licenseCode, additionalDays);
    }

    /**
     * Получение подписанного тикета для активной лицензии текущего пользователя.
     * Параметр запроса: deviceId
     * Возвращает TicketResponse, содержащий Ticket и цифровую подпись.
     */
    @PostMapping("/ticket")
    public TicketResponse getTicket(Authentication authentication,
                                    @RequestParam String deviceId) throws Exception {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        DeviceLicense activation = licenseService.findActiveActivation(user.getId(), deviceId)
                .orElseThrow(() -> new RuntimeException("No active license for this user and device"));

        License license = activation.getLicense();

        Ticket ticket = new Ticket();
        ticket.setServerCurrentTime(LocalDateTime.now());
        ticket.setTicketLifetimeSeconds(3600L);         // 1 час
        ticket.setLicenseActivationDate(license.getFirstActivationDate());
        ticket.setLicenseExpirationDate(license.getEndingDate());
        ticket.setUserId(user.getId());
        ticket.setDeviceId(deviceId);
        ticket.setIsLicenseBlocked(license.getBlocked());

        String signature = ticketSigner.sign(ticket);
        return new TicketResponse(ticket, signature);
    }
}