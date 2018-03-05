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

import com.posin.Jlist.FriListCellRenderer;
import com.posin.Jlist.FriListModel;
import com.posin.Jlist.WifiData;

public class WifiPanel {

	static JPanel wifiPanel = null; // 根布局
	private JPanel topSwitchPanel = null; // 顶部wifi开关
	private JPanel listWifiPane = null; // wifi列表
	private JList<JPanel> wifiJList = null;

	public boolean isPlayer = false;

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

		addWifiPanel();

		
		ArrayList<WifiData> listWifiDatas=new ArrayList<>();
		ImageIcon image = new ImageIcon("/mnt/nfs/posin.png");
//		ImageIcon image = new ImageIcon("E:\\nfs\\posin.png");
		Image img = image.getImage(); // 得到此图标的 Image（image.getImage()）
		img = img.getScaledInstance(40, 20, Image.SCALE_DEFAULT);
		image.setImage(img);
		for (int i = 0; i < 4; i++) {
			WifiData wifiData = new WifiData();
			wifiData.setImageIcon(image);
			wifiData.setWifiName("posin" + i);
			wifiData.setWifiStatus("已连接");
			listWifiDatas.add(wifiData);
		}

		FriListModel buddy = new FriListModel(listWifiDatas);
		wifiJList = new JList(buddy);
		wifiJList.setCellRenderer(new FriListCellRenderer());
		wifiJList.setFont(new Font(Font.SERIF, Font.PLAIN, 18));
		wifiJList.setPreferredSize(new Dimension(360, 350));
		
		// wifiJList.setSelectedIndex(1);
		/********* 给好友列表添加滚动条 **************/
		JScrollPane jp = new JScrollPane(wifiJList);
		jp.setPreferredSize(new Dimension(360, 400));
		
		wifiPanel.add(jp);
	}

	public void addWifiPanel() {

	}

	/**
	 * 顶部wifi开发
	 * 
	 * @param parentPanel
	 *            父容器
	 */
	private void initTopSwitchPanel(JPanel parentPanel) {

		topSwitchPanel = new JPanel();

		parentPanel.add(topSwitchPanel, BorderLayout.NORTH);

		topSwitchPanel.setBackground(new Color(55, 71, 79));
		// topSwitchPanel.setBackground(new Color(244, 244, 244));
		topSwitchPanel.setLayout(new BorderLayout());

		JLabel wifiSwitchStatusJLabel = new JLabel("开启");
		wifiSwitchStatusJLabel.setForeground(Color.WHITE);
		wifiSwitchStatusJLabel.setFont(new Font("楷体", Font.PLAIN, 20));
		topSwitchPanel.add(wifiSwitchStatusJLabel, BorderLayout.WEST);

		// topSwitchPanel.setBackground(Color.BLACK);
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
