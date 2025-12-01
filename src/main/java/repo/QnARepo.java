package repo;

import java.util.List;
import java.util.Locale.Category;

import org.springframework.data.jpa.repository.JpaRepository;

import model.Game;
import model.QnA;

public interface QnARepo extends JpaRepository<QnA, String> {

    // Load all QnA associated with that categoryId
    List<Category> findByGameId(String gameId);

    List<QnA> findByCategoryId(String categoryId);

    void deleteByCategoryId(String categoryId);

    List<Game> findByUserId(String userId);

}
