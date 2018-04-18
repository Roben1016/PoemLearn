package com.roshine.poemlearn.beans;

import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * @author Roshine
 * @date 2018/4/18 21:33
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public class PoetryHistory extends BmobObject {
    private String poem_user;
    private String poem_title;
    private String poem_year;
    private Date poem_last_time;
    private String poem_author;
    private String poem_content;
    private Integer poem_id;

    public String getPoem_user() {
        return poem_user;
    }

    public void setPoem_user(String poem_user) {
        this.poem_user = poem_user;
    }

    public String getPoem_title() {
        return poem_title;
    }

    public void setPoem_title(String poem_title) {
        this.poem_title = poem_title;
    }

    public String getPoem_year() {
        return poem_year;
    }

    public void setPoem_year(String poem_year) {
        this.poem_year = poem_year;
    }

    public Date getPoem_last_time() {
        return poem_last_time;
    }

    public void setPoem_last_time(Date poem_last_time) {
        this.poem_last_time = poem_last_time;
    }

    public String getPoem_author() {
        return poem_author;
    }

    public void setPoem_author(String poem_author) {
        this.poem_author = poem_author;
    }

    public String getPoem_content() {
        return poem_content;
    }

    public void setPoem_content(String poem_content) {
        this.poem_content = poem_content;
    }

    public Integer getPoem_id() {
        return poem_id;
    }

    public void setPoem_id(Integer poem_id) {
        this.poem_id = poem_id;
    }

}
