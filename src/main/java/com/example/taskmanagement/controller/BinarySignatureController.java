package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.IdsRequest;
import com.example.taskmanagement.service.BinarySignatureService;
import jakarta.validation.Valid;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/binary/signatures")
public class BinarySignatureController {

    private final BinarySignatureService binaryService;

    public BinarySignatureController(BinarySignatureService binaryService) {
        this.binaryService = binaryService;
    }

    @GetMapping("/full")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<LinkedMultiValueMap<String, Object>> getFull() throws Exception {
        BinarySignatureService.BinaryPackage pkg = binaryService.buildFullPackage();
        return buildMultipartResponse(pkg.manifest, pkg.data);
    }

    @GetMapping("/increment")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<LinkedMultiValueMap<String, Object>> getIncrement(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant since) throws Exception {
        BinarySignatureService.BinaryPackage pkg = binaryService.buildIncrementPackage(since);
        return buildMultipartResponse(pkg.manifest, pkg.data);
    }

    @PostMapping("/by-ids")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<LinkedMultiValueMap<String, Object>> getByIds(@RequestBody @Valid IdsRequest idsRequest) throws Exception {
        BinarySignatureService.BinaryPackage pkg = binaryService.buildByIdsPackage(idsRequest.getIds());
        return buildMultipartResponse(pkg.manifest, pkg.data);
    }

    private ResponseEntity<LinkedMultiValueMap<String, Object>> buildMultipartResponse(byte[] manifest, byte[] data) {
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        body.add("manifest", new ByteArrayResource(manifest) {
            @Override
            public String getFilename() {
                return "manifest.bin";
            }
        });

        body.add("data", new ByteArrayResource(data) {
            @Override
            public String getFilename() {
                return "data.bin";
            }
        });

        return ResponseEntity.ok()
                .contentType(MediaType.MULTIPART_MIXED)
                .body(body);
    }
}