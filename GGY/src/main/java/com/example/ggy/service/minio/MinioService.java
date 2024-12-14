package com.example.ggy.service.minio;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import jakarta.annotation.PostConstruct;

@Service
public class MinioService {

    @Autowired
    private MinioConfig properties;
    private MinioClient minioClient;

    @PostConstruct
    public void initializeMinioClient() {
        this.minioClient = MinioClient.builder()
                .endpoint(properties.getUrl())
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .build();

        try {
            // Check if bucket exists; if not, create it
            boolean bucketExists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(properties.getBucketName()).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(properties.getBucketName()).build());
                System.out.println("Bucket created: " + properties.getBucketName());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error initializing MinIO bucket", e);
        }

    }

    public String uploadDocument(String fileName, MultipartFile file) throws IOException {
        try (var inputStream = file.getInputStream()) {
            // Versuche, das Dokument hochzuladen
            System.out.println("Attempting to upload file to MinIO: " + fileName);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(properties.getBucketName())
                            .object(fileName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
            System.out.println("File uploaded successfully: " + fileName);
            return properties.getBucketName() + "/" + fileName;
        } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException
                | InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | ServerException
                | XmlParserException e) {
            // Detailliertes Logging des Fehlers
            System.err.println("Error uploading file to MinIO: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error uploading file to MinIO", e);
        }
    }
}
