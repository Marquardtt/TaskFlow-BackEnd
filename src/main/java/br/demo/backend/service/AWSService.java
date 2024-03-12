package br.demo.backend.service;

import br.demo.backend.model.Archive;
import br.demo.backend.model.Project;
import br.demo.backend.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.AwsClient;
import software.amazon.awssdk.awscore.AwsServiceClientConfiguration;
import software.amazon.awssdk.awscore.client.handler.AwsSyncClientHandler;
import software.amazon.awssdk.core.client.config.SdkClientConfiguration;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AWSService {

    private ProjectRepository projectRepository;
    private Environment env;
    public void insertImage(Long id, MultipartFile file){
        Project project = projectRepository.findById(id).get();

        String bucket  = env.getProperty("bucket");
        String keyId = env.getProperty("keyID");
        String keySecret = env.getProperty("keySecret");
        String awsKey = UUID.randomUUID().toString();

        Archive archive = new Archive(file);
        archive.setAwsKey(awsKey);

        project.setPicture(archive);
        projectRepository.save(project);


        AwsBasicCredentials awsCredentials =
                AwsBasicCredentials.create(keyId, keySecret);

        S3Client s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(Region.of("us-east-1"))
                .build();

        if (!doesBucketExist(s3Client, bucket)) {
            System.out.println("doesnt exists");
            return;
        }

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(awsKey)
                .contentType(archive.getType())
                .build();

        try{
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(
                    file.getInputStream(), file.getSize()));

        }catch (IOException ignore){
            System.out.println("Erro");
        }

        System.out.println("FOI");

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
