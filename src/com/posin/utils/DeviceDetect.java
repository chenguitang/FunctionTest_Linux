package com.posin.utils;

import java.util.HashMap;
import java.util.Map;

public class DeviceDetect {

	public static Map<String, Object> getSystemInfo() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("usb", UsbUtils.detectUsbInfo());
		result.put("posin", UsbUtils.getPosinDeviceSettings());
		return result;
	}
	
}
