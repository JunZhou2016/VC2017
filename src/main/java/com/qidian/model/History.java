package com.qidian.model;

import static android.R.attr.id;

/**
 * Created by xdsm on 2016/12/10.
 */

public class History {


    Integer id = 0;
    String timer = null;
    String score = null;
    String state = null;

    public History(Integer id, String timer, String score, String state) {
        this.id = id;
        this.timer = timer;
        this.score = score;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScore() {
        return score;
    }


    public void setScore(String score) {
        this.score = score;
    }


    public String getTimer() {
        return timer;
    }


    public void setTimer(String timer) {
        this.timer = timer;
    }


    public String getState() {
        return state;
    }


    public void setState(String state) {
        this.state = state;
    }


}
