package com.example.ticsys.external_service.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    public String uploadFile(MultipartFile file);
    public String deleteFile(String filePath);
}
