package repo;

import org.springframework.data.jpa.repository.JpaRepository;
import model.Category;
import model.Game;
import model.QnA;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, String> {

    // This is so that the java functions can interact with the db
    List<Category> findByGameId(String gameId);

    List<QnA> findByCategoryId(String categoryId);

    void deleteByCategoryId(String categoryId);

    List<Game> findByUserId(String userId);

}
