package com.posin.constant;

import java.io.IOException;

import com.posin.utils.ProcessUtils;

public class CommandConstant {
	
	/**
	 * 关闭窗口管理
	 */
	public static void closeViewManager(){
		try {
//			Thread.sleep(2000);
//			new ProcessUtils().createSuProcess("systemctl stop openbox");
//			System.out.println("close view manager ...");
//			System.out.println("2 close view manager ...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 打开窗口管理
	 */
	public static void openViewManager(){
		try {
//			new ProcessUtils().createSuProcess("systemctl start openbox");
//			Thread.sleep(1000);
//			System.out.println("1 open view manager ...");
			System.out.println("2 open view manager ...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
