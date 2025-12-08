/*
 * AuthController.java
 * Purpose: Authentication REST endpoints for signup and login flows used by
 * the frontend. Delegates to UserService for signup/login logic and returns
 * simple status strings (SUCCESS / ERROR or userId on login).
 */
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
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    // Java function to generate a userId once they create an account
    @PostMapping("/signup")
    public String signup(@RequestBody UserInfo user) {
        // user.getPassword() comes from the @Transient field
        return userService.signup(user, user.getPassword());
    }

    // login DTO 
    static class LoginRequest {
        public String username;
        public String password;
    }

    // Checks whether or not the user inputed the correct credentials or not
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest req) {
        String userId = userService.login(req.username, req.password);

        if (userId == null) {
            return "ERROR";
        }

        return userId;
    }
}
