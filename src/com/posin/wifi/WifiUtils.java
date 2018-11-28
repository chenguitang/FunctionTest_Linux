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
 * Wifi��������
 * 
 * @author Greetty
 * 
 */
public class WifiUtils {

	// ɨ��wifi
	private static String cmd_scan_wifi = "wpa_cli -i wlan0 scan\n";
	// wifi�б�
	private static String cmd_scan_wifi_result = "wpa_cli -i wlan0 scan_result\n";
	// add_network
	private static String cmd_add_network = "wpa_cli -i wlan0 add_network\n";
	// ����network
	private static String cmd_find_network = "wpa_cli -i wlan0 list_network\n";
	// ��ȡwifi״̬
	private static String cmd_get_status = "wpa_cli -i wlan0 status\n";

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
	 * ��������wifi
	 * 
	 * @throws Exception
	 */
	public void findAllWifi() throws Exception {
		// ��ʼɨ��wifi
		mProcessUtils.suExecCallback(cmd_scan_wifi, new Callback() {

			@Override
			public void readLine(String line) {
				if (line.toUpperCase().equals("OK")) {
					System.out.println("scan wifi success \n");
					try {
						parseData(); // ��������
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
	 * ����addNetwork
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
	 * ����һ��network
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
	 * ����wifi
	 * 
	 * @param network
	 *            addNetwork
	 * @param ssid
	 *            wifi����
	 * @param password
	 *            ����
	 * @param encry
	 *            ���ܷ�ʽ
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
	 * ��������
	 * 
	 * @param setpasword
	 *            �����������
	 * @param connectwifi
	 *            ����wifi���
	 */
	public void setPassword(String encry, final String network,
			final String password, final String connectwifi) {
		try {

			String setpasword = null;
			if (encry.contains("[WPA2-PSK") || encry.contains("[WPA-PSK")) { // PSK���ܷ�ʽ
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
	 * ����wifi
	 * 
	 * @param connectwifi
	 *            ����wifi���
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
	 * ����������Ϣ
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

			// �����ļ������ǵ��ļ���
			// mProcessUtils
			// .createSuProcess(" busybox  cp /etc/wpa_supplicant/wpa_supplicant-wlan0.conf "
			// + "/etc/wpa_supplicant.conf");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡwifi״̬
	 */
	public void getStatus() {
		try {

			mProcessUtils.suExecCallback(cmd_get_status, new Callback() {

				@Override
				public void readLine(String line) {
					// System.out.println("check connect status: " + line);
					if (line.contains("ssid=")) {
						ssId = line.substring("ssid=".length()).trim();
					} else if (line.contains("wpa_state=COMPLETED")) {
						mConnnectStatusListener.connectSuccess(ssId);
						System.out.println("wifi completed ...");
					} else if (line.contains("wpa_state=4WAY_HANDSHAKE")) {
						System.out.println("wifi 4way_handshake ...");
						mConnnectStatusListener.onConnection(ssId);
					} else if (line.contains("wpa_state=SCANNING")) {

						if (ssId != null || ssId.equals("")) {
							initSaveSsId();
						}

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
	 * ��ȡ�����SsId
	 */
	private void initSaveSsId() {

		ArrayList<String> wifiConfigList = new ArrayList<>();
		try {
			Proc.suExec("cat /etc/wpa_supplicant/wpa_supplicant-wlan0.conf ",
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
			System.out.println("��ȡwifi�����ļ������ˣ� " + e.toString());
			e.printStackTrace();
		}

	}

	/**
	 * ��������
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

					System.out.println("messages: " + messages.length);
					if (messages.length == 5) {
						WifiMessage wifiMessage = new WifiMessage();

						wifiMessage.setMacAddress(messages[0]);
						wifiMessage.setFrequency(messages[1]);
						wifiMessage.setSignalLevel(Integer.parseInt(messages[2]
								.substring(1)));
						wifiMessage.setFlags(messages[3]);
						wifiMessage.setSsid(StringUtils
								.parseWifiName(messages[4]));
						wifiMessage.setStatus("δ����");
						if (StringUtils.isChineseName(messages[4])) {
							byte[] chineseWifiNametoBye = StringUtils
									.ChineseWifiNametoBye(messages[4]);
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
	 * ����enable network or disable network
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
	 * ��������״̬
	 * 
	 * @param connnectStatusListener
	 */
	public void setConnectStatusListener(
			ConnnectStatusListener connnectStatusListener) {
		this.mConnnectStatusListener = connnectStatusListener;
	}

	public interface ConnnectStatusListener {
		/**
		 * ���ӳɹ�
		 * 
		 * @param ssId
		 *            WIFI����
		 */
		void connectSuccess(String ssId);

		/**
		 * ��������
		 * 
		 * @param ssId
		 *            WIFI����
		 */
		void onConnection(String ssId);

		/**
		 * ����ʧ��
		 * 
		 * @param ssId
		 *            WIFI����
		 */
		void connectFailure(String ssId);

		/**
		 * ˢ��UI�б�
		 */
		void connectRefresh();
		// void connectStatus(boolean isConnect, WifiMessage wifiMessage);
	}

	/**
	 * ����wifi���ݱ仯
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
	 * ����Network
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
	 * �����Ƿ����ӳɹ�
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
