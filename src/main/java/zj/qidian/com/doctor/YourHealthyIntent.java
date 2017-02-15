package zj.qidian.com.doctor;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.qidian.CustomView.HomeDiagram;

import java.util.ArrayList;
import java.util.List;

import static android.R.interpolator.linear;
import static android.view.View.Y;
import static junit.runner.Version.id;


public class YourHealthyIntent extends AppCompatActivity {
    private RelativeLayout linear = null;
    private int data = 0;
    private int older = -1;
    private int distance = 0;
    private int touchConter = 0;
    private HorizontalScrollView horizontalScrollView = null;
    private ActionBar actionBar = null;
    //这里的数据应该是从数据库中实时获取的;
    int dataArray[] = new int[]{2551,2547,916,1872,1965,1914,4219,5319,4568,213,5246,7825,1200,1543,4561,1024,3562,5428,1426, 4568,213,5246,7825,1200,1543,4561,1024,3562,5428,1426,
    };
    //是否继续滚动;
    private boolean shouldScroll = true;
    //处理滑动事件；
    private Handler scrollHadler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==12){

                //调用滚动事件；
                horizontalScrollView.setScrollX(distance++);
                ToMax();

            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_healthy_intent);
        //初始化actionbar；
        initActionBar();
        //线性图  范围10-100

        List<Integer> lists = new ArrayList<Integer>();
        //
        for (int i = 1; i < 30; i++) {
            data = dataArray[i]/200;
            lists.add(data);
        }
        linear= (RelativeLayout) findViewById(R.id.linear);
        linear.addView(new HomeDiagram(this,lists));
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.scrolltools);
        horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event){

                Toast.makeText(YourHealthyIntent.this,horizontalScrollView.getScrollY()+"",Toast.LENGTH_SHORT).show();
                touchConter++;
                if(touchConter%2==1){
                    shouldScroll = false;
                }else {
                    shouldScroll = true;
                    ToMax();

                }


                return true;
            }
        });
        ToMax();
    }

    private void initActionBar() {

        actionBar = this.getSupportActionBar();
        if(actionBar!=null){

            actionBar.hide();

        }

    }
    //获取随机数;
    public int getRandom(int min,int max){

        return (int) Math.round(Math.random()*(max-min)+min);
    }


    //向右滑动;
    private void ToMax(){

        Thread scrollTask = new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hasToMax();
                Message message = new Message();
                message.what = 12;
                scrollHadler.sendMessage(message);


            }
        });

        if(shouldScroll){

            scrollTask.start();

        }

    }
    //检测是否到达了最右侧；
    private boolean hasToMax(){

        int max = horizontalScrollView.getScrollX();
        if(max==older){
            return false;
        }
        max = older;
        return true;

    }
}
