package com.example.completablefuture.service;

import com.example.completablefuture.entity.User;
import com.example.completablefuture.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    Object target;
    Logger log = LoggerFactory.getLogger(UserService.class);
    @Async
    public CompletableFuture<List<User>> saveUsers(MultipartFile file) throws Exception {
        long start = System.currentTimeMillis();
        List<User> users = parseCSVFile(file);
        log.info("Saving list of user of size", users.size(), "" + Thread.currentThread().getName());
        users = userRepository.saveAll(users);
        long end = System.currentTimeMillis();
        log.info("Total time {}", (end - start));
        return CompletableFuture.completedFuture(users);
    }
    @Async
    public CompletableFuture<List<User>> findAllUsers() {
        log.info("get List of user by" + Thread.currentThread().getName());
        List<User> users = userRepository.findAll();
        return CompletableFuture.completedFuture(users);
    }

    public List<User> parseCSVFile(final MultipartFile file) throws Exception {
        final List<User> users = new ArrayList<>();
        try {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    final String[] data = line.split(",");
                    final User user = new User();
                    user.setName(data[0]);
                    user.setEmail(data[0]);
                    user.setGender(data[0]);
                    users.add(user);
                }
                return users;
            }
        } catch (Exception e) {
            log.error("Failed to parse CSV file { }", e);
            throw new Exception("Failed to parse CSV file {}", e);
        }
    }
}
