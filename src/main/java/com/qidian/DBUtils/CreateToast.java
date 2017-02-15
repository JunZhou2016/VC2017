package com.qidian.DBUtils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import zj.qidian.com.doctor.FindPassword;
import zj.qidian.com.doctor.R;

/**
 * Created by xdsm on 2016/12/21.
 */

public class CreateToast {
    Context context = null;
    private TextView toastText = null;
    public CreateToast(Context context) {
        this.context = context;
    }

    public void createMyToast(){
        //加载显示的视图;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.toast_mytoast,null);
        view.findViewById(R.id.toasttext);
        //创建并初始化Toast；
        Toast toast = new Toast(context);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
        relaseToast(toast);



    }

    public  void relaseToast(Toast toast){
        //避免内存浪费;
        toast = null;


    }



}
