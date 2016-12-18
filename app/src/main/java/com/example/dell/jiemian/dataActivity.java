/************************************************
 * 生理数据界面的显示
 ***********************************************/
package com.example.dell.jiemian;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.dell.jiemian.kongjian.MyView;


public class dataActivity extends Activity {

    TextView tv;
    TextView tv1;
    MyView grf;
    MyView grf2;
    String bletemp;
    String bleheart;
    dullwithBluetoothData process;
    //handler为子线程更新UI时使用
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            if (msg.what == 2) {
                tv.setText(process.temp(bletemp));
                tv1.setText(process.heart(bleheart));
            }
        }
    };


    /************************************************
     * 一些初始化工作
     ***********************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shengli_layout);
        tv = (TextView) findViewById(R.id.bletemp);
        tv1 = (TextView) findViewById(R.id.bleheart);
        process = new dullwithBluetoothData();

        Button tiwenCharts = (Button)findViewById(R.id.tiwencharts);
        tiwenCharts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dataActivity.this,TempChartsActivity.class);
                startActivity(intent);
            }
        });

        Button xinlvCharts = (Button)findViewById(R.id.xinlvCharts);
        xinlvCharts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dataActivity.this,Heartcharts.class);
                startActivity(intent);
            }
        });
        doinback();

    }

    /************************************************
     * 在子线程中进行蓝牙数据的接收和解析
     ***********************************************/
    private void doinback() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String str = mydata.BLEData;
                    String strArray[] = str.split("A");
                    if (strArray.length > 2) {
                        bletemp = strArray[1];
                        bleheart = strArray[0];

                    }
                    Message messsage = new Message();
                    messsage.what = 2;
                    handler.sendMessage(messsage);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /************************************************
     * 预留还未重写的销毁方法
     ***********************************************/
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
