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
	private static boolean excue1 = true;

	private static WifiDataChageListener mWifiDataChageListener;
	private AddNetworkListener mAddNetworkListener;
	private ConnectListener mConnectListener;

	/**
	 * 查找所有wifi
	 * 
	 * @throws Exception
	 */
	public void findAllWifi() throws Exception {
		// 开始扫描wifi
		ProcessUtils.suExecCallback(cmd_scan_wifi, new Callback() {

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
	 * 连接wifi
	 * 
	 * @param network
	 *            addNetwork
	 * @param ssid
	 *            wifi名字
	 * @param password
	 *            密码
	 * @throws Exception
	 */
	public void connect(final String network, final String ssid,
			final String password) throws Exception {

		String setSsid = "wpa_cli -i wlan0 set_network " + network.trim()
				+ " ssid \'\"" + ssid.trim() + "\"\'\n";

		final String setpasword = "wpa_cli -i wlan0 set_network " + network
				+ " psk \'\"" + password.trim() + "\"\'\n";

		final String connectwifi = "wpa_cli -i wlan0 select_network " + network
				+ "\n";

		System.out.println("==" + setSsid + "==");
		System.out.println("==" + setpasword + "==");
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
					setPassword(setpasword, connectwifi);
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
	public void setPassword(String setpasword, final String connectwifi) {
		try {
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
	 * 连接wifi
	 * 
	 * @param connectwifi
	 *            连接wifi语句
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
	 * 保存设置信息
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
	 * 解析数据
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
					wifiMessage.setStatus(" ");
					listWifiMessages.add(wifiMessage);
				}
				wifiSum++;
			}
		}, 10);
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
