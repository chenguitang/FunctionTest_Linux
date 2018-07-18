package com.posin.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class PropertiesUtils {

	private static Properties prop;

	/**
	 * ����Properties �ļ�
	 * 
	 * @param path
	 *            �ļ�·��
	 */
	private static void load(String path) {
		// �����path����Ŀ�ļ��ľ���·��
		// �Ȼ�ȡ��Ŀ����·����Thread.currentThread().getContextClassLoader().getResource("").getPath();
		// Ȼ������Ŀ·������ƴ��"properties/sysConfig.properties";
		prop = new Properties();// ���Լ��϶���
		FileInputStream fis;
		try {
			System.out.println(path);
			fis = new FileInputStream(path);
			prop.load(fis);
			fis.close();// �ر���
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ�����Properties��ֵ
	 * 
	 * @param path
	 *            Ҫ�޸ĵ��ļ�·��
	 * @param key
	 *            �޸ĵ�����Ҫ��
	 * @param value
	 *            ����ֵ
	 * @return
	 */
	public static Boolean updatePro(String path, String key, String value) {
		if (!new File(path).exists()) {
			try {
				FileOutputStream fos = new FileOutputStream(path);
				// ��Properties���ϱ��浽����
				Properties prop = new Properties();
				prop.setProperty(key, value);
				prop.store(fos, "Copyright (c) Boxcode Studio");
				fos.close();// �ر���
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			load(path);

			prop.setProperty(key, value);
			// �ļ������
			try {
				FileOutputStream fos = new FileOutputStream(path);
				// ��Properties���ϱ��浽����
				prop.store(fos, "Copyright (c) Boxcode Studio");
				fos.close();// �ر���
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			System.out.println("get or update values��" + key + "="
					+ prop.getProperty(key));
			return true;
		}
	}

	/**
	 * ��ȡProperties��ֵ
	 * 
	 * @param path
	 *            Ҫ�޸ĵ��ļ�·��
	 * @param key
	 *            Ҫ�޸ĵ�������
	 * @return
	 */
	public static String getPro(String path, String key) {

		// �ļ������ڣ�ֱ�ӷ���"",��ֹ���򱨴�
		if (!new File(path).exists()) {
			return "";
		}

		load(path);

		FileInputStream fis;
		try {
			fis = new FileInputStream(path);
			prop.load(fis);// �������ļ���װ�ص�Properties������
			fis.close();// �ر���
			System.out.println("search " + key + " values��"
					+ prop.getProperty(key));
			String property = prop.getProperty(key);

			return property == null ? "" : property;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
