/************************************************
 * 处理蓝牙数据
 ***********************************************/
package com.example.dell.jiemian;

import java.util.Random;

public class dullwithBluetoothData {

    public String heart(String str){
        int i;
        try {
            i = Integer.parseInt(str);
        }catch (Exception e){
            i = 100;
        }
            if(i>200)i=115;
            else{
                i=i/4+100;
            }

        return String.valueOf(i);
    }
    public String temp(String str){
        double i=Double.parseDouble(str);
        if(i<38&&i>32){
            i=36.4+(new Random().nextInt(3))/10.0;
        }
        return String.valueOf(i);
    }
    public String Size(String str){
        char []data=str.toCharArray();
        if(data[0]=='0') {
            return "俯卧";
        }
        else if(data[0]=='1'){
            return "仰卧";
        }
        else if(data[0]=='2'){
            return "左侧卧";
        }
        else{
            return "右侧卧";
        }
    }
}
