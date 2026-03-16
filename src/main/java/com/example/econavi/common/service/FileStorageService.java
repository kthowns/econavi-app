package com.example.econavi.common.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path rootLocation = Paths.get("/opt/econavi/uploads");

    public String store(MultipartFile file) throws IOException {
        Files.createDirectories(rootLocation);
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path destinationFile = rootLocation.resolve(filename);
        Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }

    public Resource loadAsResource(String filename) throws MalformedURLException {
        Path file = rootLocation.resolve(filename);
        Resource resource = new UrlResource(file.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("파일을 찾을 수 없습니다: " + filename);
        }
    }

    public void delete(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Files.deleteIfExists(file); // 파일 없으면 조용히 무시
        } catch (IOException e) {
            throw new RuntimeException("파일 삭제 실패: " + filename, e);
        }
    }
}