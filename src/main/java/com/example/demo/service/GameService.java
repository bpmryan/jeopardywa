package com.example.demo.service;

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
        Game game = new Game();
        String gameId = "G" + String.format("%05d", (int)(Math.random() * 100000)); // Generate GameId here
        game.setGameId(gameId);
        game.setUserId(dto.getUserId());
        gameRepo.save(game);

        // Save categories
        dto.getCategories().forEach(catDTO -> {
            Category category = new Category();
            category.setCategoryId("C" + String.format("%05d", (int)(Math.random() * 100000)));
            category.setCategoryName(catDTO.getCategoryName());
            category.setBkgColor(catDTO.getBkgColor());
            category.setTextColor(catDTO.getTextColor());
            category.setGameId(gameId);
            categoryRepo.save(category);

            // Save QnA for this category
            catDTO.getQna().forEach(qnaDTO -> {
                QnA q = new QnA();
                q.setQnaId("Q" + String.format("%05d", (int)(Math.random() * 100000)));
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
}