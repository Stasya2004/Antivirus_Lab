package com.example.taskmanagement.signature;

import org.springframework.stereotype.Service;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;

@Service
public class SigningService {
    private final KeyProvider keyProvider;

    public SigningService(KeyProvider keyProvider) {
        this.keyProvider = keyProvider;
    }

    public String sign(byte[] data) throws Exception {
        PrivateKey privateKey = keyProvider.getPrivateKey();
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(privateKey);
        sig.update(data);
        byte[] signatureBytes = sig.sign();
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

}