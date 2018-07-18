package com.posin.ethernet;

import java.io.IOException;

import com.posin.utils.ProcessUtils;

public class EthernetUtils {

	private static final class EthernetUtilsHolder {
		private static final EthernetUtils ETHERNET_UTILS_HOLDER = new EthernetUtils();
	}

	public static EthernetUtils getInstance() {
		return EthernetUtilsHolder.ETHERNET_UTILS_HOLDER;
	}

	private EthernetUtils() {
	}

	/**
	 * ��֤IP��ַ�Ƿ���Ч
	 * 
	 * @param ipAddress
	 *            IP��ַ
	 * @return
	 */
	public static boolean isIpv4Adress(String ipAddress) {
		if (ipAddress == null)
			return false;
		if (ipAddress.trim().length() == 0)
			return false;
		final String[] splitAddress = ipAddress.trim().split("\\.");
		if ((splitAddress == null) || (splitAddress.length != 4))
			return false;

		for (int i = 0; i < splitAddress.length; i++) {
			if (Integer.parseInt(splitAddress[i]) < 0) {
				return false;
			}
		}

		return true;
	}

	/**
	 * ����IP��ַ
	 * 
	 * @param name
	 *            ��������
	 * @param ipAddress
	 *            IP��ַ
	 * @throws Exception
	 */
	public void setIpAddr(String name, String ipAddress) throws Exception {
		if (ipAddress == null) {
			throw new Exception("ipAddress == null");
		}

		if (new ProcessUtils().suExecCallback("busybox ifconfig " + name + " "
				+ ipAddress + "\n", null, 1000) != 0) {
			throw new Exception("failure set ip address.");
		}
	}

	/**
	 * �������·��
	 * 
	 * @param name
	 *            ��������
	 * @param ipAddress
	 *            IP��ַ
	 * @throws Exception
	 */
	public void applyIpRoute(String name, String ipAddress) throws Exception {

		int i = ipAddress.lastIndexOf('.');
		String cmdRoute = "busybox ip route add " + ipAddress.substring(0, i)
				+ ".0/24 dev " + name + "\n";
		if (new ProcessUtils().suExecCallback(cmdRoute, null, 1000) != 0) {
			throw new Exception("failed to add route.");
		}
	}

	/**
	 * ������ر�����
	 * 
	 * @param name
	 *            ��������
	 * @param enableNet
	 *            �Ƿ�Ϊ��������
	 * @throws IOException
	 */
	public void enable(String name, boolean enableNet) throws IOException {
		if (enableNet) {
			if (new ProcessUtils().suExecCallback(
					"ifconfig " + name + " up \n", null, 1000) != 0)
				throw new IOException("failed to enable eth0.");
		} else {
			if (new ProcessUtils().suExecCallback("ifconfig " + name
					+ " down \n", null, 1000) != 0)
				throw new IOException("failed to disable eth0.");
		}
	}

	/**
	 * ��������
	 * 
	 * @param name
	 *            ��������
	 * @throws IOException
	 */
	public void restartEthernet(String name) throws IOException {
		try {
			enable(name, false);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		enable(name, true);
	}

}
