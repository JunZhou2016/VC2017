package zj.qidian.com.doctor;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

public class NoiseStanderSetting extends AppCompatActivity {
    private NumberPicker numberPickerf = null;
    private NumberPicker numberPickers = null;
    private NumberPicker numberPicker = null;
    public TextView show = null;
    private ActionBar actionBar = null;
    private int gewei = 0;
    private int shiwei = 0;
    private int baiwei = 0;
    private ImageButton imageButton = null;
    int summary = 0;
    boolean shouldtosetting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noise_stander_setting);
        initActionBar();
        initView();
        initNumberPicker();
        Toast.makeText(NoiseStanderSetting.this, "" + readFromSharedPreferences(), Toast.LENGTH_SHORT).show();
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击设置的时候判断输入值是否是合法的;
                //合法值为35-100；
                if (summary < 35 || summary > 100) {

                    shouldtosetting = false;
                    Toast.makeText(NoiseStanderSetting.this, "输入值非法！", Toast.LENGTH_SHORT).show();


                } else {

                    shouldtosetting = true;
                    //将设置参数保存至SharedPreferences文件中;
                    saveInSharePreferences(summary);

                }

                finishCurrentAnimal();

            }
        });
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //注意在此处需要转换为字符串类型的值;
                // show.setText(newVal+"");

                shiwei = newVal;
                generateUserParameter(show, baiwei, shiwei, gewei);

            }
        });

        numberPickerf.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {


                baiwei = newVal;
                generateUserParameter(show, baiwei, shiwei, gewei);
            }
        });
        numberPickers.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                gewei = newVal;
                generateUserParameter(show, baiwei, shiwei, gewei);

            }
        });
    }


    private void initNumberPicker() {


        numberPicker.setValue(3);
        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(1);
        //_____________________
        numberPickerf.setValue(0);
        numberPickerf.setMaxValue(1);
        numberPickerf.setMinValue(0);
        //-----------------------
        numberPickers.setValue(0);
        numberPickers.setMaxValue(10);
        numberPickers.setMinValue(1);

        //numberPicker.setDividerDrawable(R.color.colorPrimary);
        setNumberPickerDividerColor(numberPicker);
        setNumberPickerDividerColor(numberPickerf);
        setNumberPickerDividerColor(numberPickers);
        //设置numberpicker中的字体颜色;
       /* setNumberPickerTextColor( numberPicker,999);
        setNumberPickerTextColor(numberPickerf,000);
        setNumberPickerTextColor( numberPickers,444);*/
    }

    private void initView() {

        numberPicker = (NumberPicker) findViewById(R.id.suroundparameter);
        numberPickerf = (NumberPicker) findViewById(R.id.suroundparameterf);
        numberPickers = (NumberPicker) findViewById(R.id.suroundparameters);
        show = (TextView) findViewById(R.id.shows);
        imageButton = (ImageButton) findViewById(R.id.tosetstander);

    }

    private void initActionBar() {
        actionBar = this.getSupportActionBar();
        if (actionBar != null) {

            actionBar.hide();

        }


    }


    //设置numberpicker中的分隔线的颜色;
    public void setNumberPickerDividerColor(NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            //mSelectionDivider为每个滑动项目的系统内置名称;
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    pf.set(picker, new ColorDrawable(Color.argb(0, 0, 0, 0)));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    //设置numberpicker中的字体颜色；
    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        boolean result = false;
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    // pf.set(picker, new ColorDrawable(Color.argb(0,0,0,0)));
                    ((EditText) child).setTextColor(color);
                    numberPicker.invalidate();
                    result = true;
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /*生成参数信息;*/

    private void generateUserParameter(TextView textView, int bai, int shi, int ge) {
        summary = 0;
        //分百位，十位，个位分别处理；
        summary = ge * 1 + shi * 10 + bai * 100;
        //处理溢出值;

        //将设置的值显示在
        textView.setText(summary + "");
    }


    //将数据保存到sharedPreferences中；
    public void saveInSharePreferences(int summary) {
        //将参数设置的值保存在SharedPreferences文件中;
        SharedPreferences.Editor editor = getSharedPreferences("currentNoise", MODE_PRIVATE).edit();
        //通过文件编辑器，编辑写入数据；
        editor.putInt("noiseSummary", summary);
        //提交数据；
        editor.commit();

    }

    //从sharedPreferences中读取数据；
    public int readFromSharedPreferences() {
        int currentStander = 0;
        //获取SharedPreferences对象;
        SharedPreferences sharedPreferences = getSharedPreferences("currentNoise", MODE_PRIVATE);
        currentStander = sharedPreferences.getInt("noiseSummary", 0);
        return currentStander;
    }

    //下一步，完成环境参数设置的动态化;
    //将测试线程休眠合适的时间;

    @Override
    public void onBackPressed() {
        finishCurrentAnimal();
    }

    private void finishCurrentAnimal(){

        Toast.makeText(NoiseStanderSetting.this, "NoiseStander", Toast.LENGTH_LONG).show();
        finish();
        //调用动画效果；
        overridePendingTransition(R.anim.add_alpha, R.anim.rotate_alpha);

    }



}
