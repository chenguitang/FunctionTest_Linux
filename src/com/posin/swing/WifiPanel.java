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

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import wifi.WifiUtils;
import wifi.WifiUtils.WifiDataChageListener;

import com.posin.Jlist.FriListCellRenderer;
import com.posin.Jlist.FriListModel;
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

	// 图片数组
	Icon[] icons = { icon1, icon2, icon3, icon4, icon5 };

	static JPanel wifiPanel = null; // 根布局
	private JPanel listWifiPane = null; // wifi列表
	private JList<JPanel> wifiJList = null;
	private ArrayList<WifiMessage> listWifiDatas = null; // wifi数据聚合

	private boolean wifiIsOpen = true;
	private ImageIcon image3 = null;

	private WifiUtils wifiUtils = null;

	public WifiPanel() {
		wifiPanel = new JPanel();
		wifiPanel.setLayout(new BorderLayout());
		Font f = new Font("隶书", Font.PLAIN, 25);
		// addLine(wifiPanel, 0, 0, -8, Color.GRAY);

		initTopSwitchPanel(wifiPanel); // 顶部wifi开关
		// initWifiList(); // 获取wifi信息
		initListWifiPanel(wifiPanel); // wifi列表
	}

	/**
	 * 获取WIFI信息
	 */
	private void initWifiList() {
		try {
			wifiUtils = new WifiUtils();
			wifiUtils.findAllWifi();
			wifiUtils.setAllWifiDataListener(new WifiDataChageListener() {

				@Override
				public void wifiDataChange(
						ArrayList<WifiMessage> listWifiMessages) {
					System.out
							.println("2++++++++++++++++++++++++++++++++++++++++++++");
					System.out.println("listWifiMessages size : "
							+ listWifiMessages.size());
					System.out
							.println("++++++++++++++++++++++++++++++++++++++++++++");
					listWifiDatas.clear();
					for (WifiMessage wifiMessage : listWifiMessages) {
						listWifiDatas.add(wifiMessage);
						listWifiDatas.notify();
					}
				}
			});
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
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

		// 得到此图标的 Image（image.getImage()）
		// Image img = image.getImage();
		// img = img.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
		// image.setImage(img);
		for (int i = 0; i < 20; i++) {
			WifiMessage wifiData = new WifiMessage();
			if (i % 2 == 0) {
				wifiData.setSignalLevel("20");
			} else if (i % 3 == 0) {
				wifiData.setSignalLevel("40");
			} else if (i % 3 == 5) {
				wifiData.setSignalLevel("60");
			} else {
				wifiData.setSignalLevel("100");
			}

			wifiData.setSsid("posin" + i);
			wifiData.setStatus("未连接");
			listWifiDatas.add(wifiData);
		}

		FriListModel listModel = new FriListModel(listWifiDatas);
		wifiJList = new JList(listModel);
		wifiJList.setCellRenderer(new FriListCellRenderer(icons));
		// 设置单一选择模式（每次只能有一个元素被选中）
		wifiJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		wifiJList.setFont(new Font(Font.SERIF, Font.PLAIN, 25));

		// wifiJList.setSelectedIndex(1);
		// 添加滚动条
		JScrollPane jp = new JScrollPane(wifiJList);
		jp.setPreferredSize(new Dimension(1920, 600));
		wifiPanel.add(jp);

		wifiJList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent event) {
				listenerJlistSelect(event);
			}
		});
	}

	/**
	 * 监听Jlist的Item被点击事件
	 */
	private void listenerJlistSelect(ListSelectionEvent event) {
		int selectedPosition = wifiJList.getSelectedIndex();
		System.out.println("选中位置： " + selectedPosition);
		if (listWifiDatas != null) {
			if (listWifiDatas.size() > selectedPosition) {
				listWifiDatas.get(selectedPosition).setStatus("已连接");
				ImageIcon image = new ImageIcon(
						"E:\\nfs\\ic_wifi_lock_signal_4_teal.png");
				listWifiDatas.get(selectedPosition).setSignalLevel("60");
				// wifiJList.notify();
			}
		}

	}

	/**
	 * 顶部wifi开发
	 * 
	 * @param parentPanel
	 *            父容器
	 */
	private void initTopSwitchPanel(JPanel parentPanel) {

		final JButton wifiSwitchStatusJLabel = new JButton("Wifi已开启，点击关闭wifi");
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
			wifiSwitchStatusJLabel.setText("Wifi已关闭，点击开启wifi");
			wifiSwitchStatusJLabel.setBackground(new Color(119, 119, 136));
			wifiSwitchStatusJLabel.setForeground(Color.WHITE);
		} else {
			System.out.println("wifiIsOpen: " + wifiIsOpen);
			wifiSwitchStatusJLabel.setText("Wifi已开启，点击关闭wifi");
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
