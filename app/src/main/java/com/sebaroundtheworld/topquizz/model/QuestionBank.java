package com.sebaroundtheworld.topquizz.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class QuestionBank implements Serializable {

    private List<Question> mQuestions;
    private Integer mCurrentQuestionIndex;

    public QuestionBank (List<Question> listQuestion) {

        mCurrentQuestionIndex = 0;
        mQuestions = listQuestion;

        //shuffle question
        Collections.shuffle(mQuestions);
    }

    public Question getQuestion() {
        if(mCurrentQuestionIndex < mQuestions.size()) {
            return mQuestions.get(mCurrentQuestionIndex);
        }
        return null;
    }

    public void chargeNextQuestion(){
        mCurrentQuestionIndex++;
    }

    public List<Question> getQuestions() {
        return mQuestions;
    }

    public void setQuestions(List<Question> questions) {
        mQuestions = questions;
    }

    public Integer getCurrentQuestionIndex() {
        return mCurrentQuestionIndex;
    }
}
