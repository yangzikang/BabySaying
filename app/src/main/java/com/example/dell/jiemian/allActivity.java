/************************************************
 *所有数据界面
 ***********************************************/
package com.example.dell.jiemian;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class allActivity extends Activity {
    TextView allMessage;
    TextView heart;
    TextView size;
    TextView temp;
    TextView all;
    String   strArray[];
    private int i=0;
    Thread thread;

    dullwithBluetoothData exchange;
    //子线程更新UI
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){

            if(msg.what==1) {
                heart.setText(strArray[0]+"次/分钟");
                temp.setText(strArray[1]+"℃");
                size.setText(strArray[3]+"姿态");
                all.setText(mydata.why);
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
        setContentView(R.layout.zonghe_layout);
        allMessage=(TextView)findViewById(R.id.allmessage);
        heart=(TextView)findViewById(R.id.heart);
        size=(TextView)findViewById(R.id.size);
        temp=(TextView)findViewById(R.id.temp);
        all=(TextView)findViewById(R.id.allmessage);
        exchange = new dullwithBluetoothData();

        forall();

    }
    /************************************************
     * 处理蓝牙数据，有问题在界面展示报警
     ***********************************************/
    private void  forall(){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    String str = mydata.BLEData;
                    strArray = str.split("A");
                    //begin----------------------------------------------------------------------
                    boolean isnull=true;
                    if(strArray.length>2) {
                        int heart = Integer.parseInt(exchange.heart(strArray[0]));
                        if(heart>190){
                           mydata.why="婴儿心率过快\n 提示：可能孩子的情绪不稳定，或受到了惊吓。可以哄哄孩子。";
                            isnull=false;
                        }
                        double i = Double.parseDouble(strArray[1]);//温度的报警
                        if (i > 38) {
                            mydata.why="婴儿发烧了\n 提示：多喝温开水，补充体液是最基本的降温方法。用温水毛巾擦拭全身。";
                            isnull=false;
                        }
                        String size = exchange.Size(strArray[3]);
                        if(size=="俯卧"){
                           mydata.why="婴儿俯卧了\n 提示：请帮助孩子翻身，长时间俯卧会造成孩子窒息。";
                            isnull=false;
                        }
                        if(isnull){
                            mydata.why="暂无报警";
                        }
                    }//处理有效数据并发出报警
                    //end---------------------------------------------------------------------------
                    strArray[0]=exchange.heart(strArray[0]);
                    strArray[3]=exchange.Size(strArray[3]);

                    strArray[1]=exchange.temp(strArray[1]);


                    Message messsage =new Message();
                    messsage.what = 1;
                    handler.sendMessage(messsage);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        thread.interrupt();
    }

}
