package com.shaurya.documentstorageaws.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class S3Service {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 amazonS3;

    // Search
    public List<String> searchFiles(String userName, String searchString) {
        log.info("Searching Files in S3 ->");
        try {
            String prefix = userName + "/";
            ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName)
                                       .withPrefix(prefix).withDelimiter(prefix);

            ListObjectsV2Result result = amazonS3.listObjectsV2(req);

            // If Folder/Data is not Found for User
            if (result.getObjectSummaries().isEmpty()) return new ArrayList<>();

            return result.getObjectSummaries().stream().map(S3ObjectSummary::getKey)
                    .filter(key -> key.contains(searchString))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.info("Exception Occurred : {}", e);
        }
        return new ArrayList<>();
    }

    // Download
    public byte[] downloadFile(String userName, String fileName) {
        log.info("Downloading File From S3 for user : {}, file : {}", userName, fileName);
        String key = userName + "/" + fileName;
        try {
            S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucketName, key));
            S3ObjectInputStream inputStream = s3Object.getObjectContent();
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Error reading file from S3", e);
        } catch (Exception e) {
            return new byte[0]; // Return an empty byte array if the file does not exist
        }
    }

    // Upload
    public String uploadFile(String fileName, MultipartFile file) throws IOException {
        log.info("Uploading File to Amazon S3 --> :{}",fileName);
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), null));
        URL url = amazonS3.getUrl(bucketName, fileName);
        return url.toString();
    }

}
