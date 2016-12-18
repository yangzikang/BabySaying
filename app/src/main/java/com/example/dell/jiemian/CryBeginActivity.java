package com.example.dell.jiemian;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class CryBeginActivity extends Activity {

    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cry_begin);
        ActivityClearer.addActivity("CryBeginActivity",CryBeginActivity.this);

        imageView1 =(ImageView)findViewById(R.id.imageView) ;
        imageView2 =(ImageView)findViewById(R.id.imageView2);
        imageView3 = (ImageView)findViewById(R.id.imageView3);
        imageView1.setBackgroundResource(R.drawable.baby1);
        imageView2.setBackgroundResource(R.drawable.sound);
        imageView3.setBackgroundResource(R.drawable.androidphone2);

        Button send1=(Button)findViewById(R.id.send1);
        Button send2=(Button)findViewById(R.id.send2);
        Button send3=(Button)findViewById(R.id.send3);
        Button send4=(Button)findViewById(R.id.send4);



        send1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydata.how=1;
                Intent intent=new Intent(CryBeginActivity.this,cryActivity.class);
                startActivity(intent);
            }
        });
        send2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydata.how=2;
                Intent intent=new Intent(CryBeginActivity.this,cryActivity.class);
                startActivity(intent);
            }
        });
        send3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydata.how=3;
                Intent intent=new Intent(CryBeginActivity.this,cryActivity.class);
                startActivity(intent);
            }
        });
        send4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydata.how=4;
                Intent intent=new Intent(CryBeginActivity.this,cryActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void finish(){
        super.finish();
        MainActivity.releaseImageViewResouce(imageView1);
        MainActivity.releaseImageViewResouce(imageView2);
        MainActivity.releaseImageViewResouce(imageView3);
        onDestroy();
    }
}
