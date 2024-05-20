package com.shaurya.documentstorageaws.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class DocumentStorageService {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private UserService userService;

    public String uploadFile(String userName, MultipartFile file) throws IOException {
        log.info("Uploading Files - In Document Service");
        if (!userService.userExists(userName)) return "User does not exist. Please create the user first.";
        String s3link = s3Service.uploadFile(userName + "/" + file.getOriginalFilename(), file);
        return "File uploaded successfully : " + s3link; // Concatenating S3 Link in Response
    }

    public List<String> searchFiles(String userName, String searchString) {
        if (!userService.userExists(userName)) throw new RuntimeException("User does not exist");
        return s3Service.searchFiles(userName, searchString);
    }

    public byte[] downloadFile(String userName, String fileName) {
        if (!userService.userExists(userName)) throw new RuntimeException("User does not exist");
        return s3Service.downloadFile(userName, fileName);
    }
}
