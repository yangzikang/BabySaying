/*************************************************
 *对串口和服务进行定义管理
 ************************************************/

package com.example.dell.jiemian;

import java.util.HashMap;
public class RBLGattAttributes {
	private static HashMap<String, String> attributes = new HashMap<String, String>();
	public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
	public static String BLE_SHIELD_TX = "0000fff1-0000-1000-8000-00805f9b34fb";//读取端口
	public static String BLE_SHIELD_RX = "0000fff1-0000-1000-8000-00805f9b34fb";
	public static String BLE_SHIELD_SERVICE = "0000fff0-0000-1000-8000-00805f9b34fb";//服务口

	static {
		// RBL Services.
		attributes.put("0000fff0-0000-1000-8000-00805f9b34fb",
				"BLE Shield Service");
		// RBL Characteristics.
		attributes.put(BLE_SHIELD_TX, "BLE Shield TX");
		attributes.put(BLE_SHIELD_RX, "BLE Shield RX");
	}

	public static String lookup(String uuid, String defaultName) {
		String name = attributes.get(uuid);
		return name == null ? defaultName : name;
	}
}
