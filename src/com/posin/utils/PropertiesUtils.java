package com.posin.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class PropertiesUtils {

	private static Properties prop;

	/**
	 * 加载Properties 文件
	 * 
	 * @param path
	 *            文件路径
	 */
	private static void load(String path) {
		// 这里的path是项目文件的绝对路径
		// 先获取项目绝对路径：Thread.currentThread().getContextClassLoader().getResource("").getPath();
		// 然后在项目路径后面拼接"properties/sysConfig.properties";
		prop = new Properties();// 属性集合对象
		FileInputStream fis;
		try {
			System.out.println(path);
			fis = new FileInputStream(path);
			prop.load(fis);
			fis.close();// 关闭流
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取或更新Properties的值
	 * 
	 * @param path
	 *            要修改的文件路径
	 * @param key
	 *            修改的属性要名
	 * @param value
	 *            属性值
	 * @return
	 */
	public static Boolean updatePro(String path, String key, String value) {
		if (!new File(path).exists()) {
			try {
				FileOutputStream fos = new FileOutputStream(path);
				// 将Properties集合保存到流中
				Properties prop = new Properties();
				prop.setProperty(key, value);
				prop.store(fos, "Copyright (c) Boxcode Studio");
				fos.close();// 关闭流
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			load(path);

			prop.setProperty(key, value);
			// 文件输出流
			try {
				FileOutputStream fos = new FileOutputStream(path);
				// 将Properties集合保存到流中
				prop.store(fos, "Copyright (c) Boxcode Studio");
				fos.close();// 关闭流
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			System.out.println("get or update values：" + key + "="
					+ prop.getProperty(key));
			return true;
		}
	}

	/**
	 * 获取Properties的值
	 * 
	 * @param path
	 *            要修改的文件路径
	 * @param key
	 *            要修改的属性名
	 * @return
	 */
	public static String getPro(String path, String key) {

		// 文件不存在，直接返回"",防止程序报错
		if (!new File(path).exists()) {
			return "";
		}

		load(path);

		FileInputStream fis;
		try {
			fis = new FileInputStream(path);
			prop.load(fis);// 将属性文件流装载到Properties对象中
			fis.close();// 关闭流
			System.out.println("search " + key + " values："
					+ prop.getProperty(key));
			String property = prop.getProperty(key);

			return property == null ? "" : property;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
