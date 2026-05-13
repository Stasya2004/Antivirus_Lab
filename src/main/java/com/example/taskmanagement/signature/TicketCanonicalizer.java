package com.example.taskmanagement.signature;

import com.example.taskmanagement.dto.Ticket;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

public class TicketCanonicalizer {
    private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter ISO_DATE_TIME = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static byte[] canonicalizeToBytes(Ticket ticket) {
        // Строго определённый порядок полей, разделитель '|', без пробелов
        String result = String.join("|",
                ticket.getServerCurrentTime().format(ISO_DATE_TIME),
                String.valueOf(ticket.getTicketLifetimeSeconds()),
                ticket.getLicenseActivationDate().format(ISO_DATE),
                ticket.getLicenseExpirationDate().format(ISO_DATE),
                String.valueOf(ticket.getUserId()),
                ticket.getDeviceId(),
                String.valueOf(ticket.getIsLicenseBlocked())
        );
        return result.getBytes(StandardCharsets.UTF_8);
    }
}