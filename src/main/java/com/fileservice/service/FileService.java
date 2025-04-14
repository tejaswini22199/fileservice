package com.fileservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fileservice.model.FileMetadata;
import com.fileservice.repository.FileRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    private final String FILE_STORAGE_PATH = "path_to_storage"; // specify the file storage path

    // Upload File
    public FileMetadata uploadFile(String fileName, byte[] fileData, String fileType) throws IOException {
        // Save the file on disk
        Path filePath = Paths.get(FILE_STORAGE_PATH, fileName);
        Files.write(filePath, fileData);

        // Create metadata and save to MongoDB
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setFileName(fileName);
        fileMetadata.setFileType(fileType);
        fileMetadata.setSize(fileData.length);
        fileMetadata.setCreatedAt(new Date());

        return fileRepository.save(fileMetadata);
    }

    // Get File
    public byte[] getFile(String fileId) throws IOException {
        FileMetadata fileMetadata = fileRepository.findById(fileId)
                .orElseThrow(() -> new IOException("File not found"));
        Path filePath = Paths.get(FILE_STORAGE_PATH, fileMetadata.getFileName());
        return Files.readAllBytes(filePath);
    }

    // Update File
    public FileMetadata updateFile(String fileId, byte[] newFileData, String newFileName) throws IOException {
        FileMetadata fileMetadata = fileRepository.findById(fileId)
                .orElseThrow(() -> new IOException("File not found"));

        // Update file on disk
        Path filePath = Paths.get(FILE_STORAGE_PATH, fileMetadata.getFileName());
        Files.write(filePath, newFileData);

        // Update metadata
        fileMetadata.setFileName(newFileName);
        fileMetadata.setSize(newFileData.length);
        fileMetadata.setCreatedAt(new Date());

        return fileRepository.save(fileMetadata);
    }

    // Delete File
    public void deleteFile(String fileId) throws IOException {
        FileMetadata fileMetadata = fileRepository.findById(fileId)
                .orElseThrow(() -> new IOException("File not found"));
        
        // Delete file from disk
        Path filePath = Paths.get(FILE_STORAGE_PATH, fileMetadata.getFileName());
        Files.delete(filePath);

        // Delete metadata from DB
        fileRepository.delete(fileMetadata);
    }

    // List Files
    public List<FileMetadata> listFiles() {
        return fileRepository.findAll();
    }
}
