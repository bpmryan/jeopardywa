package controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.GameDTO;
import com.example.demo.service.GameService;

import model.Game;
import repo.GameRepo;

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

    @Autowired
    private GameRepo gameRepo;

    // Saves game, categories, and qna
    @PostMapping("/saveAll")
    public ResponseEntity<String> saveAll (@RequestBody GameDTO game) {
        String gameId = gameService.saveGame(game);
        // Returns success response
        return ResponseEntity.ok("Save gameId= " + gameId);
    }

    // retrieves all games linked to the userIf from the user table
    @GetMapping("/user/{userId}")
    public List<Game> getGamesbyUser(@PathVariable String userId) {
        return gameRepo.findByUserId(userId);
    }
    
    // Delete the whole game
    @DeleteMapping("/{gameId}")
    public ResponseEntity<?> deleteGame(@PathVariable String gameId) {
        gameRepo.deleteById(gameId);
        return ResponseEntity.ok("Deleted");
    }
}
