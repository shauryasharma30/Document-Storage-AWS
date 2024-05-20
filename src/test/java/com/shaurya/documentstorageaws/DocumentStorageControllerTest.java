package com.shaurya.documentstorageaws;

import com.shaurya.documentstorageaws.controller.DocumentStorageController;
import com.shaurya.documentstorageaws.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.shaurya.documentstorageaws.service.DocumentStorageService;
import com.shaurya.documentstorageaws.service.UserService;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class DocumentStorageControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private DocumentStorageService documentStorageService;

    @InjectMocks
    private DocumentStorageController documentStorageController;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(documentStorageController).build();
    }

    @Test
    public void testCreateUser() throws Exception {
        String userName = "testUser";
        when(userService.createUser(userName)).thenReturn(new User());
        mockMvc.perform(post("/documents/createuser")
                .param("userName", userName))
                .andExpect(status().isOk())
                .andExpect(content().string("User created successfully"));
    }

    @Test
    public void testSearchFiles() throws Exception {
        String userName      = "testUser";
        String searchString  = "demo";
        List<String> files   = Collections.singletonList("testUser/demo.txt");

        lenient().when(userService.userExists(userName)).thenReturn(true);
        when(documentStorageService.searchFiles(userName, searchString)).thenReturn(files);

        mockMvc.perform(get("/documents/search")
                .param("userName", userName)
                .param("searchString", searchString))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"testUser/demo.txt\"]"));
    }

    @Test
    public void testDownloadFile() throws Exception {
        String userName    = "testUser";
        String fileName    = "testFile.txt";
        byte[] fileContent = "demo-content".getBytes();

        when(documentStorageService.downloadFile(userName, fileName)).thenReturn(fileContent);

        mockMvc.perform(get("/documents/download")
                .param("userName", userName)
                .param("fileName", fileName))
                .andExpect(status().isOk())
                .andExpect(content().bytes(fileContent))
                .andExpect(header().string("Content-Type", "application/octet-stream"))
                .andExpect(header().string("Content-Disposition", "document; filename=testFile.txt"))
                .andExpect(header().longValue("Content-Length", fileContent.length));
    }

}
