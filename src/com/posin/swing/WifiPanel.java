package com.posin.swing;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import view.InputDialog;
import view.InputDialog.OnClickListener;

import com.posin.Jlist.FriListCellRenderer;
import com.posin.Jlist.MyDefaultListModel;
import com.posin.constant.WifiMessage;
import com.posin.global.Appconfig;
import com.posin.utils.StringUtils;
import com.posin.wifi.WifiUtils;
import com.posin.wifi.WifiUtils.AddNetworkListener;
import com.posin.wifi.WifiUtils.ConnectListener;
import com.posin.wifi.WifiUtils.ConnnectStatusListener;
import com.posin.wifi.WifiUtils.WifiDataChageListener;

/**
 * wifi设置
 * 
 * @author Greetty
 * 
 */
public class WifiPanel {

	Icon icon1 = null;
	Icon icon2 = null;
	Icon icon3 = null;
	Icon icon4 = null;
	Icon icon5 = null;

	// 图片数组
	Icon[] icons = null;

	public JPanel wifiPanel = null; // 根布局
	private JPanel listWifiPane = null; // WIFI列表
	// private JList<JPanel> wifiJList = null;
	private JList wifiJList = null;
	private ArrayList<WifiMessage> listWifiDatas = null; // WIFI数据聚合


	private WifiUtils wifiUtils = null;
	private MyDefaultListModel listModel = null;
	private boolean operation = false;

	private static final WifiPanel WIFI_PANEL_INSTANCE = new WifiPanel();

	public static WifiPanel getInstance() {
		return WIFI_PANEL_INSTANCE;
	}

	private WifiPanel() {
		wifiPanel = new JPanel();
		wifiUtils = new WifiUtils();
		wifiPanel.setLayout(new BorderLayout());

		initIcon();  //初始化图标
		initTopSwitchPanel(wifiPanel); // 顶部WiFi开关
		initListWifiPanel(wifiPanel); // WiFi列表
//		refreshWifiList();  //刷新WiFi列表
	}

	/**
	 * 初始化图标
	 */
	private void initIcon() {

		System.out.println("icon npath: "
				+ WifiPanel.class.getResource("/image/wifi_5.png"));
		icon1 = new ImageIcon(WifiPanel.class.getResource("/image/wifi_5.png"));
		icon2 = new ImageIcon(WifiPanel.class.getResource("/image/wifi_4.png"));
		icon3 = new ImageIcon(WifiPanel.class.getResource("/image/wifi_3.png"));
		icon4 = new ImageIcon(WifiPanel.class.getResource("/image/wifi_2.png"));
		icon5 = new ImageIcon(WifiPanel.class.getResource("/image/wifi_1.png"));

		icons = new Icon[] { icon1, icon2, icon3, icon4, icon5 };
	}

	/**
	 * 刷新WIFI列表
	 */
	private void refreshWifiList() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						
						Thread.sleep(Appconfig.REFRESH_WIFI_TIME);  //休眠时间
						
						if (!operation && wifiPanel.isShowing()) {
							initWifiList();
							System.out
									.println("++++ refresh wifi list +++++++");
						} else {
							System.out.println("in operation ,after refresh");
						}
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("Error: " + e.getMessage());
					}
				}
			}
		}).start();
	}

	/**
	 * 获取WIFI信息
	 */
	private void initWifiList() {
		try {
			wifiUtils.findAllWifi();
			wifiUtils.setAllWifiDataListener(new WifiDataChageListener() {

				@Override
				public void wifiDataChange(
						ArrayList<WifiMessage> listWifiMessages) {
					listWifiDatas.clear();
					// System.out.println("+++++++++++++++++++++++++++");
					for (int i = 0; i < listWifiMessages.size(); i++) {
						// System.out.println("listWifiMessages data : "
						// + listWifiMessages.get(i).toString() + "\n");
						listWifiDatas.add(listWifiMessages.get(i));
					}
					if (listWifiMessages.size() > 0) {
						System.out.println("listWifiMessages size : "
								+ listWifiMessages.size());
						// wifiJList.setListData(listWifiMessages.toArray());
						checkConnectStatus();
						// wifiJList.setListData(listWifiDatas.toArray());
					} else {
						System.out.println("No search for WiFi ");
					}
				}
			});

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 检查连接状态
	 */
	public void checkConnectStatus() {
		wifiUtils.getStatus();
		wifiUtils.setConnectStatusListener(new ConnnectStatusListener() {

			@Override
			public void connectStatus(boolean isConnect, WifiMessage wifiMessage) {
				if (isConnect && wifiMessage != null) {
					String ssid = wifiMessage.getSsid();
					System.out.println("wifi name: ====" + ssid + "===");
					for (int i = 0; i < listWifiDatas.size(); i++) {
						if (listWifiDatas.get(i).getSsid().equals(StringUtils.parseWifiName(ssid))) {
							System.out.println("-------------- has connect "
									+ ssid + "--------------");
							listWifiDatas.get(i).setStatus("已连接");
						} else {
							listWifiDatas.get(i).setStatus("未连接");
						}
					}
				} else {
					System.out.println("no wifi connect this devices");
					for (int i = 0; i < listWifiDatas.size(); i++) {
						listWifiDatas.get(i).setStatus("未连接");
					}
				}
				Collections.sort(listWifiDatas);
				wifiJList.setListData(listWifiDatas.toArray());
			}
		});
	}

	/**
	 * WIFI列表
	 * 
	 * @param parentPanel
	 *            父容器
	 */
	private void initListWifiPanel(JPanel parentPanel) {
		listWifiPane = new JPanel();
		parentPanel.add(listWifiPane, BorderLayout.CENTER);
		listWifiDatas = new ArrayList<>();

		listModel = new MyDefaultListModel(new ArrayList<WifiMessage>());
		wifiJList = new JList(listModel);
		wifiJList.setCellRenderer(new FriListCellRenderer(icons));
		// 设置单一选择模式（每次只能有一个元素被选中）
		wifiJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		wifiJList.setFont(new Font(Font.SERIF, Font.PLAIN, 25));

		// 添加滚动条
		JScrollPane jp = new JScrollPane(wifiJList);
		jp.setPreferredSize(new Dimension(1920, 850));
		System.out.println("850");
		listWifiPane.add(jp,BorderLayout.NORTH);
		
		for (int i = 1; i <= 20; i++) {
			WifiMessage wifiMessage = new WifiMessage();

			wifiMessage.setFlags("123");
			wifiMessage.setFrequency("1354");
			wifiMessage.setIpAddresss("1325454");
			wifiMessage.setMacAddress("154787");
			wifiMessage.setSignalLevel(10);
			wifiMessage.setSsid("posin" + i);
			wifiMessage.setStatus("未连接");
			listWifiDatas.add(wifiMessage);
		}
		wifiJList.setListData(listWifiDatas.toArray());
		
		
		
		
//		initWifiList(); // 刷新数据
		
		wifiJList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("##########+++++++++++++++#######");
				int selectedPosition = wifiJList.getSelectedIndex();
				System.out.println("select index： " + selectedPosition);

				if (selectedPosition >= 0) {
					System.out
							.println("wifi name : "
									+ listWifiDatas.get(selectedPosition)
											.getSsid());
					operation = true;
					getNetWork(selectedPosition);
				} else {
					System.out.println("select index < 0");
				}
			}
		});
	}

	/**
	 * 获取network
	 * 
	 * @param position
	 *            选择哪个wifi
	 */
	public void getNetWork(final int position) {
		final InputDialog dialog = new InputDialog("请输入密码");
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);

		dialog.setOnComfirmclikListener(new OnClickListener() {

			@Override
			public void onClick(boolean isOk, ActionEvent event,
					final String password) {
				dialog.dispose();
				operation = false;
				wifiJList.setSelectedIndex(-1);
				if (!isOk) {
					return;
				} else {
					if (password == null || password.trim().equals("")) {
						operation = true;
						JOptionPane.showMessageDialog(null, "密码不能为空，请输入密码");
						operation = false;
						return;
					} else {
						findOrAddnetWork(password, position);
					}
				}
			}
		});

	}

	/**
	 * find network
	 * 
	 * @param password
	 * @param position
	 */
	public void findOrAddnetWork(final String password, final int position) {
		try {
			System.out.println("password: " + password);
			final String ssid = listWifiDatas.get(position).getSsid();
			wifiUtils.findAddNetwork();
			wifiUtils.setAddNetworkListener(new AddNetworkListener() {

				@Override
				public void AddNetworkCallBack(boolean disableNetwork,
						String network) {
					System.out.println("network: " + network);
					connnectWifi(disableNetwork, position, network, ssid,
							password);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 连接wifi
	 * 
	 * @param index
	 *            选中位置
	 * @param network
	 *            addNetwork
	 * @param ssid
	 *            wifi名字
	 * @param password
	 *            密码
	 */
	public void connnectWifi(boolean disableNetwork, final int index,
			String network, String ssid, String password) {
		try {
			if (disableNetwork) {
				System.out
						.println("+++++++++++++++++++++++++++++++++++++++++++++++");
				wifiUtils.setNetworkAble(network, false);
				System.out
						.println("+++++++++++++++++++++++++++++++++++++++++++++++");
				Thread.sleep(20);
			}
			wifiUtils.connect(network, ssid, password, listWifiDatas.get(index)
					.getFlags());
			wifiUtils.setConnectListener(new ConnectListener() {

				@Override
				public void connectCallBack(boolean isSuccess) {
					try {
						System.out.println("start wait for");
						Thread.sleep(1500);
						System.out.println("end wait for");
						wifiUtils.saveConfig();
						System.out.println("save wifi config ... ");
						checkConnectStatus();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 顶部WIFI开发
	 * 
	 * @param parentPanel
	 *            父容器
	 */
	private void initTopSwitchPanel(JPanel parentPanel) {

		final JLabel wifiSwitchStatus = new JLabel("Wifi已开启",JLabel.CENTER);
		wifiSwitchStatus.setPreferredSize(new Dimension(1920, 70));
		wifiSwitchStatus.setFont(new Font("楷体", Font.PLAIN, 25));
		wifiSwitchStatus.setOpaque(true);
		wifiSwitchStatus.setBackground(new Color(77, 111, 113));
		wifiSwitchStatus.setForeground(Color.WHITE);
		parentPanel.add(wifiSwitchStatus, BorderLayout.NORTH);
	}

	/**
	 * 添加横线
	 * 
	 * @param fatherJpanel
	 *            父布局
	 * @param gridx
	 *            X轴位置
	 * @param gridy
	 *            Y轴位置
	 * @param ipady
	 *            Y轴内撑大值（Android上的padding）
	 * @param color
	 */
	public void addLine(JPanel fatherJpanel, int gridx, int gridy, int ipady,
			Color color) {
		JPanel linePanel = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = gridx;
		c.gridy = gridy;
		c.ipady = ipady;
		linePanel.setBackground(color);
		fatherJpanel.add(linePanel, c);
	}
}
