package com.posin.utils;

import java.util.ArrayList;

public class VersionUtils {

	private static String version = null;
	private static String sysVersion = null;

	/**
	 * 获取version
	 * 
	 * @return
	 * @throws Throwable
	 */
	public static String getVersion() throws Throwable {
		ArrayList<String> out = new ArrayList<String>();
		if (Proc.suExec("sysinfo get version", out, 2000) == 0
				&& out.size() > 0) {
			version = out.get(0);
			if (version.contains("**CMD-RESULT=")) {
				version = null;
			}
		}
		return version;
	}
	
	/**
	 * 获取系统版本
	 * @return
	 * @throws Throwable
	 */
	public static String getSysver() throws Throwable {
		ArrayList<String> out = new ArrayList<String>();
		if (Proc.suExec("sysinfo get sysver", out, 2000) == 0
				&& out.size() > 0) {
			sysVersion = out.get(0);
			if (sysVersion.contains("**CMD-RESULT=")) {
				sysVersion = null;
			}
		}
		return sysVersion;
	}
	

}
