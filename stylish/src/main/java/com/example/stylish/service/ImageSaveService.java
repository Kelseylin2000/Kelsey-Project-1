package com.example.stylish.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageSaveService {

    @Value("${storage.location}")
    private String storageLocation;

    public String saveImage(MultipartFile file, String type) {
        if (file.isEmpty()) {
            return null;
        }

        try {
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String newFilename = UUID.randomUUID().toString() + "-" + type + "-" + originalFilename;
            Path uploadPath = Paths.get(storageLocation);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path destinationFile = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
            
            return newFilename;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] saveImages(MultipartFile[] files, String type) {
        return Arrays.stream(files)
            .map(file -> saveImage(file, type))
            .toArray(String[]::new);
    }
}
