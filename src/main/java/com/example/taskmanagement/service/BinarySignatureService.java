package com.example.taskmanagement.service;

import com.example.taskmanagement.model.Signature;
import com.example.taskmanagement.model.SignatureStatus;
import com.example.taskmanagement.repository.SignatureRepository;
import com.example.taskmanagement.signature.SigningService;
import com.example.taskmanagement.util.BinaryWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class BinarySignatureService {

    private final SignatureRepository signatureRepository;
    private final SigningService signingService;

    private static final String MANIFEST_MAGIC = "MF-Parfenova";
    private static final String DATA_MAGIC = "DB-Parfenova";
    private static final short VERSION = 1;

    public BinarySignatureService(SignatureRepository signatureRepository, SigningService signingService) {
        this.signatureRepository = signatureRepository;
        this.signingService = signingService;
    }

    // Структура для возврата двух бинарных частей
    public static class BinaryPackage {
        public final byte[] manifest;
        public final byte[] data;
        public BinaryPackage(byte[] manifest, byte[] data) { this.manifest = manifest; this.data = data; }
    }

    @Transactional(readOnly = true)
    public BinaryPackage buildFullPackage() throws Exception {
        List<Signature> signatures = signatureRepository.findByStatus(SignatureStatus.ACTUAL);
        return buildPackage(signatures, 1, null);
    }

    @Transactional(readOnly = true)
    public BinaryPackage buildIncrementPackage(Instant since) throws Exception {
        List<Signature> signatures = signatureRepository.findByUpdatedAtAfterAndStatusIn(since,
                List.of(SignatureStatus.ACTUAL, SignatureStatus.DELETED));
        return buildPackage(signatures, 2, since);
    }

    @Transactional(readOnly = true)
    public BinaryPackage buildByIdsPackage(List<UUID> ids) throws Exception {
        List<Signature> signatures = signatureRepository.findAllById(ids);
        return buildPackage(signatures, 3, null);
    }

    private BinaryPackage buildPackage(List<Signature> signatures, int exportType, Instant since) throws Exception {
        // 1. Построить data.bin и собрать записи манифеста
        ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
        java.util.List<ManifestEntryInfo> entries = new java.util.ArrayList<>();

        long currentOffset = 0;
        for (Signature sig : signatures) {
            byte[] recordData = serializeDataRecord(sig);
            entries.add(new ManifestEntryInfo(sig, currentOffset, recordData.length, sig.getDigitalSignatureBase64()));
            dataStream.write(recordData);
            currentOffset += recordData.length;
        }
        byte[] dataBytes = dataStream.toByteArray();

        // 2. Вычислить SHA-256 от dataBytes
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] dataSha256 = digest.digest(dataBytes);

        // 3. Построить манифест (без подписи)
        byte[] manifestUnsigned = buildManifestUnsigned(signatures, entries, exportType, since, dataSha256);
        // 4. Подписать манифест
        byte[] manifestSignature = signingService.signBytes(manifestUnsigned);
        // 5. Добавить подпись в конец
        ByteArrayOutputStream manifestStream = new ByteArrayOutputStream();
        manifestStream.write(manifestUnsigned);
        // длина подписи (uint32)
        manifestStream.write((manifestSignature.length >>> 24) & 0xFF);
        manifestStream.write((manifestSignature.length >>> 16) & 0xFF);
        manifestStream.write((manifestSignature.length >>> 8) & 0xFF);
        manifestStream.write(manifestSignature.length & 0xFF);
        manifestStream.write(manifestSignature);
        byte[] manifestBytes = manifestStream.toByteArray();

        return new BinaryPackage(manifestBytes, dataBytes);
    }

    private byte[] serializeDataRecord(Signature sig) {
        BinaryWriter bw = new BinaryWriter();
        // threatName
        bw.writeString(sig.getThreatName());
        // firstBytes (hex -> bytes)
        byte[] firstBytes = hexStringToByteArray(sig.getFirstBytesHex());
        bw.writeUint32(firstBytes.length);
        bw.writeBytes(firstBytes);
        // remainderHash (hex -> bytes)
        byte[] remainderHash = hexStringToByteArray(sig.getRemainderHashHex());
        bw.writeUint32(remainderHash.length);
        bw.writeBytes(remainderHash);
        // remainderLength
        bw.writeUint64(sig.getRemainderLength());
        // fileType
        bw.writeString(sig.getFileType());
        // offsetStart, offsetEnd ------------------------------------------
        bw.writeUint64(sig.getOffsetStart());
        bw.writeUint64(sig.getOffsetEnd());
        return bw.toByteArray();
    }

    private byte[] buildManifestUnsigned(List<Signature> signatures,
                                         List<ManifestEntryInfo> entries,
                                         int exportType,
                                         Instant since,
                                         byte[] dataSha256) {
        BinaryWriter bw = new BinaryWriter();

        // Заголовок
        bw.writeString(MANIFEST_MAGIC);
        bw.writeUint16(VERSION);
        bw.writeUint8(exportType);
        long generatedAt = System.currentTimeMillis();
        bw.writeUint64(generatedAt);
        long sinceMillis = (since != null) ? since.toEpochMilli() : -1L;
        bw.writeUint64(sinceMillis);
        bw.writeUint32(signatures.size());   // recordCount
        bw.writeBytes(dataSha256);  // 32 байта

        // Массив записей
        for (ManifestEntryInfo entry : entries) {
            Signature sig = entry.getSignature();
            // id
            bw.writeUUID(sig.getId());
            // statusCode: 0 for ACTUAL, 1 for DELETED
            int statusCode = (sig.getStatus() == SignatureStatus.ACTUAL) ? 0 : 1;
            bw.writeUint8(statusCode);
            bw.writeUint64(sig.getUpdatedAt().toEpochMilli());
        // что за оно
            bw.writeUint64(entry.getDataOffset());
            bw.writeUint32(entry.getDataLength());
            // recordSignatureLength и сама подпись (байты)
            byte[] sigBytes = entry.getRecordSignatureBytes();
            bw.writeUint32(sigBytes.length);
            bw.writeBytes(sigBytes);
        }

        return bw.toByteArray();
    }

    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}