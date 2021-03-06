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
import com.posin.utils.DecodeUtils;
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

	private static String cmd_disconnect_wifi = "wpa_cli -i wlan0 disconnect\n";

	private static boolean excue1 = true;

	private static WifiDataChageListener mWifiDataChageListener;
	private AddNetworkListener mAddNetworkListener;
	private ConnectListener mConnectListener;
	private ConnnectStatusListener mConnnectStatusListener;

	private ProcessUtils mProcessUtils;
	private String ssId = "";

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
					mAddNetworkListener.AddNetworkCallBack(true,
							line.substring(0, 1));
					haveNetWork = true;
				}
				if (Appconfig.CMD_FINISH.equals(line.trim()) && !haveNetWork) {
					addNet();
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
						mAddNetworkListener.AddNetworkCallBack(false, line);
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
			final String password, final String encry, final boolean isUtf8)
			throws Exception {

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

		mProcessUtils.suExecCallbackByCode(setSsid, isUtf8 ? "UTF-8" : "GBK",
				new Callback() {
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
			} else if (encry.contains("[WEP") && !encry.contains("WPA")) {
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
						connectWifi(connectwifi, network);
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
	public void connectWifi(String connectwifi, String network) {
		try {

			setNetworkAble(network, true);
			Thread.sleep(20);

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
			mProcessUtils.suExecCallback("wpa_cli -i wlan0  save_config \n",
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
											.println("save config success failure: "
													+ line);
								}
							}
						}

					}, 10);

			// 复制文件到覆盖的文件中
			// mProcessUtils
			// .createSuProcess(" busybox  cp /etc/wpa_supplicant/wpa_supplicant-wlan0.conf "
			// + "/etc/wpa_supplicant.conf");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取wifi状态
	 */
	public void getStatus() {
		try {

			mProcessUtils.suExecCallback(cmd_get_status, new Callback() {

				@Override
				public void readLine(String line) {

//					if (isDisconnect == -1) {
//						ArrayList<String> listWifiConfig = new ArrayList<String>();
//						try {
//							Proc.suExec("cat /etc/wifiConfig.txt",
//									listWifiConfig, 2000);
//							if (listWifiConfig.size() > 0) {
//								isDisconnect = Integer.parseInt(listWifiConfig
//										.get(0));
//							}
//
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}

					System.out.println("check connect status: " + line);
					if (line.contains("ssid=")) {
						ssId = line.substring("ssid=".length()).trim();
					} else if (line.contains("wpa_state=COMPLETED")) {
						mConnnectStatusListener.connectSuccess(ssId);
						System.out.println("wifi completed ...");
					} else if (line.contains("wpa_state=4WAY_HANDSHAKE")) {
						System.out.println("wifi 4way_handshake ...");
						mConnnectStatusListener.onConnection(ssId);
					} else if (line.contains("wpa_state=DISCONNECTED")) {
						initSaveSsId();
						System.out.println("wifi disconnected ...");
						mConnnectStatusListener.disconnect(ssId);
					} else if (line.contains("wpa_state=SCANNING")) {

						initSaveSsId();
						if (ssId != null && !ssId.equals("")) {
							System.out
									.println("wifi scanning , connect failure ...");
							mConnnectStatusListener.connectFailure(ssId);
						} else {
							System.out
									.println("wifi scanning , connect refresh ...");
							mConnnectStatusListener.connectRefresh();
						}
					}
				}
			}, 10);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 断开当前WIFI
	 */
	public void disconnectWifi() {
		try {
			Proc.suExec(cmd_disconnect_wifi, null, 2000);
			// 保存配置
			Proc.suExec(" echo 1 > /etc/wifiConfig.txt", null, 1000);
//			isDisconnect = 1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 读取保存的SsId
	 */
	private void initSaveSsId() {

		if (ssId == null || ssId.equals("")) {

			ArrayList<String> wifiConfigList = new ArrayList<>();
			try {
				Proc.suExec(
						"cat /etc/wpa_supplicant/wpa_supplicant-wlan0.conf ",
						wifiConfigList, 2000);

				for (int i = 0; i < wifiConfigList.size(); i++) {
					if (wifiConfigList.get(i).trim().contains("ssid=")
							&& i <= wifiConfigList.size() - 2
							&& wifiConfigList.get(i + 2).trim().equals("}")) {

						ssId = wifiConfigList.get(i).trim();
						ssId = ssId.substring(ssId.indexOf("\"") + 1,
								ssId.length() - 1);
						System.out.println("read config ssid" + ssId);
					}

				}

			} catch (IOException e) {
				System.out.println("读取wifi配置文件出错了： " + e.toString());
				e.printStackTrace();
			}
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
					// String wifiData = StringUtils.delectEmpty(line);
					// System.out.println("wifiData: " + wifiData);
					// String[] messages = wifiData.split("   ");

					String bssid = line.substring(0, 17);
					String frequency = line.substring(17, line.indexOf("-"))
							.trim().replace(" ", "");
					String level = line
							.substring(line.indexOf("-") + 1, line.indexOf("["))
							.trim().replace(" ", "");
					String flags = line
							.substring(line.indexOf("["), line.lastIndexOf("]"))
							.trim().replace(" ", "");
					String ssid = line.substring(line.lastIndexOf("]") + 1,
							line.length()).trim();

					if (!StringUtils.isEmpty(bssid)
							&& !StringUtils.isEmpty(frequency)
							&& !StringUtils.isEmpty(level)
							&& !StringUtils.isEmpty(flags)
							&& !StringUtils.isEmpty(ssid)) {
						WifiMessage wifiMessage = new WifiMessage();
						wifiMessage.setMacAddress(bssid);
						wifiMessage.setFrequency(frequency);
						wifiMessage.setSignalLevel(Integer.parseInt(level));
						wifiMessage.setFlags(flags);
						wifiMessage.setSsid(StringUtils.parseWifiName(ssid));

						wifiMessage.setStatus("未连接");
						if (StringUtils.isChineseName(ssid)) {
							byte[] chineseWifiNametoBye = StringUtils
									.ChineseWifiNametoBye(ssid);
							wifiMessage.setUtf8(DecodeUtils
									.isUTF8(chineseWifiNametoBye));
						} else {
							wifiMessage.setUtf8(true);
						}
						listWifiMessages.add(wifiMessage);
					}
				}
				wifiSum++;
			}
		}, 10);
	}

	/**
	 * 设置enable network or disable network
	 */
	public void setNetworkAble(final String network, final boolean isEnable) {
		String cmd = "";
		System.out.println("----------------------------------------");
		System.out.println("----------------------------------------");
		System.out.println("isEnable: " + isEnable);
		System.out.println("----------------------------------------");
		System.out.println("----------------------------------------");
		if (isEnable) {
			cmd = "wpa_cli -i wlan0 enable " + network;
		} else {
			cmd = "wpa_cli -i wlan0 disable " + network;
		}

		try {
			mProcessUtils.suExecCallback(cmd, new Callback() {
				@Override
				public void readLine(String line) {
					if (line.toUpperCase().equals("OK")) {
						System.out.println(isEnable ? "pa_cli -i wlan0 enable "
								: "wpa_cli -i wlan0 disable " + network);
					}
				}
			}, 10);
		} catch (IOException e) {
			e.printStackTrace();
		}

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
		/**
		 * 连接成功
		 * 
		 * @param ssId
		 *            WIFI名称
		 */
		void connectSuccess(String ssId);

		/**
		 * 正在连接
		 * 
		 * @param ssId
		 *            WIFI名称
		 */
		void onConnection(String ssId);

		/**
		 * 连接失败
		 * 
		 * @param ssId
		 *            WIFI名称
		 */
		void connectFailure(String ssId);

		/**
		 * 已断开连接
		 * 
		 * @param ssId
		 *            WIFI名称
		 */
		void disconnect(String ssId);

		/**
		 * 刷新UI列表
		 */
		void connectRefresh();
		// void connectStatus(boolean isConnect, WifiMessage wifiMessage);
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
		void AddNetworkCallBack(boolean disableNetwork, String network);
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
