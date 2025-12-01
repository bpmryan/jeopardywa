package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "JeopardyCategory")
public class Category {
    // private methods to write to db (name: JeopardyCategory)
    @Id
    private String categoryId;
    private String gameId;

    private String qnaId;
    private Integer ptValue;
    private String questionText;
    private String answerText;

    private String questionImageUrl;
    private String questionImagePosition;
    private String questionImageScale;

    private String answerImageUrl;
    private String answerImagePosition;
    private String answerImageScale;

    // spring boot apparently needs no-args constructor
    public Category() {
    }

    // getters and setters to write, update, and retrieve data about each category
    // in their respective jeopardies
    // If there is an error writing to the db, check if the names match the table
    // attributes/columns

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getQnaId() {
        return qnaId;
    }

    public void setQnaId(String qnaId) {
        this.qnaId = qnaId;
    }

    public Integer getPtValue() {
        return ptValue;
    }

    public void setPtValue(Integer ptValue) {
        this.ptValue = ptValue;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public String getQuestionImageUrl() {
        return questionImageUrl;
    }

    public void setQuestionImageUrl(String questionImageUrl) {
        this.questionImageUrl = questionImageUrl;
    }

    public String getQuestionImagePosition() {
        return questionImagePosition;
    }

    public void setQuestionImagePosition(String questionImagePosition) {
        this.questionImagePosition = questionImagePosition;
    }

    public String getQuestionImageScale() {
        return questionImageScale;
    }

    public void setQuestionImageScale(String questionImageScale) {
        this.questionImageScale = questionImageScale;
    }

    public String getAnswerImageUrl() {
        return answerImageUrl;
    }

    public void setAnswerImageUrl(String answerImageUrl) {
        this.answerImageUrl = answerImageUrl;
    }

    public String getAnswerImagePosition() {
        return answerImagePosition;
    }

    public void setAnswerImagePosition(String answerImagePosition) {
        this.answerImagePosition = answerImagePosition;
    }

    public String getAnswerImageScale() {
        return answerImageScale;
    }

    public void setAnswerImageScale(String answerImageScale) {
        this.answerImageScale = answerImageScale;
    }
    
}
