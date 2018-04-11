package com.posin.power;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JOptionPane;

public class Main {

	private static final String TAG = "InputReaderDemo";

	private static final int KEY_TYPE_ABS = 1;

	private static final int KEY_BACK = 158;
	private static final int KEY_HOME = 102;

	private static boolean[] mKeyStatus = new boolean[2];

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				InputReader ir = null;
//				final InputDialog mDialog = new InputDialog();
//				mDialog.setVisible(true);
//				listenerDialog(InputDialog.getInstance());
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
										// JOptionPane.showMessageDialog(null, "关机");
										if (!InputDialog.getInstance().isShowing()) {
											InputDialog.getInstance().setVisible(true);
										}
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
	 * 监听关机窗口
	 */
	private static void listenerDialog(final InputDialog dialog) {
		
		dialog.addWindowListener(new WindowAdapter() {

			@Override
			public void windowDeactivated(WindowEvent e) {
				System.out.println("windowDeactivated");
				if (dialog.isShowing()) {
					dialog.hide();
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
