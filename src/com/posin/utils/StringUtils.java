package com.posin.utils;

public class StringUtils {

	/**
	 * ȥ������Ŀո����
	 * 
	 * @param message
	 * @return
	 */
	public static String delectEmpty(String message) {
		return message.replaceAll("\\s{1,}", " ");
	}
}
