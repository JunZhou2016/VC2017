package zj.qidian.com.doctor;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Alarmer extends AppCompatActivity {

    private ActionBar actionBar = null;
    private ImageView imgImageView = null;
    private TextView timerText = null;
    private String saveTime = null;
    private TextView left = null;
    private TextView leftTime = null;
    private TextView testStart = null;
    public List<Thread> thresdList = new ArrayList<>();
    //倒计时资源数组;
    //  private int djsimgs[] = new int[]{R.drawable.fir,R.drawable.sec,R.drawable.thi,R.drawable.forth,R.drawable.fif};
    //倒计时处理函数;
    private int counter = 0;
    private boolean shouldCreateNewThread = true;
    //启动数据采集;
    private Handler sleepHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 11) {
                //在2秒后启动数据采集后动;
                gotoRecording();
            }

        }
    };

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //根据消息的值改变主线程中ImageView的值;
            switch (msg.what) {
                case 1:
                    //recyleImageViewResources();
                    timerText.setText(3+"");
                    createNewThread();
                    break;
                case 2:
                    //recyleImageViewResources();
                    timerText.setText(2+"");
                    createNewThread();
                    break;
                case 3:
                    //recyleImageViewResources();
                    timerText.setText(1+"");
                    createNewThread();
                    break;
                case 4:
                    left.setText("");
                    leftTime.setText("");
                    testStart.setText("");
                    // recyleImageViewResources();
                    timerText.setText("测试开始");
                    //createNewThread();
                    startAnim();
                    gotoRecording();
                    break;
                case 5:
                    testStart.setText("");
                    left.setText("");
                    leftTime.setText("");
                    // recyleImageViewResources();
                    //开启动画;
                    startAnim();
                    timerText.setText("测试开始");

                    //此处不再开辟新的线程;
                    //*  stopThread(thresdList);*//*
                    //进入数据采集活动;
                    gotoRecording();
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏窗口;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
      /*  //当前线程休眠2000毫秒;
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        setContentView(R.layout.activity_alarmer);
        initView();
        //  recyleImageViewResources();
        //startAnim();
        ActivityCollector.addActivity(this);
        initActionBar();
        createNewThread();
        //sleepSecond();
    }

    private void initView() {

        //  imgImageView = (ImageView) findViewById(R.id.numberfif);
        timerText = (TextView) findViewById(R.id.alamer);
        left = (TextView) findViewById(R.id.left);
        leftTime = (TextView) findViewById(R.id.lefttime);
        testStart = (TextView) findViewById(R.id.testStart);

    }

    private void initActionBar() {
        actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.hide();

        }

    }

    //开辟新的线程执行定时任务：
    private void createNewThread() {
        counter = counter + 1;
        //开辟新的线程，避免ANR异常;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.what = counter;
                handler.sendMessage(message);
            }
        });
        thread.start();
        thresdList.add(thread);
    }

    private void stopThread(List<Thread> list) {

        //终止所有的线程;
        for (Thread currentThread : list) {

            currentThread.stop();
        }


    }

    private void startAnim() {

        Animation animation = AnimationUtils.loadAnimation(Alarmer.this, R.anim.reduce_alphainanimal);
        timerText.startAnimation(animation);


    }

    private void doDelayOneSecond() {

        Thread delay = new Thread(new Runnable() {
            @Override
            public void run() {

                //当前线程休眠一秒钟之后执行;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    private void gotoRecording() {
        Intent intent = new Intent(Alarmer.this, Recorder.class);
        startActivity(intent);


    }

    //回收ImageView中的资源;
    private void recyleImageViewResources() {

        Drawable toRecycle = imgImageView.getDrawable();
        if (toRecycle != null) {
            ((BitmapDrawable) imgImageView.getDrawable()).getBitmap().recycle();
        }

    }

    //休眠2秒钟后启动数据录入功能;
    private void sleepSecond() {

        Thread sleepSecondThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //此处休眠300毫秒;
                    Thread.sleep(300);
                    Message message = new Message();
                    message.what = 11;
                    sleepHandler.sendMessage(message);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });

        sleepSecondThread.start();


    }

}
