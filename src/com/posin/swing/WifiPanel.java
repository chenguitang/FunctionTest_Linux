package com.posin.swing;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class WifiPanel {

	static JPanel wifiPanel = null; // ������
	private JPanel topSwitchPanel = null; // ����wifi����
	private JScrollPane listWifiPane = null; // wifi�б�

	public boolean isPlayer = false;

	public WifiPanel() {
		wifiPanel = new JPanel();
		wifiPanel.setLayout(new GridBagLayout());
		Font f = new Font("����", Font.PLAIN, 25);
		addLine(wifiPanel, 0, 0, -8, Color.GRAY);

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
		listWifiPane = new JScrollPane();

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 10;
		c.gridx = 0;
		c.gridy = 2;
		c.ipadx = 0;
		c.ipady = 0;
		parentPanel.add(listWifiPane, c);

		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridLayout());

		JButton testButton = new JButton("1231231");
		JButton testButton1 = new JButton("1231231");
		JButton testButton2 = new JButton("1231231");
		JButton testButton3 = new JButton("1231231");
		JButton testButton4 = new JButton("1231231");
		// listWifiPane.add(testButton, 0, 1);

		jPanel.add(testButton);
		jPanel.add(testButton1);
		jPanel.add(testButton2);
		jPanel.add(testButton3);
		jPanel.add(testButton4);

		listWifiPane.setViewportView(jPanel);

	}

	/**
	 * ����wifi����
	 * 
	 * @param parentPanel
	 *            ������
	 */
	private void initTopSwitchPanel(JPanel parentPanel) {

		topSwitchPanel = new JPanel();

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 1;
		c.ipadx = 0;
		c.ipady = 0;
		parentPanel.add(topSwitchPanel, c);

		topSwitchPanel.setBackground(new Color(55, 71, 79));
//		topSwitchPanel.setBackground(new Color(244, 244, 244));
		topSwitchPanel.setLayout(new BorderLayout());

		JLabel wifiSwitchStatusJLabel = new JLabel("����");
		wifiSwitchStatusJLabel.setForeground(Color.WHITE);
		wifiSwitchStatusJLabel.setFont(new Font("����",Font.PLAIN,20));
		topSwitchPanel.add(wifiSwitchStatusJLabel, BorderLayout.WEST);

		// topSwitchPanel.setBackground(Color.BLACK);
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
