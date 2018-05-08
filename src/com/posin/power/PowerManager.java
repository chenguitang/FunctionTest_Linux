package com.posin.power;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PowerManager {

	private static final int KEY_TYPE_ABS = 1;

	private static final int KEY_BACK = 158;
	private static final int KEY_HOME = 102;

	private static long startTime = 0; // 开始按键时间
	private static int clickPowerNumber = 0; // 按电源键次数

	private static boolean[] mKeyStatus = new boolean[2];

	private static final PowerManager POWER_MANAGER_INSTANCE = new PowerManager();

	private PowerManager() {
	}

	public static PowerManager getInstance() {
		return POWER_MANAGER_INSTANCE;
	}

	/**
	 * 开始监听实体按键
	 */
	public void startPowerListener() {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				InputReader ir = null;
				try {
					ir = new InputReader("rk29-keypad") {
						@Override
						protected void onEvent(int type, int code, int value) {
							System.out.println("onEvent " + type + ", " + code
									+ ", " + value);
							if (type == KEY_TYPE_ABS) {
								switch (code) {
								case KEY_BACK:
									clickBack(value);
									break;
								case KEY_HOME:
									clickHome(value);
									break;
								}
							}
						}

						@Override
						protected void onTerminated() {
							System.out.println("thread terminated.");
						}
					};
				} catch (Exception e) {
					e.printStackTrace();
				}
				listenerDialog(PowerPage.getInstance());
			}

		});
	}

	/**
	 * 点击物理按键，BACK
	 * 
	 * @param value
	 */
	public void clickBack(int value) {
		mKeyStatus[0] = (value != 0);
		System.out.println("key back "
				+ (mKeyStatus[0] ? "pressed" : "released"));
		if (PowerPage.getInstance().isShowing()) {
			PowerPage.getInstance().setVisible(false);
		}

		System.out.println("mKeyStatus[0]: " + mKeyStatus[0]);
		if (mKeyStatus[0]) {
			System.out.println("clickPowerNumber: " + clickPowerNumber);
			if (clickPowerNumber == 5) {
				if (System.currentTimeMillis() - startTime < 800) {
					System.out.println("open setting sn page ... ");
					RegistedMachine.getInstance().setVisible(true);
				} else {
					System.out.println("click back timeout");
				}
			} else {

				if (RegistedMachine.getInstance().isShowing()) {
					RegistedMachine.getInstance().setVisible(false);
				}
			}
		}
	}

	/**
	 * 点击物理按键，HOEM
	 * 
	 * @param value
	 */
	public void clickHome(int value) {
		mKeyStatus[1] = (value != 0);
		System.out.println("key home "
				+ (mKeyStatus[1] ? "pressed" : "released"));
		if (!mKeyStatus[1]) {
			//关机页面 
			PowerPage.getInstance().setVisible(true);
			// 工厂设置
			if (clickPowerNumber == 0) {
				startTime = System.currentTimeMillis();
				clickPowerNumber++;
				System.out.println("clickPowerNumber ==0, clickPowerNumber++: "
						+ clickPowerNumber);
			} else {
				System.out.println("System.currentTimeMillis(): "
						+ System.currentTimeMillis());
				System.out.println("startTime: " + startTime);
				if (System.currentTimeMillis() - startTime < 600) {
					startTime = System.currentTimeMillis();
					clickPowerNumber++;
					System.out
							.println("clickPowerNumber !=0, clickPowerNumber++: "
									+ clickPowerNumber);
				} else {
					System.out
							.println("click timeout ... , this monitoring is invalid ....");
					clickPowerNumber = 6;
				}
			}
		}
	}

	/**
	 * 打开关机弹框页面
	 */
	public void showShutdownView() {
//		PowerPage.getInstance().setBounds(600, 250, 800, 490);
		PowerPage.getInstance().setVisible(true);
	}

	/**
	 * 监听关机窗口
	 */
	private static void listenerDialog(final PowerPage dialog) {

		dialog.addWindowListener(new WindowAdapter() {

			@Override
			public void windowDeactivated(WindowEvent e) {
				System.out.println("windowDeactivated");
				clickPowerNumber = 0;
				startTime = 0;
				if (dialog.isShowing()) {
					dialog.setVisible(false);
				}
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				System.out.println("windowActivated");
			}
		});
	}

}
