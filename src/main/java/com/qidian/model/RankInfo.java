package com.qidian.model;

import static zj.qidian.com.doctor.R.id.score;

/**
 * Created by xdsm on 2016/12/19.
 */

public class RankInfo {
    String userneme = null;
    String score = null;
    String phone = null;
    int id = 0;

    public String getUserneme() {
        return userneme;
    }

    public void setUserneme(String userneme) {
        this.userneme = userneme;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
