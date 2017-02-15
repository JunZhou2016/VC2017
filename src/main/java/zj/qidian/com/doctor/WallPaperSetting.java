package zj.qidian.com.doctor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.qidian.adapter.ImageAdapter;
import com.qidian.model.ImgInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WallPaperSetting extends AppCompatActivity {
    private int imageID = 0;
    private boolean showAlertDialog = true;
    private List<Map<String,Object>> imgList = null;
    private int setting_bg = R.drawable.changge_bg;
    private int imgIdF[] = new int[]{R.drawable.add_image,R.drawable.skin1,R.drawable.skin2,R.drawable.skin3,R.drawable.skin4
    ,R.drawable.skin5,R.drawable.skin12
    };

    private int imgIdS[] = new int[]{R.drawable.skin6,R.drawable.skin7,R.drawable.skin8,R.drawable.skin9,R.drawable.skin10,
            R.drawable.skin11,R.drawable.launch1 };

    private ListView listView = null;
    private Resources imgResources = null;

        //预期在该活动中完成网络图片下载和本地壁纸的选择；
           //网络图片的下载由okhttp或者fresco框架完成；
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ActivityCollector.addActivity(this);
        showAlertDialog = readFromSharedPreferences();
      /*  Toast.makeText(WallPaperSetting.this,"This is WallPaperSetting",Toast.LENGTH_SHORT).show();*/
       if(showAlertDialog){

           //创建自定义对话框，用于更新背景；
           {
               //创建对话框构造器；
               AlertDialog.Builder builder = new AlertDialog.Builder(this);
               //设置是否可有返回键和屏幕触摸取消；
               builder.setCancelable(false);
               //设置标题；
               builder.setTitle("请选择喜欢的背景\n").setIcon(R.drawable.logo_paper).setMessage("您可以选择本地或者网络图片作为您应用的背景图片。\n" +
                       "温馨提示：选择网络图片会耗费少量流量！");
               builder.setPositiveButton("不再显示", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                       Toast.makeText(WallPaperSetting.this,"不再显示",Toast.LENGTH_SHORT).show();
                       //这个值应该使用持久化技术进行存储；
                          //将用户的选择进行持久化保存；
                          saveToSharedPreferences();


                   }
               });

               builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                       Toast.makeText(WallPaperSetting.this,"取消",Toast.LENGTH_SHORT).show();

                   }
               });

               //使用构建起创建对话框并显示；
               builder.create().show();
           }

       }


           //添加数据到list();
          getData();


            //创建适配器；
            ImageAdapter imageAdapter = new ImageAdapter(WallPaperSetting.this,imgList);

            //设置适配器；
            listView = (ListView) findViewById(R.id.imglist);
            listView.setAdapter(imageAdapter);



    }


  //监听返回键的点击动作；
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){

            Intent intent = new Intent(WallPaperSetting.this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.add_alpha,R.anim.hideto_right);
            finish();

        }
        //注意返回值的类型；
        return false;
    }

    private void  getData(){

        //创建数据实体并构造适配器；
        imgList = new ArrayList<Map<String,Object>>();
        for(int i = 0;i<=6;i++) {
            //初始化实体;
            ImgInfo imgInfo = new ImgInfo();
            Map<String, Object> imgMap = new HashMap<String, Object>();
            //imgInfo.setResId(imgId[i]);
          //  imgInfo.setResName("壁纸" + i);
            //添加实体的属i性到Map中;
            //imgList.put("ImgID", imgInfo);
            imgMap.put("leftImgID", imgIdF[i]);
            imgMap.put("rightImgID", imgIdS[i]);
            imgList.add(imgMap);
        }

    }

    //监听壁纸图片的点击动作;
     public void selectImg(View view){
         //处理图片的点击事件；
         ImageView imageView = (ImageView) view;
          imageID = (int) imageView.getTag();
        /* if(imageID==R.drawable.add_image){
             //如果点击的是添加相册图片的按钮，则执行另一种操作;

             Toast.makeText(WallPaperSetting.this,"onBackPressed"+imageID,Toast.LENGTH_SHORT).show();
             //进入相册选择相册中的文件;


         }else{*/
             //点击的图片非添加按钮;

             // Toast.makeText(WallPaperSetting.this,"onBackPressed"+imageID,Toast.LENGTH_SHORT).show();
             //创建活动用于填充数据；
             Intent intent = new Intent();
             //添加数据；
        /*ImgInfomation imgInfomation = new ImgInfomation();
        imgInfomation.setImgresources(imgResources);
        intent.putExtra("imginfo",imgInfomation);*/
             intent.putExtra("imginfo",imageID);
             setResult(RESULT_OK,intent);
             //调用finish();
             finish();
             //调用动画效果；
             overridePendingTransition(R.anim.add_alpha,R.anim.rotate_alpha);

        // }



     }

    //该方法无故失效；
   /* //重写返回键的点击监听事件；
    @Override
    public void onBackPressed() {
        //在应用的过程中注意此处需要将super.onBackPressed()注释掉；
        //在这里纯关键
        Toast.makeText(WallPaperSetting.this,"onBackPressed",Toast.LENGTH_SHORT).show();
        finish();
    }*/
    //将数据保存到sharedPreferences中；
   public void saveToSharedPreferences(){
       SharedPreferences.Editor editor = getSharedPreferences("isShowDialog",MODE_PRIVATE).edit();
       //通过文件编辑器，编辑写入数据；
       editor.putBoolean("userDecision",false);
       //提交数据；
       editor.commit();

   }

    //从sharedPreferences中读取数据；
    public boolean readFromSharedPreferences(){
        boolean selectState = true;
        //获取SharedPreferences对象;
        SharedPreferences sharedPreferences = getSharedPreferences("isShowDialog",MODE_PRIVATE);
        selectState = sharedPreferences.getBoolean("userDecision",true);
        return selectState;
    }


}
