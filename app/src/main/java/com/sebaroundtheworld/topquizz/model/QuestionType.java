package com.sebaroundtheworld.topquizz.model;

import java.io.Serializable;

public class QuestionType implements Serializable {

    private int mNumberOfQuestion;
    private int mCategory;
    private String mDifficulty;

    public int getNumberOfQuestion() {
        return mNumberOfQuestion;
    }

    public void setNumberOfQuestion(int numberOfQuestion) {
        mNumberOfQuestion = numberOfQuestion;
    }

    public int getCategory() {
        return mCategory;
    }

    public void setCategory(int category) {
        mCategory = category;
    }

    public String getDifficulty() {
        return mDifficulty;
    }

    public void setDifficulty(String difficulty) {
        mDifficulty = difficulty;
    }
}
