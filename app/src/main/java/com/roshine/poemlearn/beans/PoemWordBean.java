package com.roshine.poemlearn.beans;

/**
 * @author L
 * @date 2018/4/15 11:04

 * @desc
 */
public class PoemWordBean {
    private String word;
    private int bottomPosition;
    private boolean isPunctuation;
    private boolean isLongClick;
    private String correctWord;
    private boolean isErrorWord;

    public boolean isErrorWord() {
        return isErrorWord;
    }

    public void setErrorWord(boolean errorWord) {
        isErrorWord = errorWord;
    }

    public String getCorrectWord() {
        return correctWord;
    }

    public void setCorrectWord(String correctWord) {
        this.correctWord = correctWord;
    }

    public boolean isLongClick() {
        return isLongClick;
    }

    public void setLongClick(boolean longClick) {
        isLongClick = longClick;
    }
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getBottomPosition() {
        return bottomPosition;
    }

    public void setBottomPosition(int bottomPosition) {
        this.bottomPosition = bottomPosition;
    }

    public boolean isPunctuation() {
        return isPunctuation;
    }

    public void setPunctuation(boolean punctuation) {
        isPunctuation = punctuation;
    }
}
