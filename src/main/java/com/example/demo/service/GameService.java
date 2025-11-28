package com.example.demo.service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

        Game game;
        String gameId;

        // Create Game entry
        // TODO: add attribute for gameName later
        // CREATE MODE
        if (dto.getGameId() == null || dto.getGameId().isBlank()) {
            gameId = "G" + String.format("%05d", (int) (Math.random() * 100000));
            game = new Game();
            game.setGameId(gameId);
            game.setUserId(dto.getUserId());
        }
        // UPDATE MODE
        else {
            gameId = dto.getGameId();
            game = gameRepo.findById(gameId)
                    .orElseThrow(() -> new RuntimeException("Game not found: " + gameId));
        }

        game.setGameName(dto.getGameName());
        gameRepo.save(game);

        // Track categoryIds to remove usused ones
        List<Category> existingCategories = categoryRepo.findByGameId(gameId);
        Set<String> incomingCategoryIds = new HashSet<>();

        for (GameDTO.CategoryDTO catDTO : dto.getCategories()) {
            Category category;

            // UPDATE existing category
            if (catDTO.getCategoryId() != null && !catDTO.getCategoryId().isBlank()) {
                category = categoryRepo.findById(catDTO.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Category missing: " + catDTO.getCategoryId()));
            }
            // CREATE new category
            else {
                category = new Category();
                category.setCategoryId("C" + String.format("%05d", (int) (Math.random() * 100000)));
                category.setGameId(gameId);
            }

            category.setCategoryName(catDTO.getCategoryName());
            category.setBkgColor(catDTO.getBkgColor());
            category.setTextColor(catDTO.getTextColor());
            categoryRepo.save(category);

            incomingCategoryIds.add(category.getCategoryId());

            // ----- PROCESS QNA -----
            List<QnA> existingQnA = qnaRepo.findByCategoryId(category.getCategoryId());
            Set<String> incomingQnAIds = new HashSet<>();

            for (GameDTO.QnADTO qdto : catDTO.getQna()) {

                QnA q;

                // UPDATE existing QnA
                if (qdto.getQnaId() != null && !qdto.getQnaId().isBlank()) {
                    q = qnaRepo.findById(qdto.getQnaId())
                            .orElseThrow(() -> new RuntimeException("Missing QnA: " + qdto.getQnaId()));
                }
                // CREATE new QnA
                else {
                    q = new QnA();
                    q.setQnaId("Q" + String.format("%05d", (int) (Math.random() * 100000)));
                    q.setCategoryId(category.getCategoryId());
                }

                q.setPtValue(qdto.getPtValue());
                q.setQuestionText(qdto.getQuestionText());
                q.setAnswerText(qdto.getAnswerText());

                if (qdto.getQuestionImage() != null) {
                    q.setQuestionImageUrl(qdto.getQuestionImage().getUrl());
                    q.setQuestionImagePosition(qdto.getQuestionImage().getPosition());
                    q.setQuestionImageScale(qdto.getQuestionImage().getScale());
                }

                if (qdto.getAnswerImage() != null) {
                    q.setAnswerImageUrl(qdto.getAnswerImage().getUrl());
                    q.setAnswerImagePosition(qdto.getAnswerImage().getPosition());
                    q.setAnswerImageScale(qdto.getAnswerImage().getScale());
                }

                qnaRepo.save(q);
                incomingQnAIds.add(q.getQnaId());
            }

            // DELETE removed QnA
            for (QnA old : existingQnA) {
                if (!incomingQnAIds.contains(old.getQnaId())) {
                    qnaRepo.delete(old);
                }
            }
        }

        // DELETE removed categories
        for (Category oldCat : existingCategories) {
            if (!incomingCategoryIds.contains(oldCat.getCategoryId())) {
                qnaRepo.deleteByCategoryId(oldCat.getCategoryId());
                categoryRepo.delete(oldCat);
            }
        }

        return gameId;
    }

    // Load list of games for dashboard
    public List<Game> getGamesByUser(String userId) {
        return gameRepo.findByUserId(userId);
    }

    // Delete entire game
    public void deleteGame(String gameId) {
        List<Category> cats = categoryRepo.findByGameId(gameId);
        for (Category c : cats) {
            qnaRepo.deleteByCategoryId(c.getCategoryId());
            categoryRepo.delete(c);
        }
        gameRepo.deleteById(gameId);
    }

    // LOAD FULL GAME FOR EDIT MODE
    public GameFullDTO loadFullGame(String gameId) {
        Game g = gameRepo.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        GameFullDTO out = new GameFullDTO();
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
                qdto.answerImageUrl = q.getAnswerImageUrl();
                cat.qna.add(qdto);
            }
            out.categories.add(cat);
        }

        return out;
    }

    // LOAD GAME FOR PLAY MODE
    public GamePlayDTO loadGameForPlay(String gameId) {
        Game g = gameRepo.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        GamePlayDTO dto = new GamePlayDTO();
        dto.gameId = g.getGameId();
        dto.gameName = g.getGameName();

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

            dto.categories.add(cat);
        }

        return dto;

    }
}