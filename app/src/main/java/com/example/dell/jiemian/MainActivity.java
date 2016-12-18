/************************************************
 * 程序的开始点
 * 蓝牙设备发现
 ***********************************************/
package com.example.dell.jiemian;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    /*************************************************
     *变量的初始化
     ************************************************/
    private ImageView imageView;
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 3000;
    private Dialog mDialog;
    public static List<BluetoothDevice> mDevices = new ArrayList<BluetoothDevice>();
    public static MainActivity instance = null;
    /*************************************************
     *检查蓝牙开启情况
     ************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        imageView = (ImageView)findViewById(R.id.welcome);
        imageView.setBackgroundResource(R.drawable.welcome);
        ActivityCollector.addActivity(this);
        ActivityClearer.addActivity("MainActivity",this);

        //检查蓝牙开启情况
        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "Ble not supported", Toast.LENGTH_SHORT)
                    .show();
            finish();
        }
        //获取系统服务
        final BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Ble not supported", Toast.LENGTH_SHORT)
                    .show();
            finish();
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        Button enter=(Button)findViewById(R.id.enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanLeDevice();

                showRoundProcessDialog(MainActivity.this, R.layout.loading_process_dialog_anim);

                Timer mTimer = new Timer();
                mTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        Intent deviceListIntent = new Intent(getApplicationContext(),
                                Device.class);
                        startActivity(deviceListIntent);
                        mDialog.dismiss();
                    }
                }, SCAN_PERIOD);
            }
        });
        scanLeDevice();

        showRoundProcessDialog(MainActivity.this, R.layout.loading_process_dialog_anim);

        Timer mTimer = new Timer();
        mTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                Intent deviceListIntent = new Intent(getApplicationContext(),
                        Device.class);
                startActivity(deviceListIntent);
                mDialog.dismiss();
            }
        }, SCAN_PERIOD);

        instance = this;
    /*
        //调试蓝牙。。。。
        Button enter=(Button)findViewById(R.id.enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,menuActivity.class);
                startActivity(intent);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i=0;
                while(true) {
                    i++;
                    if(i%4==0)
                        mydata.BLEData = "98A36.5A28.5A1";
                    else if(i%4==1)
                        mydata.BLEData = "92A36.1A23.5A2";
                    else if(i%4==2)
                        mydata.BLEData = "101A33.2A33.2A3";
                    else
                        mydata.BLEData = "104A36.9A33.5A1";

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
        */


    }
    /*************************************************
     *搜索框显示
     ************************************************/
    public void showRoundProcessDialog(Context mContext, int layout) {
        DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_HOME
                        || keyCode == KeyEvent.KEYCODE_SEARCH) {
                    return true;
                }
                return false;
            }
        };

        mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.setOnKeyListener(keyListener);
        mDialog.show();

        mDialog.setContentView(layout);
    }
    /*************************************************
     *搜索蓝牙设备
     ************************************************/
    private void scanLeDevice() {
        new Thread() {

            @Override
            public void run() {
                mBluetoothAdapter.startLeScan(mLeScanCallback);//开始搜索设备

                try {
                    Thread.sleep(SCAN_PERIOD);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }.start();
    }
    /*************************************************
     *蓝牙回调
     ************************************************/
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        //通过startLeScan(BlueToothAdapter.LeScanCallback)函数调用之后 , 会初始化一个device对象 ; 当一个LE设备被发现的时候 , 这个对象device作为参数传递进来
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi,//device:识别的远程设备 rssi : RSSI的值作为对远程蓝牙设备的报告0代表没有蓝牙设备
                             byte[] scanRecord) {// scanRecode:远程设备提供的配对号(公告)
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (device != null) {
                        if (mDevices.indexOf(device) == -1)
                            mDevices.add(device);
                    }
                }
            });
        }
    };//蓝牙的回调方法
    /*************************************************
     *上一个页面返回数据
     ************************************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT
                && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /*************************************************
     *重写的销毁方法
     ************************************************/

    public static void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }
    @Override
    public void finish(){
        super.finish();
        releaseImageViewResouce(imageView);
        onDestroy();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
