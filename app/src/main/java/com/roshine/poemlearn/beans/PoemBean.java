package com.roshine.poemlearn.beans;

import java.io.Serializable;

/**
 * @author Roshine
 * @date 2018/4/15 11:04
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public class PoemBean implements Serializable{
    private String poemTitle;
    private String poemYear;
    private String poemAuthor;
    private String correctPoem;
//    private String disturbPoem;

    public String getPoemTitle() {
        return poemTitle;
    }

    public void setPoemTitle(String poemTitle) {
        this.poemTitle = poemTitle;
    }

    public String getPoemYear() {
        return poemYear;
    }

    public void setPoemYear(String poemYear) {
        this.poemYear = poemYear;
    }

    public String getPoemAuthor() {
        return poemAuthor;
    }

    public void setPoemAuthor(String poemAuthor) {
        this.poemAuthor = poemAuthor;
    }

    public String getCorrectPoem() {
        return correctPoem;
    }

    public void setCorrectPoem(String correctPoem) {
        this.correctPoem = correctPoem;
    }

//    public String getDisturbPoem() {
//        return disturbPoem;
//    }
//
//    public void setDisturbPoem(String disturbPoem) {
//        this.disturbPoem = disturbPoem;
//    }

}
