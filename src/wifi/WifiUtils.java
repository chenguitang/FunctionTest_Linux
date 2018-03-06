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
	private static boolean excue1 = true;

	private static WifiDataChageListener mWifiDataChageListener;

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
	 * ��������
	 * 
	 * @throws IOException
	 */
	private void parseData() throws IOException {

		final ArrayList<WifiMessage> listWifiMessages = new ArrayList<>();
		ProcessUtils.suExecCallback(cmd_scan_wifi_result, new Callback() {

			int wifiSum = 0;

			@Override
			public void readLine(String line) {
				System.out.println(line);
				if (wifiSum > 0) {
					String wifiData = StringUtils.delectEmpty(line);
//					System.out.println("wifiData: " + wifiData);
					String[] messages = wifiData.split(" ");
//					System.out.println("wifidata length: " + messages.length);
					WifiMessage wifiMessage = new WifiMessage();

					wifiMessage.setMacAddress(messages[0]);
					wifiMessage.setFrequency(messages[1]);
					wifiMessage.setSignalLevel(messages[2]);
					wifiMessage.setFlags(messages[3]);
					wifiMessage.setSsid(messages[4]);
					listWifiMessages.add(wifiMessage);
				}
				mWifiDataChageListener.wifiDataChange(listWifiMessages);
				wifiSum++;
			}
		}, 10);
		
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
}
