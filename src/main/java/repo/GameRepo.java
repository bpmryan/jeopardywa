package repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import model.Game;

public interface GameRepo extends JpaRepository<Game, String>{
    List<Game> findByUserId(String userId);
}
