package com.sebaroundtheworld.topquizz.model;

import java.util.ArrayList;
import java.util.List;

public class Question {

    private String mQuestion;
    private List<String> mChoices;
    private Integer mAnswerInd;

    public Question(String question, List<String> choices, Integer answerInd) {
        mQuestion = question;
        mChoices = choices;
        mAnswerInd = answerInd;
    }

    public Question (){
        mChoices = new ArrayList<String>();
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String question) {
        mQuestion = question;
    }

    public List<String> getChoices() {
        return mChoices;
    }

    public void setChoices(List<String> choices) {
        if(choices != null && !choices.isEmpty()){
            mChoices = choices;
        }
    }

    public Integer getAnswerInd() {
        return mAnswerInd;
    }

    public void setAnswerInd(Integer answerInd) {
        mAnswerInd = answerInd;
    }
}
