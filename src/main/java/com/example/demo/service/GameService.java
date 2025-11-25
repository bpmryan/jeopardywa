package com.example.demo.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.GameDTO;
import com.example.demo.dto.GameFullDTO;
import com.example.demo.dto.GamePlayDTO;

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

    // Load full game for edit mode
    public GameFullDTO loadFullGame(String gameId) {
        GameFullDTO out = new GameFullDTO();
        Game g = gameRepo.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));
        out.gameId = g.getGameId();
        out.userId = g.getUserId();
        out.gameName = g.getGameName();

        List<Category> cats = categoryRepo.findByGameId(gameId);
        for (Category c : cats) {
            GameFullDTO.CategoryDTO cat = new GameFullDTO.CategoryDTO();
            cat.categoryId = c.getCategoryId();
            cat.categoryName = c.getCategoryName();
            cat.bkgColor = c.getBkgColor();
            cat.textColor = c.getTextColor();

            List<QnA> qnas = qnaRepo.findByCategoryId(c.getCategoryId());
            for (QnA q : qnas) {
                GameFullDTO.QnADTO qdto = new GameFullDTO.QnADTO();
                qdto.qnaId = q.getQnaId();
                qdto.pointValue = q.getPtValue();
                qdto.question = q.getQuestionText();
                qdto.answer = q.getAnswerText();
                qdto.questionImageUrl = q.getQuestionImageUrl();
                qdto.questionImagePosition = q.getQuestionImagePosition();
                qdto.questionImageScale = q.getQuestionImageScale();
                qdto.answerImageUrl = q.getAnswerImageUrl();
                qdto.answerImagePosition = q.getAnswerImagePosition();
                qdto.answerImageScale = q.getAnswerImageScale();
                cat.qna.add(qdto);
            }
            out.categories.add(cat);
        }
        return out;
    }

    // Load for play mode (arranged per category, sorted by pointValue asc)
    public GamePlayDTO loadGameForPlay(String gameId) {
        GamePlayDTO out = new GamePlayDTO();
        Game g = gameRepo.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));
        out.gameId = g.getGameId();
        out.gameName = g.getGameName();

        List<Category> cats = categoryRepo.findByGameId(gameId);
        for (Category c : cats) {
            GamePlayDTO.CategoryDTO cat = new GamePlayDTO.CategoryDTO();
            cat.categoryId = c.getCategoryId();
            cat.categoryName = c.getCategoryName();
            List<QnA> qnas = qnaRepo.findByCategoryId(c.getCategoryId())
                        .stream()
                        .sorted(Comparator.comparingInt(QnA::getPtValue))
                        .collect(Collectors.toList());
            for (QnA q : qnas) {
                GamePlayDTO.QnADTO qdto = new GamePlayDTO.QnADTO();
                qdto.qnaId = q.getQnaId();
                qdto.pointValue = q.getPtValue();
                qdto.question = q.getQuestionText();
                qdto.answer = q.getAnswerText();
                qdto.questionImageUrl = q.getQuestionImageUrl();
                qdto.answerImageUrl = q.getAnswerImageUrl();
                cat.qna.add(qdto);
            }
            out.categories.add(cat);
        }
        return out;
    }
}