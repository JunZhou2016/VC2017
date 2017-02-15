package zj.qidian.com.doctor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by xdsm on 2016/12/2.
 */

public class ClickHandler implements View.OnClickListener {


    Context context;
    AppCompatActivity currentActivity = null;

    public ClickHandler(Context context, AppCompatActivity currentActivity) {
        this.context = context;
        this.currentActivity = currentActivity;
    }





   /* public ClickHandler(Context context) {
        this.context = context;
    }*/



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //如果点击的是中央的启动按钮则执行如下的操作；
           case R.id.readytotest:
               // Toast.makeText(context,"ClickHander",Toast.LENGTH_SHORT).show();
            {
                //启动录音处理程序；
                Intent intent = new Intent(context,Loading.class);
                context.startActivity(intent);

            }
                break;
            case R.id.showRankTable:
                // Toast.makeText(context,"ClickHander",Toast.LENGTH_SHORT).show();
            {
                //启动排名显示界面；
                Intent intent = new Intent(context,RankDetailShow.class);
                context.startActivity(intent);
                //设置界面切换的效果;
                currentActivity.overridePendingTransition(R.anim.showfrom_right, R.anim.reduce_alpha);

            }
            break;


            default:
                break;

        }
    }



}
