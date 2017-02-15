package zj.qidian.com.doctor;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class KnowReal extends AppCompatActivity {

    private WebView webView = null;
    private ActionBar actionBar = null;
    private String url = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_real);
        //隐藏窗体边框;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏actiongbar;
        initActionBar();
        //初始化当前界面;
        init();
        //设置webview的相关参数;
        WebSettings webSettings = webView.getSettings();
        //设置支持js;
        webSettings.setJavaScriptEnabled(true);

        WebSettings settings = webView.getSettings();
        /*settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_CO4LUMNS);*/
        //设置访问的网页适应当前手机分辨率;
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setScrollContainer(false);
        //设置所有页面在当前窗口打开;
        webView.setWebViewClient(new WebViewClient());
        url = "http://www.viptijian.com/knowledge/201407/20140723239225.shtml";
        webView.loadUrl(url);
    }

    private void initActionBar() {

        actionBar = this.getSupportActionBar();
        if(actionBar!=null){

            actionBar.hide();


        }

    }

    private void init() {

        webView = (WebView) findViewById(R.id.managerpage);

    }
}
