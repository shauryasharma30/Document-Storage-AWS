package com.shaurya.documentstorageaws.service;

import com.shaurya.documentstorageaws.repository.UserRepository;
import com.shaurya.documentstorageaws.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(String userName) {
        log.info("Create User for : {}", userName);
        User user = new User();
        user.setUserName(userName);
        return userRepository.save(user);
    }

    public boolean userExists(String userName) {
        log.info("Checking if User Exists for : {}", userName);
        return userRepository.findByUserName(userName) != null;
    }

}
