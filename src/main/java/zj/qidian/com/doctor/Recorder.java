package zj.qidian.com.doctor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import java.io.File;
import zj.qidian.com.doctor.ActivityCollector;
import zj.qidian.com.doctor.Interrupt;
import zj.qidian.com.doctor.R;
import static junit.runner.Version.id;

public class Recorder extends AppCompatActivity {
    private ImageView imageView = null;
    private int width = 0;
    private int height = 0;
    private float scaleSize = 1;
    private Bitmap bitmap = null;
    private double db = 0;// 分贝
    private Bundle bundle = null;
    private double sum = 0;
    private float currentScale = 1;
    private boolean isStart = true;
    private int counter = 0;
    private double averageOfFive = 0;
    private double summaryFive = 0;
    private int startUpdate = 0;
    //定义用户吹气的三个阶段性标志量；
    private static final double firstState = 2000;
    private static final double secondtState = 3000;
    private static final double thirdState = 4000;
    private static final double forthState = 5000;
    //Toast标志;
    private boolean firstFlag = true;
    private boolean secondtFlag = true;
    private boolean thirdFlag = true;
    private boolean forthFlag = true;
    //统计两次采样的均值；
    private int statisticTimes = 0;
    private double twoSum = 0;
    private double twoAverage = 0;
    //当前的环境噪音；
    private double currrentNoise = 0;//以5次统计的均值为标准；
    private int statisticTimesOfFive = 0;
    private double fiveSum = 0;
    private double fiveAverage = 0;
    //录音；
    private MediaRecorder mMediaRecorder;
    //定义当前的噪音值；
    private static double currentNoise = 60;
    //获取用户记录的参数值;
    int usersetting = 0;
    //处理ActionBar;
    ActionBar actionBar = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 001) {
                //在此处更新缩放的比例并且调用缩放的方法；
                bundle = msg.getData();
                //获取每次吹气的值;
                double value = bundle.getDouble("db");
                sum = sum + value;

                //获取缩放比例；
                  //初始值为1300；
                currentScale = (float) (currentScale + (value / 1600));
                //调用变换图形的方法；
                changeImg(currentScale);

/*

                //测试当前环境中的噪音水平，以五次统计的平均值为标准；
                statisticTimesOfFive++;
                fiveSum = fiveSum+value;

                    if(statisticTimesOfFive==3){
                        fiveAverage = fiveSum/3;
                        Toast.makeText(MainActivity.this,"3次统计的平均值为"+fiveAverage,Toast.LENGTH_SHORT).show();

                        //再次初始化统计变量，为下一次的统计做准备；
                        statisticTimesOfFive = 0;
                        fiveSum = 0;
                        fiveAverage = 0;

                    }
*/


                {
                    //测试用例；
                    // Toast.makeText(MainActivity.this,value+"",Toast.LENGTH_LONG).show();

                }
                //如果用户没有继续吹气或者换气了，则采取必要的措施（跳转或者弹出提示信息）；
                statisticTimes++;
                twoSum = twoSum + value;
                if (statisticTimes == 2 && secondtFlag) {
                    //注意在次数不应该使用过多的if（）else语句，否则会导致程序运行效率很低；
                    twoAverage = twoSum / 2;


                    //将统计次数和均值清零，为下一次的重新统计做准备；
                    //判断用户是否停止了吹气或者换气；

                    if (twoAverage < currentNoise && secondtFlag) {
                        // Toast.makeText(MainActivity.this,"数据正在处理中",Toast.LENGTH_LONG).show();


                        secondtFlag = false;
                        //更新图像位图,并控制图片颤抖；
                        {
                            changeBitmap(Recorder.this.getResources(), R.drawable.good);
                            shake(imageView);

                        }
                        //停止测试活动;
                        MyRecorder myRecorder = new MyRecorder();
                        myRecorder.stopRecord();
                        Intent intent = new Intent(Recorder.this, Interrupt.class);
                        Bundle bundle = new Bundle();
                        bundle.putDouble("total", sum);
                        intent.putExtras(bundle);
                        startActivity(intent);


                    }

                    twoAverage = 0;
                    statisticTimes = 0;
                    twoSum = 0;


                }


            }

        }
    };

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);
        ActivityCollector.addActivity(this);
        //将当前活动加入集合中，方便管理;
        ActivityCollector.addActivity(this);
        //获取用户的设置参数;
        usersetting = readFromSharedPreferences();
        init();
        initActionBar();
        //当前线程休眠3000毫秒,准备开始接受测试数据;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //当前线程休眠500毫秒,正式开始接受测试数据;
        bigShake(imageView);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //添加进度条对话框，让用户做好准备；
        MyRecorder myrecorder = new MyRecorder();
        //在用户确认开始测试的时候开始进行操作；
        myrecorder.startRecord();



    }

    private void initActionBar() {

        actionBar = this.getSupportActionBar();
        if (actionBar != null) {

            actionBar.hide();

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    private void init() {
        imageView = (ImageView) findViewById(R.id.img);
        // button = (Button) findViewById(R.id.Scale);
        //获取操作位图的宽和高；
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.lowest);
        width = bitmap.getWidth();
        height = bitmap.getHeight();

    }

    //该方法实现图形变换的功能；
    private void changeImg(float Size) {

        //创建并设置矩阵;
        Matrix matrix = new Matrix();
        //设置矩阵；
        matrix.setScale(Size, Size, width / 2, height / 2);
        //应用变换矩阵；
        Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        //填充新的位图；
        imageView.setImageBitmap(bitmap2);

    }


    //----------------------------------------------------------------------------------------------

    //该类用于实现录音和音频分析功能;
    public class MyRecorder {
        private final String TAG = "MediaRecord";
        // 最大录音时长1000*60*10;
        public static final int MAX_LENGTH = 1000 * 60 * 10;
        private String filePath;


        public MyRecorder() {
            this.filePath = "/dev/null";
        }

        public MyRecorder(File file) {
            this.filePath = file.getAbsolutePath();
        }

        private long startTime;
        private long endTime;

        /**
         * 开始录音 使用amr格式
         * <p>
         * 录音文件
         *
         * @return
         */
        public void startRecord() {
            // 开始录音
            /* ①Initial：实例化MediaRecorder对象 */
            if (mMediaRecorder == null)
                mMediaRecorder = new MediaRecorder();
            try {
            /* ②setAudioSource/setVedioSource */
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                        /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            /* ③准备 */
                mMediaRecorder.setOutputFile(filePath);
                mMediaRecorder.setMaxDuration(MAX_LENGTH);
                mMediaRecorder.prepare();
            /* ④开始 */
                mMediaRecorder.start();
                // AudioRecord audioRecord.
            /* 获取开始时间* */
                startTime = System.currentTimeMillis();
                updateMicStatus();
            } catch (IllegalStateException e) {
                Log.i(TAG,
                        "call startAmr(File mRecAudioFile) failed!"
                                + e.getMessage());
            } catch (Exception e) {
                Log.i(TAG,
                        "call startAmr(File mRecAudioFile) failed!"
                                + e.getMessage());
            }
        }

        /**
         * 停止录音
         */
        public long stopRecord() {
            if (mMediaRecorder == null)
                return 0L;
            endTime = System.currentTimeMillis();
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            return endTime - startTime;
        }


        private final Handler mHandler = new Handler();


        private Runnable mUpdateMicStatusTimer = new Runnable() {

            public void run() {
                db = updateMicStatus();
            }
        };

        /**
         * 更新话筒状态
         */
        private int BASE = 1;
        /* private int SPACE = 100;// 间隔取样时间*/
        private int SPACE = 120;
        private double current = 0;

        private double updateMicStatus() {

            if (mMediaRecorder != null) {
                double ratio = (double) mMediaRecorder.getMaxAmplitude() / BASE;
                //判断采样是否成功;
                if (ratio > 1)
                    db = 20 * Math.log10(ratio);
                //Log.d(TAG, "分贝值：" + db);
                //去除环境噪音的影响；
                mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
                //Toast.makeText(MainActivity.this,db+"",Toast.LENGTH_LONG).show();
                //根据采样的数值判断是否吹气活动仍然在进行；
                if (db >= 40) {
                    //判定为正在进行吹气活动;
                    //当用户吹气的时候更新UI;
                    //更新采样频率的值；
                    // counter = counter + 1;
                    Message message = new Message();
                    message.what = 001;
                    message.obj = db;
                    Bundle bundle = new Bundle();
                    bundle.putDouble("db", db);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }


            }
            return current = db;

        }
    }

    //定义在三种状态下的初始位图；
    private void changeBitmap(Resources resources, int resourcesId) {

        bitmap = BitmapFactory.decodeResource(resources, resourcesId);
        width = bitmap.getWidth();
        height = bitmap.getHeight();
    }

    //设置晃动;
    private void shake(View view) {
        //加载动画资源文件
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        //给组件播放动画效果
        imageView.startAnimation(shake);

    }

    //设置长时间晃动;
    private void bigShake(View view) {
        //加载动画资源文件
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.bigshake);
        //给组件播放动画效果
        imageView.startAnimation(shake);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //从sharedPreferences中读取数据；
    public int readFromSharedPreferences() {
        int currentStander = 0;
        //获取SharedPreferences对象;
        SharedPreferences sharedPreferences = getSharedPreferences("currentNoise", MODE_PRIVATE);
        currentStander = sharedPreferences.getInt("noiseSummary", 40);
        return currentStander;
    }


}

