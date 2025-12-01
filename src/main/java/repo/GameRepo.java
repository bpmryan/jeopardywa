package repo;

import java.util.List;
import java.util.Locale.Category;

import org.springframework.data.jpa.repository.JpaRepository;

import model.Game;
import model.QnA;

public interface GameRepo extends JpaRepository<Game, String> {
    // Does select * from Game where userId = ""
    // Converts below command into sql query
    // Load all games that is associated with that userId
    List<Category> findByGameId(String gameId);

    List<QnA> findByCategoryId(String categoryId);

    void deleteByCategoryId(String categoryId);

    List<Game> findByUserId(String userId);

}
