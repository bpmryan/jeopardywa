package controllers;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import model.QnA;
import repo.QnARepo;

@RestController
@RequestMapping("/api/qna")
@CrossOrigin(origins = "*")
public class QnAItems {
    
    @Autowired
    private QnARepo qnaRepo;

    @PostMapping
    public QnA addQnA(@RequestBody QnA qna) {
        qna.setQnaId("Q" + String.format("%05d", (int)(Math.random() * 100000)));
        return qnaRepo.save(qna);
    }
}
