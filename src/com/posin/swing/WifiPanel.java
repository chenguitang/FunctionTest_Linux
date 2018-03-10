package com.posin.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
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
import com.posin.wifi.WifiUtils;
import com.posin.wifi.WifiUtils.AddNetworkListener;
import com.posin.wifi.WifiUtils.ConnectListener;
import com.posin.wifi.WifiUtils.ConnnectStatusListener;
import com.posin.wifi.WifiUtils.WifiDataChageListener;

public class WifiPanel {

	// Icon icon1 = new ImageIcon("E:\\nfs\\ic_wifi_lock_signal_4_teal.png");
	// Icon icon2 = new ImageIcon("E:\\nfs\\ic_wifi_lock_signal_3_teal.png");
	// Icon icon3 = new ImageIcon("E:\\nfs\\ic_wifi_signal_2_teal.png");
	// Icon icon4 = new ImageIcon("E:\\nfs\\ic_wifi_signal_1_teal.png");
	// Icon icon5 = new ImageIcon("E:\\nfs\\ic_wifi_signal_1_light.png");

	Icon icon1 = new ImageIcon("/mnt/nfs/ic_wifi_lock_signal_4_teal.png");
	Icon icon2 = new ImageIcon("/mnt/nfs/ic_wifi_lock_signal_3_teal.png");
	Icon icon3 = new ImageIcon("/mnt/nfs/ic_wifi_signal_2_teal.png");
	Icon icon4 = new ImageIcon("/mnt/nfs/ic_wifi_signal_1_teal.png");
	Icon icon5 = new ImageIcon("/mnt/nfs/ic_wifi_signal_1_light.png");

	// 图片数组
	Icon[] icons = { icon1, icon2, icon3, icon4, icon5 };

	static JPanel wifiPanel = null; // 根布局
	private JPanel listWifiPane = null; // wifi列表
	// private JList<JPanel> wifiJList = null;
	private JList wifiJList = null;
	private ArrayList<WifiMessage> listWifiDatas = null; // wifi数据聚合

	private boolean wifiIsOpen = true;
	private ImageIcon image3 = null;

	private WifiUtils wifiUtils = null;
	private MyDefaultListModel listModel = null;
	private boolean testBoo = false;
	private boolean operation = false;
	private JFrame mFrame;

	public WifiPanel(JFrame frame) {
		wifiPanel = new JPanel();
		wifiUtils = new WifiUtils();
		wifiPanel.setLayout(new BorderLayout());
		Font f = new Font("隶书", Font.PLAIN, 25);
		// addLine(wifiPanel, 0, 0, -8, Color.GRAY);

		this.mFrame = frame;
		initTopSwitchPanel(wifiPanel); // 顶部wifi开关
		// initWifiList(); // 获取wifi信息
		initListWifiPanel(wifiPanel); // wifi列表
		//
		refreshWifiList();
	}

	/**
	 * 刷新wifi列表
	 */
	private void refreshWifiList() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(8000);
						// System.out.println(" wifiPanel.isShowing(): "+
						// wifiPanel.isShowing());
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
						if (listWifiDatas.get(i).getSsid().equals(ssid)) {
							System.out.println("-------------- has connect "
									+ ssid + "--------------");
							listWifiDatas.get(i).setStatus("已连接");
						} else {
							listWifiDatas.get(i).setStatus("未连接");
							// System.out.println("++listWifiDatas size: "
							// + listWifiDatas.size() + " wifi name:=="
							// + listWifiDatas.get(i).getSsid() + "==");
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
	 * wifi列表
	 * 
	 * @param parentPanel
	 *            父容器
	 */
	private void initListWifiPanel(JPanel parentPanel) {
		listWifiPane = new JPanel();
		parentPanel.add(listWifiPane, BorderLayout.CENTER);
		listWifiPane.setBackground(Color.RED);

		listWifiDatas = new ArrayList<>();

		listModel = new MyDefaultListModel(new ArrayList<WifiMessage>());
		wifiJList = new JList(listModel);
		// wifiJList.setListData(listWifiDatas.toArray());
		wifiJList.setCellRenderer(new FriListCellRenderer(icons));
		// 设置单一选择模式（每次只能有一个元素被选中）
		wifiJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		wifiJList.setFont(new Font(Font.SERIF, Font.PLAIN, 25));

		// wifiJList.setSelectedIndex(1);
		// 添加滚动条
		JScrollPane jp = new JScrollPane(wifiJList);
		jp.setPreferredSize(new Dimension(1920, 600));
		wifiPanel.add(jp);
		initWifiList(); // 刷新数据
		// wifiJList.removel
		// 首次进入APP，判断连接状态

		wifiJList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent event) {

				// 鼠标点击释放
				if (!wifiJList.getValueIsAdjusting()) {
					// System.out.println("you release this");

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
				} else {
					// System.out.println("you click this");
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
			public void onClick(ActionEvent event, final String password) {
				dialog.dispose();
				operation = false;
				wifiJList.setSelectedIndex(-1);
				if (password == null || password.trim().equals("")) {
					operation = true;
					JOptionPane.showMessageDialog(null, "密码不能为空，请输入密码");
					operation = false;
					return;
				} else {
					findOrAddnetWork(password, position);
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
				public void AddNetworkCallBack(String network) {
					System.out.println("network: " + network);
					connnectWifi(position, network, ssid, password);
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
	public void connnectWifi(final int index, String network, String ssid,
			String password) {
		try {
			wifiUtils.connect(network, ssid, password, listWifiDatas.get(index)
					.getFlags());
			wifiUtils.setConnectListener(new ConnectListener() {

				@Override
				public void connectCallBack(boolean isSuccess) {
					try {
						System.out.println("start wait for");
						Thread.sleep(1500);
						System.out.println("end wait for");
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
	 * 顶部wifi开发
	 * 
	 * @param parentPanel
	 *            父容器
	 */
	private void initTopSwitchPanel(JPanel parentPanel) {

		final JButton wifiSwitchStatusJLabel = new JButton("Wifi已开启，点击刷新wifi");
		wifiSwitchStatusJLabel.setPreferredSize(new Dimension(1920, 70));
		wifiSwitchStatusJLabel.setFont(new Font("楷体", Font.PLAIN, 25));
		wifiSwitchStatusJLabel.setFocusPainted(false); // 取消聚焦
		parentPanel.add(wifiSwitchStatusJLabel, BorderLayout.NORTH);

		changeWifiSwitchStatus(wifiSwitchStatusJLabel);

		// wifiSwitchStatusJLabel.setText("Wifi已关闭，点击开启wifi");
		// 开启or关闭wifi
		wifiSwitchStatusJLabel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// wifiIsOpen = !wifiIsOpen;
				// changeWifiSwitchStatus(wifiSwitchStatusJLabel);
				initWifiList();
			}
		});

	}

	/**
	 * 改变WIFI开关状态
	 * 
	 * @param wifiSwitchStatusJLabel
	 */
	public void changeWifiSwitchStatus(JButton wifiSwitchStatusJLabel) {
		if (!wifiIsOpen) {
			System.out.println("wifiIsOpen: " + wifiIsOpen);
			wifiSwitchStatusJLabel.setText("Wifi已开启，点击刷新wifi");
			// wifiSwitchStatusJLabel.setBackground(new Color(119, 119, 136));
			wifiSwitchStatusJLabel.setBackground(new Color(77, 111, 113));
			wifiSwitchStatusJLabel.setForeground(Color.WHITE);
		} else {
			System.out.println("wifiIsOpen: " + wifiIsOpen);
			wifiSwitchStatusJLabel.setText("Wifi已开启，点击刷新wifi");
			wifiSwitchStatusJLabel.setBackground(new Color(77, 111, 113));
			wifiSwitchStatusJLabel.setForeground(Color.WHITE);
		}
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
