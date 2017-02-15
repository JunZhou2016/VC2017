package zj.qidian.com.doctor;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Helper extends AppCompatActivity {
    private ActionBar actionBar = null;
    private ListView helperlist = null;
    private BaseAdapter helperBaseAdapter = null;
    private int[] ImgResId = new int[]{R.drawable.img_helper_fir,R.drawable.main_helper, R.drawable.test_helper, R.drawable.result_helper};
    private int[] helpertext = new int[]{R.string.img_helper_fir,R.string.main_helper_str, R.string.test_helper_str, R.string.result_helper_str};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
        //将当前活动加入活动和集合中;
        ActivityCollector.addActivity(this);
        initActionBar();
        init();
        createBaseAdapter();
        helperlist.setAdapter(helperBaseAdapter);

    }

    //初始化适配器;
    private void createBaseAdapter(){
        helperBaseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return ImgResId.length;
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
                //获取布局填充器;
                LayoutInflater layoutInflater = LayoutInflater.from(Helper.this);
                //加载视图对象;
                View helperItem = layoutInflater.inflate(R.layout.listview_item_helper,null);
                ImageView imageView = (ImageView) helperItem.findViewById(R.id.img_helper);
                TextView textView = (TextView) helperItem.findViewById(R.id.text_helper);
                imageView.setImageResource(ImgResId[position]);
                textView.setText(helpertext[position]);



                return helperItem;
            }
        };


    }

    private void init(){

        helperlist = (ListView) findViewById(R.id.helper_list);

    }

    private void initActionBar(){
        actionBar = this.getSupportActionBar();
        if(actionBar!=null){

            actionBar.hide();

        }


    }

}
