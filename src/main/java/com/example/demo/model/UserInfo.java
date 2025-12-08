/*
 * UserInfo.java
 * Purpose: JPA entity representing a user account. Fields include userId,
 * username, email and password hash.
 *
 * Notes:
 *  - used by authentication flows and persisted via UserInfoRepo
 */
package com.example.demo.model;

import jakarta.persistence.Transient;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "UserInfo")
public class UserInfo {

    // attributes to send over to db (name: UserInfo)
    @Id
    private String userId;
    private String fName;
    private String lName;
    private String username;
    private String email;
    private String passwordHash;

    @Transient
    private String password;

    public UserInfo() {}

    // getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   
}
