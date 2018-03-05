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

	static JPanel wifiPanel = null; // ������
	private JPanel listWifiPane = null; // wifi�б�
	private JList<JPanel> wifiJList = null;

	public boolean wifiIsOpen = true;

	public WifiPanel() {
		wifiPanel = new JPanel();
		wifiPanel.setLayout(new BorderLayout());
		Font f = new Font("����", Font.PLAIN, 25);
		// addLine(wifiPanel, 0, 0, -8, Color.GRAY);

		initTopSwitchPanel(wifiPanel); // ����wifi����
		initListWifiPanel(wifiPanel); // wifi�б�
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

		ArrayList<WifiData> listWifiDatas = new ArrayList<>();
		// ImageIcon image = new ImageIcon("/mnt/nfs/posin.png");
		ImageIcon image = new ImageIcon(
				"/mnt/nfs/ic_wifi_lock_signal_4_teal.png");
		// ImageIcon image = new ImageIcon("E:\\nfs\\posin.png");
		// ImageIcon image = new
		// ImageIcon("E:\\nfs\\ic_wifi_lock_signal_4_teal.png");
		// Image img = image.getImage(); // �õ���ͼ��� Image��image.getImage()��
		// img = img.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
		// image.setImage(img);
		for (int i = 0; i < 30; i++) {
			WifiData wifiData = new WifiData();
			wifiData.setImageIcon(image);
			wifiData.setWifiName("posin" + i);
			wifiData.setWifiStatus("������");
			listWifiDatas.add(wifiData);
		}

		FriListModel buddy = new FriListModel(listWifiDatas);
		wifiJList = new JList(buddy);
		wifiJList.setCellRenderer(new FriListCellRenderer());
		wifiJList.setFont(new Font(Font.SERIF, Font.PLAIN, 35));
		wifiJList.setPreferredSize(new Dimension(1920, 500));

		// wifiJList.setSelectedIndex(1);
		/********* �������б����ӹ����� **************/
		JScrollPane jp = new JScrollPane(wifiJList);
		jp.setPreferredSize(new Dimension(1920, 600));

		wifiPanel.add(jp);
	}

	/**
	 * ����wifi����
	 * 
	 * @param parentPanel
	 *            ������
	 */
	private void initTopSwitchPanel(JPanel parentPanel) {

		final JButton wifiSwitchStatusJLabel = new JButton("Wifi�ѿ���������ر�wifi");
		wifiSwitchStatusJLabel.setPreferredSize(new Dimension(1920,70));
		wifiSwitchStatusJLabel.setBackground(new Color(77, 111, 113));
		wifiSwitchStatusJLabel.setFont(new Font("����", Font.PLAIN, 25));
		wifiSwitchStatusJLabel.setForeground(Color.WHITE);
		parentPanel.add(wifiSwitchStatusJLabel, BorderLayout.NORTH);

		// wifiSwitchStatusJLabel.setText("Wifi�ѹرգ��������wifi");
		// ����or�ر�wifi
		wifiSwitchStatusJLabel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (wifiIsOpen) {
					System.out.println("wifiIsOpen: " + wifiIsOpen);
					wifiSwitchStatusJLabel.setText("Wifi�ѹرգ��������wifi");
					wifiSwitchStatusJLabel.setBackground(new Color(119, 119, 136));
					wifiSwitchStatusJLabel.setForeground(Color.WHITE);
					wifiIsOpen = !wifiIsOpen;
				} else {
					System.out.println("wifiIsOpen: " + wifiIsOpen);
					wifiSwitchStatusJLabel.setText("Wifi�ѿ���������ر�wifi");
					wifiSwitchStatusJLabel.setBackground(new Color(77, 111, 113));
					wifiSwitchStatusJLabel.setForeground(Color.WHITE);
					wifiIsOpen = !wifiIsOpen;
				}

			}
		});

	}

	/**
	 * ���Ӻ���
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