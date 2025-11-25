package repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import model.Game;

public interface GameRepo extends JpaRepository<Game, String>{
    // Does select * from Game where userId = ""
    // Converts below command into sql query
    // Load all games that is associated with that userId
    List<Game> findByUserId(String userId);

    // Load singular game
    Game findbyGameId(String gameId);
}
