/************************************************
 * 哭声识别界面
 ***********************************************/
package com.example.dell.jiemian;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class cryActivity extends Activity {
    String string;
    SoundRecorder sr;

    String sendSound;

    //子线程更行UI
    private Handler handler =new Handler(){
        public void handleMessage(Message msg){
            if(msg.what==1){
                Toast.makeText(cryActivity.this,"录音完成",Toast.LENGTH_SHORT).show();
                
                forHTTP();
            }
            else if(msg.what==2){
                Intent intent = new Intent(cryActivity.this, questionActivity.class);
                intent.putExtra("extra_data",msg.obj.toString());
                startActivity(intent);
            }
        }
    };
    /************************************************
     * 加载界面
     ***********************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.kuqi_layout);
        ActivityClearer.removeActivity("CryBeginActivity");
        ActivityClearer.addActivity("cryActivity",this);
    }
    /************************************************
     * 完全启动后开始录音
     ***********************************************/
    @Override
    protected void onResume(){
        super.onResume();
        new  Thread(new Runnable() {
            @Override
            public void run() {
                sr =new SoundRecorder();
                sr.startRecording();
                string=sr.str;
                //1
                long ll;
                long l=System.currentTimeMillis();
                ll = l;
                while(true) {
                    //System.out.println("倒计时开始！");
                    l = System.currentTimeMillis();
                    long d = l - ll;
                    if (d % 1000 == 0)
                        System.out.println(new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date(l)));
                    if (d > 10000) {
                        System.out.println("倒计时结束！");
                        break;
                    }
                }
                sr.stopRecording();
                Message message = new Message();
                message.what=1;
                handler.sendMessage(message);
                //2
            }
        }).start();
    }
    /************************************************
     * 婴儿哭声录完后，发送HTTP请求
     ***********************************************/
    private void forHTTP(){
        System.out.println("for http");
        if(!isConnect(this)){
            Toast.makeText(cryActivity.this,"网络未连接",Toast.LENGTH_LONG).show();
        }
       new Thread(new Runnable() {
           @Override
           public void run() {
               HttpURLConnection connection =null;
               URL url= null;
               try {
                   url = new URL("http://babywebserver.applinzi.com/server/babysitServer.php");
                   connection =(HttpURLConnection)url.openConnection();
                   connection.setRequestMethod("POST");
                   DataOutputStream out=new DataOutputStream(connection.getOutputStream());
                   sendSound = "sound="+mydata.how+"";
                   out.writeBytes(sendSound);
                   connection.setConnectTimeout(8000);
                   connection.setReadTimeout(8000);
                   InputStream in=connection.getInputStream();
                   BufferedReader reader= new BufferedReader(new InputStreamReader(in,"utf-8"));

                   StringBuilder response =new StringBuilder();

                   String line;
                   while((line = reader.readLine())!=null){
                       response.append(line);
                   }
                   Log.d("cryActivity",response.toString());
                       Message message = new Message();
                       message.what=2;
                       message.obj=response.toString();
                       handler.sendMessage(message);

               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
       }).start();
    }

    public boolean isConnect(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null&& info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }

            }

        } catch (Exception e) {
            Log.v("error",e.toString());
        }
        return false;
    }
    @Override
    public void finish(){
        super.finish();
        onDestroy();
    }
}
