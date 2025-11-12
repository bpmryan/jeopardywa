package repo;

import org.springframework.data.jpa.repository.JpaRepository;
import model.Category;

public interface CategoryRepo extends JpaRepository<Category, String> {

    // This is so that the java functions can interact with the db
    
    // temporary fix to line 23 in CategoryController (redline save)
    Category save(Category category);

    
}
