package com.posin.ethernet;

import com.posin.constant.EthernetConstant;
import com.posin.utils.PropertiesUtils;

import sun.java2d.pipe.LoopBasedPipe;

public class EthernetThread implements Runnable {

	// ѭ������
	private static final int APPLY_TIMEOUT = 10 * 1000;
	// ��������������
	private static final String NET_NAME = "eth0";

	private EthernetListener mEthernetListener;

	// �߳��Ƿ���������
	private boolean isRunning = false;
	// �Ƿ���Ҫ�˳��߳�
	private boolean isNeedExit = false;
	// ������Ϣ����λ��
	private String mConfigSavePath;
	// ��������
	private final Object mEventLock = new Object();
	// ��̫����Ϣ�ı�
	private boolean mSettingChanged = false;
	// ������̫��
	private boolean mRestart = false;

	private static String newIPAddress = "";

	private static class EthernetThreadHolder {
		private static final EthernetThread ETHERNET_THREAD_HOLDER = new EthernetThread();
	}

	public static EthernetThread getInstance() {
		return EthernetThreadHolder.ETHERNET_THREAD_HOLDER;
	}

	private EthernetThread() {
	}

	/**
	 * �����߳�
	 */
	public synchronized void start() {
		if (isRunning)
			return;

		// mConfigSavePath = ethoConfigPath;
		isRunning = true;
		isNeedExit = false;
		System.out.println("start EthernetThread . ");
		new Thread(this).start();
	}

	/**
	 * ֹͣ�߳�
	 */
	public synchronized void stop() {
		if (!isRunning)
			return;
		isNeedExit = true;
		System.out.println("stop EthernetThread . ");
	}

	/**
	 * ������̫��
	 */
	public void restartEthernet() {
		synchronized (mEventLock) {
			mEventLock.notifyAll();
			mRestart = true;
		}
	}

	/**
	 * ������̫��IP��ַ
	 */
	public void setIpAddress(String ipAddress) {
		synchronized (mEventLock) {
			mEventLock.notifyAll();
			mSettingChanged = true;
			newIPAddress = ipAddress;
		}
	}

	@Override
	public void run() {
		while (!isNeedExit) {
			try {
				loop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void loop() throws Exception {

		System.out.println("start loop ...");
		synchronized (mEventLock) {
			System.out.println("enter to synchronized method ...");
			if (!mSettingChanged && !mRestart) {
				System.out.println("start wait ...");
				mEventLock.wait(APPLY_TIMEOUT);
				System.out.println("end wait ... ");
			}
		}

		System.out.println("loop ......");
		String ipAddress = PropertiesUtils.getPro(
				EthernetConstant.ETHERNET_CONFIGURE_PATH,
				EthernetConstant.ETHERNET_IP_ADDRESS);
		if (mRestart) {
			System.out.println("ethernet restart . ");
			EthernetUtils.getInstance().restartEthernet(NET_NAME);
			if (ipAddress != null && ipAddress.trim() != "") {
				Thread.sleep(1000);
				EthernetUtils.getInstance().setIpAddr(NET_NAME, ipAddress);
			}

		} else {
			if (ipAddress != null && ipAddress.trim() != "") {
				EthernetUtils.getInstance().setIpAddr(NET_NAME, ipAddress);
			}
		}

		if (mSettingChanged) {
			// ������̫��
			EthernetUtils.getInstance().enable(NET_NAME, true);
			PropertiesUtils.updatePro(EthernetConstant.ETHERNET_CONFIGURE_PATH,
					EthernetConstant.ETHERNET_IS_WITHIN_KEY, "yes");
			if (newIPAddress != null) {
				EthernetUtils.getInstance().setIpAddr(NET_NAME, newIPAddress);

				EthernetUtils.getInstance()
						.applyIpRoute(NET_NAME, newIPAddress);
				System.out.println("set ip ..... ");
			}

		}
		mRestart = false;
		mSettingChanged = false;

		if (mEthernetListener != null) {
			Thread.sleep(2000);
			mEthernetListener.refreshEthernet();
			System.out.println("refreshEthernet .  ");
		}
	}

	public void setEthernetListener(EthernetListener ethernetListener) {
		this.mEthernetListener = ethernetListener;
	}

	public interface EthernetListener {
		void refreshEthernet();
	}

}
