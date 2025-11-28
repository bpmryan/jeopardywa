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
            gameId = "G" + String.format("%05d", (int)(Math.random() * 100000));
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

        // --- UPDATE QnA ---
            List<QnA> existingQnA = qnaRepo.findByCategoryId(category.getCategoryId());
            Set<String> incomingQnAIds = new HashSet<>();

            for (GameDTO.QnADTO qdto : catDTO.getQna()) {
                QnA q;

                if (qdto.getQnAId() != null && !qdto.getQnaId().isBlank()) {
                    q = qnaRepo.findById(qdto.getQnaId())
                            .orElseThrow(() -> new RuntimeException("Missing QnA: " + qdto.getQnaId()));
                } else {
                    q = new QnA();
                    q.setQnaId("Q" + String.format("%05d", (int)(Math.random() * 100000)));
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
        for (Category oldCat : existingCats) {
            if (!incomingCatIds.contains(oldCat.getCategoryId())) {
                qnaRepo.deleteByCategoryId(oldCat.getCategoryId());
                categoryRepo.delete(oldCat);
            }
        }

        return gameId;
    }
}