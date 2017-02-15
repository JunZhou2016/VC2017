package com.qidian.DBUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by xdsm on 2016/12/7.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context myContext = null;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        myContext = context;
    }
    public static final String Create_History = "create table myscore("
            +"id integer primary key autoincrement,"
            +"timer text,"
            +"score text,"
            +"state text"
            +")";
//声明建表语句;
/*public static final String Create_History = "create table myscore("
        +"scoreID integer primary key autoincrement,"+"timer text,"+"score text,"+"state text"+")";*/

    @Override
    public void onCreate(SQLiteDatabase db) {

        //创建数据库；
        db.execSQL(Create_History);
        Log.i("Intert","Hello");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
