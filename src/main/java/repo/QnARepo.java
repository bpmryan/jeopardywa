package repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import model.QnA;

public interface QnARepo extends JpaRepository<QnA, String>{
    List<QnA> findByCategoryId(String categoryId);

    
}
