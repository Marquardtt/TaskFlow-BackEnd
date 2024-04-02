package br.demo.backend.service;

import br.demo.backend.model.Archive;
import br.demo.backend.model.Project;
import br.demo.backend.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Service
public class AWSService {

    private final ProjectRepository projectRepository;
    private final  String bucket ;
    private final  String keyId ;
    private final  String keySecret ;

    public AWSService (ProjectRepository projectRepository, Environment env){
        this.projectRepository = projectRepository;
        this.bucket  = env.getProperty("bucket");
        this.keyId= env.getProperty("keyID");
        this.keySecret= env.getProperty("keySecret");
    }

    private AwsBasicCredentials getCredentials(){
        return AwsBasicCredentials.create(keyId, keySecret);
    }
    private S3Client getClient(){
        return  S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(getCredentials()))
                .region(Region.of("us-east-1"))
                .build();
    }


    public void insertImage(Long id, MultipartFile file){
        if (!doesBucketExist(getClient(), bucket)) {
            return;
        }
        Project project = projectRepository.findById(id).get();
        String awsKey = UUID.randomUUID().toString();
        Archive archive = new Archive(file);
        archive.setAwsKey(awsKey);
        project.setPicture(archive);
        projectRepository.save(project);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(awsKey)
                .contentType(archive.getType())
                .build();
        try{
            getClient().putObject(putObjectRequest, RequestBody.fromInputStream(
                    file.getInputStream(), file.getSize()));
        }catch (IOException ignore){
            System.out.println("Erro");
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

    private String createPresignedGetUrl(String bucketName, String keyName) {
        try (S3Presigner presigner = S3Presigner
                .builder()
                .region(Region.of("us-east-1"))
                .credentialsProvider(StaticCredentialsProvider.create(getCredentials()))
                .build()) {
            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .getObjectRequest(objectRequest)
                    .build();
            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            return presignedRequest.url().toExternalForm();
        }
    }

    public String getImage(String key){
        if (doesBucketExist(getClient(), bucket)) {
            return createPresignedGetUrl(bucket, key);
        }
        return "";
    }
}
