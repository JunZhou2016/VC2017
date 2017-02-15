package zj.qidian.com.doctor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class NoiseStander extends AppCompatActivity {

    private boolean shouldShowNoiseDialog = true;
    private ImageButton sure_to_setting = null;
    private ImageButton startTest = null;
    private TextView currentnoisestander = null;
    private int clickCounter = 1;
    private MyRecorderHandler myRecorderHandler = null;
    private ActionBar actionBar = null;
    //在该活动接收音频数据并进行处理；
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noise_stander);
        //将当前活动加入活动和集合中;
        ActivityCollector.addActivity(this);
        init();
        initActiongBar();
        //创建使用提示对话框;
        createAlertDialog();
        //监听设置按钮的点击事件；
        sure_to_setting.setOnClickListener(new StanderClickHandler(this));
        //当sure_to_setting按钮被点击之后就获取数据显示框中的数据并传递至Recorder活动中更新某个静态变量的值;
        //思路：发送广播或者使用全局静态变量（考虑使用的先后顺序）进行值的传递;
        //思路2：将ImageView的tag属性设置为图片资源的ID;
        // startTest.setOnClickListener(new StanderClickHandler(this));


    }

    @Override
    public void onBackPressed() {
        Toast.makeText(NoiseStander.this, "NoiseStander", Toast.LENGTH_LONG).show();
        finish();
        //调用动画效果；
        overridePendingTransition(R.anim.add_alpha, R.anim.rotate_alpha);
    }


    //后期实现该段代码的复用；
    public void createAlertDialog() {

        shouldShowNoiseDialog = readFromSharedPre("showNoise");

        if (shouldShowNoiseDialog) {

            //创建提示对话框；
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //这是不可通过点击返回键取消对话框；
            builder.setCancelable(false);
            builder.setTitle("操作指南").setIcon(R.drawable.remind).setMessage("当你点击中央的蓝色按钮时，将会开始记录周围的噪音水平," +
                    "请实时注意文本框中的数据，当您在适宜的情况下点击设置按钮时，文本框中的数据将会被当作个性化设置被系统使用！");
            builder.setPositiveButton("不再显示", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Toast.makeText(MainActivity.this,"您单击了确认按钮",Toast.LENGTH_LONG).show();
                    shouldShowNoiseDialog = false;
                    saveToSharedPre("showNoise", shouldShowNoiseDialog);
                }
            });

            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(NoiseStander.this, "您单击了取消按钮", Toast.LENGTH_LONG).show();
                }
            });

            //创建AlertDialog并显示；
            //builder.create()；此方法返回的才是AlertDialog对象;
            builder.create().show();

        }


    }

    //保存数据到SharedPreferences中;
    public void saveToSharedPre(String saveKey, boolean resState) {
        SharedPreferences sharedPreferences = getSharedPreferences("bgInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(saveKey, resState);
        editor.commit();

    }

    //从SharedPreferences中读取数据;
    public boolean readFromSharedPre(String readKey) {
        boolean res = false;
        SharedPreferences sharedPre = getSharedPreferences("bgInfo", MODE_PRIVATE);
        res = sharedPre.getBoolean(readKey, true);
        return res;
    }

    public void init() {
        sure_to_setting = (ImageButton) findViewById(R.id.sure_to_setting);
        startTest = (ImageButton) findViewById(R.id.startTest);
        //数据显示框;
        currentnoisestander = (TextView) findViewById(R.id.currentnoisestander);
        //进入点击事件处理的业务逻辑;


    }

    public void changePlayState(View view) {
        if (view.getId() == R.id.startTest) {
            if (clickCounter % 2 == 1) {
                clickCounter++;
                //初次点击按钮则切换至开始图标;
                //Toast.makeText(NoiseStander.this,"changePlayState",Toast.LENGTH_LONG).show();
                startTest = (ImageButton) view;
                // startTest.setImageResource(R.drawable.noise_test_start);
                startTest.setBackgroundResource(R.drawable.noise_test_start);
                //此处执行开始测试的业务逻辑;
                //实际为更新数据显示框中的数据;
                myRecorderHandler = new MyRecorderHandler(currentnoisestander);
                myRecorderHandler.startGetData();


            } else if (clickCounter % 2 == 0) {
                clickCounter++;
                //再次点击按钮则切换至停止图标;
                // Toast.makeText(NoiseStander.this,"changePlayState",Toast.LENGTH_LONG).show();
                startTest = (ImageButton) view;
                // startTest.setImageResource(R.drawable.noise_test_start);
                startTest.setBackgroundResource(R.drawable.noise_test_stop);
                myRecorderHandler = new MyRecorderHandler(currentnoisestander);
                myRecorderHandler.stopGetData();
                //Toast.makeText(NoiseStander.this,"changePlayState",Toast.LENGTH_LONG).show();

            }


        }
    }

    //隐藏ActionBar；
    private void initActiongBar(){

        actionBar = this.getSupportActionBar();
        if(actionBar!=null){

            actionBar.hide();


        }



    }


}




