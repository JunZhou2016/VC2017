package com.qidian.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.qidian.model.RankInfo;

import java.util.List;

import zj.qidian.com.doctor.Detail;
import zj.qidian.com.doctor.R;

import static zj.qidian.com.doctor.R.id.rphone;
import static zj.qidian.com.doctor.R.id.rrank;
import static zj.qidian.com.doctor.R.id.rscore;
import static zj.qidian.com.doctor.R.id.rusername;

/**
 * Created by xdsm on 2016/12/19.
 */

public class RankListAdapter extends BaseAdapter {

    Context context = null;
    List<RankInfo> datalist = null;
    LayoutInflater layoutInflater = null;
   /* private TextView rusername = null;
    private TextView rscore = null;
    private TextView rrank = null;
    private TextView rphone = null;*/
    private String UN = null;
    private String SC = null;
    private String Rank = null;
    private String PN = null;

    public RankListAdapter(Context context, List<RankInfo> datalist) {
        this.context = context;
        this.datalist = datalist;
    }



    @Override
    public int getCount() {
        return  datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //获取布局加载器;
        layoutInflater = LayoutInflater.from(context);
        //加载视图;
        View view = layoutInflater.inflate(R.layout.rank_listitem,null);
        TextView rusername = (TextView) view.findViewById(R.id.rusername);
        TextView rscore = (TextView) view.findViewById(R.id.rscore);
        TextView rrank = (TextView) view.findViewById(R.id.rrank);
        TextView rphone = (TextView) view.findViewById(R.id.rphone);
        View layout = view.findViewById(R.id.call);
        //initView(view);

        //初始化视图数据;
        final RankInfo  rankInfo = datalist.get(position);
        UN = rankInfo.getUserneme();
        SC = rankInfo.getScore();
        Rank = (position+1)+"";
        PN = rankInfo.getPhone();
        rusername.setText(UN);
        rscore.setText(SC);
        rrank.setText(Rank);
        rphone.setText(PN);
        //为电话栏目添加点击效果;
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(context,"You Click Me!Fuck You!",Toast.LENGTH_SHORT).show();
                //这里应该使用自定义对话框实现详细信息提示的功能;
              /*  AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.detailphone);
                builder.setTitle("详细信息");
                builder.setMessage("")*/
                Detail detail = new Detail(context,R.layout.detail,rankInfo,position);
                detail.setCancelable(false);
                detail.setCanceledOnTouchOutside(false);
                detail.show();


            }
        });

        return view;
    }


}
