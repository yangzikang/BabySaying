/************************************************
 *一些全局变量
 ***********************************************/
package com.example.dell.jiemian;

public class mydata{
    public static String BLEData; //保存由蓝牙收到的生理数据
    public static String why;     //保存报警原因

    public static int heartData[]=new int[144];
    public static int heartData60[] = new int[60];

    public static float tempData[] =new float[144];
    public static float tempData60[] = new float[60];

    public static long sleep0;
    public static long sleep1;
    public static long sleep2;
    public static long sleep3;

    public static int how;

}


