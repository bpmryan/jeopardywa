/*
 * GameDTO.java
 * Purpose: Data Transfer Object representing the full game payload sent
 * between frontend and backend for create/update operations. Contains nested
 * structures for categories and QnA items.
 */
package com.example.demo.dto;

import java.util.List;

public class GameDTO {
    // handles saving everything in gameContent.html (saveAll function in
    // gameCreate.js)
    // DTO : Data Transfer Object

    private String userId;
    private String gameId;
    private String gameName;
    private List<CategoryDTO> categories;

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getGameId() {
        return gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    // Setters
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }

    // category information being sent
    public static class CategoryDTO {
        private String categoryId;
        private String categoryName;
        private String bkgColor;
        private String textColor;
        private List<QnADTO> qna;

        public String getCategoryId() {
            return categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public String getBkgColor() {
            return bkgColor;
        }

        public String getTextColor() {
            return textColor;
        }

        public List<QnADTO> getQna() {
            return qna;
        }

        public void setCategoryId(String id) {
            this.categoryId = id;
        }

        public void setCategoryName(String n) {
            this.categoryName = n;
        }

        public void setBkgColor(String c) {
            this.bkgColor = c;
        }

        public void setTextColor(String c) {
            this.textColor = c;
        }

        public void setQna(List<QnADTO> qna) {
            this.qna = qna;
        }
    }

    // qna information being sent
    public static class QnADTO {
        private String qnaId;
        private Integer ptValue;
        private String questionText;
        private String answerText;
        private ImageDTO questionImage;
        private ImageDTO answerImage;

        public String getQnaId() {
            return qnaId;
        }

        public Integer getPtValue() {
            return ptValue;
        }

        public String getQuestionText() {
            return questionText;
        }

        public String getAnswerText() {
            return answerText;
        }

        public ImageDTO getQuestionImage() {
            return questionImage;
        }

        public ImageDTO getAnswerImage() {
            return answerImage;
        }

        public void setQnaId(String id) {
            this.qnaId = id;
        }

        public void setPtValue(Integer val) {
            this.ptValue = val;
        }

        public void setQuestionText(String t) {
            this.questionText = t;
        }

        public void setAnswerText(String t) {
            this.answerText = t;
        }

        public void setQuestionImage(ImageDTO img) {
            this.questionImage = img;
        }

        public void setAnswerImage(ImageDTO img) {
            this.answerImage = img;
        }
    }

    // image information being sent
    // TODO: double check if this or any of the attributes are correctly named
    public static class ImageDTO {
        private String url;
        private String position;
        private String scale;

        public String getUrl() {
            return url;
        }

        public String getPosition() {
            return position;
        }

        public String getScale() {
            return scale;
        }

        public void setUrl(String u) {
            this.url = u;
        }

        public void setPosition(String p) {
            this.position = p;
        }

        public void setScale(String s) {
            this.scale = s;
        }
    }
}
