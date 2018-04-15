package com.roshine.poemlearn.beans;

/**
 * @author Roshine
 * @date 2018/4/15 11:04
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public class PoemWordBean {
    private String word;
    private int bottomPosition;
    private boolean isPunctuation;
    private boolean isLongClick;

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
