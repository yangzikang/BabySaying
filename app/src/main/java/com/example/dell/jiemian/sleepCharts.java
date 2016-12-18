package com.example.dell.jiemian;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

public class sleepCharts extends Activity {
    private PieChart mChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sleep_charts);

        mChart = (PieChart) findViewById(R.id.spread_pie_chart);
        PieData mPieData = getPieData(4, 100);
        showChart(mChart, mPieData);
    }

    private void showChart(PieChart pieChart, PieData pieData) {
        pieChart.setHoleColorTransparent(true);

        pieChart.setHoleRadius(60f);  //半径
        pieChart.setTransparentCircleRadius(100f); // 半透明圈
        pieChart.setHoleRadius(0);  //实心圆

        pieChart.setDescription("姿态饼状图");
        pieChart.setDescriptionTextSize(28);
        // mChart.setDrawYValues(true);
        pieChart.setDrawCenterText(false);  //饼状图中间可以添加文字

        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90); // 初始旋转角度



        // draws the corresponding description value into the slice
        // mChart.setDrawXValues(true);

        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true); // 可以手动旋转

        // display percentage values
        pieChart.setUsePercentValues(true);  //显示成百分比
        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
//      mChart.setOnChartValueSelectedListener(this);
        // mChart.setTouchEnabled(false);

//      mChart.setOnAnimationListener(this);

        //pieChart.setCenterText("已监测睡眠中");  //饼状图中间的文字
        //pieChart.setCenterTextSize(28);
        //设置数据
        pieChart.setData(pieData);

        // undo all highlights
//      pieChart.highlightValues(null);
//      pieChart.invalidate();

        Legend mLegend = pieChart.getLegend();  //设置比例图
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  //最右边显示
//      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);

        pieChart.animateXY(1000, 1000);  //设置动画
        // mChart.spin(2000, 0, 360);
    }

    /**
     *
     * @param count 分成几部分
     * @param range
     */
    private PieData getPieData(int count, float range) {

        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容

        /*for (int i = 0; i < count; i++) {
            xValues.add("Quarterly" + (i + 1));  //饼块上显示成Quarterly1, Quarterly2, Quarterly3, Quarterly4
        }*/

        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据

        // 饼图数据
        /**
         * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38
         * 所以 14代表的百分比就是14%
         */
        float quarterly1 = mydata.sleep1;
        float quarterly2 = mydata.sleep0;
        float quarterly3 = mydata.sleep2;
        float quarterly4 = mydata.sleep3;


        if(quarterly1>0) {
            xValues.add("仰卧");
            yValues.add(new Entry(quarterly1, 0));
        }
        if(quarterly2>0) {
            xValues.add("俯卧");
            yValues.add(new Entry(quarterly2, 1));
        }
        if(quarterly3>0){
            xValues.add("左侧卧");
            yValues.add(new Entry(quarterly3, 2));
        }
        if(quarterly4>0){
            xValues.add("右侧卧");
            yValues.add(new Entry(quarterly4, 3));
        }



        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, "如上所示，不同睡姿"/*显示在比例图上*/);
        pieDataSet.setSliceSpace(1f); //设置个饼状图之间的距离

        ArrayList<Integer> colors = new ArrayList<Integer>();

        // 饼图颜色
        colors.add(Color.rgb(205, 205, 205));
        colors.add(Color.rgb(114, 188, 223));
        colors.add(Color.rgb(255, 123, 124));
        colors.add(Color.rgb(57, 135, 200));

        pieDataSet.setColors(colors);

        PieData pieData = new PieData(xValues, pieDataSet);
        pieData.setValueTextSize(20);

        return pieData;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        onDestroy();
    }


    @Override
    protected void onDestroy() {
        mChart.clear();
        super.onDestroy();
    }
}
