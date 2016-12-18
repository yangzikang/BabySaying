/*************************************************
 *蓝牙服务
 ************************************************/
package com.example.dell.jiemian;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.UUID;


public class RBLService extends Service {
	/*************************************************
	 *变量或常量初始化
	 ************************************************/
	private final static String TAG = RBLService.class.getSimpleName();

	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	private String mBluetoothDeviceAddress;
	private BluetoothGatt mBluetoothGatt;

	public final static String ACTION_GATT_CONNECTED = "ACTION_GATT_CONNECTED";
	public final static String ACTION_GATT_DISCONNECTED = "ACTION_GATT_DISCONNECTED";
	public final static String ACTION_GATT_SERVICES_DISCOVERED = "ACTION_GATT_SERVICES_DISCOVERED";
	public final static String ACTION_GATT_RSSI = "ACTION_GATT_RSSI";
	public final static String ACTION_DATA_AVAILABLE = "ACTION_DATA_AVAILABLE";
	public final static String EXTRA_DATA = "EXTRA_DATA";

	public final static UUID UUID_BLE_SHIELD_TX = UUID
			.fromString(RBLGattAttributes.BLE_SHIELD_TX);
	public final static UUID UUID_BLE_SHIELD_RX = UUID
			.fromString(RBLGattAttributes.BLE_SHIELD_RX);
	public final static UUID UUID_BLE_SHIELD_SERVICE = UUID
			.fromString(RBLGattAttributes.BLE_SHIELD_SERVICE);
	/*************************************************
	 *蓝牙串口数据的回调
	 ************************************************/
	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
											int newState) {
			String intentAction;

			if (newState == BluetoothProfile.STATE_CONNECTED) {
				intentAction = ACTION_GATT_CONNECTED;
				broadcastUpdate(intentAction);
				Log.i(TAG, "Connected to GATT server.");
				// Attempts to discover services after successful connection.
				Log.i(TAG, "Attempting to start service discovery:"
						+ mBluetoothGatt.discoverServices());
			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				intentAction = ACTION_GATT_DISCONNECTED;
				Log.i(TAG, "Disconnected from GATT server.");
				broadcastUpdate(intentAction);
			}
		}

		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				broadcastUpdate(ACTION_GATT_RSSI, rssi);
			} else {
				Log.w(TAG, "onReadRemoteRssi received: " + status);
			}
		};

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
			} else {
				Log.w(TAG, "onServicesDiscovered received: " + status);
			}
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
										 BluetoothGattCharacteristic characteristic, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
			}
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
		}
	};
	/*************************************************
	 *广播更新
	 ************************************************/
	private void broadcastUpdate(final String action) {
		final Intent intent = new Intent(action);
		sendBroadcast(intent);
	}
	/*************************************************
	 *广播更新 （重载）
	 ************************************************/
	private void broadcastUpdate(final String action, int rssi) {
		final Intent intent = new Intent(action);
		intent.putExtra(EXTRA_DATA, String.valueOf(rssi));
		sendBroadcast(intent);
	}
	/*************************************************
	 *广播更新 （重载）
	 ************************************************/
	private void broadcastUpdate(final String action,final BluetoothGattCharacteristic characteristic) {
		final Intent intent = new Intent(action);
		if (UUID_BLE_SHIELD_RX.equals(characteristic.getUuid())) {//找到正确的串口
			final byte[] rx = characteristic.getValue(); //将串口数据存入byte数组中
			intent.putExtra(EXTRA_DATA, rx);
			if(rx!=null) {

				String data = new String(rx);
				String str[] = data.split("A");//正则表达式解析数据
				if (str.length > 2) {
					mydata.BLEData=data;//将数据存储在全局变量中，以便使用
				}
			}
		}

		sendBroadcast(intent);
	}
	/*************************************************
	 *用于本地绑定
	 ************************************************/
	public class LocalBinder extends Binder {
		RBLService getService() {
			return RBLService.this;
		}
	}
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	@Override
	public boolean onUnbind(Intent intent) {
		close();
		return super.onUnbind(intent);
	}
	private final IBinder mBinder = new LocalBinder();
	/*************************************************
	 *初始化
	 ************************************************/
	public boolean initialize() {
		if (mBluetoothManager == null) {
			mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			if (mBluetoothManager == null) {
				Log.e(TAG, "Unable to initialize BluetoothManager.");
				return false;
			}
		}

		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
			return false;
		}

		return true;
	}

	/*************************************************
	 *连接到GATT服务
	 ************************************************/
	public boolean connect(final String address) {
		if (mBluetoothAdapter == null || address == null) {
			Log.w(TAG,
					"BluetoothAdapter not initialized or unspecified address.");
			return false;
		}

		// Previously connected device. Try to reconnect.
		if (mBluetoothDeviceAddress != null
				&& address.equals(mBluetoothDeviceAddress)
				&& mBluetoothGatt != null) {
			Log.d(TAG,
					"Trying to use an existing mBluetoothGatt for connection.");
			if (mBluetoothGatt.connect()) {
				return true;
			} else {
				return false;
			}
		}

		final BluetoothDevice device = mBluetoothAdapter
				.getRemoteDevice(address);
		if (device == null) {
			Log.w(TAG, "Device not found.  Unable to connect.");
			return false;
		}
		// We want to directly connect to the device, so we are setting the
		// autoConnect
		// parameter to false.
		mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
		Log.d(TAG, "Trying to create a new connection.");
		mBluetoothDeviceAddress = address;

		return true;
	}

	/*************************************************
	 *断开或取消连接
	 ************************************************/
	public void disconnect() {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.disconnect();
	}

	/*************************************************
	 *连接过后关闭
	 ************************************************/
	public void close() {
		if (mBluetoothGatt == null) {
			return;
		}
		mBluetoothGatt.close();
		mBluetoothGatt = null;
	}

	/*************************************************
	 *读取characteristic
	 ************************************************/
	public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}

		mBluetoothGatt.readCharacteristic(characteristic);
	}

	/*************************************************
	 *是否收到数据发送通知（测试）
	 ************************************************/
	public void setCharacteristicNotification(
			BluetoothGattCharacteristic characteristic, boolean enabled) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

		if (UUID_BLE_SHIELD_RX.equals(characteristic.getUuid())) {
			BluetoothGattDescriptor descriptor = characteristic
					.getDescriptor(UUID
							.fromString(RBLGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
			descriptor
					.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			mBluetoothGatt.writeDescriptor(descriptor);
		}
	}

	public BluetoothGattService getSupportedGattService() {
		if (mBluetoothGatt == null)
			return null;
		//UUID_BLE_SHIELD_SERVICE是个Services
		return mBluetoothGatt.getService(UUID_BLE_SHIELD_SERVICE);//获得指定的服务
	}


}
