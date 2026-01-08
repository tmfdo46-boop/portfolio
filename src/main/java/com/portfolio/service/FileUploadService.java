package com.portfolio.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

@Service
public class FileUploadService {

    @Value("${file.upload-dir}")
    private String uploadDir; // application.properties에 upload 폴더 경로

    public String saveFile(MultipartFile file) throws IOException {
        LocalDate now = LocalDate.now();
        String year = String.valueOf(now.getYear());
        String month = String.format("%02d", now.getMonthValue());

        Path folder = Paths.get(uploadDir, year, month);
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = folder.resolve(fileName);

        file.transferTo(filePath.toFile());

        // 웹 접근용 경로
        return "/" + uploadDir + "/" + year + "/" + month + "/" + fileName;
    }
}
