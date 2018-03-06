package com.posin.utils;

public class StringUtils {

	/**
	 * 去掉多余的空格符号
	 * 
	 * @param message
	 * @return
	 */
	public static String delectEmpty(String message) {
		return message.replaceAll("\\s{1,}", " ");
	}
}
