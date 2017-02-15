package zj.qidian.com.doctor;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qidian.DBUtils.CreateToast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import NeccessaryRes.NecRes;
import okhttp3.Call;

public class FindPassword extends AppCompatActivity {
    private ActionBar actionbar = null;
    private EditText findby = null;
    private CheckBox byphone = null;
    private CheckBox byusername = null;
    private Button gotofind = null;
    private String inputinfor = null;
    private String findmethord = null;
    CreateToast createToast = null;
    private boolean shouldToFind = false;
    private String callBackinfo = null;
    //消息处理对象;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 5) {
                //将文本框中的数据更行;
                callBackinfo = (String) msg.obj;
                findby.setText(callBackinfo);


            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        initActionBar();
        initView();
      //ActivityCollector.addActivity(this);
        //监听复选框的状态改变活动;
        byphone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    byusername.setChecked(false);

                }
            }
        });

        byusername.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    byphone.setChecked(false);

                }
            }
        });

        gotofind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入信息;
                inputinfor = findby.getText().toString().trim();
                //判断找回方式;
                if (byphone.isChecked()) {

                    findmethord = "byphone";


                } else if (byusername.isChecked()) {

                    findmethord = "byusername";


                }
                //发送网络请求;
                //判断输入信息是否为空;
                if (inputinfor.equals("")) {
                    //输入值为空;
                    Toast.makeText(FindPassword.this, "输入值为空，请重新出入！", Toast.LENGTH_SHORT).show();
                    //自定义的toast;
                    // createToast.createMyToast();

                } else {
                    //输入值不为空;
                    // createToast.createMyToast();
                    shouldToFind = true;
                }

                //发送网络请求;
                if (shouldToFind) {

                    sendHttpRequest(findmethord, inputinfor);

                }

            }

        });


    }

    private void initView() {
        createToast = new CreateToast(FindPassword.this);
        findby = (EditText) findViewById(R.id.findby);
        byphone = (CheckBox) findViewById(R.id.findbytel);
        byusername = (CheckBox) findViewById(R.id.findbyusername);
        gotofind = (Button) findViewById(R.id.gotofind);


    }

    private void initActionBar() {
        actionbar = this.getSupportActionBar();
        if (actionbar != null) {

            actionbar.hide();

        }


    }

    @Override
    public void onBackPressed() {
        //进入登陆界面;
        FindPassword.this.finish();
        Intent intent = new Intent(FindPassword.this, Login.class);
        startActivity(intent);
        //设置界面切换的效果;
        overridePendingTransition(R.anim.showfrom_right, R.anim.hideto_right);
    }


    private void sendHttpRequest(String methord, String inputinfor) {
        Toast.makeText(FindPassword.this,methord+"&"+inputinfor,Toast.LENGTH_SHORT).show();
        //发送网络请求，接收网络信息并显示在发送文本框中;
        String url = "http://" + NecRes.ipaddress + ":8080/MysqlForAndroid//FindPassword?methord=" + methord + "&byinformation=" + inputinfor;
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
                      //  Toast.makeText(FindPassword.this, response, Toast.LENGTH_LONG).show();
                        Message message = new Message();
                        message.what = 005;
                        message.obj = response;
                        handler.sendMessage(message);

                    }

                });


    }


}
