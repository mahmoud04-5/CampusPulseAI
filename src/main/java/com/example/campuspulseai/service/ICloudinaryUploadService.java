package com.example.campuspulseai.service;

import com.example.campuspulseai.domain.dto.response.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ICloudinaryUploadService {
    FileUploadResponse uploadFile(MultipartFile file) throws IOException;
}
