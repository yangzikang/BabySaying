package com.example.dell.jiemian;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

public class WaringActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_waring);
        WebView wView = (WebView)findViewById(R.id.webView);
        WebSettings wSet = wView.getSettings();
        wSet.setJavaScriptEnabled(true);
        wView.loadUrl("file:///android_asset/gif.html");

        Intent intent =getIntent();
        String data = intent.getStringExtra("send");

        System.out.println("123"+data+"321");

        TextView warningMessage =(TextView)findViewById(R.id.warningMessage);
        TextView howToDullWarning = (TextView)findViewById(R.id.howToDullWaring);

        if(data.equals("俯卧")){
            warningMessage.setText("婴儿俯卧了");
            howToDullWarning.setText("提示：请帮助孩子翻身，长时间俯卧会造成孩子窒息。");
        }
        else if(data.equals("踢被")){
            warningMessage.setText("婴儿踢被了");
            howToDullWarning.setText("提示：请帮助孩子盖好被子，孩子不盖被子容易着凉。");
        }
        else{
            warningMessage.setText("婴儿发烧了");
            howToDullWarning.setText("提示：多喝温开水，补充体液是最基本的降温方法。用温水毛巾擦拭全身。");
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
        onDestroy();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}
