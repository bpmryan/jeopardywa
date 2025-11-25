package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

public class GameFullDTO {
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
        public Integer pointValue;
        public String question;
        public String answer;
        public String questionImageUrl;
        public String questionImagePosition;
        public String questionImageScale;
        public String answerImageUrl;
        public String answerImagePosition;
        public String answerImageScale;
    }
}

