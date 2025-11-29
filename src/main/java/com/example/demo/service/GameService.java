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

import jakarta.transaction.Transactional;
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

    // Create or update entire game. Keeps DB in sync with incoming DTO.
    @Transactional
    public String saveGame(GameDTO dto) {

        // determines whether the game needs to be updated or created
        Game game;
        String gameId;

        // Create game
        // generate gameId 
        if (dto.getGameId() == null || dto.getGameId().isBlank()) {
            gameId = "G" + String.format("%05d", (int) (Math.random() * 100000));
            game = new Game();
            game.setGameId(gameId);
            game.setUserId(dto.getUserId());
        }
        // update game
        // else checks for if there is an existing gameId 
        else {
            gameId = dto.getGameId();
            game = gameRepo.findById(gameId)
                    .orElseThrow(() -> new RuntimeException("Game not found: " + gameId));
        }

        // sets the name of the jeopardy (can return null)
        game.setGameName(dto.getGameName());
        gameRepo.save(game);

        // fetch existing categories (via categorgId) for removal detection
        List<Category> existingCategories = categoryRepo.findByGameId(gameId);
        Set<String> incomingCategoryIds = new HashSet<>();

        // iterate incoming categories
        for (GameDTO.CategoryDTO catDTO : dto.getCategories()) {
            Category category;

            // update mode for category
            if (catDTO.getCategoryId() != null && !catDTO.getCategoryId().isBlank()) {
                category = categoryRepo.findById(catDTO.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Category missing: " + catDTO.getCategoryId()));
            }
            // create new categoryId and category
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

            // process QnA inside category
            List<QnA> existingQnA = qnaRepo.findByCategoryId(category.getCategoryId());
            Set<String> incomingQnAIds = new HashSet<>();

            for (GameDTO.QnADTO qdto : catDTO.getQna()) {
                QnA q;

                // function is similar to category section above
                // update existing QnA
                if (qdto.getQnaId() != null && !qdto.getQnaId().isBlank()) {
                    q = qnaRepo.findById(qdto.getQnaId())
                            .orElseThrow(() -> new RuntimeException("Missing QnA: " + qdto.getQnaId()));
                }
                // create new QnAId and QnA
                else {
                    q = new QnA();
                    q.setQnaId("Q" + String.format("%05d", (int) (Math.random() * 100000)));
                    q.setCategoryId(category.getCategoryId());
                }

                // map DTO -> entity 
                q.setPtValue(qdto.getPtValue());
                q.setQuestionText(qdto.getQuestionText());
                q.setAnswerText(qdto.getAnswerText());

                if (qdto.getQuestionImage() != null) {
                    q.setQuestionImageUrl(qdto.getQuestionImage().getUrl());
                    q.setQuestionImagePosition(qdto.getQuestionImage().getPosition());
                    q.setQuestionImageScale(qdto.getQuestionImage().getScale());
                } else {
                    q.setQuestionImageUrl(null);
                    q.setQuestionImagePosition(null);
                    q.setQuestionImageScale(null);
                }

                if (qdto.getAnswerImage() != null) {
                    q.setAnswerImageUrl(qdto.getAnswerImage().getUrl());
                    q.setAnswerImagePosition(qdto.getAnswerImage().getPosition());
                    q.setAnswerImageScale(qdto.getAnswerImage().getScale());
                } else {
                    q.setAnswerImageUrl(null);
                    q.setAnswerImagePosition(null);
                    q.setAnswerImageScale(null);
                }

                qnaRepo.save(q);
                incomingQnAIds.add(q.getQnaId());
            }

            // delete removed QnA in respective category
            for (QnA old : existingQnA) {
                if (!incomingQnAIds.contains(old.getQnaId())) {
                    qnaRepo.delete(old);
                }
            }
        }

        // delete removed categories along with their qna
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

    // Delete entire game along with category and qna info for it 
    public void deleteGame(String gameId) {
        List<Category> cats = categoryRepo.findByGameId(gameId);
        for (Category c : cats) {
            qnaRepo.deleteByCategoryId(c.getCategoryId());
            categoryRepo.delete(c);
        }
        gameRepo.deleteById(gameId);
    }

    // loads full game for the user to edit 
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

    // load game for user to present/play their jeopardy
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