package com.example.dell.jiemian;
/*************************************************
 *页面以窗口的方式展示
 * 搜索蓝牙设备
 ************************************************/
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Device extends Activity implements OnItemClickListener {
	/*************************************************
	 *变量的初始化
	 ************************************************/
	private ArrayList<BluetoothDevice> devices;
	private List<Map<String, String>> listItems = new ArrayList<Map<String, String>>();
	private SimpleAdapter adapter;
	private Map<String, String> map = null;
	private ListView listView;
	private String DEVICE_NAME = "name";
	private String DEVICE_ADDRESS = "address";
	public static final int RESULT_CODE = 31;
	public final static String EXTRA_DEVICE_ADDRESS = "EXTRA_DEVICE_ADDRESS";
	public final static String EXTRA_DEVICE_NAME = "EXTRA_DEVICE_NAME";
	/*************************************************
	 *搜索的设备用ListView显示
	 ************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_list);
		ActivityCollector.addActivity(this);
		ActivityClearer.addActivity("Device",this);
		setTitle("已找到的设备：");

		listView = (ListView) findViewById(R.id.listView);

		devices = (ArrayList<BluetoothDevice>) MainActivity.mDevices;
		for (BluetoothDevice device : devices) {
			map = new HashMap<String, String>();
			map.put(DEVICE_NAME, device.getName());
			map.put(DEVICE_ADDRESS, device.getAddress());
			listItems.add(map);
		}

		adapter = new SimpleAdapter(getApplicationContext(), listItems,
				R.layout.list_item, new String[] { "name", "address" },
				new int[] { R.id.deviceName, R.id.deviceAddr });
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}
	/*************************************************
	 *重写的ListView的点击事件
	 ************************************************/
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
							int position, long id) {
		HashMap<String, String> hashMap = (HashMap<String, String>) listItems
				.get(position);
		String addr = hashMap.get(DEVICE_ADDRESS);
		String name = hashMap.get(DEVICE_NAME);
		Intent intent = new Intent(Device.this, Chat.class);
		intent.putExtra(EXTRA_DEVICE_ADDRESS, addr);
		intent.putExtra(EXTRA_DEVICE_NAME, name);
		startActivity(intent);
		MainActivity.instance.finish();
		finish();
	}

	@Override
	public void finish(){
		super.finish();
		onDestroy();
	}
}
