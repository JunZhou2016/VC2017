package zj.qidian.com.doctor;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qidian.CustomView.HomeArc;
import com.qidian.DBUtils.MyDatabaseHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.DecimalFormat;
import java.util.Calendar;

import NeccessaryRes.NecRes;
import okhttp3.Call;
import okhttp3.Response;

import static android.R.attr.action;
import static android.R.attr.key;
import static junit.runner.Version.id;

public class Interrupt extends AppCompatActivity {
    //环形数据显示所在的布局;
    LinearLayout arc;
    private TextView textView = null;
    private Paint paint = null;
    private String userData = null;
    private MyDatabaseHelper myDatabaseHelper = null;
    private SQLiteDatabase sqLiteDatabase = null;
    private String timer = null;
    //是否重复的标志;
    private String isRepeat = null;
    private String UN = null;
    private String SC = null;
    private String TE = null;
    //环形内部显示的数据;
    private int currentUserDate = 0;
    //处理ActionBar;
    ActionBar actionBar = null;

    //state的值应该有if语句实时判断得到;
    private String state ="Bad";

    private String score = "0";
    //异步消息处理;
    private Handler repeatHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==003){

                isRepeat = msg.obj.toString();
               /* Toast.makeText(Interrupt.this,isRepeat,Toast.LENGTH_SHORT).show();*/
                //判断数据库中数据的状态;
                if(isRepeat.equals("NoHad")){
                    //不重复，数据库中无此用户的记录;
                      //执行插入操作;
                    insertIntoHttpMySQL(UN,SC,"1",TE);


                }


                if(isRepeat.equals("Had")){
                        //数据重复，执行更新操作;
                    updateMySQLData(UN,SC);
                   // Toast.makeText(Interrupt.this,"Had",Toast.LENGTH_SHORT).show();

                }

            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interrupt);
        //将当前活动加入活动和集合中;
        ActivityCollector.addActivity(this);
        //隐藏ActionBar;
        initActionBar();
        UN = Login.ApplicationUserName;
        ActivityCollector.addActivity(this);
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        Double sum = bundle.getDouble("total");
        //--------------------------------------------
       /* textView = (TextView) findViewById(R.id.userData);
        paint = textView.getPaint();
        //设置字体为粗体;
        paint.setFakeBoldText(true);*/
        arc = (LinearLayout) findViewById(R.id.arc);
        //格式化数据，保留三位小数;
        DecimalFormat df = new DecimalFormat("###.000");
        userData = df.format(sum);
        DecimalFormat currentDf = new DecimalFormat("###");
        currentUserDate = Integer.parseInt(currentDf.format(sum));

        Toast.makeText(Interrupt.this,currentUserDate+"",Toast.LENGTH_SHORT).show();

        arc.addView(new HomeArc(this, currentUserDate));
        //--------------------------------------------
       /* textView.setText("您的肺活量"+userData);*/
        //测试用;
          UN = Login.ApplicationUserName;
          SC = userData;
        //此处的TE应该由注册数据查得;
           //单机使用则由SharedPreferences中获取；
            //由SharedPreferences中获取可能获取错误;
          TE = "13886803562";
        {//获取到系统的当前时间;
            Calendar calendar = Calendar.getInstance();
          int day = calendar.get(Calendar.DAY_OF_MONTH);
          int minute = calendar.get(calendar.MINUTE);
          int hours = calendar.get(Calendar.HOUR_OF_DAY);
          timer = day+"/"+hours+"/"+minute+"/";
          score = userData;
        }
        initDBRes();
        //插入数据到SQLite数据库中;
        insert(timer,score,state);
        //判断MySQL数据库中是否有用户记录，如果没有则执行插入操作；
        IsRepeatInMySQL(UN);

        /*//插入数据为name,phone,timer,score,state;因为id 设置为自增长模式，故此处不需特意插入;
        //如果MySQL数据库中没有用户记录则执行更新操作;
        //query();
        //Toast.makeText(Interrupt.this,timer+"您已停止了测试\n"+sum,Toast.LENGTH_LONG).show();*/


    }

    private void initActionBar() {
        actionBar = this.getSupportActionBar();
        if(actionBar!=null){

            actionBar.hide();

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }


    public void insert(String timer,String score,String state){
   /* public void insert(String timer){*/

        String insert_sql = "insert into myscore (timer,score,state) values(?,?,?)";
        String insert_selectionArgs[] = new String[]{timer,score,state};
        sqLiteDatabase.execSQL(insert_sql,insert_selectionArgs);

       /* String insert_sql  = "insert into myscore(timer) values(?)";
        String insert_inflate[] = new String[]{timer};
        sqLiteDatabase.execSQL(insert_sql,insert_inflate);*/


    }


    //初始化数据库资源;
    public void initDBRes(){

        myDatabaseHelper = new MyDatabaseHelper(Interrupt.this,"History",null,15);
        sqLiteDatabase = myDatabaseHelper.getReadableDatabase();

    }


    public void query() {

        Cursor cursor = null;


            //查询数据库中的所有列；
                //*cursor = sqLiteDatabase.query("hisscores",null,null,null,null,null,null,null);*//*
            cursor = sqLiteDatabase.rawQuery("select * from myscore",null);
            if(cursor.moveToNext()){

                do{

                    String queryDatatimer = cursor.getString(cursor.getColumnIndex("timer"));
                    String queryDatascore = cursor.getString(cursor.getColumnIndex("score"));
                    String queryDatastate = cursor.getString(cursor.getColumnIndex("state"));
                   // Toast.makeText(Interrupt.this,queryDatatimer+":"+queryDatascore+":"+queryDatastate,Toast.LENGTH_LONG).show();
                    Log.i("ZJ", "query: "+queryDatatimer+":"+queryDatascore+":"+queryDatastate);

                  //  show.setText(""+queryData);

                }while (cursor.moveToNext());

            }

            cursor.close();



        }

    //此处可以复用;
    private void insertIntoHttpMySQL(String UN,String SC,String Rank,String TE){
      //  Toast.makeText(Interrupt.this,"insertIntoHttpMySQL",Toast.LENGTH_SHORT).show();
        String url = "http://"+NecRes.ipaddress+":8080/MysqlForAndroid//InsertRankInfoToMySQL?UN="+UN+"&SC="+SC+"&Rank="+Rank+"&TE="+TE;
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
                        // textView.setText(response);
                       // Toast.makeText(Interrupt.this,response.toString(),Toast.LENGTH_LONG).show();
                        // parseJSONWithGSON(response);
                    }

                });

    }

    //判断数据库中是否已经有记录信息;
       //如果已经有记录信息则执行更新操作;
    private void IsRepeatInMySQL(String un){
        String url = "http://"+NecRes.ipaddress+":8080/MysqlForAndroid//IsHasRankInfo?un="+un;
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
                        // textView.setText(response);
                        //Toast.makeText(Interrupt.this,response.toString(),Toast.LENGTH_LONG).show();
                        // parseJSONWithGSON(response);
                        Message message = new Message();
                        message.what = 003;
                        message.obj = response;
                        repeatHandler.sendMessage(message);

                    }

                });

    }


    private void updateMySQLData(String un,String sc){
        //当排名信息表中有记录的时候执行此项操作;
        String url = "http://"+ NecRes.ipaddress+":8080/MysqlForAndroid//UpdateMySQLData?un="+un+"&sc="+sc;
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
                        // textView.setText(response);
                        Toast.makeText(Interrupt.this,"updateMySQLData successfully!",Toast.LENGTH_LONG).show();
                        // parseJSONWithGSON(response);
                       /* Message message = new Message();
                        message.what = 003;
                        message.obj = response;
                        repeatHandler.sendMessage(message);*/

                    }

                });

    }

//当前控制类中的tel为固定值，下午完成注册模块;
   //添加退出效果;
      //此处应该关闭所有的活动;


    @Override
    public void onBackPressed() {
        //此处应该禁止父类的方法,否则将导致监听无效;
        this.finish();
        //进入主界面;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        //结束所有的活动;
        //将当前活动加入活动和集合中;
        ActivityCollector.finishAll();
        startActivity(intent);
       /* overridePendingTransition(R.anim.reduce_alpha,R.anim.add_alpha);*/
        overridePendingTransition(R.anim.showfrom_right,R.anim.scaletosmall);
    }



}
