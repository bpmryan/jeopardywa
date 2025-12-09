/*
 * GameController.java
 * Purpose: REST endpoints to manage games. Exposes APIs to save a full game
 * (categories + QnA), retrieve games for a user, delete a game, and load
 * games for editing or play.
 *
 * Important endpoints:
 *  - POST /api/game/saveAll : save or update full game payload (returns gameId)
 *  - GET  /api/game/user/{userId} : list games for dashboard
 *  - DELETE /api/game/{gameId} : delete a game and related data
 *  - GET /api/game/full/{gameId} : load full game for edit
 *  - GET /api/game/play/{gameId} : load game for playing
 */
package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.GameDTO;
import com.example.demo.dto.GameFullDTO;
import com.example.demo.dto.GamePlayDTO;
import com.example.demo.service.GameService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "*")
public class GameController {

    @Autowired
    private GameService gameService;

    // Saves game, categories, and qna (create/update)
    @PostMapping("/saveAll")
    public String saveAll(@RequestBody GameDTO dto) {
        return gameService.saveGame(dto); // return gameId
    }

    // retrieves all games linked to the userId from the user table
    // puts it into a list 
    @GetMapping("/user/{userId}")
    public List<GameFullDTO> getGamesByUser(@PathVariable String userId) {
        return gameService.getGamesForDashboard(userId);
    }

    // Delete the whole game
    @DeleteMapping("/{gameId}")
    public String deleteGame(@PathVariable String gameId) {
        gameService.deleteGame(gameId);
        return "DELETED";
    }

    // Load game for user to edit
    // TODO: double check what trhis function does
    @GetMapping("/full/{gameId}")
    public GameFullDTO getFull(@PathVariable String gameId) {
        return gameService.loadFullGame(gameId);
    }

    // Load game once user wants to play
    @GetMapping("/play/{gameId}")
    public GamePlayDTO getPlay(@PathVariable String gameId) {
        return gameService.loadGameForPlay(gameId);
    }

}
