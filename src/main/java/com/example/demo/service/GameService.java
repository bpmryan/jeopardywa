/*
 * GameService.java
 * Purpose: Core business logic to create, update, delete and load full game
 * structures including categories and QnA rows.
 *
 * Primary responsibilities:
 *  - saveGame(GameDTO): create or update a full game payload (creates ids,
 *    reconciles existing categories/QnA, deletes removed entries)
 *  - deleteGame(gameId): delete a game and all nested categories/QnA
 *  - loadFullGame(gameId): load a deep representation for editing
 *  - loadGameForPlay(gameId): load a trimmed representation for presenting/playing
 *  - getGamesForDashboard(userId): lightweight list of games for dashboard
 */
package com.example.demo.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.GameDTO;
import com.example.demo.dto.GameFullDTO;
import com.example.demo.dto.GamePlayDTO;
import com.example.demo.model.Category;
import com.example.demo.model.Game;
import com.example.demo.model.QnA;
import com.example.demo.repo.CategoryRepo;
import com.example.demo.repo.GameRepo;
import com.example.demo.repo.QnARepo;

@Service
public class GameService {

    @Autowired
    private GameRepo gameRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private QnARepo qnaRepo;

    /**
     * Save a full game structure. Handles CREATE and UPDATE.
     * - If gameId is missing or blank -> create new Game.
     * - If gameId present -> update existing Game and reconcile categories/qna
     * (delete removed).
     *
     * The incoming GameDTO structure expected:
     * {
     * userId, gameId (optional for create), gameName,
     * categories: [
     * { categoryId (optional), categoryName, bkgColor, textColor, qna: [
     * { qnaId (optional), ptValue, questionText, answerText, questionImage,
     * answerImage }
     * ]}
     * ]
     * }
     */

    // Create or update entire game. Keeps DB in sync with incoming DTO.
    @Transactional
    public String saveGame(GameDTO dto) {
        Game game;
        // determines whether the game needs to be updated or created

        /**
         * sql code translation:
         * 
         * INSERT INTO Game (gameId, userId, gameName)
         * VALUES (?, ?, ?);
         * 
         */
        String incomingId = dto.getGameId();
        if (incomingId == null || incomingId.trim().isEmpty()) {
            game = new Game();
            game.setGameId(UUID.randomUUID().toString());
            game.setUserId(dto.getUserId());
        }
        // update game
        // else checks for if there is an existing gameId
        else {
            game = gameRepo.findById(dto.getGameId())
                    .orElseThrow(() -> new RuntimeException("Game not found: " + dto.getGameId()));
            game.setUserId(dto.getUserId());
        }

        String gameId = game.getGameId();
        System.out.println("Final Game ID before save: " + game.getGameId());

        // sets the name of the jeopardy (can return null)
        // update basic fields
        game.setGameName(dto.getGameName());
        gameRepo.save(game);

        // fetch existing categories (via categorgId) for removal detection
        /**
         * sql code translation:
         * 
         * INSERT INTO JeopardyCategory (categoryId, gameId, categoryName, bkgColor,
         * textColor)
         * VALUES (?, ?, ?, ?, ?);
         * 
         */
        List<Category> existingCategories = categoryRepo.findByGameId(gameId);
        Set<String> incomingCategoryIds = new HashSet<>();

        // iterate incoming categories
        if (dto.getCategories() != null) {
            for (GameDTO.CategoryDTO catDTO : dto.getCategories()) {

                Category category;

                // update mode for category
                if (catDTO.getCategoryId() != null && !catDTO.getCategoryId().isBlank()) {
                    category = categoryRepo.findById(catDTO.getCategoryId())
                            .orElseThrow(() -> new RuntimeException("Category missing: " + catDTO.getCategoryId()));
                } else {
                    // create new categoryId and category
                    category = new Category();
                    category.setCategoryId(UUID.randomUUID().toString());
                }

                // set category fields
                category.setGameId(gameId);
                category.setCategoryName(catDTO.getCategoryName());
                category.setBkgColor(catDTO.getBkgColor());
                category.setTextColor(catDTO.getTextColor());
                categoryRepo.save(category);

                incomingCategoryIds.add(category.getCategoryId());

                // process QnA inside category
                // reconcile qna inside this category
                /**
                 * sql code translation:
                 * 
                 * INSERT INTO QnAInfo (
                 * qnaId, categoryId, ptValue,
                 * questionText, answerText,
                 * questionImageUrl, answerImageUrl
                 * )
                 * VALUES (?, ?, ?, ?, ?, ?, ?);
                 * 
                 */
                List<QnA> existingQnA = qnaRepo.findByCategoryId(category.getCategoryId());
                Set<String> incomingQnAIds = new HashSet<>();

                if (catDTO.getQna() != null) {
                    for (GameDTO.QnADTO qdto : catDTO.getQna()) {

                        QnA q;
                        // function is similar to category section above
                        // update existing QnA
                        if (qdto.getQnaId() != null && !qdto.getQnaId().isBlank()) {
                            q = qnaRepo.findById(qdto.getQnaId())
                                    .orElseThrow(() -> new RuntimeException("QnA missing: " + qdto.getQnaId()));
                        }
                        // create new QnAId and QnA
                        else {
                            q = new QnA();
                            q.setQnaId(UUID.randomUUID().toString());
                        }

                        // required to have otherwise the program crashes
                        q.setCategoryId(category.getCategoryId());

                        // map DTO -> entity (null safety)
                        q.setPtValue(qdto.getPtValue() == null ? 0 : qdto.getPtValue());
                        q.setQuestionText(qdto.getQuestionText());
                        q.setAnswerText(qdto.getAnswerText());

                        // allows user to not have to add a question/answer image url
                        // also clears the db entries for it when cleared
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
                }

                /**
                 * sql code translation:
                 * 
                 * DELETE FROM QnAInfo
                 * WHERE categoryId IN (
                 * SELECT categoryId
                 * FROM JeopardyCategory
                 * WHERE gameId = ?
                 * );
                 * 
                 * 
                 */
                // delete removed QnA in respective category
                for (QnA old : existingQnA) {
                    if (!incomingQnAIds.contains(old.getQnaId())) {
                        qnaRepo.delete(old);
                    }
                }
            }
        }

        // delete removed categories along with their qna
        // delete removed categories
        /**
         * sql code translation:
         * 
         * DELETE FROM JeopardyCategory
         * WHERE gameId = ?;
         */
        for (Category oldCat : existingCategories) {
            if (!incomingCategoryIds.contains(oldCat.getCategoryId())) {
                qnaRepo.deleteByCategoryId(oldCat.getCategoryId());
                categoryRepo.delete(oldCat);
            }
        }
        return gameId;
    }

    // Delete entire game along with category and qna info for it
    @Transactional
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
                qdto.ptValue = q.getPtValue();
                qdto.questionText = q.getQuestionText();
                qdto.answerText = q.getAnswerText();
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
                qdto.ptValue = q.getPtValue();
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

    public List<GameFullDTO> getGamesForDashboard(String userId) {
        List<Game> games = gameRepo.findByUserId(userId);
        return games.stream().map(g -> {
            GameFullDTO dto = new GameFullDTO();
            dto.gameId = g.getGameId();
            dto.userId = g.getUserId();
            dto.gameName = g.getGameName();
            dto.categories = List.of(); // dashboard doesn’t need deep data
            return dto;
        }).toList();
    }

}
