package com.sebaroundtheworld.topquizz.model;

public class User {

    private String mFirstname;
    private int mgoodAnswers;
    private int mTotalAnswers;


    public User (){
        this.reinitAnswers();
    }

    public void setTotalAnswers(int totalAnswers) {
        mTotalAnswers = totalAnswers;
    }

    public void reinitAnswers (){
        mgoodAnswers = 0;
        mTotalAnswers = 0;
    }

    public void addGoodAnswers (){
        mTotalAnswers++;
        mgoodAnswers++;
    }

    public void addWrongAnswers () {
        mTotalAnswers++;
    }

    public String getScore () {
        return mgoodAnswers + "/" + mTotalAnswers;
    }
    public int getMgoodAnswers() {
        return mgoodAnswers;
    }

    public String getFirstname() {
        return mFirstname;
    }

    public void setFirstname(String firstname) {
        mFirstname = firstname;
    }

    public void setMgoodAnswers(int mgoodAnswers) {
        this.mgoodAnswers = mgoodAnswers;
    }

    public int getTotalAnswers() {
        return mTotalAnswers;
    }

}
