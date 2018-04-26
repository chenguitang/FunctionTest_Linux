package com.posin.utils;

import java.util.ArrayList;

public class SnUtils {

	private static String SN = null;

	/**
	 * ��ȡSN��
	 * 
	 * @return
	 * @throws Throwable
	 */
	public static String getSN() throws Throwable {
		ArrayList<String> out = new ArrayList<String>();
		if (Proc.suExec("sysinfo get sn", out, 2000) == 0 && out.size() > 0) {
			SN = out.get(0);
			if (SN.contains("**CMD-RESULT=")) {
				SN = null;
			}
		}
		return SN;
	}

	/**
	 * ����SN��
	 * 
	 * @param sn
	 * @throws Throwable
	 */
	public static void setSN(String sn) throws Throwable {
		Proc.createSuProcess("sysinfo set sn " + sn);
	}

	/**
	 * �ж�SN���Ƿ���Ч
	 * 
	 * @param sn
	 * @return
	 */
	public static boolean isAvaildSN(String sn) {
		if (sn == null) {
			return false;
		} else if (sn.length() != 12 || !sn.startsWith("E")) {
			return false;
		} else {
			return true;
		}
	}
}
