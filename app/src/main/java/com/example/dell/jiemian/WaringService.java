/************************************************
 * 监测信息发出警报的服务
 ***********************************************/
package com.example.dell.jiemian;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;


import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class WaringService extends Service {
    private dullwithBluetoothData exchange;
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            if(msg.what==1){
                Toast.makeText(WaringService.this,"婴儿出现问题",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.setAction( "android.intent.action.waring");
                //不加下面这行也行，因为intent的这个属性默认值即系Intent.CATEGORY_DEFAULT
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("send","发烧");
                startActivity(intent);

                NotificationManager mNManager;
                Notification notify1;
                mNManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                Notification.Builder mBuilder = new Notification.Builder(WaringService.this);
                mBuilder.setContentTitle("婴儿出现问题")                        //标题
                        .setContentText("婴儿发烧了，请注意！")      //内容
                        .setSubText("可以打开报警信息查看")                    //内容下面的一小段文字
                        .setTicker("婴儿出现问题~")             //收到信息后状态栏显示的文字信息
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
            if(msg.what==2){
                Toast.makeText(WaringService.this,"婴儿出现问题",Toast.LENGTH_SHORT).show();
                NotificationManager mNManager;
                Notification notify1;
                mNManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                Notification.Builder mBuilder = new Notification.Builder(WaringService.this);
                mBuilder.setContentTitle("婴儿出现问题")                        //标题
                        .setContentText("婴儿心率过快，请注意！")      //内容
                        .setSubText("可以打开报警信息查看")                    //内容下面的一小段文字
                        .setTicker("婴儿出现问题~")             //收到信息后状态栏显示的文字信息
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
            if(msg.what==3){
                 Toast.makeText(WaringService.this,"婴儿出现问题",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.setAction( "android.intent.action.waring");
                //不加下面这行也行，因为intent的这个属性默认值即系Intent.CATEGORY_DEFAULT
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("send","俯卧");
                startActivity(intent);

                NotificationManager mNManager;
                Notification notify1;
                mNManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                Notification.Builder mBuilder = new Notification.Builder(WaringService.this);
                mBuilder.setContentTitle("婴儿出现问题")                        //标题
                        .setContentText("婴儿俯卧了，请注意！")      //内容
                        .setSubText("可以打开报警信息查看")                    //内容下面的一小段文字
                        .setTicker("婴儿出现问题~")             //收到信息后状态栏显示的文字信息
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
     * 对心率，体温，睡姿进行实时监测。有问题通过handle进行更新UI
     ***********************************************/
    @Override
    public void onCreate(){

        exchange = new dullwithBluetoothData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String str = mydata.BLEData;
                     try {
                         String strArray[] = str.split("A");

                        if(strArray.length>2) {
                            double i = Double.parseDouble(strArray[1]);//温度的报警
                            float forCharts= Float.parseFloat(exchange.temp(strArray[1]));

                            if (i > 38) {
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                                try {
                                    Thread.sleep(60000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            int heart = Integer.parseInt(exchange.heart(strArray[0]));
 //绘制图表所保存************************
                            Calendar calendar=Calendar.getInstance();
                            int hour=calendar.get(Calendar.HOUR_OF_DAY);
                            int minute = calendar.get(Calendar.MINUTE);
                            int timeFor144 = hour*6+(minute/10);

                            if(heart>200)heart=200;
                            mydata.heartData[timeFor144]=heart;

                            mydata.heartData60[minute]=heart;

                            if(forCharts>40)forCharts=40.0f;
                            if(forCharts<32)forCharts=32.0f;
                            mydata.tempData[timeFor144]=forCharts;
                            mydata.tempData60[minute]=forCharts;
//*************************************
                            if(heart>190){
                                Message message = new Message();
                                message.what = 2;
                                handler.sendMessage(message);
                                try {
                                    Thread.sleep(60000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            String size = exchange.Size(strArray[3]);
                            if(size=="俯卧"){
                                mydata.sleep0++;
                                Message message = new Message();
                                message.what = 3;
                                handler.sendMessage(message);
                                try {
                                    Thread.sleep(60000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            else if(size=="仰卧"){
                                mydata.sleep1++;
                            }
                            else if(size=="左侧卧"){
                                mydata.sleep2++;
                            }
                            else{
                                mydata.sleep3++;
                            }
                        }//处理有效数据并发出报警
                     }catch (Exception  e){
                         System.out.print(e.getMessage());
                         System.exit(0);
                     }

                }//while

            }
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



}
