package com.qidian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qidian.model.History;

import java.util.List;
import java.util.Map;

import zj.qidian.com.doctor.R;

/**
 * Created by xdsm on 2016/12/10.
 */

public class HistoryAdapter extends BaseAdapter {

    //该类主要实现填充数据的历史记录表中;
    //自定义数据适配器;
    private List<Map<String,Object>> datasList = null;
    Context context = null;
    private LayoutInflater layoutInflater = null;

    public HistoryAdapter(List<Map<String, Object>> datasList, Context context) {

        this.datasList = datasList;
        this.context = context;
    }


    @Override
    public int getCount() {

        return datasList.size();

    }

    @Override
    public Object getItem(int position) {

        return datasList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //创建布局加载器;
        layoutInflater = LayoutInflater.from(context);
        View myview = layoutInflater.inflate(R.layout.listitems_history,null);
        TextView Number = (TextView) myview.findViewById(R.id.number);
        TextView Timers = (TextView) myview.findViewById(R.id.timer);
        TextView ScoreItem = (TextView) myview.findViewById(R.id.score);
        TextView State = (TextView) myview.findViewById(R.id.state);
        History historyRecorder = (History) datasList.get(position).get("historyitems");
        Number.setText(""+historyRecorder.getId());
        Timers.setText(historyRecorder.getTimer());
        ScoreItem.setText(""+historyRecorder.getScore());
        State.setText(historyRecorder.getState());
        return myview;
    }
}
