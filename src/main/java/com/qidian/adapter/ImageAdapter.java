package com.qidian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;
import java.util.Map;

import zj.qidian.com.doctor.R;

/**
 * Created by xdsm on 2016/11/30.
 */

public class ImageAdapter extends BaseAdapter {
    //在该适配器中实现壁纸图片的适配；
    //创建列表用于接收图片资源；
    List<Map<String,Object>>  ImgsList = null;
    private Context context = null;
    private LayoutInflater layoutInflater = null;
    public ImageAdapter(Context context, List<Map<String, Object>> imgsList) {
        this.context = context;
        this.ImgsList = imgsList;
    }

    @Override
    public int getCount() {

        //该值的作用是确定生成的列表子项的精确数目；
        return ImgsList.size();
    }

    @Override
    public Object getItem(int position) {
        return ImgsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       final int infoPosition = position;
        layoutInflater = LayoutInflater.from(context);
       // View con = layoutInflater.inflate(R.layout.list_item,null);
        final View con = layoutInflater.inflate(R.layout.list_item,null);
        ImageView leftImg = (ImageView) con.findViewById(R.id.leftImage);

        ImageView rightImg = (ImageView) con.findViewById(R.id.rightImage);
        //初始化；
            int leftID = ((Integer) ImgsList.get(position).get("leftImgID"));
            int rightID = ((Integer) ImgsList.get(position).get("rightImgID"));
            leftImg.setImageResource(leftID);
            //设置tag标记值；
             leftImg.setTag(leftID);
            rightImg.setImageResource(rightID);
            //设置tag标记值；
            rightImg.setTag(rightID);

        /*if(R.drawable.add_image==leftID){

            leftImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"This is"+"add_icon",Toast.LENGTH_SHORT).show();
                }
            });
        }*/
           /* //为左侧图片添加监听；
           if(R.drawable.add_image==leftID){

            leftImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"This is"+infoPosition,Toast.LENGTH_SHORT).show();
                }
            });
             }
            //为右侧图片添加监听；
            rightImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"This is"+infoPosition,Toast.LENGTH_SHORT).show();
                }
            });*/


        return con;
    }
}
