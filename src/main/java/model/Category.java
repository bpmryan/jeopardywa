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
    private String categoryName;
    private String bkgColor;
    private String textColor;

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
      public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBkgColor() {
        return bkgColor;
    }

    public void setBkgColor(String bkgColor) {
        this.bkgColor = bkgColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

  

}
