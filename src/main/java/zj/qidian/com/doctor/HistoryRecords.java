package zj.qidian.com.doctor;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.qidian.DBUtils.MyDatabaseHelper;
import com.qidian.adapter.HistoryAdapter;
import com.qidian.model.History;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryRecords extends AppCompatActivity {
    //在该空白活动中将要完成历史记录的显示;
    private List<Map<String,Object>> historyItems = null;
    private HistoryAdapter historyAdapter = null;
    private  ListView listView = null;
    private MyDatabaseHelper myDatabaseHelper = null;
    private SQLiteDatabase sqLiteDatabase = null;
    private Cursor cursor = null;
    private ActionBar actionBar = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_records);
       // initActionBar();
        //将当前活动加入活动和集合中;
        ActivityCollector.addActivity(this);
        Toast.makeText(HistoryRecords.this,"This is HistoryRecords",Toast.LENGTH_LONG).show();
        getDatas();
        init();

        //为listview设置适配器;
        listView.setAdapter(historyAdapter);


    }

    @Override
    public void onBackPressed() {
        /*Toast.makeText(NoiseStander.this,"NoiseStander",Toast.LENGTH_LONG).show();*/
        finish();
        //调用动画效果；
        overridePendingTransition(R.anim.add_alpha,R.anim.rotate_alpha);
    }



    public void getDatas(){
        //查询SQLite数据库;
        historyItems = new ArrayList<Map<String,Object>>();
          //获取到数据库管理系统对象实例;
        myDatabaseHelper = new MyDatabaseHelper(HistoryRecords.this,"History",null,15);
         //获取到可操作的数据库实例;
        sqLiteDatabase = myDatabaseHelper.getReadableDatabase();
        //填充数据到historyItems;
        query();

    }


    public void init(){
        //获取将要进行适配的数据;

        listView = (ListView) findViewById(R.id.history);
        historyAdapter = new HistoryAdapter(historyItems,HistoryRecords.this);


    }


    public void query(){

        //查询数据库中的所有列；
        //*cursor = sqLiteDatabase.query("hisscores",null,null,null,null,null,null,null);*//*
        cursor = sqLiteDatabase.rawQuery("select * from myscore",null);
        if(cursor.moveToNext()){

            do{
                int     id = cursor.getInt(cursor.getColumnIndex("id"));
                String queryDatatimer = cursor.getString(cursor.getColumnIndex("timer"));
                String queryDatascore = cursor.getString(cursor.getColumnIndex("score"));
               /* //保留三位小数;
                DecimalFormat df = new DecimalFormat("###.000");
                queryDatascore = df.format(Integer.parseInt(queryDatascore));*/
                String queryDatastate = cursor.getString(cursor.getColumnIndex("state"));
               //将记录信息封装到集合中并填充至historyItems中;
               //  Log.i("ZJ", "query: "+id+queryDatatimer+queryDatascore+queryDatastate);
                //封装数据;
                History history = new History(id,queryDatatimer,queryDatascore,queryDatastate);
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("historyitems",history);
                historyItems.add(map);




            }while (cursor.moveToNext());

        }

        cursor.close();

    }

  private void initActionBar(){

      actionBar = getSupportActionBar();
      if(actionBar!=null){

          actionBar.hide();

      }

  }



}
