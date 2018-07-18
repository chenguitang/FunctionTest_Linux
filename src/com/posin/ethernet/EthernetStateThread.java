package com.posin.ethernet;

import com.posin.constant.EthernetConstant;
import com.posin.ethernet.ProcessEthernetUtils.Callback;
import com.posin.utils.PropertiesUtils;

public class EthernetStateThread implements Runnable {

	// ��������������
	private static final String NET_NAME = "eth0";

	// �߳��Ƿ���������
	private boolean isRunning = false;
	// �Ƿ���Ҫ�˳��߳�
	private boolean isNeedExit = false;

	private EthernetStateListener mEthernetStateListener;

	// ��������
	private final Object mEventLock = new Object();
	// ��̫����Ϣ�ı�
	private boolean isNeedRefresh = false;
	// ��̫����Ϣ�ı�
	private boolean mSettingChanged = false;
	// ������̫��
	private boolean mRestart = false;
	// �Ƿ�ʹ�ñ�����
	private boolean isUseThis = true;

	private static String newIPAddress = "";

	private static class EthernetStateThreadHolder {
		private static final EthernetStateThread ETHERNET_STATE_THREAD_HOLDER = new EthernetStateThread();
	}

	public static EthernetStateThread getInstance() {
		return EthernetStateThreadHolder.ETHERNET_STATE_THREAD_HOLDER;
	}

	private EthernetStateThread() {
	}

	/**
	 * �����߳�
	 */
	public synchronized void start() {
		if (isRunning)
			return;
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
		synchronized (mEventLock) {
			isNeedExit = true;
			isRunning = false;
			mEventLock.notifyAll();
		}
		System.out.println("stop EthernetThread . ");
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

	/**
	 * ��������
	 */
	public void restartEth0(String ipAddress) {
		synchronized (mEventLock) {
			mEventLock.notifyAll();
			mRestart = true;
			newIPAddress = ipAddress;
		}
	}

	public void refreshEthernet() {
		synchronized (mEventLock) {
			mEventLock.notifyAll();
			isNeedRefresh = true;
		}
	}

	/**
	 * �Ƿ�ʹ�ñ�����
	 * 
	 * @param isUse
	 */
	public void isUseThisManager(boolean isUse) {
		synchronized (mEventLock) {
			isUseThis = isUse;
			mEventLock.notifyAll();
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

	private void loop() throws Exception {
		synchronized (mEventLock) {
			if (!isNeedRefresh) {
				mEventLock.wait(5000);
				isNeedRefresh = false;
			}
		}

		if (!mRestart && !mSettingChanged && isUseThis) {
			// ����IP��ַ
			String ipAddress = PropertiesUtils.getPro(
					EthernetConstant.ETHERNET_CONFIGURE_PATH,
					EthernetConstant.ETHERNET_IP_ADDRESS);
			System.out.println("ipAddress: " + ipAddress);
			System.out.println("***********************************");
			if (ipAddress.equals("")) {
				EthernetUtils.getInstance().setIpAddr(NET_NAME,
						"192.168.100.123");
			} else {
				EthernetUtils.getInstance().setIpAddr(NET_NAME, ipAddress);
			}
		}

		if (mRestart) {
			System.out.println("ethernet restart . ");
			mRestart = false;
			EthernetUtils.getInstance().restartEthernet(NET_NAME);
			if (newIPAddress != null && newIPAddress.trim() != "") {
				EthernetUtils.getInstance().setIpAddr(NET_NAME, newIPAddress);
				PropertiesUtils.updatePro(
						EthernetConstant.ETHERNET_CONFIGURE_PATH,
						EthernetConstant.ETHERNET_IS_WITHIN_KEY, "yes");
				Thread.sleep(1200);
				EthernetUtils.getInstance()
						.applyIpRoute(NET_NAME, newIPAddress);
			}

		}

		if (mSettingChanged) {
			mSettingChanged = false;

			// ������̫��
			EthernetUtils.getInstance().enable(NET_NAME, true);
			PropertiesUtils.updatePro(EthernetConstant.ETHERNET_CONFIGURE_PATH,
					EthernetConstant.ETHERNET_IS_WITHIN_KEY, "yes");
			if (newIPAddress != null) {
				EthernetUtils.getInstance().setIpAddr(NET_NAME, newIPAddress);
				Thread.sleep(1000);
				EthernetUtils.getInstance()
						.applyIpRoute(NET_NAME, newIPAddress);
				System.out.println("set ip ..... ");
			}
		}

		// ���״̬
		new ProcessEthernetUtils().suExecCallback("busybox ifconfig eth0 \n",
				new MyCallBack(), 2000);

	}

	/**
	 * ָ��ص�����
	 * 
	 * @author Greetty
	 * 
	 */
	private class MyCallBack implements Callback {

		boolean isConnect = false;
		String mac = null;
		String ipAddress = null;
		String bcast = null;
		String mask = null;

		@Override
		public void readLine(String line) {

			if (line.contains("UP BROADCAST RUNNING")) {
				isConnect = true;
			} else if (line.contains("Link encap:Ethernet  HWaddr")) {
				mac = line.substring(line.indexOf("HWaddr") + 7);
			} else if (line.contains("inet addr") && line.contains("Bcast")
					&& line.contains("Mask")) {
				ipAddress = line.substring(line.indexOf("inet addr") + 10,
						line.indexOf("Bcast") - 2);
				bcast = line.substring(line.indexOf("Bcast") + 6,
						line.indexOf("Mask") - 2);
				mask = line.substring(line.indexOf("Mask") + 5);
			}

			if (line.equals("**CMD-RESULT**")) {
				if (mEthernetStateListener != null) {
					mEthernetStateListener.refreshEthernet(isConnect, mac,
							ipAddress, bcast, mask);
				}
			}
		}

	}

	public void setEthernetStateListener(
			EthernetStateListener ethernetStateListener) {
		this.mEthernetStateListener = ethernetStateListener;
	}

	public interface EthernetStateListener {
		void refreshEthernet(boolean isConnect, String mac, String ipAddress,
				String bcast, String mask);
	}
}
