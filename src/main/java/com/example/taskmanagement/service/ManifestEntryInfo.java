package com.example.taskmanagement.service;

import com.example.taskmanagement.model.Signature;

import java.util.Base64;

public class ManifestEntryInfo {
    private final Signature signature;
    private final long dataOffset;
    private final int dataLength;
    private final byte[] recordSignatureBytes;

    public ManifestEntryInfo(Signature signature, long dataOffset, int dataLength, String signatureBase64) {
        this.signature = signature;
        this.dataOffset = dataOffset;
        this.dataLength = dataLength;
        this.recordSignatureBytes = Base64.getDecoder().decode(signatureBase64);
    }

    public Signature getSignature() { return signature; }
    public long getDataOffset() { return dataOffset; }
    public int getDataLength() { return dataLength; }
    public byte[] getRecordSignatureBytes() { return recordSignatureBytes; }
}