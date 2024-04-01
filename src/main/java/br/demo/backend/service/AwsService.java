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
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
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
public class AwsService {
    private ProjectRepository projectRepository;
    private final String awsKeyId;
    private final String keySecret;
    private final String bucket;
    private final String region;

    public AwsService(Environment env, ProjectRepository proj){
        this.awsKeyId = env.getProperty("keyID");
        this.keySecret = env.getProperty("keySecret");
        this.bucket = env.getProperty("bucket");
        this.region = "us-east-1";
        this.projectRepository = proj;
    }


    public String getImage(Long id){
        Project project = projectRepository.findById(id).get();
//        String awsKeyId = env.getProperty("keyID");
//        String keySecret = env.getProperty("keySecret");
//        String bucket = env.getProperty("bucket");
//        String region = "us-east-1";

        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(awsKeyId, keySecret);
        if (doesBucketExist( S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(Region.of(region))
                .build(), bucket)){
            try (S3Presigner presigner = S3Presigner.builder().region(Region.of(region)).credentialsProvider(StaticCredentialsProvider.create(awsCredentials)).build()) {

                GetObjectRequest objectRequest = GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(project.getPicture().getAwsKey())
                        .build();

                GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(10))  // The URL will expire in 10 minutes.
                        .getObjectRequest(objectRequest)
                        .build();

                PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);

                return presignedRequest.url().toExternalForm();
            }
        }
        return null;
    }

    public Archive createImage(Long id, MultipartFile file) throws IOException {
        Project project = projectRepository.findById(id).get();
//        String awsKeyId = env.getProperty("keyID");
//        String keySecret = env.getProperty("keySecret");
//        String bucket = env.getProperty("bucket");
        Archive builtFile = new Archive(file);
        builtFile.setAwsKey(UUID.randomUUID().toString());
        System.out.println(awsKeyId + keySecret + bucket);
        project.setPicture(builtFile);
        projectRepository.save(project);
        return builtFile;

    }


    public boolean uploadFile(Long id, MultipartFile file) throws IOException {
        Archive builtFile = createImage(id, file);


        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(awsKeyId, keySecret);

        try (S3Client s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(Region.of(region))
                .build()) {

            if (!doesBucketExist(s3Client, bucket)) {
                return false;
            }

            String fileKey = builtFile.getAwsKey();; // Assumindo que você deseja usar o nome original do arquivo como chave
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
