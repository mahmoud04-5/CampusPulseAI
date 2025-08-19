package com.example.campuspulseai.northbound.controller;

import com.example.campuspulseai.domain.dto.response.FileUploadResponse;
import com.example.campuspulseai.service.ICloudinaryUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/files")
@RequiredArgsConstructor
public class FileUploadController {
    private final ICloudinaryUploadService cloudinaryService;


    @PostMapping()
    public FileUploadResponse upload(@RequestParam("file") MultipartFile file) throws IOException {
        return cloudinaryService.uploadFile(file);
    }
}
