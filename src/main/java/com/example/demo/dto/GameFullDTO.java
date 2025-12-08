/*
 * GameFullDTO.java
 * Purpose: DTO used to send a fully-populated game (including categories and
 * QnA) to the frontend when editing a game. Mirrors the structure created by
 * GameService.loadFullGame.
 */
package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

public class GameFullDTO {
    // handles loading for editing (gameContent.html? gameId=)
    public String gameId;
    public String userId;
    public String gameName;
    public List<CategoryDTO> categories = new ArrayList<>();

    public static class CategoryDTO {
        public String categoryId;
        public String categoryName;
        public String bkgColor;
        public String textColor;
        public List<QnADTO> qna = new ArrayList<>();
    }

    // TODO: make sure that answer and question have ids too
    public static class QnADTO {
        public String qnaId;
        public Integer ptValue;
        public String questionText;
        public String answerText;

        public String questionImageUrl;
        public String questionImagePosition;
        public String questionImageScale;

        public String answerImageUrl;
        public String answerImagePosition;
        public String answerImageScale;
    }
}
