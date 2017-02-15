package zj.qidian.com.doctor;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qidian.adapter.RankListAdapter;
import com.qidian.model.RankInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import NeccessaryRes.NecRes;
import okhttp3.Call;

public class RankDetailShow extends AppCompatActivity {

    private ActionBar actionBar = null;
    //定义排名相关变量;
    private List<RankInfo> rankinformation = null;
    private RankListAdapter rankListAdapter = null;
    private ListView ranklistView = null;
    //处理排名相关的数据;
    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            //判断消息来源;
            if (msg.what == 13) {
                //初始化数据列表;
                rankinformation = (List<RankInfo>) msg.obj;
                // Toast.makeText(MainActivity.this,rankinformation.get(0).getPhone(),Toast.LENGTH_LONG).show();
                Toast.makeText(RankDetailShow.this,"handleMessage",Toast.LENGTH_LONG).show();
                //在这里初始化适配器;
                initRank(rankinformation);

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_detail_show);
        initWindow();
        initView();
        //发送网络请求获取排名数据；
        getRankInfoFromMysql();


    }

    private void initWindow() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    private void initView() {

        actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.hide();

        }

        ranklistView = (ListView) findViewById(R.id.rank);

    }

    //设置返回特效;


    @Override
    public void onBackPressed() {



        finish();
        //调用动画效果；
        overridePendingTransition(R.anim.add_alpha,R.anim.hideto_right);


    }

    public void getRankInfoFromMysql() {

        //初始化数据列表;
        //发送网络请求到数据库；
        sendRequestToMysql();
        //封装数据的数据列表;
        //返回数据列表;


    }
    /*测试用方法;*/
    private void sendRequestToMysql() {
        /*List<RankInfo> rankListsend = null;*/
        String url = "http://" + NecRes.ipaddress + ":8080/MysqlForAndroid///RankCl";
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
                        /*Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();*/
                        Toast.makeText(RankDetailShow.this,"Show Detail",Toast.LENGTH_SHORT).show();
                        Message message = new Message();
                        message.what = 13;
                        //将排名信息数据封装进Message;
                        message.obj = parseJSONWithGSON(response);
                        handler.sendMessage(message);


                    }

                });

    }



    private List<RankInfo> parseJSONWithGSON(String jsonData) {

        Gson gson = new Gson();
        List<RankInfo> rankList = gson.fromJson(jsonData, new TypeToken<List<RankInfo>>() {
        }.getType());
        return rankList;
    }


    //Rank表的数据处理;
    private void initRank(List<RankInfo> datalist) {


        //为rankinformation赋值;
        //* rankinformation = getRankInfoFromMysql();*//*

        //添加适配器到ListVIew；*/

        if (rankinformation != null) {
            Toast.makeText(RankDetailShow.this, "Not Null!", Toast.LENGTH_LONG).show();
            //初始化数据列表;
            rankinformation = datalist;
            //创建Rank适配器;
            rankListAdapter = new RankListAdapter(RankDetailShow.this, rankinformation);
            //Toast.makeText(MainActivity.this,datalist.get(0).getUserneme()+"/"+datalist.get(0).getId()+"/"+datalist.get(0).getScore()+"/"+datalist.get(0).getPhone(),Toast.LENGTH_LONG).show();
            //添加适配器到ListVIew；
            ranklistView.setAdapter(rankListAdapter);
            // Toast.makeText(MainActivity.this,datalist.get(0).getUserneme()+"/"+datalist.get(0).getId()+"/"+datalist.get(0).getScore()+"/"+datalist.get(0).getPhone(),Toast.LENGTH_LONG).show();


        } else {

            Toast.makeText(RankDetailShow.this, "No Data To Show,Please Waite a Minute!", Toast.LENGTH_LONG).show();
        }

    }


}
