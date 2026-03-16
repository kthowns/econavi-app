package com.example.econavi.common.controller;

import com.example.econavi.common.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
@RequiredArgsConstructor
@Tag(name = "정적 리소스 관련 서비스", description = "이미지, 파일 등 다운로드")
public class FileController {
    private final FileStorageService fileStorageService;

    @Operation(
            summary = "파일 다운로드",
            description = "정적 리소스 url을 통해 다운로드",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/download")
    public ResponseEntity<Resource> download(
            @RequestParam String file_name
    ) throws MalformedURLException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(fileStorageService.loadAsResource(file_name));
    }
}