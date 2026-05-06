package com.example.taskmanagement.signature;

import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;

@Component
public class KeyProvider {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public KeyProvider(SignatureProperties properties) throws Exception {
        String path = properties.getKeyStorePath();
        InputStream is;
        if (path.startsWith("classpath:")) {
            String resource = path.substring("classpath:".length());
            is = getClass().getResourceAsStream(resource);
            if (is == null) throw new RuntimeException("Keystore not found in classpath: " + resource);
        } else if (path.startsWith("file:")) {
            String filePath = path.substring("file:".length());
            is = Files.newInputStream(Paths.get(filePath));
        } else {
            is = Files.newInputStream(Paths.get(path));
        }

        KeyStore ks = KeyStore.getInstance(properties.getKeyStoreType());
        char[] storePwd = properties.getKeyStorePassword().toCharArray();
        ks.load(is, storePwd);
        is.close();

        char[] keyPwd = properties.getKeyPassword() != null ?
                properties.getKeyPassword().toCharArray() : storePwd;
        Key key = ks.getKey(properties.getKeyAlias(), keyPwd);
        if (key instanceof PrivateKey) {
            this.privateKey = (PrivateKey) key;
            Certificate cert = ks.getCertificate(properties.getKeyAlias());
            this.publicKey = cert.getPublicKey();
        } else {
            throw new RuntimeException("No private key found with alias " + properties.getKeyAlias());
        }
    }

    public PrivateKey getPrivateKey() { return privateKey; }
    public PublicKey getPublicKey() { return publicKey; }
}