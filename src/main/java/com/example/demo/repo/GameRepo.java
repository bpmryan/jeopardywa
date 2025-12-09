/*
 * GameRepo.java
 * Purpose: Spring Data JPA repository for Game entities. Used to find games
 * by userId and standard CRUD operations.
 */
package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Game;

@Repository
public interface GameRepo extends JpaRepository<Game, String> {
    // Converts below command into sql query
    // Load all games that is associated with that userId
    /**
     * sql code translation:
     * 
     * select * from Game
     * where userId = ""
     */
    List<Game> findByUserId(String userId);

}
