package com.posin.ethernet;

import com.posin.constant.EthernetConstant;
import com.posin.utils.PropertiesUtils;

import sun.java2d.pipe.LoopBasedPipe;

public class EthernetThread implements Runnable {

	// 循环周期
	private static final int APPLY_TIMEOUT = 10 * 1000;
	// 操作的网络名称
	private static final String NET_NAME = "eth0";

	private EthernetListener mEthernetListener;

	// 线程是否正在运行
	private boolean isRunning = false;
	// 是否需要退出线程
	private boolean isNeedExit = false;
	// 设置信息保存位置
	private String mConfigSavePath;
	// 加锁对象
	private final Object mEventLock = new Object();
	// 以太网信息改变
	private boolean mSettingChanged = false;
	// 重启以太网
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
	 * 启动线程
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
	 * 停止线程
	 */
	public synchronized void stop() {
		if (!isRunning)
			return;
		isNeedExit = true;
		System.out.println("stop EthernetThread . ");
	}

	/**
	 * 重启以太网
	 */
	public void restartEthernet() {
		synchronized (mEventLock) {
			mEventLock.notifyAll();
			mRestart = true;
		}
	}

	/**
	 * 设置以太网IP地址
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
			// 开启以太网
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
