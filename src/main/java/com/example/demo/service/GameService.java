package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.GameDTO;

import model.Category;
import model.QnA;
import repo.CategoryRepo;
import repo.QnARepo;

@Service
public class GameService {
    
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private QnARepo qnaRepo;

    // Function that saves whole game to db
    public void saveGame(GameDTO game) {
        // if incoming gameId == null, server generates
        String gameId = game.gameId;
        if (gameId == null || gameId.isEmpty()) {
            gameId = "G" + String.format("%05d", (int)(Math.random()*100000)); // Generate GameId here
        }

        // cat stores all category information
        for (GameDTO.CatgeoryDTO c : game.categories) {
            Category cat = new Category();
            cat.setCategoryId("C" + String.format("%05d", (int)(Math.random()*100000))); // Generate CategoryId here
            cat.setCategoryName(c.categoryName);
            cat.setBkgColor(c.bkgColor);
            cat.setTextColor(c.textColor);
            cat.setGameId(gameId);
            categoryRepo.save(cat);

            if (c.qna != null) {
                for (GameDTO.QnADTO qnadto : c.qna) {
                    QnA q = new QnA();

                    // generate ids for question, answer, and qna
                    q.setQnaId("Q" + String.format("%05d", (int)(Math.random()*100000)));
                    q.setQuestionId("QT" + String.format("%05d", (int)(Math.random()*10000)));
                    q.setAnswerId("AN" + String.format("%05d", (int)(Math.random()*10000)));

                    // Link to category
                    q.setCategoryId(cat.getCategoryId());

                    // Other data being sent to db
                    q.setPtValue(qnadto.ptValue != null ? qnadto.ptValue : 0);
                    q.setQuestionText(qnadto.questionText);
                    q.setAnswerText(qnadto.answerText);

                    // Image data being sent to db
                    if (qnadto.questionImage != null) {
                        q.setQuestionImageUrl(qnadto.questionImage.url);
                        q.setQuestionImagePosition(qnadto.questionImage.position);
                        q.setQuestionImageScale(qnadto.questionImage.scale);
                    }
                    if (qnadto.answerImage != null) {
                        q.setAnswerImageUrl(qnadto.answerImage.url);
                        q.setAnswerImagePosition(qnadto.answerImage.position);
                        q.setAnswerImageScale(qnadto.answerImage.scale);
                    }
                    qnaRepo.save(q);
                }
            }
        }


    }
}
