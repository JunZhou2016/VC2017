package com.qidian.CommonUtils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by xdsm on 2016/12/23.
 */

public class SharedPreferencedBO {

    //这个业务类的兼容性实现比较复杂，可用性不高，暂时舍弃;

    /*public SharedPreferencedBO(String sharedPreferenceName, String valueName, Context content) {
        SharedPreferenceName = sharedPreferenceName;
        this.valueName = valueName;
        this.content = content;
    }

    public String SharedPreferenceName = null;
    public String valueName = null;
    public Context content = null;


     //读取SharedPreferences中的数据;
    //将数据保存到sharedPreferences中；
  public void saveInSharePreferences(int summary) {
      //将参数设置的值保存在SharedPreferences文件中;
      SharedPreferences.Editor editor = content.getSharedPreferences("currentNoise", MODE_PRIVATE).edit();
      //通过文件编辑器，编辑写入数据；
      editor.putInt("noiseSummary", summary);
      //提交数据；
      editor.commit();

  }

    //从sharedPreferences中读取数据；
    public int readFromSharedPreferences() {
        int currentStander = 0;
        //获取SharedPreferences对象;
        SharedPreferences sharedPreferences = content.getSharedPreferences("currentNoise", MODE_PRIVATE);
        currentStander = sharedPreferences.getInt("noiseSummary", 0);
        return currentStander;
    }
    //保存数据到SharedPreferences;


*/


}
