package com.posin.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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

	/**
	 * ��ȡϵͳ������Ϣ
	 * 
	 * @param path
	 *            �����ļ���·��
	 * @param name
	 *            �����������
	 * @return ����ֵ
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
	 * �Ƿ��Զ�����ʱ��
	 * 
	 * @return
	 */
	public static boolean isAutoRefreshData() {
		String buildMessage = StringUtils.getBuildMessage("/etc/date.prop",
				"ro.autorefresh.date");
		return buildMessage.equals("yes") ? true : false;
	}
}
