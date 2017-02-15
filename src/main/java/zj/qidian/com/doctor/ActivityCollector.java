package zj.qidian.com.doctor;
import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QiDain on 2016/11/25.
 */

public class ActivityCollector {
    //该程序应该以单例模式存在；
    //创建list，用于收集已经启动的Activity;
    public static  List<Activity> list = new ArrayList<Activity>();

    //创建Activity的管理方法；
    public static   void addActivity(Activity activity) {

        list.add(activity);

    }

    public static  void removeActivity(Activity activity){

        list.remove(activity);
    }

    public static  void finishAll(){
        //判断活动是否仍然在运行；
        //for(Activity app:list){
        Activity app = null;
            for(int i = 0;i<list.size();i++){
                app = list.get(i);
                if(!app.isFinishing()){
                    app.finish();
                    list.remove(app);

                }
            }



       // }

    }


    private void removeCurrentActivity(Activity currentActivity){
        currentActivity.finish();
        currentActivity = null;

    }

}
