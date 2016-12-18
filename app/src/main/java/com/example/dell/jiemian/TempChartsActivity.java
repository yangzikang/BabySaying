package com.example.dell.jiemian;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.example.dell.jiemian.kongjian.temp;
import com.example.dell.jiemian.kongjian.temp60;

import java.util.Calendar;

public class TempChartsActivity extends Activity {
    temp temp24;
    temp60 mtemp60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_temp_charts);

        temp24 = (temp)findViewById(R.id.temp24);
        mtemp60 =(temp60)findViewById(R.id.temp60);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    int number=0;
                    Calendar calendar=Calendar.getInstance();
                    int hour=calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(calendar.MINUTE);
                    int timeFor144 = hour*6+(minute/10);
                    temp24.data.clear();
                    for(int i=timeFor144+1;i<144;i++){
                        temp24.data.add(mydata.tempData[i]);
                    }
                    for(int i=0;i<timeFor144+1;i++){
                        temp24.data.add(mydata.tempData[i]);
                        System.out.println(mydata.tempData[i]);
                    }

                    if(number%120==0){
                        temp24.Drawing();
                    }
                    number++;


                    //**************

                    mtemp60.data.clear();
                    for(int i=minute+1;i<60;i++){
                        mtemp60.data.add(mydata.tempData60[i]);
                    }
                    for(int i=0;i<minute+1;i++){
                        mtemp60.data.add(mydata.tempData60[i]);
                    }

                    mtemp60.Drawing();
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
