package zj.qidian.com.doctor;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import NeccessaryRes.NecRes;
import okhttp3.Call;

public class Login extends AppCompatActivity {
    private Button login = null;
    private EditText username = null;
    private EditText password = null;
    private TextView lPassword = null;
    private TextView lRegister = null;
    private String islegal = "notLegal";
    private String UserName = null;
    private String Password = null;
    //声明静态全局变量保存用户名;
    public static String ApplicationUserName = null;
    public static String ApplicationPassword = null;
    //(原理？)
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 001) {

                if (msg.obj != null) {
                    //更新islegal的值;
                    islegal = msg.obj.toString();
                    Log.i("Handler", "handleMessage: " + msg.obj);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉窗体留白；
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        initActionBar();
        init();
        //将当前活动添加进活动链表中;
        ActivityCollector.addActivity(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取登陆信息;
                UserName = username.getText().toString();
                Password = password.getText().toString();
                ApplicationUserName = UserName;
                ApplicationPassword = Password;
                // UserName = "zhoujun";
                //Toast.makeText(Login.this,UserName,Toast.LENGTH_LONG).show();
                //Password = password.getText().toString();
                //点击后进入数据库校验；
                check(UserName, Password);
                if (islegal.equals("Legal")) {
                    // Toast.makeText(Login.this,"Legal",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);

                } else {
                    password.setTextColor(R.color.warning);
                    password.setHint("UN Or PW Is Error!");
                    //未联网情况下的测试;
                    {
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    }

                }
            }
        });


        lRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转至注册界面;
                //进入注册界面;
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                //设置界面切换的效果;
                overridePendingTransition(R.anim.showfrom_right, R.anim.reduce_alpha);
            }
        });

        lPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转至注册界面;
                //进入注册界面;
                Intent intent = new Intent(Login.this, FindPassword.class);
                startActivity(intent);
                //设置界面切换的效果;
                overridePendingTransition(R.anim.showfrom_right, R.anim.reduce_alpha);

            }
        });


    }

    private void init() {

        login = (Button) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        lPassword = (TextView) findViewById(R.id.lPassword);
        lRegister = (TextView) findViewById(R.id.lRegister);


    }


    public void check(String userName, String password) {

        // String keyValue = key.getText().toString();
        // String url = "http://192.168.43.70:8080/MysqlForAndroid//LoginCl?un="+userName;
        // String url = "http://192.168.43.70:8080/MysqlForAndroid//UserCl";
        String url = "http://" + NecRes.ipaddress + ":8080/MysqlForAndroid//LoginCl?un=" + userName + "&pw=" + password;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //更新登陆状态标志值;
                        //islegal = response;
                        Toast.makeText(Login.this, response, Toast.LENGTH_LONG).show();
                        Message message = new Message();
                        message.what = 001;
                        message.obj = response;
                        handler.sendMessage(message);

                    }

                });


    }

    private void initActionBar() {
        //在获取ActionBar的时候切记之用support支持的；
        ActionBar actionBar = this.getSupportActionBar();
        //判断ActionBar是否为空，避免异常的发生；
        if (actionBar != null) {
           /* //设置显示向左的图标；
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu_back);
            //设置应用程序的图标显示；
            actionBar.setDisplayShowHomeEnabled(true);
            //设置应用程序的图标可点击；
            actionBar.setHomeButtonEnabled(true);
            //设置显示应用程序的标题；
            actionBar.setDisplayShowTitleEnabled(true);*/
            actionBar.hide();

        }

    }

    @Override
    public void onBackPressed() {

        ActivityCollector.finishAll();

    }
}
