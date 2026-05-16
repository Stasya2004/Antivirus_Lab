package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.Ticket;
import com.example.taskmanagement.dto.TicketResponse;
import com.example.taskmanagement.model.DeviceLicense;
import com.example.taskmanagement.model.License;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.signature.JsonCanonicalizer;
import com.example.taskmanagement.signature.SigningService;
import com.example.taskmanagement.service.LicenseService;
import com.example.taskmanagement.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/licenses")
public class LicenseController {

    private final LicenseService licenseService;
    private final UserService userService;
    private final SigningService signingService;
    private final JsonCanonicalizer jsonCanonicalizer;   // добавлено

    public LicenseController(LicenseService licenseService,
                             UserService userService,
                             SigningService signingService,
                             JsonCanonicalizer jsonCanonicalizer) {   // инжектируем
        this.licenseService = licenseService;
        this.userService = userService;
        this.signingService = signingService;
        this.jsonCanonicalizer = jsonCanonicalizer;
    }

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

    @PostMapping("/activate")
    public DeviceLicense activateLicense(@RequestBody Map<String, Object> body) {
        String licenseCode = (String) body.get("licenseCode");
        Long userId = Long.valueOf(body.get("userId").toString());
        String deviceId = (String) body.get("deviceId");
        return licenseService.activateLicense(licenseCode, userId, deviceId);
    }

    @GetMapping("/check")
    public boolean checkLicense(@RequestParam String licenseCode,
                                @RequestParam Long userId,
                                @RequestParam String deviceId) {
        return licenseService.checkLicense(licenseCode, userId, deviceId);
    }

    @PostMapping("/extend")
    @PreAuthorize("hasRole('ADMIN')")
    public License extendLicense(@RequestBody Map<String, Object> body) {
        String licenseCode = (String) body.get("licenseCode");
        int additionalDays = (int) body.get("additionalDays");
        return licenseService.extendLicense(licenseCode, additionalDays);
    }

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
        ticket.setTicketLifetimeSeconds(3600L);
        ticket.setLicenseActivationDate(license.getFirstActivationDate());
        ticket.setLicenseExpirationDate(license.getEndingDate());
        ticket.setUserId(user.getId());
        ticket.setDeviceId(deviceId);
        ticket.setIsLicenseBlocked(license.getBlocked());

        // Используем JsonCanonicalizer для получения канонической строки JSON
        String canonicalJson = jsonCanonicalizer.canonizeJson(ticket);
        byte[] canonicalBytes = canonicalJson.getBytes(StandardCharsets.UTF_8);
        String signature = signingService.sign(canonicalBytes);

        return new TicketResponse(ticket, signature);
    }
}