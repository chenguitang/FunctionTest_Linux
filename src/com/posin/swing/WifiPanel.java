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

	// ͼƬ����
	Icon[] icons = { icon1, icon2, icon3, icon4, icon5 };

	static JPanel wifiPanel = null; // ������
	private JPanel listWifiPane = null; // wifi�б�
	// private JList<JPanel> wifiJList = null;
	private JList wifiJList = null;
	private ArrayList<WifiMessage> listWifiDatas = null; // wifi���ݾۺ�

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
		Font f = new Font("����", Font.PLAIN, 25);
		// addLine(wifiPanel, 0, 0, -8, Color.GRAY);

		this.mFrame = frame;
		initTopSwitchPanel(wifiPanel); // ����wifi����
		// initWifiList(); // ��ȡwifi��Ϣ
		initListWifiPanel(wifiPanel); // wifi�б�
		//
		refreshWifiList();
	}

	/**
	 * ˢ��wifi�б�
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
	 * ��ȡWIFI��Ϣ
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
	 * �������״̬
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
							listWifiDatas.get(i).setStatus("������");
						} else {
							listWifiDatas.get(i).setStatus("δ����");
							// System.out.println("++listWifiDatas size: "
							// + listWifiDatas.size() + " wifi name:=="
							// + listWifiDatas.get(i).getSsid() + "==");
						}
					}
				} else {
					System.out.println("no wifi connect this devices");
					for (int i = 0; i < listWifiDatas.size(); i++) {
						listWifiDatas.get(i).setStatus("δ����");
					}
				}
				Collections.sort(listWifiDatas);
				wifiJList.setListData(listWifiDatas.toArray());
			}
		});
	}

	/**
	 * wifi�б�
	 * 
	 * @param parentPanel
	 *            ������
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
		// ���õ�һѡ��ģʽ��ÿ��ֻ����һ��Ԫ�ر�ѡ�У�
		wifiJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		wifiJList.setFont(new Font(Font.SERIF, Font.PLAIN, 25));

		// wifiJList.setSelectedIndex(1);
		// ��ӹ�����
		JScrollPane jp = new JScrollPane(wifiJList);
		jp.setPreferredSize(new Dimension(1920, 600));
		wifiPanel.add(jp);
		initWifiList(); // ˢ������
		// wifiJList.removel
		// �״ν���APP���ж�����״̬

		wifiJList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent event) {

				// ������ͷ�
				if (!wifiJList.getValueIsAdjusting()) {
					// System.out.println("you release this");

					System.out.println("##########+++++++++++++++#######");
					int selectedPosition = wifiJList.getSelectedIndex();
					System.out.println("select index�� " + selectedPosition);

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
	 * ��ȡnetwork
	 * 
	 * @param position
	 *            ѡ���ĸ�wifi
	 */
	public void getNetWork(final int position) {
		final InputDialog dialog = new InputDialog("����������");
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
					JOptionPane.showMessageDialog(null, "���벻��Ϊ�գ�����������");
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
	 * ����wifi
	 * 
	 * @param index
	 *            ѡ��λ��
	 * @param network
	 *            addNetwork
	 * @param ssid
	 *            wifi����
	 * @param password
	 *            ����
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
	 * ����wifi����
	 * 
	 * @param parentPanel
	 *            ������
	 */
	private void initTopSwitchPanel(JPanel parentPanel) {

		final JButton wifiSwitchStatusJLabel = new JButton("Wifi�ѿ��������ˢ��wifi");
		wifiSwitchStatusJLabel.setPreferredSize(new Dimension(1920, 70));
		wifiSwitchStatusJLabel.setFont(new Font("����", Font.PLAIN, 25));
		wifiSwitchStatusJLabel.setFocusPainted(false); // ȡ���۽�
		parentPanel.add(wifiSwitchStatusJLabel, BorderLayout.NORTH);

		changeWifiSwitchStatus(wifiSwitchStatusJLabel);

		// wifiSwitchStatusJLabel.setText("Wifi�ѹرգ��������wifi");
		// ����or�ر�wifi
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
	 * �ı�WIFI����״̬
	 * 
	 * @param wifiSwitchStatusJLabel
	 */
	public void changeWifiSwitchStatus(JButton wifiSwitchStatusJLabel) {
		if (!wifiIsOpen) {
			System.out.println("wifiIsOpen: " + wifiIsOpen);
			wifiSwitchStatusJLabel.setText("Wifi�ѿ��������ˢ��wifi");
			// wifiSwitchStatusJLabel.setBackground(new Color(119, 119, 136));
			wifiSwitchStatusJLabel.setBackground(new Color(77, 111, 113));
			wifiSwitchStatusJLabel.setForeground(Color.WHITE);
		} else {
			System.out.println("wifiIsOpen: " + wifiIsOpen);
			wifiSwitchStatusJLabel.setText("Wifi�ѿ��������ˢ��wifi");
			wifiSwitchStatusJLabel.setBackground(new Color(77, 111, 113));
			wifiSwitchStatusJLabel.setForeground(Color.WHITE);
		}
	}

	/**
	 * ��Ӻ���
	 * 
	 * @param fatherJpanel
	 *            ������
	 * @param gridx
	 *            X��λ��
	 * @param gridy
	 *            Y��λ��
	 * @param ipady
	 *            Y���ڳŴ�ֵ��Android�ϵ�padding��
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
