package com.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class FileUploadService {

    private final String uploadDir = "uploads/";

    public FileUploadService() {
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public File saveFile(MultipartFile file, String fileName) throws IOException {
        Path uploadPath = Path.of(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toFile();
    }

    public void deleteFile(String fileName) {
        try {
            Path filePath = Path.of(uploadDir + fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // ignore
        }
    }
}
