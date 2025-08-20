package com.example.campuspulseai.northbound.controller;

import com.example.campuspulseai.domain.dto.response.FileUploadResponse;
import com.example.campuspulseai.service.ICloudinaryUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "File Upload", description = "File Upload API")
@RestController
@RequestMapping("api/files")
@RequiredArgsConstructor
public class FileUploadController {
    private final ICloudinaryUploadService cloudinaryService;


    @Operation(summary = "Upload a file", description = "Uploads a file to the cloud storage.")
    @PreAuthorize("hasRole('ORGANIZER')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping()
    public FileUploadResponse upload(@RequestParam("file") MultipartFile file) throws IOException {
        return cloudinaryService.uploadFile(file);
    }
}
