package com.example.demo.dto;

import java.util.List;

public class GameDTO {
    // DTO : Data Transfer Object

    private String userId;
    private String gameId;
    private List<CategoryDTO> categories;

    // Getters
    public String getUserId() { return userId; }
    public String getGameId() { return gameId; }
    public List<CategoryDTO> getCategories() { return categories; }

    // Setters
    public void setUserId(String userId) { this.userId = userId; }
    public void setGameId(String gameId) { this.gameId = gameId; }
    public void setCategories(List<CategoryDTO> categories) { this.categories = categories; }


    // category information being sent 
    public static class CategoryDTO {
        private String categoryName;
        private String bkgColor;
        private String textColor;
        private List<QnADTO> qna;

        public String getCategoryName() { return categoryName; }
        public String getBkgColor() { return bkgColor; }
        public String getTextColor() { return textColor; }
        public List<QnADTO> getQna() { return qna; }

        public void setCategoryName(String name) { this.categoryName = name; }
        public void setBkgColor(String c) { this.bkgColor = c; }
        public void setTextColor(String c) { this.textColor = c; }
        public void setQna(List<QnADTO> qna) { this.qna = qna; }
    }


    // qna information being sent
    public static class QnADTO {
        private Integer ptValue;
        private String questionText;
        private String answerText;
        private ImageDTO questionImage;
        private ImageDTO answerImage;

        public Integer getPtValue() { return ptValue; }
        public String getQuestionText() { return questionText; }
        public String getAnswerText() { return answerText; }
        public ImageDTO getQuestionImage() { return questionImage; }
        public ImageDTO getAnswerImage() { return answerImage; }

        public void setPtValue(Integer v) { this.ptValue = v; }
        public void setQuestionText(String t) { this.questionText = t; }
        public void setAnswerText(String t) { this.answerText = t; }
        public void setQuestionImage(ImageDTO img) { this.questionImage = img; }
        public void setAnswerImage(ImageDTO img) { this.answerImage = img; }
    }


    // image information being sent 
    // TODO: double check if this or any of the attributes are correctly namees and/or
    public static class ImageDTO {
        private String url;
        private String position;
        private String scale;

        public String getUrl() { return url; }
        public String getPosition() { return position; }
        public String getScale() { return scale; }

        public void setUrl(String url) { this.url = url; }
        public void setPosition(String pos) { this.position = pos; }
        public void setScale(String scale) { this.scale = scale; }
    }
}
