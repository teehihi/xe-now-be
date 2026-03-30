package com.rental.controller;

import com.rental.dto.ApiResponse;
import com.rental.dto.FileResponseDTO;
import com.rental.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<FileResponseDTO>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folder") String folder) {
        
        try {
            FileResponseDTO response = fileService.saveFile(file, folder);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(response, "Tải tệp lên thành công"));
        } catch (IllegalArgumentException | SecurityException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi hệ thống khi lưu file: " + e.getMessage()));
        }
    }
}
