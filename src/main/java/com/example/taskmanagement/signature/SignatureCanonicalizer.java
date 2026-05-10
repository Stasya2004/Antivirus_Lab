package com.example.taskmanagement.signature;

import com.example.taskmanagement.dto.SignatureRequest;
import com.example.taskmanagement.model.Signature;
import com.example.taskmanagement.model.SignatureStatus;
import java.nio.charset.StandardCharsets;

public class SignatureCanonicalizer {

    /**
     * Формирует канонические байты для подписи на основе DTO (при создании/обновлении).
     * Порядок полей строго определён, разделитель '|'.
     * Поле status всегда ACTUAL при создании/обновлении (кроме удаления).
     */
    public static byte[] canonicalize(SignatureRequest request, SignatureStatus status) {
        String data = String.join("|",
                request.getThreatName(),
                request.getFirstBytesHex(),
                request.getRemainderHashHex(),
                String.valueOf(request.getRemainderLength()),
                request.getFileType(),
                String.valueOf(request.getOffsetStart()),
                String.valueOf(request.getOffsetEnd()),
                status.name()
        );
        return data.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Канонизация для существующей сущности (используется при удалении,
     * когда статус меняется на DELETED).
     */
    public static byte[] canonicalize(Signature signature) {
        String data = String.join("|",
                signature.getThreatName(),
                signature.getFirstBytesHex(),
                signature.getRemainderHashHex(),
                String.valueOf(signature.getRemainderLength()),
                signature.getFileType(),
                String.valueOf(signature.getOffsetStart()),
                String.valueOf(signature.getOffsetEnd()),
                signature.getStatus().name()
        );
        return data.getBytes(StandardCharsets.UTF_8);
    }
}