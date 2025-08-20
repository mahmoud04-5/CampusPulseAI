package com.example.campuspulseai.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.campuspulseai.domain.dto.response.FileUploadResponse;
import com.example.campuspulseai.service.ICloudinaryUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class CloudinaryUploadServiceImpl implements ICloudinaryUploadService {

    private final Cloudinary cloudinary;

    public FileUploadResponse uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty!");
        }

        String contentType = file.getContentType();
        if (!(MediaType.IMAGE_JPEG_VALUE.equals(contentType) || MediaType.IMAGE_PNG_VALUE.equals(contentType))) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Only JPG and PNG files are allowed!");
        }

        long maxSize = 5 * 1024 * 1024; // ie 5 mb
        if (file.getSize() > maxSize) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "File size exceeds the 5 MB limit!");
        }
        
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("resource_type", "auto"));

        return new FileUploadResponse(uploadResult.get("secure_url").toString());
    }
}
