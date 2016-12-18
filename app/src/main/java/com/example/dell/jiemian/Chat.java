/*************************************************
 *进行服务和连接页面
 * 搜索服务和串口，连接接收数据
 ************************************************/
package com.example.dell.jiemian;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Chat extends Activity {
	/*************************************************
	 *变量的初始化
	 ************************************************/
	private final static String TAG = Chat.class.getSimpleName();
	private static String data;
	private String mDeviceName;
	private String mDeviceAddress;
	private RBLService mBluetoothLeService;
	private Map<UUID, BluetoothGattCharacteristic> map = new HashMap<UUID, BluetoothGattCharacteristic>();
	boolean jump=true;
	//实现服务连接
	private final ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName componentName,
				IBinder service) {
			mBluetoothLeService = ((RBLService.LocalBinder) service)
					.getService();
			if (!mBluetoothLeService.initialize()) {
				Log.e(TAG, "Unable to initialize Bluetooth");
				finish();
			}
			mBluetoothLeService.connect(mDeviceAddress);
		}
		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mBluetoothLeService = null;
		}
	};
	//广播接收器接收蓝牙数据
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {//2
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();

			if (RBLService.ACTION_GATT_DISCONNECTED.equals(action)) {
			} else if (RBLService.ACTION_GATT_SERVICES_DISCOVERED
					.equals(action)) {
				getGattService(mBluetoothLeService.getSupportedGattService());
			} else if (RBLService.ACTION_DATA_AVAILABLE.equals(action)) {
				displayData(intent.getByteArrayExtra(RBLService.EXTRA_DATA));
			}
		}
	};
	/*************************************************
	 *开启服务
	 ************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connect_layout);
		ActivityCollector.addActivity(this);
		ActivityClearer.addActivity("Chat",this);
		Intent intent = getIntent();

		mDeviceAddress = intent.getStringExtra(Device.EXTRA_DEVICE_ADDRESS);
		mDeviceName = intent.getStringExtra(Device.EXTRA_DEVICE_NAME);


		Intent gattServiceIntent = new Intent(this, RBLService.class);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
	}
	/*************************************************
	 *在界面完全开启后注册广播接收器
	 ************************************************/
	@Override
	protected void onResume() {//3
		super.onResume();

		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
	}
	/*************************************************
	 *重写按下home键
	 ************************************************/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			mBluetoothLeService.disconnect();
			mBluetoothLeService.close();

			System.exit(0);
		}

		return super.onOptionsItemSelected(item);
	}
	/*************************************************
	 *从写停止，取消注册广播接收
	 ************************************************/
	@Override
	protected void onStop() {
		super.onStop();

		unregisterReceiver(mGattUpdateReceiver);
	}
	/*************************************************
	 *重写销毁方法，断开连接
	 ************************************************/
	@Override
	protected void onDestroy() {
		super.onDestroy();

		mBluetoothLeService.disconnect();
		mBluetoothLeService.close();

		System.exit(0);
	}
	/*************************************************
	 *数据判断，页面跳转
	 ************************************************/
	private void displayData(byte[] byteArray) {//1
		if (byteArray != null) {
				data = new String(byteArray);
				String str[] = data.split("A");//正则表达式，以A为分隔把字符串拆分为字符串数组
				if(str.length>2){ //当数据段数超过2时，认为数据有效
					if(jump==true) {
						jump=false;
						Intent a = new Intent(Chat.this, menuActivity.class);
						startActivity(a);
					}
				}
		}

	}
	/*************************************************
	 *获得串口服务
	 ************************************************/
	private void getGattService(BluetoothGattService gattService) {
		if (gattService == null)//确定服务不为空
			return;

		BluetoothGattCharacteristic characteristic = gattService
				.getCharacteristic(RBLService.UUID_BLE_SHIELD_TX);//用characteristic保存选择的串口
		map.put(characteristic.getUuid(), characteristic);

		BluetoothGattCharacteristic characteristicRx = gattService
				.getCharacteristic(RBLService.UUID_BLE_SHIELD_RX);
		mBluetoothLeService.setCharacteristicNotification(characteristicRx,
				true);
		mBluetoothLeService.readCharacteristic(characteristicRx);
	}
	/*************************************************
	 *动态添加广播的intentFilter
	 ************************************************/
	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();

		intentFilter.addAction(RBLService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(RBLService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(RBLService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(RBLService.ACTION_DATA_AVAILABLE);

		return intentFilter;
	}

	@Override
	public void finish(){
		super.finish();
		onDestroy();
	}

}


