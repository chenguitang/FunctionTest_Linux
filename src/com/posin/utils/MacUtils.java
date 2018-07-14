package com.posin.utils;

import java.util.ArrayList;

public class MacUtils {

	/**
	 * 获取网络相关信息
	 * 
	 * @return
	 * @throws Throwable
	 */
	public static NetworkInfo getNetInfo() throws Throwable {

		ArrayList<String> out = new ArrayList<String>();
		NetworkInfo ni = new NetworkInfo();
		if (Proc.suExec("busybox ifconfig eth0 | busybox grep HWaddr", out,
				2000) == 0 && out.size() > 0) {
			for (int i = 0; i < out.size(); i++) {
				String line = out.get(i);
				if (isValidString(line)) {
					int pos = line.indexOf("HWaddr ");
					if (pos >= 0) {
						ni.setEth0_mac(line.substring(pos + 7).trim());
						break;
					}
				}
			}
		}

		out.clear();
		if (Proc.suExec("busybox ifconfig eth0 | busybox grep \"inet addr:\"",
				out, 2000) == 0 && out.size() > 0) {
			for (int i = 0; i < out.size(); i++) {
				String line = out.get(i);
				if (line.startsWith("          inet addr:")) {
					String[] ss = line.substring(20).split(" ");
					if (ss != null && ss.length > 0) {
						ni.setEth0_ip(ss[0].trim());
						break;
					}
				}
			}
		}

		out.clear();
		if (Proc.suExec("busybox ifconfig wlan0 | busybox grep HWaddr", out,
				2000) == 0 && out.size() > 0) {
			for (int i = 0; i < out.size(); i++) {
				String line = out.get(i);
				if (isValidString(line)) {
					int pos = line.indexOf("HWaddr ");
					if (pos >= 0) {
						ni.setWlan0_mac(line.substring(pos + 7).trim());
						break;
					}
				}
			}
		}

		out.clear();
		if (Proc.suExec("busybox ifconfig wlan0 | busybox grep \"inet addr:\"",
				out, 2000) == 0 && out.size() > 0) {
			for (int i = 0; i < out.size(); i++) {
				String line = out.get(i);
				if (line.startsWith("          inet addr:")) {
					String[] ss = line.substring(20).split(" ");
					if (ss != null && ss.length > 0) {
						ni.setWlan0_ip(ss[0].trim());
						break;
					}
				}
			}
		}
		return ni;
	}

	public static boolean isValidString(String str) {
		if (str == null)
			return false;
		str = str.trim();
		if (str.length() == 0)
			return false;
		return true;
	}

}
