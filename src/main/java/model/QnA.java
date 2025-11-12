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
    private int pointValue;
    private String question;
    private String answer;
    private String imageUrl;
    private String imagePosition;
    private String imageScale;

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
    public int getPointValue() {
        return pointValue;
    }
    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }
    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getImagePosition() {
        return imagePosition;
    }
    public void setImagePosition(String imagePosition) {
        this.imagePosition = imagePosition;
    }
    public String getImageScale() {
        return imageScale;
    }
    public void setImageScale(String imageScale) {
        this.imageScale = imageScale;
    }


}
