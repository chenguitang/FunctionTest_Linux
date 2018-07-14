package com.posin.utils;

import java.util.ArrayList;

public class Eth0Utils {

	/**
	 * 获取以太网Mac地址
	 * 
	 * @return
	 * @throws Throwable
	 */
	public static String getEth0Mac() throws Throwable {
		String mac = "";
		ArrayList<String> out = new ArrayList<String>();
		if (Proc.suExec("sysinfo get eth0_mac", out, 2000) == 0
				&& out.size() > 0) {
			mac = out.get(0);
			if (mac.contains("**CMD-RESULT=")) {
				mac = null;
			}
		}
		return mac;
	}

	public static String getIPAddress() throws Throwable {
		String ip = "";
		ArrayList<String> out = new ArrayList<String>();
		if (Proc.suExec("ifconfig", out, 2000) == 0 && out.size() > 0) {
			String content = out.get(1);
			if (ip.contains("**CMD-RESULT=")) {
				ip = null;
			}

			if (content.contains(" inet addr:") && content.contains("Bcast")
					&& content.contains("Mask")) {
				int start = content.indexOf("inet addr");
				int end = content.indexOf("Bcast");
				ip = content.substring(start + 10, end - 2);
			}

		}
		return ip;
	}

	public static String getBcast() throws Throwable {
		String bCast = "";
		ArrayList<String> out = new ArrayList<String>();
		if (Proc.suExec("ifconfig", out, 2000) == 0 && out.size() > 0) {
			String content = out.get(1);
			if (bCast.contains("**CMD-RESULT=")) {
				bCast = null;
			}

			if (content.contains(" inet addr:") && content.contains("Bcast")
					&& content.contains("Mask")) {
				int start = content.indexOf("Bcast");
				int end = content.indexOf("Mask");
				bCast = content.substring(start+6, end - 2);
			}

		}
		return bCast;
	}
	
	
	public static String getMask() throws Throwable {
		String mask = "";
		ArrayList<String> out = new ArrayList<String>();
		if (Proc.suExec("ifconfig", out, 2000) == 0 && out.size() > 0) {
			String content = out.get(1);
			if (mask.contains("**CMD-RESULT=")) {
				mask = null;
			}

			if (content.contains(" inet addr:") && content.contains("Mask")
					&& content.contains("Mask")) {
				int start = content.indexOf("Mask");
				mask = content.substring(start+5);
			}

		}
		return mask;
	}
}
