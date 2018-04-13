package com.posin.power;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PowerManager {

	private static final int KEY_TYPE_ABS = 1;

	private static final int KEY_BACK = 158;
	private static final int KEY_HOME = 102;

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
									mKeyStatus[0] = (value != 0);
									System.out.println("key back "
											+ (mKeyStatus[0] ? "pressed"
													: "released"));
									if (InputDialog.getInstance().isShowing()) {
										InputDialog.getInstance().setVisible(
												false);
									}

									break;
								case KEY_HOME:
									mKeyStatus[1] = (value != 0);
									System.out.println("key home "
											+ (mKeyStatus[1] ? "pressed"
													: "released"));
									if (!mKeyStatus[1]) {
										InputDialog.getInstance().setBounds(
												600, 300, 800, 400);
										InputDialog.getInstance().setVisible(
												true);
									}
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
			}
		});
		InputDialog.getInstance().setEnabled(true); // 加载页面

		listenerDialog(InputDialog.getInstance());
		InputDialog.getInstance().setBounds(2000, 1100, 800, 400);
		InputDialog.getInstance().setVisible(true);

	}

	/**
	 * 打开关机弹框页面
	 */
	public void showShutdownView() {
		InputDialog.getInstance().setBounds(600, 300, 800, 400);
		InputDialog.getInstance().setVisible(true);
	}

	/**
	 * 监听关机窗口
	 */
	private static void listenerDialog(final InputDialog dialog) {

		dialog.addWindowListener(new WindowAdapter() {

			@Override
			public void windowDeactivated(WindowEvent e) {
				System.out.println("windowDeactivated");
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
