/************************************************
 * 程序的主页面
 * 跳转界面
 ***********************************************/
package com.example.dell.jiemian;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class menuActivity extends Activity {
    /************************************************
     * 加载主界面，给按钮添加响应
     ***********************************************/
    private String getFromSP;
    private String getFromSP60;

    private String getTempFromSP;
    private String getTempFromSP60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhujiemian);

        ActivityClearer.removeActivity("MainActivity");
        //ActivityClearer.removeActivity("Device");


        ActivityCollector.addActivity(this);
        Intent intent =new Intent(menuActivity.this,WaringService.class);
        startService(intent);

        mydata.sleep0=0;
        mydata.sleep1=0;
        mydata.sleep2=0;
        mydata.sleep3=0;
        //处理存储的数据
        loadData(menuActivity.this);
        System.out.println(getTempFromSP);
        String strArray[] = getFromSP.split("A");
        for(int i=0;i<144;i++){
            mydata.heartData[i]= Integer.parseInt(strArray[i]);
        }

        String strArray60[] = getFromSP60.split("A");
        for(int i=0;i<60;i++){
            mydata.heartData60[i]=Integer.parseInt(strArray60[i]);
        }

        String temps[]=getTempFromSP.split("A");
        for(int i=0 ;i<144 ;i++){
            System.out.println(temps[i]);
        }
        for(int i=0;i<144;i++){
            mydata.tempData[i]=Float.parseFloat(temps[i]);
        }
        String temps60[]=getTempFromSP60.split("A");
        for(int i=0;i<60;i++){
            mydata.tempData60[i]=Float.parseFloat(temps60[i]);
        }

//.....
        Button cry=(Button)findViewById(R.id.cry);
        cry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(menuActivity.this,CryBeginActivity.class);
                startActivity(intent);
            }
        });

        Button data=(Button)findViewById(R.id.data);
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(menuActivity.this,dataActivity.class);
                startActivity(intent);
            }
        });

        Button waring =(Button)findViewById(R.id.waring);
        waring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(menuActivity.this,sleepActivity.class);
                startActivity(intent);
            }
        });
        Button all =(Button)findViewById(R.id.all);
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(menuActivity.this,allActivity.class);
                startActivity(intent);
            }
        });
    }
    /************************************************
     * 暂未重写的销毁方法
     ***********************************************/
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    protected void onStart(){
        //心率
        String string="";
        for(int i=0;i<144;i++){
            string+=mydata.heartData[i]+"A";
        }


        String string60="";
        for(int i=0;i<60;i++){
            string60+=mydata.heartData60[i]+"A";
        }

        //体温
        String temp="";
        for(int i=0;i<144;i++){
            temp+=mydata.tempData[i]+"A";
        }
        String temp60="";
        for(int i=0;i<60;i++){
            temp60+=mydata.tempData60[i]+"A";

        }

        saveData(menuActivity.this,string,string60,temp,temp60);

        super.onStart();
    }
    /************************************************
     * 重写的back按钮，在该页面点击back按钮后完全退出
     ***********************************************/
    @Override
    public void onBackPressed(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(menuActivity.this);
                dialog.setTitle("程序将完全退出");
                dialog.setMessage("期待再次使用");
                dialog.setCancelable(false);

                dialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ActivityCollector.finishAll();
                        Intent stopIntent = new Intent(menuActivity.this,WaringService.class);
                        stopService(stopIntent);
                        stopIntent = new Intent(menuActivity.this,RBLService.class);
                        stopService(stopIntent);


                    }
                });
                dialog.show();

    }
    private void saveData(Context context, String string,String string60,String temp,String temp60){
        SharedPreferences sp = context.getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("content", string);
        editor.putString("content60",string60);
        editor.putString("temp", temp);
        editor.putString("temp60",temp60);
        editor.commit();
    }
    private void loadData(Context context) {
        SharedPreferences sp = context.getSharedPreferences("config", MODE_PRIVATE);
        getFromSP=sp.getString("content", "155A154A153A152A151A155A153A148A147A146A145A144A143A142A141" +
                "A138A136A135A134A133A133A133A133A132A130A130A130A140A123A122A135A139A140A115A113A115A"+
                "155A154A153A152A151A150A149A148A147A146A145A144A143A142A141" +
                "A138A136A135A134A133A133A133A133A132A130A130A130A125A123A122A122A122A120A115A113A115A"+
                "155A154A153A152A151A150A149A148A147A146A145A144A143A142A141" +
                "A138A136A155A154A153A152A133A140A123A122A135A139A140A123A122A122A122A120A115A113A115A"+
                "155A154A153A152A151A150A149A148A147A146A145A144A143A142A141" +
                "A138A136A135A134A133A133A133A133A132A130A130A130A125A123A122A122A122A120A115A113A115A"+
                "155A154A153A152A151A150A149A148A147A146A145A144A143A142A141" +
                "A138A136A135A134A133A133A133A133A132A130A130A130A125A123A122A122A122A120A115A113A115A"+
                "155A154A153A152A151A150A149A148A147A146A145A144A143A142A141" +
                "A138A136A135A134A133A133A133A133A132A130A130A130A125A123A122A122A122A120A115A113A115A"+
                "155A154A153A152A151A150A149A148A147A146A145A144A143A142A141" +
                "A138A136A135A134A133A133A133A133A132A130A130A130A125A123A122A122A122A120A115A113A115A").toString();

        getFromSP60=sp.getString("content60","138A136A135A134A133A133A133A133A132A130A130A130A125A123A122A122A122A120A115A113A115A"+
                "155A154A153A152A151A150A149A148A147A146A145A144A143A142A141" +
                "A138A136A135A134A133A133A133A133A132A130A130A130A125A123A122A122A122A120A115A113A115A"+
                "155A154A153A152A151A150A149A148A147A146A145A144A143A142A141A"+
                "155A154A153A152A151A150A149A148A147A146A145A144A143A142A141" +
                "A138A136A135A134A133A133A133A133A132A130A130A130A125A123A122A122A122A120A115A113A115A").toString();

        getTempFromSP=sp.getString("temp","36.5A36.5A36.5A36.6A36.6A36.6A36.7A36.7A36.7A36.7A36.8A36.8A36.8A36.8A36.9" +
                "A36.9A37.0A37.0A37.0A37.1A37.1A37.1A37.0A37.0A37.0A36.9A36.9A36.9A36.8A36.8A36.7A36.6A36.5A36.5A36.4A36.5A"+
                "36.5A36.5A36.5A36.6A36.6A36.6A36.7A36.7A36.7A36.7A36.8A36.8A36.8A36.8A36.9" +
                "A36.8A36.7A36.7A36.7A36.6A36.6A36.6A36.5A36.5A36.6A36.7A36.8A36.9A36.8A36.8A36.7A36.6A36.5A36.5A36.4A36.5A"+
                "36.5A36.5A36.5A36.6A36.6A36.6A36.7A36.7A36.7A36.7A36.8A36.8A36.8A36.8A36.9" +
                "A36.9A37.0A37.0A37.0A37.1A37.1A37.1A37.0A37.0A37.0A36.9A36.9A36.9A36.8A36.8A36.7A36.6A36.5A36.5A36.4A36.5A"+
                "36.5A36.5A36.5A36.6A36.6A36.6A36.7A36.7A36.7A36.7A36.8A36.8A36.8A36.8A36.9" +
                "A36.9A37.0A37.0A37.0A37.1A37.1A37.1A37.0A37.0A37.0A36.9A36.9A36.9A36.8A36.8A36.7A36.6A36.5A36.5A36.4A36.5A"+
                "36.5A36.5A36.5A36.6A36.6A36.6A36.7A36.7A36.7A36.7A36.8A36.8A36.8A36.8A36.9" +
                "A36.9A37.0A37.0A37.0A37.1A36.7A36.7A36.7A36.7A37.0A36.9A36.9A36.9A36.8A36.8A36.7A36.6A36.5A36.5A36.4A36.5A"+
                "36.5A36.5A36.5A36.6A36.6A36.6A36.7A36.7A36.7A36.7A36.8A36.8A36.8A36.8A36.9" +
                "A36.9A37.0A37.0A37.0A37.1A37.1A37.1A37.0A37.0A37.0A36.9A36.9A36.9A36.8A36.8A36.7A36.6A36.5A36.5A36.4A36.5A"
        ).toString();

        getTempFromSP60=sp.getString("temp60","36.4A36.4A36.4A36.5A36.3A36.3A36.4A36.5A36.7A36.7A36.7A36.9A36.9A36.8A036.8A36.6A36.6A36.6A36.6A36.5A36.5A36.4A36.4A36.3A36.2A36.1A36.2A36.3A36.5A36.4A"+
                "36.5A36.5A36.6A36.6A36.5A36.5A36.6A36.5A36.5A36.5A36.5A36.4A36.5A36.5A36.6A36.5A36.6A36.6A36.6A36.6A36.5A36.5A36.5A36.4A36.4A36.4A36.3A36.3A36.2A36.5A").toString();
    }



}
