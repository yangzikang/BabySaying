package com.example.dell.jiemian;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.example.dell.jiemian.R;
import com.example.dell.jiemian.kongjian.Heart10min;
import com.example.dell.jiemian.kongjian.MyView;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class Heartcharts extends Activity {
    MyView twofour;
    Heart10min ten;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_heartcharts);


        twofour =(MyView)findViewById(R.id.twofour);
        ten =(Heart10min)findViewById(R.id.ten);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    int number=0;
                    Calendar calendar=Calendar.getInstance();
                    int hour=calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(calendar.MINUTE);
                    int timeFor144= hour*6+(minute/10);
                    twofour.data.clear();
                    for(int i=timeFor144+1;i<144;i++){
                        twofour.data.add(mydata.heartData[i]);
                    }
                    for(int i=0;i<timeFor144+1;i++){
                        twofour.data.add(mydata.heartData[i]);
                        System.out.println(mydata.heartData[i]);
                    }


                    if(number%120==0){
                        twofour.Drawing();
                    }
                    number++;
//***************************************************************************

                    ten.data.clear();
                    for(int i=minute+1;i<60;i++){
                        ten.data.add(mydata.heartData60[i]);
                    }
                    for(int i=0;i<minute+1;i++){
                        ten.data.add(mydata.heartData60[i]);
                    }

                    ten.Drawing();
//****************************************************************************
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

}
