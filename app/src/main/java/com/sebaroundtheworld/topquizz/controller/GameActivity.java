package com.sebaroundtheworld.topquizz.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sebaroundtheworld.topquizz.R;
import com.sebaroundtheworld.topquizz.data.QuestionData;
import com.sebaroundtheworld.topquizz.model.Question;
import com.sebaroundtheworld.topquizz.model.QuestionBank;
import com.sebaroundtheworld.topquizz.model.QuestionType;
import com.sebaroundtheworld.topquizz.model.User;


public class GameActivity extends AppCompatActivity implements View.OnClickListener, QuestionBankView {

    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";

    private QuestionData questionData;

    private TextView mQuestionText;
    private TextView mScoreText;
    private Button mAnswer1Button;
    private Button mAnswer2Button;
    private Button mAnswer3Button;
    private Button mAnswer4Button;
    private ProgressBar mProgressBar;

    private QuestionBank mQuestionBank;
    private Question mCurrentQuestion;
    private User mUser;
    private QuestionType mQuestionType;

    private boolean enableTouchEvent;

    private static int NUMBER_OF_QUESTION = 10;
    public static final String BUNDLE_STATE_SCORE = "currentScore";
    public static final String BUNDLE_STATE_QUESTION = "currentQuestion";
    public static final String BUNDLE_STATE_QUESTION_BANK = "QUESTION_BANK";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        enableTouchEvent = false;

        initWidgets();
        initListeners();
        initWithLastStateIfExist(savedInstanceState);

        mQuestionType = (QuestionType) getIntent().getSerializableExtra("QuestionType");

        //Should be a singleton
        questionData = new QuestionData(this);
        if (mQuestionBank == null) {
            mProgressBar.setVisibility(View.VISIBLE);
            questionData.getQuestions(mQuestionType);
        }
    }

    @Override
    public void onClick(final View v) {

        int backgroundColor;

        boolean isGoodAnswer = validateAnswer(Integer.parseInt(v.getTag().toString()), mCurrentQuestion);
        Toast.makeText(this, isGoodAnswer ? R.string.good_answer : R.string.wrong_answer, Toast.LENGTH_SHORT).show();

        mScoreText.setText(mUser.getScore());

        changeBackgroundColorOfTheAnswers(isGoodAnswer, v);

        if(!isGoodAnswer) {
            displayGoodAnswer();
        }

        enableTouchEvent = false;
        mQuestionBank.chargeNextQuestion();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!loadNextQuestionIfExist()){
                    endGame();
                }
                resetButtonBackgroundColor();
            }
        }, 2300);

    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        bundle.putInt(this.BUNDLE_STATE_SCORE, mUser.getMgoodAnswers());
        bundle.putInt(this.BUNDLE_STATE_QUESTION, mUser.getTotalAnswers());
        bundle.putSerializable(this.BUNDLE_STATE_QUESTION_BANK, mQuestionBank);
        super.onSaveInstanceState(bundle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return enableTouchEvent && super.dispatchTouchEvent(event);
    }

    @Override
    public void questionBankReady(QuestionBank questionBank) {
        mQuestionBank = questionBank;
        mProgressBar.setVisibility(View.GONE);
        loadNextQuestionIfExist();
    }

    private void initQuestionView (final Question question) {

        //best with a recycler view
        this.mQuestionText.setText(question.getQuestion());

        this.mAnswer1Button.setText(question.getChoices().get(0));
        this.mAnswer2Button.setText(question.getChoices().get(1));
        if(question.getChoices().size()>2) {
            this.mAnswer3Button.setText(question.getChoices().get(2));
            this.mAnswer3Button.setVisibility(View.VISIBLE);

            if(question.getChoices().size()>3){
                this.mAnswer4Button.setText(question.getChoices().get(3));
                this.mAnswer4Button.setVisibility(View.VISIBLE);
            }
        } else {
            this.mAnswer4Button.setVisibility(View.INVISIBLE);
            this.mAnswer3Button.setVisibility(View.INVISIBLE);
        }
    }

    private boolean validateAnswer (int indexAnswer, Question question) {

        if (question != null && question.getAnswerInd() <= question.getChoices().size()) {
            if(question.getAnswerInd() == indexAnswer) {
                mUser.addGoodAnswers();
            }
            else {
                mUser.addWrongAnswers();
            }
            return question.getAnswerInd() == indexAnswer;
        }
        return false;
    }

    private void displayGoodAnswer(){
        View goodAnswerButton;
        switch(mCurrentQuestion.getAnswerInd()) {
            case 0 : goodAnswerButton = mAnswer1Button;
                break;
            case 1 : goodAnswerButton = mAnswer2Button;
                break;
            case 2: goodAnswerButton = mAnswer3Button;
                break;
            default : goodAnswerButton = mAnswer4Button;
                break;
        }
        changeBackgroundColorOfTheAnswers(true, goodAnswerButton);
    }

    private boolean loadNextQuestionIfExist() {
        if(mUser.getTotalAnswers()< NUMBER_OF_QUESTION && mQuestionBank.getQuestions().size()> mUser.getTotalAnswers()) {
            mCurrentQuestion = mQuestionBank.getQuestion();
            initQuestionView(mCurrentQuestion);
            enableTouchEvent = true;
            return true;
        }
        return false;
    }

    private void endGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Good game!")
                .setMessage("Your score is " + mUser.getScore())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent result = new Intent();
                        result.putExtra(BUNDLE_EXTRA_SCORE, mUser.getScore());
                        setResult(RESULT_OK, result);
                        finish();
                    }
                })
                .create()
                .show();
    }

    private void initWidgets(){
        mQuestionText = (TextView) findViewById(R.id.activity_game_question_text);
        mScoreText = (TextView) findViewById(R.id.activity_game_score_text);
        mAnswer1Button = (Button) findViewById(R.id.activity_game_answer1_btn);
        mAnswer2Button = (Button) findViewById(R.id.activity_game_answer2_btn);
        mAnswer3Button = (Button) findViewById(R.id.activity_game_answer3_btn);
        mAnswer4Button = (Button) findViewById(R.id.activity_game_answer4_btn);
        mProgressBar = (ProgressBar) findViewById(R.id.activity_game_progress_bar);
    }

    private void initListeners() {
        mAnswer1Button.setOnClickListener(this);
        mAnswer2Button.setOnClickListener(this);
        mAnswer3Button.setOnClickListener(this);
        mAnswer4Button.setOnClickListener(this);
    }

    private void initWithLastStateIfExist(Bundle savedInstanceState){
        mUser = new User();

        if (savedInstanceState != null && savedInstanceState.getSerializable(BUNDLE_STATE_QUESTION_BANK) != null){
            Log.i("last state exist", "je suis dans le dernier état");
            mUser.setMgoodAnswers(savedInstanceState.getInt(BUNDLE_STATE_SCORE));
            mUser.setTotalAnswers(savedInstanceState.getInt(BUNDLE_STATE_QUESTION));
            mQuestionBank = (QuestionBank) savedInstanceState.getSerializable(BUNDLE_STATE_QUESTION_BANK);
            mScoreText.setText(mUser.getScore());

            loadNextQuestionIfExist();
        }
    }

    private void changeBackgroundColorOfTheAnswers(boolean isGoodAnswer, View v) {
        int backgroundColor;
        backgroundColor = isGoodAnswer ? R.color.green : R.color.red;
        v.setBackgroundColor(getResources().getColor(backgroundColor));
    }

    private void resetButtonBackgroundColor() {
        mAnswer1Button.setBackgroundColor(getResources().getColor(R.color.white));
        mAnswer2Button.setBackgroundColor(getResources().getColor(R.color.white));
        mAnswer3Button.setBackgroundColor(getResources().getColor(R.color.white));
        mAnswer4Button.setBackgroundColor(getResources().getColor(R.color.white));

    }
}
