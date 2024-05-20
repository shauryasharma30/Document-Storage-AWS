package com.shaurya.documentstorageaws.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.shaurya.documentstorageaws.service.DocumentStorageService;
import com.shaurya.documentstorageaws.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/documents")
@Slf4j
public class DocumentStorageController {

    @Autowired
    private DocumentStorageService documentStorageService;

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public ResponseEntity<List<String>> searchFiles(@RequestParam String userName, @RequestParam String searchString) {
        log.info("Search in S3 Storage for user : {}, searchString : {}", userName, searchString);
        List<String> docs = documentStorageService.searchFiles(userName, searchString);
        return new ResponseEntity<>(docs, HttpStatus.OK);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam String userName, @RequestParam String fileName) {
        log.info("Request to Download File From S3 for user : {}, fileName : {}", userName, fileName);
        byte[] fileContent = documentStorageService.downloadFile(userName, fileName);

        if (fileContent.length == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(fileContent.length);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "document; filename=" + fileName);
        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam String userName,
                                             @RequestParam("file") MultipartFile file) {
        log.info("Request to Upload File : {}", file.getOriginalFilename());
        try {
            String response = documentStorageService.uploadFile(userName, file);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createuser")
    public ResponseEntity<String> createUser(@RequestParam String userName) {
        log.info("Request to create User : {}", userName);
        userService.createUser(userName);
        return new ResponseEntity<>("User created successfully", HttpStatus.OK);
    }

}
