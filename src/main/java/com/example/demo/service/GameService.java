package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.GameDTO;

import model.Category;
import model.Game;
import model.QnA;
import repo.CategoryRepo;
import repo.GameRepo;
import repo.QnARepo;

@Service
public class GameService {

    @Autowired
    private GameRepo gameRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private QnARepo qnaRepo;

    // Save full game structure
    public String saveGame(GameDTO dto) {

        // Create Game entry
        // TODO: add attribute for gameName later
        Game game = new Game();
        String gameId = "G" + String.format("%05d", (int) (Math.random() * 100000)); // Generate GameId here
        game.setGameId(gameId);
        game.setUserId(dto.getUserId());
        gameRepo.save(game);

        // Save categories
        dto.getCategories().forEach(catDTO -> {
            Category category = new Category();
            category.setCategoryId("C" + String.format("%05d", (int) (Math.random() * 100000)));
            category.setCategoryName(catDTO.getCategoryName());
            category.setBkgColor(catDTO.getBkgColor());
            category.setTextColor(catDTO.getTextColor());
            category.setGameId(gameId);
            categoryRepo.save(category);

            // Save QnA for this category
            catDTO.getQna().forEach(qnaDTO -> {
                QnA q = new QnA();
                q.setQnaId("Q" + String.format("%05d", (int) (Math.random() * 100000)));
                q.setCategoryId(category.getCategoryId());
                q.setPtValue(qnaDTO.getPtValue());
                q.setQuestionText(qnaDTO.getQuestionText());
                q.setAnswerText(qnaDTO.getAnswerText());

                // Question image
                if (qnaDTO.getQuestionImage() != null) {
                    q.setQuestionImageUrl(qnaDTO.getQuestionImage().getUrl());
                    q.setQuestionImagePosition(qnaDTO.getQuestionImage().getPosition());
                    q.setQuestionImageScale(qnaDTO.getQuestionImage().getScale());
                }

                // Answer image
                if (qnaDTO.getAnswerImage() != null) {
                    q.setAnswerImageUrl(qnaDTO.getAnswerImage().getUrl());
                    q.setAnswerImagePosition(qnaDTO.getAnswerImage().getPosition());
                    q.setAnswerImageScale(qnaDTO.getAnswerImage().getScale());
                }
                qnaRepo.save(q);
            });
        });

        return gameId;
    }

    // Load games for logged in user
    public List<Game> getGamesByUser(String userId) {
        return gameRepo.findByUserId(userId);
    }

    // delete entire game
    public void deleteGame(String gameId) {
        gameRepo.deleteById(gameId);
    }

    // retrieve data to contiune editing the game when coming back 
    public GameFullDTO loadFullGame(String gameId) {
        Game game = gameRepo.findById(gameId).orElseThrow();

        List<Category> categories = categoryRepo.findByGameId(gameId);

        GameFullDTO dto = new GameFullDTO();
        dto.gameId = game.getGameId();
        dto.userId = game.getUserId();

        for (Category c : categories) {
            GameFullDTO.CategoryDTO catDTO = new GameFullDTO.CategoryDTO();
            catDTO.categoryId = c.getCategoryId();
            catDTO.categoryName = c.getCategoryName();
            catDTO.bkgColor = c.getBkgColor();
            catDTO.textColor = c.getTextColor();

            List<QnA> qnas = qnaRepo.findByCategoryId(c.getCategoryId());
            for (QnA q : qnas) {
                GameFullDTO.QnADTO qdto = new GameFullDTO.QnADTO();
                qdto.pointValue = q.getPointValue();
                qdto.question = q.getQuestion();
                qdto.answer = q.getAnswer();
                qdto.questionImageUrl = q.getQuestionImageUrl();
                qdto.answerImageUrl = q.getAnswerImageUrl();
                catDTO.qna.add(qdto);
            }

            dto.categories.add(catDTO);
        }

        return dto;
    }

    // Function to grab questions to present 
    public GamePlayDTO loadGameForPlay(String gameId) {
        GamePlayDTO dto = new GamePlayDTO();

        List<Category> categories = categoryRepo.findByGameId(gameId);

        for (Category c : categories) {
            GamePlayDTO.CategoryDTO catDTO = new GamePlayDTO.CategoryDTO();
            catDTO.categoryName = c.getCategoryName();

            List<QnA> qnas = qnaRepo.findByCategoryId(c.getCategoryId());
            qnas.sort(Comparator.comparingInt(QnA::getPointValue));

            for (QnA q : qnas) {
                GamePlayDTO.QnADTO qdto = new GamePlayDTO.QnADTO();
                qdto.pointValue = q.getPointValue();
                qdto.question = q.getQuestion();
                qdto.answer = q.getAnswer();
                qdto.questionImageUrl = q.getQuestionImageUrl();
                qdto.answerImageUrl = q.getAnswerImageUrl();
                catDTO.qna.add(qdto);
            }

            dto.categories.add(catDTO);
        }

        return dto;
    }

}