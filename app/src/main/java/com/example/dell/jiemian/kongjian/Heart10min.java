package com.example.dell.jiemian.kongjian;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/7/22.
 */
public class Heart10min extends View{
    //坐标轴原点的位置
    private int xPoint=120;
    private int yPoint=540;
    //刻度长度
    private int xScale=14;  //35个单位构成一个刻度
    private int yScale=80;
    //x与y坐标轴的长度
    private int xLength;

    {
        xLength = 840;
    }

    private int yLength=480;

    private int MaxDataSize=xLength/xScale;   //横坐标  最多可绘制的点

    public List<Integer> data=new ArrayList<Integer>();   //存放 纵坐标 所描绘的点

    private String[] yLabel=new String[yLength/yScale];  //Y轴的刻度上显示字的集合
    private String[] xLabel=new String[xLength/xScale];  // X轴



    public Handler mh=new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(msg.what==0){                //判断接受消息类型
                Heart10min.this.invalidate();  //刷新View
            }
        };
    };
    public Heart10min(Context context, AttributeSet attrs) {
        super(context, attrs);
        for (int i = 0; i <yLabel.length; i++) {
            yLabel[i]=(i*40)+"";
            if(i==0){
                yLabel[i]="无";
            }
        }
        for(int j = 0;j<xLabel.length;j++){
            xLabel[j]=(61-j)+"";
        }
    }

    public void Drawing(){
        if(data.size()>MaxDataSize){  //判断集合的长度是否大于最大绘制长度
            data.remove(0);  //删除头数据

        }
        //data.add(new Random().nextInt(5)+1);  //生成1-6的随机数
        mh.sendEmptyMessage(0);   //发送空消息通知刷新
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint=new Paint();
        Paint paint2=new Paint();
        Paint paint3=new Paint();

        paint2.setStyle(Paint.Style.STROKE);
        paint2.setAntiAlias(true);
        paint2.setColor(Color.WHITE);
        paint2.setTextSize(40);

        paint3.setStyle(Paint.Style.STROKE);
        paint3.setAntiAlias(true);
        paint3.setColor(Color.WHITE);
        paint3.setStrokeWidth(5);
        paint3.setAntiAlias(true);

        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(10);
        paint.setAntiAlias(true);
        canvas.drawText("次/分",xPoint-100,yPoint-yLength,paint2);
        canvas.drawText("分钟前",xPoint+xLength-10,yPoint+40,paint2);
        //绘制Y轴
        canvas.drawLine(xPoint, yPoint-yLength, xPoint, yPoint, paint);
        //绘制Y轴左右两边的箭头
        canvas. drawLine(xPoint+3, yPoint-yLength, xPoint-10,yPoint-yLength+15, paint);
        canvas.drawLine(xPoint-3, yPoint-yLength, xPoint+10,yPoint-yLength+15, paint);

        //Y轴上的刻度与文字
        for (int i = 0; i * yScale< yLength; i++) {
            if(i<3){
                canvas.drawLine(xPoint, yPoint-i*yScale, xPoint+15, yPoint-i*yScale, paint);  //刻度
                canvas.drawText(yLabel[i], xPoint-60, yPoint-i*yScale, paint2);//文字
            }
            else{
                canvas.drawLine(xPoint, yPoint-i*yScale, xPoint+15, yPoint-i*yScale, paint);  //刻度
                canvas.drawText(yLabel[i], xPoint-80, yPoint-i*yScale, paint2);//文字
            }
        }
        //X轴上的刻度与文字
        for(int i=0; i*xScale<xLength; i++){
            if(i%10==0) {
                canvas.drawLine(xPoint + i * xScale, yPoint, xPoint + i * xScale, yPoint - 15, paint); //x轴刻度
                canvas.drawText(xLabel[i+1], xPoint + i * xScale, yPoint + 40, paint2);
            }
        }
        //X轴
        canvas.drawLine(xPoint-5, yPoint, xPoint+xLength, yPoint, paint);
        //绘制X轴左右两边的箭头
        canvas.drawLine(xPoint+xLength,yPoint-3,xPoint+xLength-15,yPoint+10,paint);
        canvas.drawLine(xPoint+xLength,yPoint+3,xPoint+xLength-15,yPoint-10,paint);
        //如果集合中有数据
        if(data.size()>1){

            for (int i = 1; i < data.size(); i++) {  //依次取出数据进行绘制
                if (data.get(i - 1) != 0&&data.get(i)!=0) {
                    canvas.drawLine(xPoint + (i - 1) * xScale, yPoint - data.get(i - 1) * 2.0f, xPoint + i * xScale, yPoint - data.get(i) * 2.0f, paint3);
                }
            }
            for(int i=0;i<data.size();i++){
                if(data.get(i)!=0){
                    canvas.drawCircle(xPoint+i*xScale,yPoint-data.get(i)*2.0f,2,paint3);
                }
            }

        }

    }
}
