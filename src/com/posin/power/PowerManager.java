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
	
	public void startPowerListener() {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				InputReader ir = null;
				listenerDialog(InputDialog.getInstance());
				InputDialog.getInstance().setVisible(true);
				InputDialog.getInstance().setVisible(false);
				try {
					ir = new InputReader("rk29-keypad") {
						@Override
						protected void onEvent(int type, int code, int value) {
							System.out.println("onEvent " + type + ", " + code + ", "
									+ value);
							if (type == KEY_TYPE_ABS) {
								switch (code) {
								case KEY_BACK:
									mKeyStatus[0] = (value != 0);
									System.out.println("key back "
											+ (mKeyStatus[0] ? "pressed" : "released"));
									if (InputDialog.getInstance().isShowing()) {
										InputDialog.getInstance().setVisible(false);
									}

									break;
								case KEY_HOME:
									mKeyStatus[1] = (value != 0);
									System.out.println("key home "
											+ (mKeyStatus[1] ? "pressed" : "released"));
									System.out.println("value: " + value);
									System.out.println("code: " + code);
									if (!mKeyStatus[1]) {
										// JOptionPane.showMessageDialog(null, "�ػ�");
//										if (!InputDialog.getInstance().isShowing()) {
											InputDialog.getInstance().setVisible(true);
//										}
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
				}finally{
//					if (ir != null) {
//						ir.stop();
//					}
				}
			}
		});
	}
	
	/**
	 * �����ػ�����
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
