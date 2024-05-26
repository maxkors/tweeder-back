package com.maxkors.tweeder.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class StorageService {

    @Value("${aws.bucket-name}")
    private String bucketName;

    private final S3Client s3Client;

    public String uploadFile(MultipartFile multipartFile) {
        File file = this.convertMultiPartToFile(multipartFile);

        String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));

        file.delete();

        return fileName;
    }

    public String createPresignedPutUrl(String keyname) {
        try (S3Presigner presigner = S3Presigner.create()) {
            var putObjectRequest = PutObjectRequest.builder()
                    .bucket(this.bucketName)
                    .key(keyname + "_" + System.currentTimeMillis())
                    .build();

            var putObjectPresignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .putObjectRequest(putObjectRequest)
                    .build();

            PresignedPutObjectRequest presignedPutObjectRequest = presigner.presignPutObject(putObjectPresignRequest);
            log.info("Presigned URL: [{}]", presignedPutObjectRequest.url().toString());
            log.info("HTTP method: [{}]", presignedPutObjectRequest.httpRequest().method());

            return presignedPutObjectRequest.url().toExternalForm();
        }
    }

    private File convertMultiPartToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());

        try(FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to File", e);
        }

        return convertedFile;
    }
}
