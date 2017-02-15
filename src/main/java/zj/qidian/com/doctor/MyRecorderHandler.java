package zj.qidian.com.doctor;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by xdsm on 2016/12/4.
 */

public class MyRecorderHandler {

    private TextView audioShow = null;
    private  double db = 0;// 分贝
    //录音；
    private MediaRecorder mMediaRecorder;
    private Bundle bundle = null;
    private String AudioDatas = null;
    private boolean stopTask = false;


    public MyRecorderHandler(TextView audioShow) {


        this.audioShow = audioShow;


    }

    public void startGetData(){

        GetMyRecorderData getMyRecorderData = new GetMyRecorderData();
        getMyRecorderData.startRecord();

    }

    public void stopGetData(){

        GetMyRecorderData getMyRecorderData = new GetMyRecorderData();
        getMyRecorderData.stopRecord();
      //  System.exit(0);
        //彻底杀死当前线程;
        //利用标志位停止线程任务;
        stopTask = false;

    }


    /*protected void removeCurrentThread() {


        //彻底杀死当前线程;
       // android.os.Process.killProcess(android.os.Process.myPid());//使用该语句会出现白屏；
        //思路：使用循环杀死当前线程;
        //思路二：使用定时器完成当前目标任务;
        //将线程从当前队列中移出;


    }*/



    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==001){
                //在此处更新缩放的比例并且调用缩放的方法；
                bundle = msg.getData();
                //获取每次吹气的值;
                double value = bundle.getDouble("db")+33.0;
                //将实时数据格式化为保留三位小数;
                DecimalFormat df = new DecimalFormat("###.000");
                AudioDatas = df.format(value);
                //将每次吹气的值填入数据显示框中;
                audioShow.setText(""+AudioDatas);
               // Log.i("audio", "handleMessage: hhhh");
            }

        }
    };



    //----------------------------------------------------------------------------------------------
    //该类用于实现录音和音频分析功能;
    public class GetMyRecorderData {
        private final String TAG = "MediaRecord";
        // 最大录音时长1000*60*10;
        public static final int MAX_LENGTH = 1000 * 60 * 10;
        private String filePath;


        public GetMyRecorderData(){
            this.filePath = "/dev/null";
        }

        public GetMyRecorderData(File file) {
            this.filePath = file.getAbsolutePath();
        }

        private long startTime;
        private long endTime;

        /**
         * 开始录音 使用amr格式
         *
         *            录音文件
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
         *
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

                        if(!stopTask){
                            db = updateMicStatus();
                        }



            }
        };



        /**
         * 更新话筒状态
         *
         */
        private int BASE = 1;
        private int SPACE = 100;// 间隔取样时间
        private double current = 0;
        private double updateMicStatus() {

            if (mMediaRecorder != null) {
                double ratio = (double) mMediaRecorder.getMaxAmplitude() / BASE;

                if (ratio > 1)
                    db = 20 * Math.log10(ratio);
                //Log.d(TAG, "分贝值：" + db);
                //去除环境噪音的影响；
                mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
                //Toast.makeText(MainActivity.this,db+"",Toast.LENGTH_LONG).show();
                //根据采样的数值判断是否吹气活动仍然在进行；
                //根据采样的数值判断是否吹气活动仍然在进行；
               /* if(db>=40){*/
                    //判定为正在进行吹气活动;
                    //当用户吹气的时候更新UI;
                    //更新采样频率的值；
                    // counter = counter + 1;
                    Message message = new Message();
                    message.what = 001;
                    message.obj = db;
                    Bundle bundle  = new Bundle();
                    bundle.putDouble("db",db);
                    message.setData(bundle);
                    handler.sendMessage(message);
             /*   }*/


            }
            return current = db;

        }



    }




}
