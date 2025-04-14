package com.fileservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fileservice.model.FileMetadata;

@Repository
public interface FileRepository extends MongoRepository<FileMetadata, String> {
}
