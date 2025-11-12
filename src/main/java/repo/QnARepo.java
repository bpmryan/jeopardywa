package repo;

import org.springframework.data.jpa.repository.JpaRepository;

import model.QnA;

public interface QnARepo extends JpaRepository<QnA, String>{

    // Temp fix to save qna function in QnAItems.java line 20 (save)
    QnA save(QnA qna);
    
}
