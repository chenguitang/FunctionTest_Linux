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

	// ͼƬ����
	Icon[] icons = { icon1, icon2, icon3, icon4, icon5 };

	static JPanel wifiPanel = null; // ������
	private JPanel listWifiPane = null; // wifi�б�
	private JList<JPanel> wifiJList = null;
	private ArrayList<WifiMessage> listWifiDatas = null; // wifi���ݾۺ�

	private boolean wifiIsOpen = true;
	private ImageIcon image3 = null;

	private WifiUtils wifiUtils = null;

	public WifiPanel() {
		wifiPanel = new JPanel();
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
			wifiData.setStatus("δ����");
			listWifiDatas.add(wifiData);
		}

		FriListModel listModel = new FriListModel(listWifiDatas);
		wifiJList = new JList(listModel);
		wifiJList.setCellRenderer(new FriListCellRenderer(icons));
		// ���õ�һѡ��ģʽ��ÿ��ֻ����һ��Ԫ�ر�ѡ�У�
		wifiJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		wifiJList.setFont(new Font(Font.SERIF, Font.PLAIN, 25));

		// wifiJList.setSelectedIndex(1);
		// ��ӹ�����
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
	 * ����Jlist��Item������¼�
	 */
	private void listenerJlistSelect(ListSelectionEvent event) {
		int selectedPosition = wifiJList.getSelectedIndex();
		System.out.println("ѡ��λ�ã� " + selectedPosition);
		if (listWifiDatas != null) {
			if (listWifiDatas.size() > selectedPosition) {
				listWifiDatas.get(selectedPosition).setStatus("������");
				ImageIcon image = new ImageIcon(
						"E:\\nfs\\ic_wifi_lock_signal_4_teal.png");
				listWifiDatas.get(selectedPosition).setSignalLevel("60");
				// wifiJList.notify();
			}
		}

	}

	/**
	 * ����wifi����
	 * 
	 * @param parentPanel
	 *            ������
	 */
	private void initTopSwitchPanel(JPanel parentPanel) {

		final JButton wifiSwitchStatusJLabel = new JButton("Wifi�ѿ���������ر�wifi");
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
			wifiSwitchStatusJLabel.setText("Wifi�ѹرգ��������wifi");
			wifiSwitchStatusJLabel.setBackground(new Color(119, 119, 136));
			wifiSwitchStatusJLabel.setForeground(Color.WHITE);
		} else {
			System.out.println("wifiIsOpen: " + wifiIsOpen);
			wifiSwitchStatusJLabel.setText("Wifi�ѿ���������ر�wifi");
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
