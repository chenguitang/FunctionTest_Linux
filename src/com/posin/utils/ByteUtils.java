package com.posin.utils;

import java.nio.ByteBuffer;

public class ByteUtils {

	public static String bytesToHexString(byte[] data) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length; i++)
			sb.append(String.format("%02X ", data[i]));
		return sb.toString();
	}

	/**
	 * 十六进制字符串转化为byte数组
	 * 
	 * @param splitSign
	 *            分割字符串标记
	 * @param txt
	 *            要转化的内容
	 * @return
	 * @throws Exception
	 */
	public static byte[] hexStringToBytes(String splitSign, String txt)
			throws Exception {
		txt = txt.toLowerCase();
		String[] items = txt.split(splitSign);
		ByteBuffer bb = ByteBuffer.allocate(1024);
		for (String t : items) {
			if (t.length() == 1)
				bb.put((byte) Character.digit(t.charAt(0), 16));
			else if (t.length() == 2) {
				int data = (Character.digit(t.charAt(0), 16) << 4)
						| Character.digit(t.charAt(1), 16);
				bb.put((byte) data);
			} else {
				throw new Exception("error : unknow hex string format.");
			}
		}
		if (bb.position() > 0) {
			byte[] result = new byte[bb.position()];
			bb.flip();
			bb.get(result);
			return result;
		}
		return null;
	}

}
