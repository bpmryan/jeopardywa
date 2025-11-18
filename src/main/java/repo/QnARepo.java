package repo;

import org.springframework.data.jpa.repository.JpaRepository;

import model.QnA;

public interface QnARepo extends JpaRepository<QnA, String>{


    
}
