package com.sebaroundtheworld.topquizz.data;


import androidx.annotation.Nullable;

import com.sebaroundtheworld.topquizz.controller.QuestionBankView;
import com.sebaroundtheworld.topquizz.model.Question;
import com.sebaroundtheworld.topquizz.model.QuestionBank;
import com.sebaroundtheworld.topquizz.model.QuestionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class QuestionData implements QuizzDataAPICalls.Callbacks {

    private QuestionBankView mQuestionBankView;

    public QuestionData(QuestionBankView questionBankView){
        mQuestionBankView = questionBankView;
    }

    public void getQuestions(QuestionType questionType) {
        QuizzDataAPICalls.getQuestionBank(this, questionType);
    }

    @Override
    public void onResponse(@Nullable ResponseQuestionBankPojo questionBankPojos) {
        if(questionBankPojos != null && mQuestionBankView != null){
            mQuestionBankView.questionBankReady(questionBankPojoToQuestionBank(questionBankPojos.getQuestionBankPojos()));
        }
    }

    @Override
    public void onFailure() {

    }

    private QuestionBank questionBankPojoToQuestionBank (List<QuestionBankPojo> questionBankPojosList) {
        List<Question> questions = new ArrayList<>();
        Random random = new Random();
        int indexGoodAnswer;

        for(QuestionBankPojo questionPojo : questionBankPojosList) {
            Question question = new Question();

            question.setQuestion(changeIllegalCharacter(questionPojo.getQuestion()));

            indexGoodAnswer = random.nextInt(questionPojo.getIncorrectAnswers().size()+1);
            question.setAnswerInd(indexGoodAnswer);
            List<String> answers = new ArrayList<>();

            for(String answer : questionPojo.getIncorrectAnswers()) {
                if(answers.size()== indexGoodAnswer) {
                    answers.add(changeIllegalCharacter(questionPojo.getCorrectAnswer()));
                }
                answers.add(answer);
            }

            if(answers.size()== questionPojo.getIncorrectAnswers().size()) {
                answers.add(questionPojo.getCorrectAnswer());
            }
            question.setChoices(answers);
            questions.add(question);
        }
        return new QuestionBank(questions);
    }

    private String changeIllegalCharacter(String text) {
        return text.replace("&quot;", "\"")
                    .replace("&#039;", "'")
                    .replace("&shy;", "");
    }
}
