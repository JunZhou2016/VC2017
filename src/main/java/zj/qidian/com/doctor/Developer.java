package zj.qidian.com.doctor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

public class Developer extends AppCompatActivity {

    private AdapterViewFlipper adapterViewFlipper = null;
    private BaseAdapter baseAdapter = null;
    private int shouldFliper = 1;
    private AlertDialog alertDialog = null;
    private int[] imgs = new int[]{
            R.drawable.fimg,R.drawable.simg,R.drawable.timg,R.drawable.foimg
    };
    private String zj = "刘亦菲个人首张专辑《刘亦菲》于2006年8月31日中国首发专辑，" +
            "刘亦菲清新如氧气般的声音将带有英伦风、流行风、田园乡村等风格的歌曲成 " +
            "功展现在大家的面前，这张专辑也是新索音乐送给刘亦菲8月25日19好的礼物。";
    private String lmx = "高圆圆（1979年10月5日－，原名高媛媛）， 北京人，影视演员和模特。" +
            "从《爱情麻辣烫》开始涉足电影。2001年，参加拍摄的电影《十七岁的单车》夺得柏林电" +
            "影节银熊奖，从此正式开始演艺之路。2003年高圆圆在《倚天屠龙记》中饰演峨眉派掌门周芷若。";

    private  String dt = "林依晨（Ariel Lin），1982年10月29日出生于台湾省宜兰县，中国台湾女演员、" +
            "歌手。 2000年，获得《台北捷运报》举办的“第一届捷运超美少女比赛”冠军 。2001年，考入" +
            "国立政治大学韩文系，其后陆续参与MV与广告的拍摄。2002年正式出道，处女作是《十八岁的约定》。";

    private  String wzs = "范冰冰，1981年9月16日生于山东青岛，毕业于上海师范大学谢晋影视艺术学院，中" +
            "国女演员，歌手。 1998年参演电视剧《还珠格格》成名。2004年主演电影《手机》获得第27届大众" +
            "电影百花奖最佳女演员奖。2007年成立范冰冰工作室；同年凭借电影《心中有鬼》获得第44届台湾" +
            "电影金马奖最佳女配角奖。";

    private String[] personInfo = new String[]{zj,lmx,dt,wzs};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
        //将当前活动加入活动和集合中;
        ActivityCollector.addActivity(this);
        //隐藏顶部活动条;
        initActionBar();
        //初始化界面;
        init();
        setAdapter();
        adapterViewFlipper.setAdapter(baseAdapter);
        adapterViewFlipper.startFlipping();


    }

    private void init() {

        adapterViewFlipper = (AdapterViewFlipper) findViewById(R.id.flipper);

    }

    private void initActionBar(){

        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();

        }

    }

    private void setAdapter(){
        //该方法最终将完成适配器的初始化功能;
        baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return imgs.length;
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                //这里创建并初始化滑动视图;
                ImageView imageView = new ImageView(Developer.this);
                final String msg = personInfo[position];
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createDialog(msg);
                        showDialog();

                    }
                });
                imageView.setImageResource(imgs[position]);
                //设置图片的缩放方式;
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                //设置imageview的布局方式;
                   // 为imageView设置布局参数
                imageView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return imageView;
            }
        };

    }

    private void createDialog(String msg ){
        //创建dialog构造器;
        AlertDialog.Builder builder = new AlertDialog.Builder(Developer.this);
        //构造dialog;
        builder.setCancelable(false);
        builder.setTitle("人物简介").setMessage(msg);
        builder.setNegativeButton("取消",new DialogInterface.OnClickListener(){


            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(Developer.this,"You Had Click Cancel!",Toast.LENGTH_SHORT);
                //对话框被取消的时候图片启动自动播放;
                //adapterViewFlipper.startFlipping();
                //显示对话框;
                hiddenDialog();

            }
        });

        builder.setPositiveButton("确定",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(Developer.this,"You Had Click Sure!",Toast.LENGTH_SHORT);
                //对话框被取消的时候图片启动自动播放;
               // adapterViewFlipper.startFlipping();
                //隐藏对话框;
                hiddenDialog();

            }
        });

        builder.setIcon(R.drawable.introduce_icon);
        alertDialog = builder.create();
        setAlertDialog(alertDialog);




    }

    private void  showDialog(){
        adapterViewFlipper.stopFlipping();
        alertDialog.show();

    }

    private void hiddenDialog(){

        alertDialog.hide();
        adapterViewFlipper.startFlipping();

    }

    private void setAlertDialog(AlertDialog dialog){
        //设置对话框的透明度;
           //获取alertdialog的窗体对象;
        Window window = alertDialog.getWindow();
        //获取窗体管理器;
        WindowManager.LayoutParams lp = window.getAttributes();
        //注意透明度一定为浮点数，否则无效;
        lp.alpha = 0.6f;
        window.setAttributes(lp);


    }



}
