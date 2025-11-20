package com.example.demo.dto;

import java.util.List;

public class GameDTO {
    // DTO : Data Transfer Object

    public String gameId;
    public List<CatgeoryDTO> categories;

    public static class CatgeoryDTO {
        public String categoryName;
        public String bkgColor;
        public String textColor;
        public List<QnADTO> qna;
    }

    public static class QnADTO {
        public Integer ptValue;
        public String questionText;
        public String answerText;
        public ImageDTO questionImage;
        public ImageDTO answerImage;
    }

    public static class ImageDTO {
        public String url;
        public String position;
        public String scale;
    }
}
