/************************************************
 * 睡眠监测界面
 ***********************************************/
package com.example.dell.jiemian;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class sleepActivity extends Activity {
    TextView tv1;
    TextView tv2;

    ImageView imageView1;
    ImageView imageView2;
    String blesize;

    Thread doinback;
    dullwithBluetoothData process;

    //子线程监听数据变化，有问题发送通知
    private Handler handler =new Handler(){
        public void handleMessage(Message msg){
            if(msg.what==1){
                tv1.setText(process.Size(blesize));
            }
            if(msg.what==2){


               // Toast.makeText(sleepActivity.this,"shdasjd",Toast.LENGTH_SHORT).show();
                tv2.setText("是");

                Intent intent = new Intent();
                intent.setAction( "android.intent.action.waring");
                //不加下面这行也行，因为intent的这个属性默认值即系Intent.CATEGORY_DEFAULT
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("send","踢被");
                startActivity(intent);


                NotificationManager mNManager;
                Notification notify1;
                mNManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                Notification.Builder mBuilder = new Notification.Builder(sleepActivity.this);
                mBuilder.setContentTitle("婴儿踢被了")                        //标题
                        .setContentText("孩子不盖好会导致着凉")      //内容
                        .setSubText("请帮助孩子盖好被子")                    //内容下面的一小段文字
                        .setTicker("踢被了~")             //收到信息后状态栏显示的文字信息
                        .setWhen(System.currentTimeMillis())           //设置通知时间
                        .setSmallIcon(R.mipmap.ic_launcher)            //设置小图标
                        //.setLargeIcon(LargeBitmap)                     //设置大图标
                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)    //设置默认的三色灯与振动器
                        .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alarm))  //设置自定义的提示音
                        .setAutoCancel(true);                        //设置点击后取消Notification
                //.setContentIntent(pit);                        //设置PendingIntent
                notify1 = mBuilder.build();
                mNManager.notify(1, notify1);
            }
        }
    };
    /************************************************
     * 初始化，踢被线程的控制
     ***********************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shuizi_layout);

        imageView1 =(ImageView)findViewById(R.id.shuizi);
        imageView1.setBackgroundResource(R.drawable.yangwo);
        imageView2 =(ImageView)findViewById(R.id.tibei);
        imageView2.setBackgroundResource(R.drawable.tibei);
        Switch sw=(Switch)findViewById(R.id.switch2);
        tv1= (TextView)findViewById(R.id.shuizitext);
        tv2= (TextView)findViewById(R.id.tibeitext);

        process= new dullwithBluetoothData();

        final Button sleepCharts = (Button)findViewById(R.id.sleepCharts);
        sleepCharts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(sleepActivity.this,sleepCharts.class);
                startActivity(intent);
            }
        });

        doinback();
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                doinback=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        /*while(true){
                            String str=mydata.BLEData;
                            String strArray[] = str.split("A");
                            double environment =Double.parseDouble(strArray[2]);
                            double temp = Double.parseDouble(strArray[1]);
                            if((temp-environment)>3){
                                Message message = new Message();
                                message.what=2;
                                handler.sendMessage(message);
                                break;
                            }

                        }*/
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message message =new Message();
                        message.what = 2;
                        handler.sendMessage(message);

                    }
                });
                if (isChecked) {
                    doinback.start();
                }
                else {

                    doinback.interrupt();
                    tv2.setText("否");
                }
            }
        });
    }
    /************************************************
     * 后台监测睡姿数据
     ***********************************************/
    private void doinback(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    String str=mydata.BLEData;
                    String strArray[] = str.split("A");
                    if(strArray.length>2){
                        blesize=strArray[3];
                    }
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
        }).start();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
        onDestroy();
    }

    /************************************************
     * 重写的销毁方法（暂无扩展）
     ***********************************************/
    @Override
    protected void onDestroy() {
        MainActivity.releaseImageViewResouce(imageView1);
        MainActivity.releaseImageViewResouce(imageView2);
        super.onDestroy();
    }
}
