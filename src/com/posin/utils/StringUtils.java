package com.posin.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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

	/**
	 * 获取系统配置信息
	 * 
	 * @param path
	 *            保存文件的路径
	 * @param name
	 *            配置项的名称
	 * @return 配置值
	 */
	public static String getBuildMessage(String path, String name) {
		Properties p = new Properties();
		FileInputStream fis = null;
		if (!new File(path).exists()) {
			return "";
		}
		try {
			fis = new FileInputStream(path);
			p.load(fis);
			return p.getProperty(name);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}

	/**
	 * 是否自动更新时间
	 * 
	 * @return
	 */
	public static boolean isAutoRefreshData() {
		String buildMessage = StringUtils.getBuildMessage("/etc/date.prop",
				"ro.autorefresh.date");
		return buildMessage.equals("yes") ? true : false;
	}
}
