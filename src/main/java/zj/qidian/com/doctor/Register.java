package zj.qidian.com.doctor;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import NeccessaryRes.NecRes;
import okhttp3.Call;

public class Register extends AppCompatActivity {
    private EditText registerUN = null;
    private EditText registerPW = null;
    private EditText registerTE = null;
    private Button regsterButton = null;
    private String username = null;
    private String password = null;
    private String telephoneNumber = null;
    public boolean UNState = true;
    public boolean PWState = true;
    public boolean TEState = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initActionBar();
        initView();
       // ActivityCollector.addActivity(this);
        //为登陆按钮添加点击监听;
           //发送网络数据到服务器;
              regsterButton.setOnClickListener(new View.OnClickListener() {

                  @Override
                  public void onClick(View v) {
                        //还原标志状态,此处不再重新定义变量，避免耗费内存;
                      UNState = true;
                      PWState = true;
                      TEState = true;
                      //获取用户输入的数据;
                      username = registerUN.getText().toString().trim();
                      if(username.equals("")){

                          UNState = false;
                         // registerUN.setTextColor(Color.RED);
                         //registerUN.setTextColor(R.color.warning);
                         //registerUN.setTextColor(Color.GRAY);
                        // Toast.makeText(Register.this,"RUning",Toast.LENGTH_LONG).show();

                      }
                      password = registerPW.getText().toString().trim();
                      if(password.equals("")){

                          PWState = false;


                      }
                      telephoneNumber = registerTE.getText().toString().trim();
                      if(telephoneNumber.equals("")){

                          TEState = false;


                      }


                      if(UNState&&PWState&&TEState){
                          registerToMySQL(username,password,telephoneNumber);

                      }else{

                          //弹出警示框;
                          NullTypeWarn nullTypeWarn = new NullTypeWarn(Register.this);
                          nullTypeWarn.show();

                      }



                  }
              });



    }



    private void initActionBar() {

        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!=null){

            actionBar.hide();

        }
    }

    @Override
    public void onBackPressed() {

        //进入登陆界面;
        this.finish();
        Intent intent = new Intent(Register.this,Login.class);
        startActivity(intent);
        //设置界面切换的效果;
        overridePendingTransition(R.anim.showfrom_right,R.anim.hideto_right);

    }

    private void initView(){

         registerUN = (EditText) findViewById(R.id.registerUN);
         registerPW = (EditText) findViewById(R.id.registerPW);
         registerTE = (EditText) findViewById(R.id.registerTE);
        regsterButton = (Button) findViewById(R.id.regsterButton);

    }

    private void registerToMySQL(String un,String pw,String tel) {

        //当排名信息表中有记录的时候执行此项操作;
       // String url = "http://172.16.17.18:8080/MysqlForAndroid//addAppUsers?un="+un+"&pw="+pw+"&tel="+tel;
        String url = "http://"+NecRes.ipaddress+":8080/MysqlForAndroid//addAppUsers?un="+un+"&pw="+pw+"&tel="+tel;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback()
                {

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        Toast.makeText(Register.this,response,Toast.LENGTH_LONG).show();
                       if(response.equals("Registered successfully!")){

                           //进入登陆界面;
                           Register.this.finish();
                           Intent intent = new Intent(Register.this,Login.class);
                           startActivity(intent);
                           //设置界面切换的效果;
                           overridePendingTransition(R.anim.showfrom_right,R.anim.hideto_right);
                       }

                    }

                });

    }

}
