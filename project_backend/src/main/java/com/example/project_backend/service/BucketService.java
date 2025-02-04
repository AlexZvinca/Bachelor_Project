package com.example.project_backend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.example.project_backend.entities.User;
import com.example.project_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
public class BucketService {

    private final UserRepository userRepository;
    private final AmazonS3 s3Client;

    @Value("${s3.bucket.name}")
    private String bucketName;

    public BucketService(UserRepository userRepository, AmazonS3 s3Client) {
        this.userRepository = userRepository;
        this.s3Client = s3Client;
    }

    public void uploadFile(String userId, MultipartFile file) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        File fileObj = convertMultiPartFileToFile(file);
        String fileName = userId + "_ID_Document";
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        try {
            if (fileObj.exists()) {
                boolean deleted = fileObj.delete();
                if (!deleted) {
                    System.err.println("Failed to delete temp file: " + fileObj.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            System.err.println("Error while deleting temp file: " + e.getMessage());
        }

        if (user.getIdDocument() != null)
        {
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, user.getIdDocument().substring(user.getIdDocument().lastIndexOf("/") + 1)));
        }

        user.setIdDocument(fileName);
        userRepository.save(user);
    }

    public S3Object downloadFile(String fileName) {
        return s3Client.getObject(bucketName, fileName);
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }

}
