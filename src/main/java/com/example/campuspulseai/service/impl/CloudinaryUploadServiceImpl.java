package com.example.campuspulseai.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.campuspulseai.domain.dto.response.FileUploadResponse;
import com.example.campuspulseai.service.ICloudinaryUploadService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
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
    private static final long MAX_FILE_SIZE = (long) 5 * 1024 * 1024; // 5 MB

    public FileUploadResponse uploadFile(MultipartFile file) throws IOException {

        validateFileExistance(file);
        validateFileType(file);
        validateFileSize(file);

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("resource_type", "auto"));

        return new FileUploadResponse(uploadResult.get("secure_url").toString());
    }

    private void validateFileExistance(MultipartFile file) throws BadRequestException {
        if (file.isEmpty()) {
            throw new BadRequestException("File is empty!");
        }
    }

    private void validateFileType(MultipartFile file) {
        String contentType = file.getContentType();
        if (!(MediaType.IMAGE_JPEG_VALUE.equals(contentType) || MediaType.IMAGE_PNG_VALUE.equals(contentType))) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Only JPG and PNG files are allowed!");
        }
    }

    private void validateFileSize(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "File size exceeds the 5 MB limit!");
        }
    }
}
