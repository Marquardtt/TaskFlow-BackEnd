package br.demo.backend.service;

import br.demo.backend.model.Archive;
import br.demo.backend.model.Project;
import br.demo.backend.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;

@Service
@AllArgsConstructor

public class AwsService {
    private Environment environment;
    private ProjectRepository projectRepository;

    public Archive update(Long id, MultipartFile file, String key) throws IOException {
        Project project = projectRepository.findById(id).get();
        String awsKeyId = environment.getProperty("keyID");
        String keySecret = environment.getProperty("keySecret");
        String bucket = environment.getProperty("bucket");
        Archive fileMaked = new Archive(file);
        fileMaked.setAwsKey(key);
        System.out.println(awsKeyId + keySecret + bucket);
        project.setPicture(fileMaked);
        String region = "us-east-1";
        projectRepository.save(project);
        return fileMaked;

    }

    public boolean uploadFile(Long id, MultipartFile file) {
        String keyId = environment.getProperty("keyId");
        String keySecret = environment.getProperty("keySecret");
        String region = "us-east-1";
        String bucket = environment.getProperty("bucket");
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(keyId, keySecret);

        try (S3Client s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(Region.of(region))
                .build()) {

            if (!doesBucketExist(s3Client, bucket)) {
                return false;
            }

            String key = UUID.randomUUID().toString();
            String contentType = file.getContentType();

            try (InputStream fileInputStream = file.getInputStream()) {
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentType(contentType)
                        .build();

                s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(fileInputStream, file.getSize()));
                update(id, file, key);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private boolean doesBucketExist(S3Client s3Client, String bucketName) {
        try {
            s3Client.headBucket(b -> b.bucket(bucketName));
            return true;
        } catch (S3Exception e) {
            return false;
        }
    }

    public String findAws3(String bucketName, String keyName, AwsBasicCredentials awsBasicCredentials) {
        try (S3Presigner presigner = S3Presigner.builder().region(Region.US_EAST_1).
                credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials)).build()) {

            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))  // The URL will expire in 10 minutes.
                    .getObjectRequest(objectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);

            return presignedRequest.url().toExternalForm();

        }
    }

    public String getAws3(Long projectId) {
        String keyId = environment.getProperty("keyId");
        String keySecret = environment.getProperty("keySecret");
        String region = "us-east-1";
        String bucket = environment.getProperty("bucket");
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(keyId, keySecret);

        try (S3Client s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(Region.of(region))
                .build()) {

            if (doesBucketExist(s3Client, bucket)) {
                return findAws3(bucket, projectRepository.findById(projectId).get().getPicture().getAwsKey(), awsCredentials);
            }


        }
        return "";

    }
}