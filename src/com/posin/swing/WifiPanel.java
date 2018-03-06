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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.posin.Jlist.FriListCellRenderer;
import com.posin.Jlist.FriListModel;
import com.posin.Jlist.WifiData;

public class WifiPanel {

	static JPanel wifiPanel = null; // 根布局
	private JPanel listWifiPane = null; // wifi列表
	private JList<JPanel> wifiJList = null;
	private ArrayList<WifiData> listWifiDatas = null; // wifi数据聚合

	private boolean wifiIsOpen = true;
	private ImageIcon image3 = null;

	public WifiPanel() {
		wifiPanel = new JPanel();
		wifiPanel.setLayout(new BorderLayout());
		Font f = new Font("隶书", Font.PLAIN, 25);
		// addLine(wifiPanel, 0, 0, -8, Color.GRAY);

		initTopSwitchPanel(wifiPanel); // 顶部wifi开关
		initListWifiPanel(wifiPanel); // wifi列表
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
		// ImageIcon image = new ImageIcon(
		// "/mnt/nfs/ic_wifi_lock_signal_4_teal.png");
		ImageIcon image = new ImageIcon(
				"E:\\nfs\\ic_wifi_lock_signal_4_teal.png");

		ImageIcon image2 = new ImageIcon("E:\\nfs\\ic_wifi_signal_2_teal.png");

		ImageIcon image3 = new ImageIcon("E:\\nfs\\ic_wifi_signal_1_light.png");

		// 得到此图标的 Image（image.getImage()）
		// Image img = image.getImage();
		// img = img.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
		// image.setImage(img);
		for (int i = 0; i < 20; i++) {
			WifiData wifiData = new WifiData();
			if (i % 2 == 0) {
				wifiData.setImageIcon(image3);
			} else {
				wifiData.setImageIcon(image2);
			}

			wifiData.setWifiName("posin" + i);
			wifiData.setWifiStatus("未连接");
			listWifiDatas.add(wifiData);
		}

		FriListModel listModel = new FriListModel(listWifiDatas);
		wifiJList = new JList(listModel);
		wifiJList.setCellRenderer(new FriListCellRenderer());
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
				listWifiDatas.get(selectedPosition).setWifiStatus("已连接");
				ImageIcon image = new ImageIcon(
						"E:\\nfs\\ic_wifi_lock_signal_4_teal.png");
				listWifiDatas.get(selectedPosition).setImageIcon(image);
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
		wifiSwitchStatusJLabel.setBackground(new Color(77, 111, 113));
		wifiSwitchStatusJLabel.setFont(new Font("楷体", Font.PLAIN, 25));
		wifiSwitchStatusJLabel.setForeground(Color.WHITE);
		wifiSwitchStatusJLabel.setFocusPainted(false); // 取消聚焦
		parentPanel.add(wifiSwitchStatusJLabel, BorderLayout.NORTH);

		// wifiSwitchStatusJLabel.setText("Wifi已关闭，点击开启wifi");
		// 开启or关闭wifi
		wifiSwitchStatusJLabel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (wifiIsOpen) {
					System.out.println("wifiIsOpen: " + wifiIsOpen);
					wifiSwitchStatusJLabel.setText("Wifi已关闭，点击开启wifi");
					wifiSwitchStatusJLabel.setBackground(new Color(119, 119,
							136));
					wifiSwitchStatusJLabel.setForeground(Color.WHITE);
					wifiIsOpen = !wifiIsOpen;
				} else {
					System.out.println("wifiIsOpen: " + wifiIsOpen);
					wifiSwitchStatusJLabel.setText("Wifi已开启，点击关闭wifi");
					wifiSwitchStatusJLabel
							.setBackground(new Color(77, 111, 113));
					wifiSwitchStatusJLabel.setForeground(Color.WHITE);
					wifiIsOpen = !wifiIsOpen;
				}

			}
		});

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
