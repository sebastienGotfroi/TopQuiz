package com.sebaroundtheworld.topquizz.data;

import androidx.annotation.Nullable;

import com.sebaroundtheworld.topquizz.model.QuestionType;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizzDataAPICalls {

    public interface Callbacks {
        void onResponse(@Nullable ResponseQuestionBankPojo users);
        void onFailure();
    }

    public static void getQuestionBank(Callbacks callbacks, QuestionType questionType) {

        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<Callbacks>(callbacks);

        QuizzAPIService quizzAPIService = QuizzAPIService.retrofit.create(QuizzAPIService.class);

        Call<ResponseQuestionBankPojo> call = quizzAPIService.getQuestions( String.valueOf(questionType.getNumberOfQuestion()),
                                                                            String.valueOf(questionType.getCategory()),
                                                                                    questionType.getDifficulty());
        call.enqueue(new Callback<ResponseQuestionBankPojo>() {

            @Override
            public void onResponse(Call<ResponseQuestionBankPojo> call, Response<ResponseQuestionBankPojo> response) {
                if (callbacksWeakReference.get() != null) {
                    callbacksWeakReference.get().onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseQuestionBankPojo> call, Throwable t) {
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }
}
