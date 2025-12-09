/*
 * UserInfoRepo.java
 * Purpose: Spring Data JPA repository for UserInfo entities. Provides CRUD
 * access and finder helpers used by authentication flows (existsByUsername,
 * existsByEmail, findByUsername).
 */
package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.UserInfo;

public interface UserInfoRepo extends JpaRepository<UserInfo, String> {
    /**
     * sql code translation:
     * 
     * select * from UserInfo
     * where username = ""
     */
    UserInfo findByUsername(String username);

    /**
     * sql code translation:
     * 
     * select * from UserInfo
     * where email = ""
     */
    UserInfo findByEmail(String email);

    /**
     * sql code translation:
     * 
     * SELECT EXISTS (
     * SELECT 1
     * FROM UserInfo
     * WHERE username = ?
     * );
     * 
     */
    boolean existsByUsername(String username);

     /**
     * sql code translation:
     * 
     * SELECT EXISTS (
     * SELECT 1
     * FROM UserInfo
     * WHERE email = ?
     * );
     * 
     */
    boolean existsByEmail(String email);

}
