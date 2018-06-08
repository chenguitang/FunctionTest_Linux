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
	 * ����wifi����
	 * 
	 * @param name
	 *            wifi ssid
	 * @return
	 */
	public static String parseWifiName(String name) {
		try {
			if (name == null) {
				throw new Exception("name is null, please check your name ...");
			}
			if (name.contains("\\x")) {
				String beginName = null;
				String behindName = null;
				int beginIndex = name.indexOf("\\x");
				if (beginIndex > 0) {
					beginName = name.substring(0, beginIndex);
					behindName = name.substring(beginIndex);
				} else {
					behindName = name;
				}
				String hexString = behindName.substring(2).replace("\\x", " ");
				System.out.println("hexString: " + hexString);
				byte[] bytes = ByteUtils.hexStringToBytes(" ", hexString);
				String mTxt = new String(bytes, "utf-8");
				System.out.println("mtxt: " + mTxt);
				return beginName == null ? mTxt : beginName + mTxt;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
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

	/**
	 * ƴ���ַ���
	 * 
	 * @param message
	 * @return
	 */
	public static String SpliceString(String... message) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < message.length; i++) {
			sb.append(message[i]);
		}
		return sb.toString();
	}

}
