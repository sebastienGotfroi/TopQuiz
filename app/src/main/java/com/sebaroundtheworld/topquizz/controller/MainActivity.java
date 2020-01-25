package com.sebaroundtheworld.topquizz.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sebaroundtheworld.topquizz.R;
import com.sebaroundtheworld.topquizz.model.QuestionType;
import com.sebaroundtheworld.topquizz.model.User;

import static com.sebaroundtheworld.topquizz.controller.GameActivity.BUNDLE_EXTRA_SCORE;

public class MainActivity extends AppCompatActivity {

    private EditText mNameInput;
    private EditText mNumberOfQuestionInput;
    private Spinner mCategoryInput;
    private Spinner mDifficulyInput;
    private Button mPlayButton;

    private User user;
    private QuestionType mQuestionType;

    private SharedPreferences mSharedPreferences;

    private static final int GAME_ACTIVITY_REQUEST_CODE = 42;
    public static final String PREF_KEY_FIRSTNAME = "PREF_KEY_FIRSTNAME";
    public static final String PREF_KEY_SCORE = "PREF_KEY_SCORE";
    public static final String BUNDLE_KEY_QUESTION_TYPE = "QUESTION_TYPE";
    public static final int START_IND_CATEGORY = 9;
    public static final String DIFFICULTY_EASY = "easy";
    public static final String DIFFICULTY_MEDIUM = "medium";
    public static final String DIFFICULTY_HARD = "hard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initQuestionTypeIfNotExist(savedInstanceState);
        initWidgets();


        user = new User();
        user.setFirstname(mNameInput.getText().toString());
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        bundle.putSerializable(this.BUNDLE_KEY_QUESTION_TYPE, mQuestionType);
        bundle.putString(this.PREF_KEY_FIRSTNAME, user.getFirstname());
        super.onSaveInstanceState(bundle);
    }

    /**
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(GAME_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            mSharedPreferences.edit().putString(PREF_KEY_SCORE, data.getCharSequenceExtra(BUNDLE_EXTRA_SCORE).toString()).apply();
        }
    }**/

    private void initWidgets (){
        mSharedPreferences = getPreferences(MODE_PRIVATE);

        mNameInput = (EditText) findViewById(R.id.activity_main_name_input);
        mNameInput.setText(mSharedPreferences.getString(PREF_KEY_FIRSTNAME,""));

        mNumberOfQuestionInput = (EditText) findViewById(R.id.activity_main_number_questions_input);
        mCategoryInput = (Spinner) findViewById(R.id.activity_main_category_input);
        mDifficulyInput = (Spinner) findViewById(R.id.activity_main_difficulty_input);

        mPlayButton = (Button) findViewById(R.id.activity_main_play_btn);

        initNameInputListener();
        initButtonListener();
        initCategoryListener();
        initDifficultyListener();
        initNumberOfQuesitonInputListener();

        mPlayButton.setEnabled(false);
    }

    private void initNameInputListener(){
        mNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                user.setFirstname(s.toString());
                mPlayButton.setEnabled(checkEnableStartButton());
            }
        });
    }

    private boolean checkEnableStartButton(){

        if (user.getFirstname().length()> 0 && mQuestionType.getNumberOfQuestion() > 0) {
            mPlayButton.setTextColor(getResources().getColor(R.color.colorPrimary));
            return true;
        }

        mPlayButton.setTextColor(getResources().getColor(R.color.colorAccent));
        return false;
    }

    private void initNumberOfQuesitonInputListener(){
        mNumberOfQuestionInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0) {
                    int numberOfQuesiton = Integer.parseInt(s.toString());
                    if (numberOfQuesiton > 0) {
                        mQuestionType.setNumberOfQuestion(numberOfQuesiton);
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.number_below_0, Toast.LENGTH_LONG).show();
                        mNumberOfQuestionInput.setText("");
                    }

                }

                mPlayButton.setEnabled(checkEnableStartButton());
            }
        });
    }

    private void initButtonListener(){
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameActivity = new Intent(MainActivity.this, GameActivity.class);
                user.setFirstname(mNameInput.getText().toString());

                mSharedPreferences.edit().putString(PREF_KEY_FIRSTNAME, user.getFirstname()).apply();
                gameActivity.putExtra("name", mNameInput.getText().toString());
                gameActivity.putExtra("QuestionType", mQuestionType);
                startActivityForResult(gameActivity, GAME_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    private void initCategoryListener (){
        mCategoryInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mQuestionType.setCategory(position+ START_IND_CATEGORY);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initDifficultyListener(){
        mDifficulyInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch(position) {
                    case 0 : mQuestionType.setDifficulty(DIFFICULTY_EASY);
                        break;
                    case 1 : mQuestionType.setDifficulty(DIFFICULTY_MEDIUM);
                        break;
                    case 2 : mQuestionType.setDifficulty(DIFFICULTY_HARD);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initQuestionTypeIfNotExist(Bundle saveInstanceState){

        if(saveInstanceState == null || saveInstanceState.getSerializable(this.BUNDLE_KEY_QUESTION_TYPE) == null) {
            mQuestionType = new QuestionType();
            mQuestionType.setNumberOfQuestion(0);
            mQuestionType.setDifficulty(DIFFICULTY_EASY);
            mQuestionType.setCategory(START_IND_CATEGORY);
        } else {
            mQuestionType = (QuestionType) saveInstanceState.getSerializable(this.BUNDLE_KEY_QUESTION_TYPE);
        }
    }
}
