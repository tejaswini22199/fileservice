package com.fileservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fileservice.model.FileMetadata;
import com.fileservice.service.FileService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    // Upload File API
    @PostMapping("/upload")
    public ResponseEntity<FileMetadata> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        byte[] fileData = file.getBytes();
        String fileType = file.getContentType();

        FileMetadata uploadedFile = fileService.uploadFile(fileName, fileData, fileType);
        return ResponseEntity.ok(uploadedFile);
    }

    // Read File API
    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> getFile(@PathVariable String fileId) throws IOException {
        byte[] fileData = fileService.getFile(fileId);
        return ResponseEntity.ok(fileData);
    }

    // Update File API
    @PutMapping("/{fileId}")
    public ResponseEntity<FileMetadata> updateFile(@PathVariable String fileId, @RequestParam("file") MultipartFile file) throws IOException {
        byte[] newFileData = file.getBytes();
        String newFileName = file.getOriginalFilename();
        FileMetadata updatedFile = fileService.updateFile(fileId, newFileData, newFileName);
        return ResponseEntity.ok(updatedFile);
    }

    // Delete File API
    @DeleteMapping("/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileId) throws IOException {
        fileService.deleteFile(fileId);
        return ResponseEntity.ok("File deleted successfully");
    }

    // List Files API
    @GetMapping
    public ResponseEntity<List<FileMetadata>> listFiles() {
        List<FileMetadata> files = fileService.listFiles();
        return ResponseEntity.ok(files);
    }
}
