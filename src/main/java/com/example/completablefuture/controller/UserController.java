package com.example.completablefuture.controller;

import com.example.completablefuture.entity.User;
import com.example.completablefuture.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/users", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ResponseEntity saveUsers(@RequestParam(value = "files") MultipartFile[] files) throws Exception {
        for (MultipartFile file : files) {
            userService.saveUsers(file);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/getAllUsers", produces = "application/json")
    public CompletableFuture<ResponseEntity> findAllUsers() {
        return userService.findAllUsers().thenApply(ResponseEntity::ok);
    }

    @GetMapping
    public ResponseEntity getAllUsers() {
        CompletableFuture<List<User>> user1 = userService.findAllUsers();
        CompletableFuture<List<User>> user2 = userService.findAllUsers();
        CompletableFuture<List<User>> user3 = userService.findAllUsers();
        CompletableFuture.allOf(user1, user2, user3).join();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
