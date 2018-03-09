package wifi;

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
	// ��ȡwifi״̬
	private static String cmd_get_status = "wpa_cli -i wlan0 status\n";

	private static boolean excue1 = true;

	private static WifiDataChageListener mWifiDataChageListener;
	private AddNetworkListener mAddNetworkListener;
	private ConnectListener mConnectListener;
	private ConnnectStatusListener mConnnectStatusListener;

	/**
	 * ��������wifi
	 * 
	 * @throws Exception
	 */
	public void findAllWifi() throws Exception {
		// ��ʼɨ��wifi
		ProcessUtils.suExecCallback(cmd_scan_wifi, new Callback() {

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

		ProcessUtils.suExecCallback(cmd_add_network, new Callback() {

			@Override
			public void readLine(String line) {
				if (!Appconfig.CMD_FINISH.equals(line.trim())) {
					mAddNetworkListener.AddNetworkCallBack(line);
				}
			}
		}, 10);
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

		ProcessUtils.suExecCallback(setSsid, new Callback() {
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
						+ "psk \'\"" + password.trim() + "\"\'\n";
			} else if (encry.contains("[WEP")) {
				setpasword = "wpa_cli -i wlan0 set_network " + network
						+ "wep_key0 \'\"" + password.trim() + "\"\'\n";
			} else {
				setpasword = "wpa_cli -i wlan0 set_network " + network
						+ " key_mgmt NONE\n";
			}
			System.out.println("==" + setpasword + "==");
			ProcessUtils.suExecCallback(setpasword, new Callback() {
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
	 * ����wifi
	 * 
	 * @param connectwifi
	 *            ����wifi���
	 */
	public void connectWifi(String connectwifi) {
		try {
			ProcessUtils.suExecCallback(connectwifi, new Callback() {

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
			ProcessUtils.suExecCallback("wpa_cli -i wlan0  save_config",
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
	 * ��ȡwifi״̬
	 */
	public void getStatus() {
		try {
			final WifiMessage wifiMessage = new WifiMessage();
			ProcessUtils.suExecCallback(cmd_get_status, new Callback() {
				boolean isConnect = false;

				@Override
				public void readLine(String line) {
					System.out
							.println("++++++++++++++++++===================++++++++++++++++");
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
	 * ��������
	 * 
	 * @throws IOException
	 */
	private void parseData() throws IOException {

		final ArrayList<WifiMessage> listWifiMessages = new ArrayList<>();
		ProcessUtils.suExecCallback(cmd_scan_wifi_result, new Callback() {

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
					wifiMessage.setStatus("δ����");
					listWifiMessages.add(wifiMessage);
				}
				wifiSum++;
			}
		}, 10);
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
		void connectStatus(boolean isConnect, WifiMessage wifiMessage);
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
		void AddNetworkCallBack(String network);
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
