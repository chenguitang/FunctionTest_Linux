package com.posin.utils;

import java.util.ArrayList;

public class VersionUtils {

	private static String version = null;

	/**
	 * ªÒ»°version
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
	
	

}
