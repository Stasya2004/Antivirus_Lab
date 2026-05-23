package com.example.taskmanagement.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class BinaryWriter {
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    public void writeUint8(int value) {
        baos.write(value & 0xFF);
    }

    public void writeUint16(int value) {
        baos.write((value >>> 8) & 0xFF);
        baos.write(value & 0xFF);
    }

    public void writeUint32(long value) {
        baos.write((int) (value >>> 24) & 0xFF);
        baos.write((int) (value >>> 16) & 0xFF);
        baos.write((int) (value >>> 8) & 0xFF);
        baos.write((int) value & 0xFF);
    }

    public void writeUint64(long value) {
        baos.write((int) (value >>> 56) & 0xFF);
        baos.write((int) (value >>> 48) & 0xFF);
        baos.write((int) (value >>> 40) & 0xFF);
        baos.write((int) (value >>> 32) & 0xFF);
        baos.write((int) (value >>> 24) & 0xFF);
        baos.write((int) (value >>> 16) & 0xFF);
        baos.write((int) (value >>> 8) & 0xFF);
        baos.write((int) value & 0xFF);
    }

    public void writeBytes(byte[] bytes) {
        try {
            baos.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeString(String s) {
        if (s == null) s = "";
        byte[] utf8 = s.getBytes(StandardCharsets.UTF_8);
        writeUint32(utf8.length);
        writeBytes(utf8);
    }

    public void writeUUID(UUID uuid) {
        writeUint64(uuid.getMostSignificantBits());
        writeUint64(uuid.getLeastSignificantBits());
    }

    public byte[] toByteArray() {
        return baos.toByteArray();
    }

    public void reset() {
        baos.reset();
    }
}