package com.posin.utils;

import java.util.ArrayList;

public class ModelUtils {

	private static String model = null;

	/**
	 * ªÒ»°model£¨–Õ∫≈
	 * 
	 * @return
	 * @throws Throwable
	 */
	public static String getModel() throws Throwable {
		ArrayList<String> out = new ArrayList<String>();
		if (Proc.suExec("sysinfo get model", out, 2000) == 0 && out.size() > 0) {
			model = out.get(0);
			if (model.contains("**CMD-RESULT=")) {
				model = null;
			}
		}
		return model;
	}

}
