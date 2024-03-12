package br.demo.backend.service;

import br.demo.backend.model.Archive;
import br.demo.backend.model.Project;
import br.demo.backend.repository.GroupRepository;
import br.demo.backend.repository.ProjectRepository;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class AwsService {
    private Environment environment;
    private ProjectRepository projectRepository;

    public void update(Long id, MultipartFile file) {
        Project project = projectRepository.findById(id).get();
        Archive archive = new Archive(file);
        String keyId = environment.getProperty("keyId");
        String keySecret = environment.getProperty("keySrecret");

        String bucket = environment.getProperty("bucket");

        archive.setAwsKey(UUID.randomUUID().toString());
        project.setPicture(archive);


        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(awsKeyId, awsKeySecret);

    }
    public boolean uploadFile(Long id, MultipartFile file) {
        String keyId = environment.getProperty("keyId");
        String keySecret = environment.getProperty("keySrecret");
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

//            fileMaked.setAwsKey(UUID.randomUUID().toString());
            String fileKey = file.getOriginalFilename(); // Assumindo que você deseja usar o nome original do arquivo como chave
            String contentType = file.getContentType();

            try (InputStream fileInputStream = file.getInputStream()) {
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(fileKey)
                        .contentType(contentType)
                        .build();

                s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(fileInputStream, file.getSize()));
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

}
