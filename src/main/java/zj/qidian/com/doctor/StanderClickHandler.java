package zj.qidian.com.doctor;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * Created by xdsm on 2016/12/4.
 */

public class StanderClickHandler implements View.OnClickListener {
    //View view = null;
    public StanderClickHandler(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    Activity currentActivity = null;
    @Override
    public void onClick(View v) {

        switch(v.getId()){
           case R.id.sure_to_setting:
                currentActivity.finish();
                currentActivity.overridePendingTransition(R.anim.add_alpha,R.anim.rotate_alpha);
              /* Intent intent = new Intent();
               intent.setAction(Intent.ACTION_MAIN);
               intent.addCategory(Intent.CATEGORY_HOME);
               currentActivity.startActivity(intent);
               currentActivity.overridePendingTransition(R.anim.showfrom_right,R.anim.scaletosmall);*/
                break;
            default:
                break;

        }

    }
}
