package com.sebaroundtheworld.topquizz.data;

import com.sebaroundtheworld.topquizz.model.QuestionBank;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QuizzAPIService {

    @GET("api.php?category=9&type=multiple")
    Call<ResponseQuestionBankPojo> getQuestions(@Query("amount") String numberOfQuestions,
                                                @Query("category") String category,
                                                @Query("difficulty") String difficulty);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://opentdb.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}

