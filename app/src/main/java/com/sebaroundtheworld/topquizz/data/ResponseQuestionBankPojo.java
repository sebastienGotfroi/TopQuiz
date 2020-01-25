package com.sebaroundtheworld.topquizz.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseQuestionBankPojo {

    @SerializedName("response_code")
    @Expose
    private int responseCode;

    @SerializedName("results")
    @Expose
    private List<QuestionBankPojo> mQuestionBankPojos;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public List<QuestionBankPojo> getQuestionBankPojos() {
        return mQuestionBankPojos;
    }

    public void setQuestionBankPojos(List<QuestionBankPojo> questionBankPojos) {
        mQuestionBankPojos = questionBankPojos;
    }
}
