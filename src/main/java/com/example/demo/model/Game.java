/*
 * Game.java
 * Purpose: JPA entity representing a Jeopardy game. Holds gameId, userId and
 * gameName. Higher-level relations (categories, qna) are managed by service
 * methods and separate repositories.
 */
package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
/**
 * Entities classes do not write to db directly
 * they only describe how tables and columns are mapped to Java fields
 * mappingn only
 */
@Table(name = "Game") 
public class Game {
    
    @Id
    @Column(name = "gameId", length = 36, nullable = false) //primary key
    private String gameId; 

    @Column(name = "userId", length = 36, nullable = false) //freign key
    private String userId;

    @Column(name = "gameName")
    private String gameName;

    public Game() {}

    // getters and setters
    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

}
