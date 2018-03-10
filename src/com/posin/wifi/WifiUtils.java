package com.posin.wifi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;

import javax.swing.text.AbstractDocument.BranchElement;

import com.posin.constant.WifiMessage;
import com.posin.global.Appconfig;
import com.posin.utils.Proc;
import com.posin.utils.ProcessUtils;
import com.posin.utils.StringUtils;
import com.posin.utils.ProcessUtils.Callback;

/**
 * Wifi管理工具类
 * 
 * @author Greetty
 * 
 */
public class WifiUtils {

	// 扫描wifi
	private static String cmd_scan_wifi = "wpa_cli -i wlan0 scan\n";
	// wifi列表
	private static String cmd_scan_wifi_result = "wpa_cli -i wlan0 scan_result\n";
	// add_network
	private static String cmd_add_network = "wpa_cli -i wlan0 add_network\n";
	// 查找network
	private static String cmd_find_network = "wpa_cli -i wlan0 list_network\n";
	// 获取wifi状态
	private static String cmd_get_status = "wpa_cli -i wlan0 status\n";

	private static boolean excue1 = true;

	private static WifiDataChageListener mWifiDataChageListener;
	private AddNetworkListener mAddNetworkListener;
	private ConnectListener mConnectListener;
	private ConnnectStatusListener mConnnectStatusListener;

	private ProcessUtils mProcessUtils;

	public WifiUtils() {
		mProcessUtils = new ProcessUtils();
	}

	/**
	 * 查找所有wifi
	 * 
	 * @throws Exception
	 */
	public void findAllWifi() throws Exception {
		// 开始扫描wifi
		mProcessUtils.suExecCallback(cmd_scan_wifi, new Callback() {

			@Override
			public void readLine(String line) {
				if (line.toUpperCase().equals("OK")) {
					System.out.println("scan wifi success \n");
					try {
						parseData(); // 解析数据
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("scan wifi failure");
				}
			}
		}, 10);
	}

	/**
	 * 查找addNetwork
	 * 
	 * @return
	 */
	public void findAddNetwork() throws Exception {

		mProcessUtils.suExecCallback(cmd_find_network, new Callback() {
			boolean haveNetWork = false;

			@Override
			public void readLine(String line) {
				System.out.println("my line: " + line);
				if (line.contains("[CURRENT]")) {
					System.out.println("line.substring(0, 1): "
							+ line.substring(0, 1));
					mAddNetworkListener.AddNetworkCallBack(line.substring(0, 1));
					haveNetWork = true;
				}
				if (Appconfig.CMD_FINISH.equals(line.trim()) && !haveNetWork) {
					// addNet();
					System.out.println("need to add network");
				}

			}
		}, 10);
	}

	/**
	 * 增加一个network
	 */
	public void addNet() {
		try {
			mProcessUtils.suExecCallback(cmd_add_network, new Callback() {

				@Override
				public void readLine(String line) {
					if (!Appconfig.CMD_FINISH.equals(line.trim())) {
						mAddNetworkListener.AddNetworkCallBack(line);
					}
				}

			}, 10);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 连接wifi
	 * 
	 * @param network
	 *            addNetwork
	 * @param ssid
	 *            wifi名字
	 * @param password
	 *            密码
	 * @param encry
	 *            加密方式
	 * 
	 * @throws Exception
	 */
	public void connect(final String network, final String ssid,
			final String password, final String encry) throws Exception {

		String setSsid = "wpa_cli -i wlan0 set_network " + network.trim()
				+ " ssid \'\"" + ssid.trim() + "\"\'\n";

		final String connectwifi = "wpa_cli -i wlan0 select_network " + network
				+ "\n";

		System.out.println("==" + setSsid + "==");
		// System.out.println("==" + setpasword + "==");
		System.out.println("==" + connectwifi + "==");
		// System.out.println("setSsid: " + setSsid);
		// System.out.println("setpasword: " + setpasword);
		// System.out.println("connectwifi: " + connectwifi);

		mProcessUtils.suExecCallback(setSsid, new Callback() {
			@Override
			public void readLine(String line) {
				System.out.println("connect wifi result: " + line);
				if (line.toUpperCase().equals("OK")) {
					System.out.println("set ssid success");
					setPassword(encry, network, password, connectwifi);
				} else {
					if (!line.trim().equals(Appconfig.CMD_FINISH)) {
						mConnectListener.connectCallBack(false);
						System.out.println("set ssid Line: " + line);
					}
				}
			}
		}, 10);
	}

	/**
	 * 设置密码
	 * 
	 * @param setpasword
	 *            设置密码语句
	 * @param connectwifi
	 *            连接wifi语句
	 */
	public void setPassword(String encry, final String network,
			final String password, final String connectwifi) {
		try {

			String setpasword = null;
			if (encry.contains("[WPA2-PSK") || encry.contains("[WPA-PSK")) { // PSK加密方式
				setpasword = "wpa_cli -i wlan0 set_network " + network
						+ " psk \'\"" + password.trim() + "\"\'\n";
			} else if (encry.contains("[WEP")) {
				setpasword = "wpa_cli -i wlan0 set_network " + network
						+ " wep_key0 \'\"" + password.trim() + "\"\'\n";
			} else {
				setpasword = "wpa_cli -i wlan0 set_network " + network
						+ " key_mgmt NONE\n";
			}
			System.out.println("==" + setpasword + "==");
			mProcessUtils.suExecCallback(setpasword, new Callback() {
				@Override
				public void readLine(String line) {
					if (line.toUpperCase().equals("OK")) {
						System.out.println("set password success");
						connectWifi(connectwifi);
					} else {
						if (!line.trim().equals(Appconfig.CMD_FINISH)) {
							mConnectListener.connectCallBack(false);
							System.out.println("set password Line: " + line);
						}
					}
				}
			}, 10);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error: " + e.getMessage());
		}
	}

	/**
	 * 连接wifi
	 * 
	 * @param connectwifi
	 *            连接wifi语句
	 */
	public void connectWifi(String connectwifi) {
		try {
			mProcessUtils.suExecCallback(connectwifi, new Callback() {

				@Override
				public void readLine(String line) {
					if (line.toUpperCase().equals("OK")) {
						System.out.println("connect wifi success");
						mConnectListener.connectCallBack(true);
					} else {
						if (!line.trim().equals(Appconfig.CMD_FINISH)) {
							mConnectListener.connectCallBack(false);
							System.out.println("connect wifi Line: " + line);
						}
					}
				}

			}, 10);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error: " + e.getMessage());
		}
	}

	/**
	 * 保存设置信息
	 */
	public void saveConfig() {
		try {
			mProcessUtils.suExecCallback("wpa_cli -i wlan0  save_config",
					new Callback() {

						@Override
						public void readLine(String line) {
							if (line.toUpperCase().equals("OK")) {
								System.out.println("save config success");
								// mConnectListener.connectCallBack(true);
							} else {
								if (!line.trim().equals(Appconfig.CMD_FINISH)) {
									// mConnectListener.connectCallBack(false);
									System.out
											.println("csave config success failure: "
													+ line);
								}
							}
						}

					}, 10);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取wifi状态
	 */
	public void getStatus() {
		try {
			final WifiMessage wifiMessage = new WifiMessage();
			System.out.println("======================================");
			mProcessUtils.suExecCallback(cmd_get_status, new Callback() {
				boolean isConnect = false;

				@Override
				public void readLine(String line) {
					System.out.println("check connect status: " + line);
					if (line.contains("ssid=")) {
						wifiMessage.setSsid(line.substring("ssid=".length())
								.trim());
					} else if (line.contains("key_mgmt=")) {
						wifiMessage.setFlags(line.substring(
								"key_mgmt=".length()).trim());
					} else if (line.contains("ip_address=")) {
						wifiMessage.setIpAddresss(line.substring(
								"ip_address=".length()).trim());
					} else if (line.contains("bssid=")) {
						wifiMessage.setMacAddress(line.substring(
								"bssid=".length()).trim());
					} else if (line.contains("wpa_state=COMPLETED")) {
						isConnect = true;
					} else if (line.trim().equals(Appconfig.CMD_FINISH)) {
						System.out.println("listener isConnect: " + isConnect);
						if (isConnect) {
							mConnnectStatusListener.connectStatus(true,
									wifiMessage);
						} else {
							mConnnectStatusListener.connectStatus(false,
									wifiMessage);
						}
					}
				}
			}, 10);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析数据
	 * 
	 * @throws IOException
	 */
	private void parseData() throws IOException {

		final ArrayList<WifiMessage> listWifiMessages = new ArrayList<>();
		mProcessUtils.suExecCallback(cmd_scan_wifi_result, new Callback() {

			int wifiSum = 0;
			boolean isEnd = false;

			@Override
			public void readLine(String line) {
				System.out.println(line);
				if (Appconfig.CMD_FINISH.equals(line.trim())) {
					mWifiDataChageListener.wifiDataChange(listWifiMessages);
					isEnd = true;
				}
				if (wifiSum > 0 && !isEnd) {
					String wifiData = StringUtils.delectEmpty(line);
					// System.out.println("wifiData: " + wifiData);
					String[] messages = wifiData.split(" ");
					// System.out.println("wifidata length: " +
					// messages.length);
					WifiMessage wifiMessage = new WifiMessage();

					wifiMessage.setMacAddress(messages[0]);
					wifiMessage.setFrequency(messages[1]);
					wifiMessage.setSignalLevel(messages[2].substring(1));
					wifiMessage.setFlags(messages[3]);
					wifiMessage.setSsid(messages[4]);
					wifiMessage.setStatus("未连接");
					listWifiMessages.add(wifiMessage);
				}
				wifiSum++;
			}
		}, 10);
	}

	/**
	 * 监听连接状态
	 * 
	 * @param connnectStatusListener
	 */
	public void setConnectStatusListener(
			ConnnectStatusListener connnectStatusListener) {
		this.mConnnectStatusListener = connnectStatusListener;
	}

	public interface ConnnectStatusListener {
		void connectStatus(boolean isConnect, WifiMessage wifiMessage);
	}

	/**
	 * 监听wifi数据变化
	 * 
	 * @param WifiDataChangeListener
	 */
	public void setAllWifiDataListener(
			WifiDataChageListener WifiDataChangeListener) {
		this.mWifiDataChageListener = WifiDataChangeListener;
	}

	public interface WifiDataChageListener {
		void wifiDataChange(ArrayList<WifiMessage> listWifiMessages);
	}

	/**
	 * 监听Network
	 * 
	 * @param addNetworkListener
	 */
	public void setAddNetworkListener(AddNetworkListener addNetworkListener) {
		this.mAddNetworkListener = addNetworkListener;
	}

	public interface AddNetworkListener {
		void AddNetworkCallBack(String network);
	}

	/**
	 * 监听是否连接成功
	 * 
	 * @param connectListener
	 */
	public void setConnectListener(ConnectListener connectListener) {
		this.mConnectListener = connectListener;
	}

	public interface ConnectListener {
		void connectCallBack(boolean isSuccess);
	}
}
