package zj.qidian.com.doctor;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qidian.CustomView.HomeDiagram;
import com.qidian.adapter.RankListAdapter;
import com.qidian.model.RankInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import NeccessaryRes.NecRes;
import okhttp3.Call;

import static android.view.View.Y;

//_______________________________________________当前程序开始________________________________________
public class MainActivity extends AppCompatActivity {
    //在本活动中预期完成主界面数据到额初始化;
    //在这里定义一些变量；
    private int imgRes = 0;
    //定义默认的滑动速度；
    public static final int SNAP_VELOCITY = 200;
    private int leftEdge = 0;
    private int rightEdge = 0;
    private LinearLayout.LayoutParams menuParams;
    private LinearLayout.LayoutParams contentParams;
    private int menuwidth = 0;
    private int contentwidth = 0;
    private int padding = 200;
    private int screenWidth = 0;
    private View menu = null;
    private View content = null;
    //定义速度监听器；
    private VelocityTracker mVelocityTracker = null;
    private float xDown = 0;
    private float xUp = 0;
    private float xMove = 0;
    private int distanceX = 0;
    private ImageView startButton = null;
    //设置菜单显示的标志；
    //boolean值的默认值为false;
    private boolean isMenuVisible = false;
    private int LaunchImg = 0;
    private boolean hadClicked = false;
    private int startButtonBg = 0;
    private boolean shouldShowDialog = true;
    private TextView showRankTable = null;
    //定义排名相关变量;
    private List<RankInfo> rankinformation = null;
    private RankListAdapter rankListAdapter = null;
    private ListView ranklistView = null;
    //定义图片资源；
    private int imgIdF[] = new int[]{R.drawable.add_image, R.drawable.skin1, R.drawable.skin2, R.drawable.skin3, R.drawable.skin4
            , R.drawable.skin5
    };

    private int imgIdS[] = new int[]{R.drawable.skin6, R.drawable.skin7, R.drawable.skin8, R.drawable.skin9, R.drawable.skin10,
            R.drawable.skin11};
    //处理排名相关的数据;
    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            //判断消息来源;
            if (msg.what == 002) {
                //初始化数据列表;
                rankinformation = (List<RankInfo>) msg.obj;
                // Toast.makeText(MainActivity.this,rankinformation.get(0).getPhone(),Toast.LENGTH_LONG).show();
                //在这里初始化适配器;
                // initRank(rankinformation);

            }
        }
    };


    //滚动数据部分
    private RelativeLayout linear = null;
    private int data = 0;
    private int older = -1;
    private int distance = 0;
    private int touchConter = 0;
    private HorizontalScrollView horizontalScrollView = null;
    //这里的数据应该是从数据库中实时获取的;
    int dataArray[] = new int[]{2551, 2547, 916, 1872, 1965, 1914, 4219, 5319, 4568, 213, 5246, 7825, 1200, 1543, 4561, 1024, 3562, 5428, 1426, 4568, 213, 5246, 7825, 1200, 1543, 4561, 1024, 3562, 5428, 1426,
    };
    //是否继续滚动;
    private boolean shouldScroll = true;
    //处理滑动事件；
    private Handler scrollHadler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 12) {

                //调用滚动事件；
                horizontalScrollView.setScrollX(distance++);
                ToMax();

            }

        }
    };


    //活动的生命周期开始；
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏当前窗体;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //初始化界面；
        createAlertDialog();
        initActionBar();
        initView();
        //ActivityCollector.addActivity(this);
        // sendRequestToMysql();
        //监听内容布局视图的触摸事件；
        content.setOnTouchListener(new TouchHandler());
        //getRankInfoFromMysql();

    }


    private void initActionBar() {
        //在获取ActionBar的时候切记之用support支持的；
        ActionBar actionBar = this.getSupportActionBar();
        //判断ActionBar是否为空，避免异常的发生；
        if (actionBar != null) {
            //设置显示向左的图标；
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu_back);
            //设置应用程序的图标显示；
            actionBar.setDisplayShowHomeEnabled(true);
            //设置应用程序的图标可点击；
            actionBar.setHomeButtonEnabled(true);
            //设置显示应用程序的标题；
            actionBar.setDisplayShowTitleEnabled(true);

        }

    }

    private void initView() {
        //初始化界面，使得初次显示的时候菜单布局被隐藏；
        startButton = (ImageView) findViewById(R.id.readytotest);
        menu = findViewById(R.id.mymenumenu);
        content = findViewById(R.id.mycontent);
        showRankTable = (TextView) findViewById(R.id.showRankTable);
        //  ranklistView = (ListView) findViewById(R.id.rank);
        startButton.setOnClickListener(new ClickHandler(MainActivity.this, this));
        showRankTable.setOnClickListener(new ClickHandler(MainActivity.this, this));


        int startButtonBg = readButtonInfoFromSharedPreferences("buttonInfo");
        //当为默认值时进行如下逻辑操作；
        if (R.drawable.buttonselect == startButtonBg) {
            Toast.makeText(MainActivity.this, "equal", Toast.LENGTH_SHORT).show();
            startButtonBg = R.drawable.buttonselect;

        }

        //startButton.setBackgroundResource(startButtonBg);


        //动态更改视图的背景；
        // LaunchImg = readFromSharedPreferences("imgID");
        LaunchImg = R.drawable.mainbgreview;
        content.setBackgroundResource(LaunchImg);


        menuParams = (LinearLayout.LayoutParams) menu.getLayoutParams();
        contentParams = (LinearLayout.LayoutParams) content.getLayoutParams();
        //获取窗口的尺寸；
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenWidth = windowManager.getDefaultDisplay().getWidth();
        //设置绘图参数；
        //设置菜单的尺寸为屏幕的尺寸减去padding，这样可以保证在菜单显示的时候内容可以部分显示，增强了可视化效果；
        menuParams.width = screenWidth - padding;
        //初始化左侧边界值；
        leftEdge = -menuParams.width;
        menuParams.leftMargin = -menuParams.width;
        //将内容的宽度设置为窗口的宽度；
        contentParams.width = screenWidth;


        //图标显示初始化
        List<Integer> lists = new ArrayList<Integer>();
        //
        for (int i = 1; i < 30; i++) {
            data = dataArray[i] / 200;
            lists.add(data);
        }
        linear = (RelativeLayout) findViewById(R.id.linear);
        linear.addView(new HomeDiagram(this, lists));
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.scrolltools);
        horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Toast.makeText(MainActivity.this, horizontalScrollView.getScrollY() + "", Toast.LENGTH_SHORT).show();
                touchConter++;
                if (touchConter % 2 == 1) {
                    shouldScroll = false;
                } else {
                    shouldScroll = true;
                    ToMax();

                }


                return true;
            }
        });
        ToMax();

    }

    //定义触摸事件处理对象；
    class TouchHandler implements View.OnTouchListener {


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //根据触摸活动的生命周期进行处理；
            //首先根据触摸活动获取手指在屏幕上的滑动速度；
            //创建轨迹速度跟踪器；
            createVelocityTracker(event);
            //进行分类处理；
            switch (event.getAction()) {
                //手指按下的时候出发此类活动；
                case MotionEvent.ACTION_DOWN:
                    //手指按下时候记录此点的横坐标；
                    xDown = event.getRawX();
                    break;
                //手指移动的时候触发此类活动；
                case MotionEvent.ACTION_MOVE:
                    /*Toast.makeText(MainActivity.this,"Hello JunZhou",Toast.LENGTH_SHORT).show();*/
                    //记录手指移动过程中各点的横坐标；
                    xMove = event.getRawX();
                    //记录移动的距离；
                    distanceX = (int) (xMove - xDown);
                    if (isMenuVisible) {
                        //菜单完全显示后候进入此处逻辑；
                        menuParams.leftMargin = rightEdge + distanceX;

                    } else {
                        //菜单收缩状态下进入此处逻辑；
                        menuParams.leftMargin = leftEdge + distanceX;
                    }
                    //判断是否达到边界条件；
                    //如果到达边界条件则将leftmargin的值设置为默认的边界值；
                    //此处为了在移动中不留空隙，以“>”和“<”为判定条件；
                    if (menuParams.leftMargin < leftEdge) {

                        menuParams.leftMargin = leftEdge;
                    } else if (menuParams.leftMargin > rightEdge) {

                        menuParams.leftMargin = rightEdge;

                    }
                    //根据更新后的参数重新绘制界面；
                    //这句话很重要，不写的话只是在内存中更新值，在手机窗口中则无体现；
                    menu.setLayoutParams(menuParams);
                    break;
                //手指抬起的时候触发此类活动；
                case MotionEvent.ACTION_UP:
                    //距离手指抬起点的坐标；
                    xUp = event.getRawX();
                    if (wantToShowMenu()) {
                        //进行意图判断；
                        if (shuouldScrollToMenu()) {

                            scrollToMenu();

                        } else {
                            //没有达到滚动到目标的条件则进行回滚，这句产生了弹性效果；
                            scrollToContent();

                        }

                    } else if (wantToShowContent()) {
                        //进行意图判断；
                        if (shuouldScrollToContent()) {

                            scrollToContent();

                        } else {
                            //没有达到滚动到目标的条件则进行回滚，这句产生了弹性效果；
                            scrollToMenu();

                        }

                    }


            }

            return true;
        }


    }

    private void scrollToMenu() {

        new ScrollTask().execute(30);

    }

    private void scrollToContent() {

        new ScrollTask().execute(-30);

    }

    private boolean shuouldScrollToMenu() {
        //判断是否应该滚动至菜单界面；
        return xUp - xDown > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;

    }

    private boolean shuouldScrollToContent() {
        //判断是否应该滚动至菜单界面；
        return xDown - xUp + padding > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;

    }

    private int getScrollVelocity() {
        //设置轨迹检测单位为一秒；
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        //因为速度可能是负值，所以此处取绝对值；
        return Math.abs(velocity);

    }

    private boolean wantToShowMenu() {
        //此时全局变量的作用就显现出来了；
        //当手指右划且菜单当前隐藏的时候；
        return xUp - xDown > 0 && !isMenuVisible;

    }

    private boolean wantToShowContent() {
        //此时全局变量的作用就显现出来了；
        //当手指左划且当前菜单显示的时候；
        return xUp - xDown < 0 && isMenuVisible;

    }


    //创建轨迹速度监听器；
    private void createVelocityTracker(MotionEvent event) {

        if (mVelocityTracker == null) {

            mVelocityTracker = VelocityTracker.obtain();

        }
        //绑定移动事件到轨迹监听器；
        mVelocityTracker.addMovement(event);

    }

    //创建滚动处理异步任务；
    class ScrollTask extends AsyncTask<Integer, Integer, Integer> {
        //AsyncTask<Integer, Integer, Integer>
        //上面色三个参数：第一个为：doInBackground(Integer... params) 中的输入值参数类型；
        //上面色三个参数：第二个为：onProgressUpdate 中的输入值参数类型；
        //上面色三个参数：第三个为：onPostExecute(Float aFloat)中输入值参数类型；
        //上面色三个参数：第三个为：doInBackground(Integer... params)中的返回值值参数类型；
        //执行耗时任务的
        @Override
        protected Integer doInBackground(Integer... params) {
            //在此处开始耗时任务；
            int leftMargin = menuParams.leftMargin;
            //根据传入的速度来滚动界面，当到达便捷值得时候跳出循环；
            while (true) {
                leftMargin = leftMargin + params[0];
                if (leftMargin > rightEdge) {
                    leftMargin = rightEdge;
                    break;
                }

                if (leftMargin < leftEdge) {

                    leftMargin = leftEdge;
                    break;
                }

                //更新界面；
                publishProgress(leftMargin);
                //为了产生滚动的效果，每次循环使得线程睡眠20秒；
                sleep(20);

            }
            //根据显示结果更新现实的额标志值；
            if (params[0] > 0) {
                //如果是向右滚动则菜单显示；
                isMenuVisible = true;

            } else {
                isMenuVisible = false;

            }


            return leftMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //更新UI;
            menuParams.leftMargin = values[0];
            menu.setLayoutParams(menuParams);


        }

        @Override
        protected void onPostExecute(Integer integer) {

            //当滚动任务结束的时候UI界面可能出现空隙，在此处进行微调；
            super.onPostExecute(integer);
            menuParams.leftMargin = integer;
            menu.setLayoutParams(menuParams);

        }
    }

    private void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void showDetail(View view) {
        //响应菜单项的点击操作，展示菜单项的详细内容;
        switch (view.getId()) {
            case R.id.wallpaper: {
                //进入更换壁纸的详细界面；
                Intent intent = new Intent(MainActivity.this, WallPaperSetting.class);
                startActivityForResult(intent, 1);
                //设置界面切换的效果;
                overridePendingTransition(R.anim.showfrom_right, R.anim.reduce_alpha);
            }
            break;
            case R.id.noisestander: { //进入基准噪音参数设置的界面;
                // Intent intent = new Intent(MainActivity.this, NoiseStander.class);
                Intent intent = new Intent(MainActivity.this, NoiseStanderSetting.class);
                startActivity(intent);
                //设置界面切换的效果;
                overridePendingTransition(R.anim.showfrom_right, R.anim.reduce_alpha);
            }
            break;
            case R.id.historyrecord:
                //Toast.makeText(this, "historyrecord", Toast.LENGTH_SHORT).show();
            { //进入历史记录显示界面;
                Intent intent = new Intent(MainActivity.this, HistoryRecords.class);
                startActivity(intent);
                //设置界面切换的效果;
                overridePendingTransition(R.anim.showfrom_right, R.anim.reduce_alpha);
            }

            break;
            case R.id.devdescribe: { //进入开发者简介界面;
                Intent intent = new Intent(MainActivity.this, Developer.class);
                startActivity(intent);
                //设置界面切换的效果;
                overridePendingTransition(R.anim.showfrom_right, R.anim.reduce_alpha);
            }
            break;
            case R.id.userhelper: { //进入使用帮助界面;
                Intent intent = new Intent(MainActivity.this, Helper.class);
                startActivity(intent);
                //设置界面切换的效果;
                overridePendingTransition(R.anim.showfrom_right, R.anim.reduce_alpha);
            }
            break;
            case R.id.hintent: { //进入健康趋势界面界面;
                Intent intent = new Intent(MainActivity.this, YourHealthyIntent.class);
                startActivity(intent);
                //设置界面切换的效果;
                overridePendingTransition(R.anim.showfrom_right, R.anim.reduce_alpha);
            }
            break;
            case R.id.hkonw: { //进入健康知道界面;
                Intent intent = new Intent(MainActivity.this, KnowReal.class);
                startActivity(intent);
                //设置界面切换的效果;
                overridePendingTransition(R.anim.showfrom_right, R.anim.reduce_alpha);
            }
            break;
            case R.id.exitapp: {
                //退出当前应用;
                exitApp();
                //结束所有当前正在运行的活动;
                ActivityCollector.finishAll();

            }
            break;
            default:
                break;

        }

    }

    //重写onActivityResult()方法接收被启动的活动返回来的数据；

/*

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    //数据返回成功后初始化背景图片的资源ID;
                    imgRes = data.getIntExtra("imginfo",0);
                    Toast.makeText(MainActivity.this,"MainActivity"+imgRes,Toast.LENGTH_SHORT).show();
                    //将用户选中的背景图片信息保存至SharedPreferences文件中；
                    saveToSharedPreferences(imgRes,"imgID");
                    //更新用界面的背景图片；
                    dynamicChangeContentBg(content,imgRes);
                }
                break;
            default:
                break;

        }

    }
*/

    public void dynamicChangeContentBg(View content, int imgRes) {
        //此方法用于动态的更改内容界面的背景；
        //这个方法应在在初始化的时候调用；
        content.setBackgroundResource(imgRes);

       /*Bitmap bitmap = null;
       BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);*/


    }


    //-----------------------------------------该段代码可服用，后期可整合处理-------------------------
    //保存用户的设置内容到SharedPreferences文件中;
    public void saveToSharedPreferences(int currentResID, String saveKey) {
        //保存用户选中的背景图片到SharedPreferences文件中;
        SharedPreferences sharedPreferences = getSharedPreferences("bgInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(saveKey, currentResID);
        editor.commit();
    }

    //将按钮样式信息保存至SharedPreferences文件中；
    public void saveBurronInfoToSharedPreferences(int currentResID, String saveKey) {
        //保存用户选中的背景图片到SharedPreferences文件中;
        SharedPreferences sharedPreferences = getSharedPreferences("bgInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(saveKey, currentResID);
        editor.commit();
    }

    public void saveToSharedPre(String saveKey, boolean resState) {
        SharedPreferences sharedPreferences = getSharedPreferences("bgInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(saveKey, resState);
        editor.commit();

    }

    //从SharedPreferences文件中获取用户保存的设置内容;
    public int readFromSharedPreferences(String readKey) {
        int currentRes = 0;
        SharedPreferences sharedPre = getSharedPreferences("bgInfo", MODE_PRIVATE);
        // currentRes = sharedPre.getInt(readKey, R.drawable.launch1);
        currentRes = sharedPre.getInt(readKey, R.drawable.mainbgreview);
        return currentRes;
    }

    //从SharedPreferences文件中读取按钮样式信息保存至；
    public int readButtonInfoFromSharedPreferences(String readKey) {
        int currentRes = 0;
        SharedPreferences sharedPre = getSharedPreferences("bgInfo", MODE_PRIVATE);
        currentRes = sharedPre.getInt(readKey, R.drawable.buttonselect);
        return currentRes;
    }

    //此处是否应该设置一个还原默认设置的选项呢？
    public boolean readFromSharedPre(String readKey) {
        boolean res = false;
        SharedPreferences sharedPre = getSharedPreferences("bgInfo", MODE_PRIVATE);
        res = sharedPre.getBoolean(readKey, true);
        return res;
    }

    //-----------------------------------------该段代码可服用，后期可整合处理-------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //在导航栏添加菜单;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //监听菜单项的点击效果；
        switch (item.getItemId()) {
            case R.id.purple:
                startButton.setBackgroundResource(R.drawable.purplebuttonselect);
                saveBurronInfoToSharedPreferences(R.drawable.purplebuttonselect, "buttonInfo");
                break;
            case R.id.brown:
                startButton.setBackgroundResource(R.drawable.brownbuttonselect);
                saveBurronInfoToSharedPreferences(R.drawable.brownbuttonselect, "buttonInfo");
                break;
            case R.id.blue:
                startButton.setBackgroundResource(R.drawable.bluebuttonselect);
                saveBurronInfoToSharedPreferences(R.drawable.bluebuttonselect, "buttonInfo");
                break;
            case android.R.id.home:
                //监听左侧应用图标的点击效果;
                if (!isMenuVisible) {
                    //如果菜单选项当前状态为隐藏则执行菜单显示任务；
                    scrollToMenu();
                } else if (isMenuVisible) {
                    //如果菜单选项当前状态为展开则执行主界面（content）显示任务；
                    scrollToContent();
                }


                break;
            default:
                break;

        }


        return super.onOptionsItemSelected(item);
    }


    //后期实现该段代码的复用；
    public void createAlertDialog() {

        shouldShowDialog = readFromSharedPre("showState");

        if (shouldShowDialog) {

            //创建提示对话框；
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //这是不可通过点击返回键取消对话框；
            builder.setCancelable(false);
            builder.setTitle("操作指南").setIcon(R.drawable.remind).setMessage("您可以在设置模块中打造您的专属应用！");
            builder.setPositiveButton("不再显示", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Toast.makeText(MainActivity.this,"您单击了确认按钮",Toast.LENGTH_LONG).show();
                    shouldShowDialog = false;
                    saveToSharedPre("showState", shouldShowDialog);
                }
            });

            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(MainActivity.this, "您单击了取消按钮", Toast.LENGTH_LONG).show();
                }
            });

            //创建AlertDialog并显示；
            //builder.create()；此方法返回的才是AlertDialog对象;
            builder.create().show();

        }


    }

    //Rank表的数据处理;
    private void initRank(List<RankInfo> datalist) {


        //为rankinformation赋值;
        //* rankinformation = getRankInfoFromMysql();*//*

        //添加适配器到ListVIew；*/

        if (rankinformation != null) {
            Toast.makeText(MainActivity.this, "Not Null!", Toast.LENGTH_LONG).show();
            //初始化数据列表;
            rankinformation = datalist;
            //创建Rank适配器;
            rankListAdapter = new RankListAdapter(MainActivity.this, rankinformation);
            //Toast.makeText(MainActivity.this,datalist.get(0).getUserneme()+"/"+datalist.get(0).getId()+"/"+datalist.get(0).getScore()+"/"+datalist.get(0).getPhone(),Toast.LENGTH_LONG).show();
            //添加适配器到ListVIew；
            ranklistView.setAdapter(rankListAdapter);
            // Toast.makeText(MainActivity.this,datalist.get(0).getUserneme()+"/"+datalist.get(0).getId()+"/"+datalist.get(0).getScore()+"/"+datalist.get(0).getPhone(),Toast.LENGTH_LONG).show();


        } else {

            Toast.makeText(MainActivity.this, "No Data To Show,Please Waite a Minute!", Toast.LENGTH_LONG).show();
        }

    }


    public void getRankInfoFromMysql() {
        //初始化数据列表;
        //发送网络请求到数据库；
        sendRequestToMysql();
        //封装数据的数据列表;
        //返回数据列表;


    }

  /* public List<RankInfo> parseWithGson(){
       //解析Gson格式的字符串；
       return null;
   }*/

    /*测试用方法;*/
    private void sendRequestToMysql() {
        /*List<RankInfo> rankListsend = null;*/
        String url = "http://" + NecRes.ipaddress + ":8080/MysqlForAndroid///RankCl";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //更新登陆状态标志值;
                        //islegal = response;
                        /*Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();*/
                        Message message = new Message();
                        message.what = 002;
                        //将排名信息数据封装进Message;
                        message.obj = parseJSONWithGSON(response);
                        handler.sendMessage(message);


                    }

                });

    }


    private List<RankInfo> parseJSONWithGSON(String jsonData) {

        Gson gson = new Gson();
        List<RankInfo> rankList = gson.fromJson(jsonData, new TypeToken<List<RankInfo>>() {
        }.getType());
        return rankList;
    }
    //_______________________________________当前程序结尾____________________________________________

/*

    //新建内部类，用户处理从相册中选择图片;
    */
/*class ChooseImgFromAlbum{*/


    public static final int CHOOSE_PHOTO = 2;
    Context context = MainActivity.this;

    //在该活动中将实现的是从相册中选择图片并设置为背景;
    //权限检查;
    public void checkPermissions() {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAlbum();
        }


    }


    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        MainActivity.this.startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册

    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(context, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {

                    //数据返回成功后初始化背景图片的资源ID;
                    imgRes = data.getIntExtra("imginfo", 0);
                    //判断点击的是否为图片添加按钮;
                    if (imgRes == R.drawable.add_image) {

                        checkPermissions();
                        Toast.makeText(MainActivity.this, "Equal" + imgRes, Toast.LENGTH_SHORT).show();

                    } else {

                        // imgRes = data.getIntExtra("imginfo",0);
                        //Toast.makeText(MainActivity.this,"MainActivity"+imgRes,Toast.LENGTH_SHORT).show();
                        //将用户选中的背景图片信息保存至SharedPreferences文件中；
                        saveToSharedPreferences(imgRes, "imgID");
                        //更新用界面的背景图片；
                        dynamicChangeContentBg(content, imgRes);

                    }

                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;

        }

    }


    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            //获取Drawable对象;
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
            //设置主界面的背景;
            content.setBackground(bitmapDrawable);

        } else {
            Toast.makeText(context, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    //退出应用;
    private void exitApp() {
        //实际是返回了系统界面，当前活动进入了停止状态，并未销毁;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        // overridePendingTransition(R.anim.showfrom_right,R.anim.reduce_alpha);
        overridePendingTransition(R.anim.showfrom_right, R.anim.scaletosmall);

    }


    //------------------------------图表显示部分--------------------------------------；

    //获取随机数;
    public int getRandom(int min, int max) {

        return (int) Math.round(Math.random() * (max - min) + min);
    }


    //向右滑动;
    private void ToMax() {

        Thread scrollTask = new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hasToMax();
                Message message = new Message();
                message.what = 12;
                scrollHadler.sendMessage(message);


            }
        });

        if (shouldScroll) {

            scrollTask.start();

        }

    }

    //检测是否到达了最右侧；
    private boolean hasToMax() {

        int max = horizontalScrollView.getScrollX();
        if (max == older) {
            return false;
        }
        max = older;
        return true;

    }

}

