package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "QnAInfo")
public class QnA {

    // attributes to send to the db (atttribute: QnAInfo)
    // Go back and fix the attributes, missing questionId and answerId
    @Id
    private String qnaId;
    private String categoryId;
    private int ptValue;
    private String questionId;
    private String answerId; 
    private String questionText;
    private String answerText;
    private String questionImageUrl;
    private String questionImagePosition;
    private String questionImageScale;
    private String answerImageUrl;
    private String answerImagePosition;
    private String answerImageScale;

    // Apparently spring boot need no-args constructors to run
    public QnA() {}

    // getters and setters
    public String getQnaId() {
        return qnaId;
    }
    public void setQnaId(String qnaId) {
        this.qnaId = qnaId;
    }
    public String getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    public int getPtValue() {
        return ptValue;
    }
    public void setPtValue(int pointValue) {
        this.ptValue = pointValue;
    }
    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }
    public String getQuestionText() {
        return questionText;
    }
    public void setQuestionText(String question) {
        this.questionText = question;
    }
    public String getAnswerText() {
        return answerText;
    }
    public void setAnswerText(String answer) {
        this.answerText = answer;
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
