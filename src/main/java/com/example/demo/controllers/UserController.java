package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.UserInfo;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // User signup
    @PostMapping("/signup")
    public String signup(@RequestBody UserInfo user) {
        return userService.signup(user, user.getPassword());
    }

    // User login
    static class LoginRequest {
        public String username;
        public String password;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest req) {
        String userId = userService.login(req.username, req.password);
        return (userId == null) ? "ERROR" : userId;
    }
}
