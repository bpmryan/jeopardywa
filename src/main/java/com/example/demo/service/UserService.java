/*
 * UserService.java
 * Purpose: Business logic for user authentication and account creation.
 * - signup(UserInfo, rawPassword): validate uniqueness, hash password, save user
 * - login(username, password): validate credentials and return userId on success
 *
 * Notes:
 *  - Uses a SHA-256 hashing helper for password hashing (simple, not salted).
 */
package com.example.demo.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.UserInfo;
import com.example.demo.repo.UserInfoRepo;

@Service
public class UserService {

    @Autowired
    private UserInfoRepo userInfoRepo;

    // Password hashing simple SHA-256)
    private String hash(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes)
                sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Hashing failed", e);
        }
    }

    // Signup
    public String signup(UserInfo user, String rawPassword) {
        // Check for if the username and/or email already exists in the database
        if (userInfoRepo.existsByUsername(user.getUsername()))
            return "ERROR: Username already exists";

        if (userInfoRepo.existsByEmail(user.getEmail()))
            return "ERROR: Email already exists";

        // generate userId
        user.setUserId(UUID.randomUUID().toString());

        // hash password
        user.setPasswordHash(hash(rawPassword));

        /**
         * SQL translation for signup flow:
         *
         * -- Uniqueness checks (already performed above with repo helpers)
         * SELECT EXISTS (SELECT 1 FROM UserInfo WHERE username = ?);
         * SELECT EXISTS (SELECT 1 FROM UserInfo WHERE email = ?);
         *
         * -- Insert new user
         * INSERT INTO UserInfo (userId, fName, lName, username, email, passwordHash)
         * VALUES (?, ?, ?, ?, ?, ?);
         */
        userInfoRepo.save(user);

        return "SUCCESS";
    }

    // Login: return userId on success, null on fail
    public String login(String username, String password) {
        UserInfo user = userInfoRepo.findByUsername(username);
        if (user == null)
            return null;

        if (!user.getPasswordHash().equals(hash(password)))
            return null;

        return user.getUserId();
    }
}
