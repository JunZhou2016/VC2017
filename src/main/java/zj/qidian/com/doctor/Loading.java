package zj.qidian.com.doctor;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Loading extends AppCompatActivity {

    private LoadingDialog dialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){

                if (dialog != null && dialog.isShowing()) {
                    //销毁对话框;
                    dialog.dismiss();
                    //启动目标活动；
                    /*Intent intent = new Intent(Loading.this,Recorder.class);
                    startActivity(intent);*/
                    //启动倒计时活动;
                    Intent intent = new Intent(Loading.this,Alarmer.class);
                    startActivity(intent);
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ActivityCollector.addActivity(this);
        showProgressBar();
    }



    public void showProgressBar(){

        dialog = new LoadingDialog(this, R.layout.view_tips_loading2);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        //此处需要开启新的线程执行定时机制，否则UI主线程将会被堵塞，造成ANR异常；
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
            }
        }).start();

    }
}
