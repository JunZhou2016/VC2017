package com.qidian.Anim;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import zj.qidian.com.doctor.R;

/**
 * Created by xdsm on 2016/12/26.
 */

public class AnimTask {

    private LinearLayout.LayoutParams layoutParams = null;

    public AnimTask(View target, int scrollSpeed) {
        this.layoutParams = (LinearLayout.LayoutParams) target.getLayoutParams();
        this.target = target;
        this.scrollSpeed = scrollSpeed;
    }

    private View target = null;
    private  int scrollSpeed = 0;
    private  int showSpeed = scrollSpeed;
    private  int hideSpeed = -scrollSpeed;

    private Handler animHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == showSpeed) {
                layoutParams.height = layoutParams.height + showSpeed;
                target.setLayoutParams(layoutParams);
                if (layoutParams.height <= 250) {
                    show(showSpeed);
                }

            }

            if (msg.what == hideSpeed) {
                layoutParams.height = layoutParams.height + hideSpeed;
                target.setLayoutParams(layoutParams);
                if (layoutParams.height >= 0) {
                    hide(hideSpeed);
                }

            }


        }
    };


    private void show(final int speed) {

        Thread scrollTask = new Thread(new Runnable() {
            @Override
            public void run() {

                //发送延时消息;
                Message message = new Message();
                message.what = speed;
                animHandler.sendMessage(message);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        scrollTask.start();
    }

    private void hide(final int reducespeed) {


        Thread hideTask = new Thread(new Runnable() {
            @Override
            public void run() {

                //发送延时消息;
                Message message = new Message();
                message.what = reducespeed;
                animHandler.sendMessage(message);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        hideTask.start();


    }


}
