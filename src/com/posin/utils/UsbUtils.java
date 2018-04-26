package com.posin.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UsbUtils {

	/**
	 * 获取并生成需要上传的信息
	 * 
	 * @return
	 */
	public static String buildRegString() {

		StringBuilder sb = new StringBuilder();

		Map<String, Object> map = detectUsbInfo();
		for (String key : map.keySet()) {
			if (key.startsWith("usb"))
				continue;

			String vid = null;
			String pid = null;
			String product = null;
			Object data = map.get(key);
			if (data instanceof Map<?, ?>) {
				Map<String, Object> props = (Map<String, Object>) data;
				for (String p : props.keySet()) {
					Object value = props.get(p);
					if ("idVendor".equals(p)) {
						vid = value.toString();
					} else if ("idProduct".equals(p)) {
						pid = value.toString();
					} else if ("product".equals(p)) {
						product = value.toString();
					}
				}
			} else {
				// unknow
				continue;
			}

			if ("05e3".equals(vid)) {
				// skip hub
				continue;
			}
			sb.append("<br>");
			sb.append("USB[").append(key).append("]=");
			
			if ("8483".equals(vid)) {
				if ("1001".equals(pid))
					sb.append("单色屏客显");
				if ("1002".equals(pid))
					sb.append("3.2吋彩屏客显");
				else if ("1003".equals(pid))
					sb.append("4.3吋彩屏客显");
				else if ("1004".equals(pid))
					sb.append("192x64单色客显");
				else if ("1005".equals(pid))
					sb.append("LED客显");
				else
					sb.append("***未知客显***");
			} else if ("19e2".equals(vid) && "2533".equals(pid)) {
				sb.append("JC电容触摸屏");
			} else if ("0eff".equals(vid) && "0001".equals(pid)) {
				sb.append("Egalax电阻触摸屏");
			} else if ("0457".equals(vid) && "f817".equals(pid)) {
				sb.append("DPT电容触摸屏");
			} else if ("222a".equals(vid) && "0068".equals(pid)) {
				sb.append("YTG电容触摸屏");
			} else if ("29bd".equals(vid) && "4101".equals(pid)) {
				sb.append("DY电容触摸屏");
			} else if ("8866".equals(vid) && "0100".equals(pid)) {
				sb.append("QRS80打印机");
			} else if ("0b95".equals(vid) && "772b".equals(pid)) {
				sb.append("以太网控制器");
			} else if ("0bda".equals(vid) && "8179".equals(pid)) {
				sb.append("USB WIFI控制器");
			} else if ("0483".equals(vid) && "5710".equals(pid)) {
				if (product != null && product.contains("IDCard")) {
					sb.append("ID或MSR刷卡器");
				} else {
					sb.append(" ***未知的USB设备***");
				}
			} else if ("1a86".equals(vid) && "5523".equals(pid)) {
				sb.append("CH341设备");
			} else {
				sb.append(" ***未知的USB设备***");
			}

			if (product != null)
				sb.append(" (").append(product).append(')');
			sb.append("<br/>");
		}

		map = detectInputInfo();
		for (String key : map.keySet()) {
			if (!key.startsWith("input"))
				continue;

			Object data = map.get(key);
			if (data instanceof Map<?, ?>) {
				Map<String, Object> props = (Map<String, Object>) data;
				String e = (String) props.get("uevent");
				sb.append("<br>");
				if (e.contains("rk29-keypad")) {
					sb.append(key + "=RK按键板");
				} else if (e.contains("goodix-ts")) {
					sb.append(key + "=IIC触摸屏");
				} else if (e.contains("PHYS=\"input/ts\"")) {
					sb.append(key + "=触摸屏 : " + props.get("name"));
				} else {
					sb.append(key += "=" + props.get("name"));
				}
				sb.append("<br/>");
			}
		}
		return sb.toString();
	}

	/**
	 * 加载USB设备
	 * 
	 * @return
	 */
	public static Map<String, Object> detectUsbInfo() {
		Map<String, Object> result = new HashMap<String, Object>();
		File root = new File("/sys/bus/usb/devices/");
		File[] devs = root.listFiles();

		for (File dev : devs) {
			if (!dev.getName().contains(":")) {
				loadUsbDevice(result, dev);
			}
		}
		return result;
	}

	/**
	 * 加载输入设备
	 * 
	 * @return
	 */
	private static Map<String, Object> detectInputInfo() {
		Map<String, Object> result = new HashMap<String, Object>();
		File root = new File("/sys/class/input/");
		File[] devs = root.listFiles();

		for (File dev : devs) {
			loadInputDevice(result, dev);
		}
		return result;
	}
	
	private static void loadInputDevice(Map<String, Object> root, File dev) {
		String devname = dev.getName();
		String path = dev.getAbsolutePath();

		HashMap<String, Object> data = new HashMap<String, Object>();
		root.put(devname, data);

		addProperty(data, path, "uevent");

		if(devname.startsWith("event")) {
		} else if(devname.startsWith("input")) {
			addProperty(data, path, "name");
			addProperty(data, path, "phys");
			addProperty(data, path, "modalias");
			addProperty(data, path, "properties");

			HashMap<String, Object> id = new HashMap<String, Object>();
			data.put("id", id);
			path = path + "/id";
			addProperty(id, path, "bustype");
			addProperty(id, path, "product");
			addProperty(id, path, "vendor");
			addProperty(id, path, "version");
		}
	}

	private static void loadUsbDevice(Map<String, Object> root, File dev) {
		String devname = dev.getName();
		String path = dev.getAbsolutePath();

		HashMap<String, Object> usbdata = new HashMap<String, Object>();
		root.put(devname, usbdata);

		addProperty(usbdata, path, "authorized");
		addProperty(usbdata, path, "avoid_reset_quirk");
		addProperty(usbdata, path, "bConfigurationValue");
		addProperty(usbdata, path, "bDeviceClass");
		addProperty(usbdata, path, "bDeviceProtocol");
		addProperty(usbdata, path, "bDeviceSubClass");
		addProperty(usbdata, path, "bMaxPacketSize0");
		addProperty(usbdata, path, "bNumConfigurations");
		addProperty(usbdata, path, "bNumInterfaces");
		addProperty(usbdata, path, "bcdDevice");
		addProperty(usbdata, path, "bmAttributes");
		addProperty(usbdata, path, "busnum");
		addProperty(usbdata, path, "dev");
		addProperty(usbdata, path, "devnum");
		addProperty(usbdata, path, "devpath");
		addProperty(usbdata, path, "idProduct");
		addProperty(usbdata, path, "idVendor");
		addProperty(usbdata, path, "manufacturer");
		addProperty(usbdata, path, "maxchild");
		addProperty(usbdata, path, "product");
		addProperty(usbdata, path, "serial");
		addProperty(usbdata, path, "speed");
		addProperty(usbdata, path, "uevent");
		addProperty(usbdata, path, "urbnum");
		addProperty(usbdata, path, "version");

		File[] subs = dev.listFiles();
		for (File sub : subs) {
			if (sub.getName().startsWith("ep_")) {
				loadEndpoint(usbdata, sub);
			} else if (sub.getName().contains(":")) {
				loadInterface(usbdata, sub);
			}
		}
	}

	private static void addProperty(HashMap<String, Object> map, String path,
			String name) {
		try {
			File f = new File(path, name);
			if (f.exists()) {
				// String s;
				map.put(name, TextFile.loadAsString(f.getAbsolutePath(), 2048)
						.trim());
			} else {
				System.err.println("file not exists : " + f.getAbsolutePath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void loadEndpoint(Map<String, Object> parent, File ep) {
		HashMap<String, Object> epdata = new HashMap<String, Object>();
		parent.put(ep.getName(), epdata);
		addProperty(epdata, ep.getAbsolutePath(), "bEndpointAddress");
		addProperty(epdata, ep.getAbsolutePath(), "bLength");
		addProperty(epdata, ep.getAbsolutePath(), "bmAttributes");
		addProperty(epdata, ep.getAbsolutePath(), "direction");
		addProperty(epdata, ep.getAbsolutePath(), "interval");
		addProperty(epdata, ep.getAbsolutePath(), "type");
	}

	private static void loadInterface(Map<String, Object> usbdevice, File iface) {
		String devname = iface.getName();
		String path = iface.getAbsolutePath();
		HashMap<String, Object> usbdata = new HashMap<String, Object>();
		usbdevice.put(devname, usbdata);

		addProperty(usbdata, path, "bAlternateSetting");
		addProperty(usbdata, path, "bInterfaceClass");
		addProperty(usbdata, path, "bInterfaceNumber");
		addProperty(usbdata, path, "bInterfaceProtocol");
		addProperty(usbdata, path, "bInterfaceSubClass");
		addProperty(usbdata, path, "bNumEndpoints");
		addProperty(usbdata, path, "modalias");
		addProperty(usbdata, path, "uevent");

		File[] eps = iface.listFiles();
		for (File ep : eps) {
			if (!ep.getName().startsWith("ep_")) {
				continue;
			}
			loadEndpoint(usbdata, ep);
		}
	}
	
	public static Map<String, Object> getPosinDeviceSettings() {
		Map<String, Object> result = new HashMap<String, Object>();
		String[] props = {
				"/data/posin/printer.prop",
				"/data/posin/cashdrawer.prop",
				"/data/posin/customerdisplay.prop",
				"/data/posin/mifare.prop",
				"/data/posin/msr.prop",
				"/system/etc/posin/ssd2533.prop",
				"/system/build.prop"
		};
		for(String s : props) {
			File f = new File(s);
			if(!f.exists()) {
				result.put(f.getName(), "Error : file not exists.");
				continue;
			}
			try {
				result.put(f.getName(), TextFile.loadAsString(f.getAbsolutePath(), 2048));
			} catch (IOException e) {
				e.printStackTrace();
				result.put(f.getName(), e.getMessage());
			}
		}
		return result;
	}

}
