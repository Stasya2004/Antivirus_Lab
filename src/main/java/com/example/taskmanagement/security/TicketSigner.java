package com.example.taskmanagement.security;

import com.example.taskmanagement.dto.Ticket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class TicketSigner {

    @Value("${ticket.signing.secret}")
    private String secret;

    public String sign(Ticket ticket) throws Exception {
        String data = ticket.getServerCurrentTime().toString() + "|" +
                ticket.getTicketLifetimeSeconds() + "|" +
                ticket.getLicenseActivationDate().toString() + "|" +
                ticket.getLicenseExpirationDate().toString() + "|" +
                ticket.getUserId().toString() + "|" +
                ticket.getDeviceId() + "|" +
                ticket.getIsLicenseBlocked().toString();
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(keySpec);
        byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(rawHmac);
    }

    public boolean verify(Ticket ticket, String signature) throws Exception {
        return sign(ticket).equals(signature);
    }
}

