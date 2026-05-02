package com.amit_codes.user_service.controller;

import com.amit_codes.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestParam(name = "name") String name) {
        return ResponseEntity.ok(userService.createUser(name));
    }

    @PostMapping("/all")
    public ResponseEntity<List<String>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }
}
