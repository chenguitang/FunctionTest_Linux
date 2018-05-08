package com.posin.utils;

import java.util.ArrayList;

public class HwUtils {

	private static String hw = null;

	/**
	 * ªÒ»°HW
	 * 
	 * @return
	 * @throws Throwable
	 */
	public static String getHw() throws Throwable {
		ArrayList<String> out = new ArrayList<String>();
		if (Proc.suExec("sysinfo get hw", out, 2000) == 0 && out.size() > 0) {
			hw = out.get(0);
			if (hw.contains("**CMD-RESULT=")) {
				hw = null;
			}
		}
		return hw;
	}

}
