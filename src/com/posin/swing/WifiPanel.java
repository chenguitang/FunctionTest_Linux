package com.posin.swing;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javax.sound.sampled.Line;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import wifi.WifiUtils;
import wifi.WifiUtils.AddNetworkListener;
import wifi.WifiUtils.ConnectListener;
import wifi.WifiUtils.WifiDataChageListener;

import com.posin.Jlist.FriListCellRenderer;
import com.posin.Jlist.FriListModel;
import com.posin.Jlist.MyDefaultListModel;
import com.posin.constant.WifiMessage;
import com.posin.utils.StringUtils;

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

	public WifiPanel() {
		wifiPanel = new JPanel();
		wifiUtils = new WifiUtils();
		wifiPanel.setLayout(new BorderLayout());
		Font f = new Font("����", Font.PLAIN, 25);
		// addLine(wifiPanel, 0, 0, -8, Color.GRAY);

		initTopSwitchPanel(wifiPanel); // ����wifi����
		// initWifiList(); // ��ȡwifi��Ϣ
		initListWifiPanel(wifiPanel); // wifi�б�
	}

	/**
	 * ��ȡWIFI��Ϣ
	 */
	private void initWifiList() {
		try {
			wifiUtils.findAllWifi();
			// final ArrayList<WifiMessage> listWifiDatas = new ArrayList<>();
			wifiUtils.setAllWifiDataListener(new WifiDataChageListener() {

				@Override
				public void wifiDataChange(
						ArrayList<WifiMessage> listWifiMessages) {
					listWifiDatas.clear();
					// System.out.println("+++++++++++++++++++++++++++");
					for (int i = 0; i < listWifiMessages.size(); i++) {
						System.out.println("listWifiMessages data : "
								+ listWifiMessages.get(i).toString() + "\n");
						listWifiDatas.add(listWifiMessages.get(i));
					}
					// System.out.println("++++++++++++++++++++++++++");

					if (listWifiMessages.size() > 0) {
						System.out.println("listWifiMessages size : "
								+ listWifiMessages.size());
						// wifiJList.setListData(listWifiMessages.toArray());
						wifiJList.setListData(listWifiDatas.toArray());
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

		// �õ���ͼ��� Image��image.getImage()��
		// Image img = image.getImage();
		// img = img.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
		// image.setImage(img);
		// for (int i = 0; i < 20; i++) {
		// WifiMessage wifiData = new WifiMessage();
		// if (i % 2 == 0) {
		// wifiData.setSignalLevel("20");
		// } else if (i % 3 == 0) {
		// wifiData.setSignalLevel("40");
		// } else if (i % 3 == 5) {
		// wifiData.setSignalLevel("60");
		// } else {
		// wifiData.setSignalLevel("100");
		// }
		//
		// wifiData.setSsid("posin" + i);
		// wifiData.setStatus("δ����");
		// listWifiDatas.add(wifiData);
		// }

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

		wifiJList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent event) {

				// ������ͷ�
				if (!wifiJList.getValueIsAdjusting()) {
					// System.out.println("you release this");
					// listenerJlistSelect();
					System.out.println("#####################");
					int selectedPosition = wifiJList.getSelectedIndex();
					// System.out.println("select index�� " + selectedPosition);
					System.out.println("wifi name : "
							+ listWifiDatas.get(selectedPosition).getSsid());

					getNetWork(selectedPosition);
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
		try {
			wifiUtils.findAddNetwork();
			wifiUtils.setAddNetworkListener(new AddNetworkListener() {

				@Override
				public void AddNetworkCallBack(String network) {
					System.out.println("network: " + network);
					if (network != null) {
						String password = JOptionPane.showInputDialog("��������");
						if (password != null && !password.trim().equals("")) {
							System.out.println("password: " + password);
							String ssid = listWifiDatas.get(position).getSsid();
							connnectWifi(position, network, ssid, password);
						} else {
							JOptionPane.showMessageDialog(null, "���벻��Ϊ�գ�����������");
						}
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error: " + e.getMessage());
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
			wifiUtils.connect(network, ssid, password);
			wifiUtils.setConnectListener(new ConnectListener() {

				@Override
				public void connectCallBack(boolean isSuccess) {
					if (isSuccess) {
						System.out.println("wifiJPanel connect success");
						System.out.println("listWifiDatas size: "
								+ listWifiDatas.size() + "  index: " + index);
						 listWifiDatas.get(index).setStatus("������");
						 listWifiDatas.notifyAll();
//						wifiJList.setListData(listWifiDatas.toArray());
						// listWifiDatas.notify();
					} else {
						System.out.println("wifiJPanel connect failure");
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * ����Jlist��Item������¼�
	 */
	private void listenerJlistSelect() {
		int selectedPosition = wifiJList.getSelectedIndex();
		System.out.println("my select index: " + selectedPosition);
		// if (listWifiDatas != null) {
		// if (listWifiDatas.size() > selectedPosition) {
		// listWifiDatas.get(selectedPosition).setStatus("������");
		// ImageIcon image = new ImageIcon(
		// "E:\\nfs\\ic_wifi_lock_signal_4_teal.png");
		// listWifiDatas.get(selectedPosition).setSignalLevel("60");
		// // wifiJList.notify();
		// }
		// }
		System.out.println("#####################");
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
