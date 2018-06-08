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
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.posin.utils.ModelUtils;
import com.posin.utils.StringUtils;
import com.posin.utils.VersionUtils;

/**
 * 喇叭测试
 * 
 * @author Greetty
 * 
 */
public class AboutPanel {

	public JPanel aboutPanel = null; // 根布局
	private JLabel versionLabel = null;
	private JLabel modelLabel = null;
	private Font f;

	private static class AboutHolder {
		private static final AboutPanel HORN_PANEL_INSTANCE = new AboutPanel();
	}

	public static AboutPanel getInstance() {
		return AboutHolder.HORN_PANEL_INSTANCE;
	}

	private AboutPanel() {
		aboutPanel = new JPanel();
		aboutPanel.setBackground(Color.white);
		aboutPanel.setLayout(new GridBagLayout());
		f = new Font("隶书", Font.PLAIN, 25);

		addLine(aboutPanel, 0, 0, -9, Color.GRAY);

		iniModelUI();

		addLine(aboutPanel, 0, 2, -9, Color.GRAY);

		iniVersionUI();

		addLine(aboutPanel, 0, 4, -9, Color.GRAY);
		initEmptyPanelUI();

		initData();
	}

	private void initData() {
		try {
			versionLabel.setText(StringUtils.SpliceString("Version: ",
					VersionUtils.getSysver(), " ", VersionUtils.getVersion()));
			// versionLabel.setText(StringUtils.SpliceString("Version: 20180502"));
			// modelLabel.setText("Model: m102L");
			modelLabel.setText(StringUtils.SpliceString("Model: ",
					ModelUtils.getModel()));
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	/**
	 * 系统版本UI布局
	 */
	private void iniVersionUI() {
		versionLabel = new JLabel();
		versionLabel.setPreferredSize(new Dimension(1920, 80));
		versionLabel.setOpaque(true);
		versionLabel.setBackground(Color.white);
		versionLabel.setFont(f);

		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 3;
		c.ipadx = 0;
		c.ipady = 40;
		aboutPanel.add(versionLabel, c);
	}

	/**
	 * 系统版本UI布局
	 */
	private void iniModelUI() {

		modelLabel = new JLabel();
		modelLabel.setPreferredSize(new Dimension(1920, 80));
		modelLabel.setOpaque(true);
		modelLabel.setBackground(Color.white);
		modelLabel.setFont(f);

		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 1;
		c.ipadx = 0;
		c.ipady = 40;
		aboutPanel.add(modelLabel, c);
	}

	public void initEmptyPanelUI() {
		JPanel emptyPanel = new JPanel();
		emptyPanel.setBackground(Color.white);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 7;
		c.ipadx = 0;
		aboutPanel.add(emptyPanel, c);
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
