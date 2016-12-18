/************************************************
 * 结果展示页面
 ***********************************************/
package com.example.dell.jiemian;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

public class  resultActivity extends Activity {
    TextView tv;
    /************************************************
     * 对HTTP返回的结果进行解析
     ***********************************************/
    ImageView imageView6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.kuqiresult_layout);
        ActivityClearer.removeActivity("questionActivity");

        imageView6 =(ImageView)findViewById(R.id.imageView6);
        imageView6.setBackgroundResource(R.drawable.zitaic);
        Intent intent =getIntent();
        String data = intent.getStringExtra("extra_data");

        tv=(TextView)findViewById(R.id.suggest);

        char array[]= data.toCharArray();

        if(array[0]=='4'){
            data="病了";
            String str = "  根据我们的判断，婴儿可能病了。最好带婴儿去做监察，以确定孩子安全。";
            tv.setText(str);

        }
        else if(array[0]=='3'){
            data="无聊";
            String str="      是不是有时间没有陪孩子玩耍了，赶紧陪陪孩子了。";
            tv.setText(str);
        }
        else if(array[0]=='2'){
            data="困了";
            String str="     是不是玩了很久了，婴儿困了想睡觉，家长可以哄睡觉。";
            tv.setText(str);

        }
        else if(array[0]=='1'){
            data="饿了";
            String str="      是不是有一段时间婴儿没有进食了，婴儿都感到饿了，快去帮助婴儿进食。";
            tv.setText(str);
        }
        TextView tv=(TextView)findViewById(R.id.result);
        tv.setText(data);
    }
    /************************************************
     * 重写退出按钮，直接退出到menuActivity
     ***********************************************/
    @Override
    public void onBackPressed(){
        Intent intent =new Intent(resultActivity.this,menuActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void finish(){
        super.finish();
        onDestroy();
    }
    @Override
    protected void onDestroy(){
        MainActivity.releaseImageViewResouce(imageView6);
        super.onDestroy();

    }
}
