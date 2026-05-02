package com.amit_codes.user_service.service;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {

    public String createUser(String name) {
        log.info("Creating user with name: {}", name);

        if (name == null) {
            log.error("User name is null");
            throw new RuntimeException("Invalid input");
        }

        log.info("User created successfully");
        return "User created";
    }

    public List<String> getAll() {
        log.info("Fetching all users");
        List<String> users = List.of("Amit Kumar", "Sumit Kumar", "Saloni Sharma");
        log.info("Fetched all users");
        return users;
    }
}