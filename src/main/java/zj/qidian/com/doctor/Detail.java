package zj.qidian.com.doctor;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.qidian.model.RankInfo;

/**
 * Created by xdsm on 2016/12/19.
 */

public class Detail extends AlertDialog {

    int ResId = 0;
    private ImageButton cancleButton = null;
    RankInfo rankInfo = null;
    int rank = 0;
    private TextView detailusername = null;
    private TextView detailscore = null;
    private TextView detailId = null;
    private TextView detailphone = null;
    private QuickContactBadge quickContactBadge = null;
    public Detail(Context context, int resId, RankInfo rankInfo, int rank) {
        super(context);
        ResId = resId;
        this.rankInfo = rankInfo;
        this.rank = rank;
    }

    /*public Detail(Context context,int resId, String messagetoshow) {
        super(context);
        ResId = resId;
        this.messagetoshow = messagetoshow;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(ResId);
        initView();
        detailusername.setText(rankInfo.getUserneme());
        detailscore.setText(rankInfo.getScore());
        detailId.setText(rank+"");
        detailphone.setText(rankInfo.getPhone());
        //初始化信息;
        cancleButton = (ImageButton) this.findViewById(R.id.cancledetatil);
        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });

        //注意在此处声明权限;
        quickContactBadge.assignContactFromPhone(rankInfo.getPhone(),false);

    }

    private void initView(){
         detailusername = (TextView) findViewById(R.id.detailusername);
         detailscore = (TextView) findViewById(R.id.detailscore);
         detailId = (TextView) findViewById(R.id.detailId);
         detailphone = (TextView) findViewById(R.id.detailphone);
         quickContactBadge = (QuickContactBadge) findViewById(R.id.quickcontact);


    }




}
