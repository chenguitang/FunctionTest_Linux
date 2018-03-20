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
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * 喇叭测试
 * @author Greetty
 *
 */
public class HornPanel {

	public JPanel hornPanel = null; // 根布局
	JButton playButton = null;
	AudioClip currentSong = null;
	String path = null;
	public boolean isPlayer = false;
	private AudioClip au;

	private static final HornPanel HORN_PANEL_INSTANCE=new HornPanel();
	
	public static HornPanel getInstance() {
		return HORN_PANEL_INSTANCE;
	}
	
	private HornPanel() {
		hornPanel = new JPanel();
		hornPanel.setLayout(new GridBagLayout());
		Font f = new Font("隶书", Font.PLAIN, 25);

		addLine(hornPanel, 0, 0, -8, Color.GRAY);

		path = Class.class.getClass().getResource("/").getPath()
				+ "music/test.wav";

		playButton = new JButton("播放");
		playButton.setPreferredSize(new Dimension(160, 80));
		playButton.setFocusable(false);
		playButton.setFont(f);
		// hornPanel.add(playButton);

		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.ipadx = 0;
		c.ipady = 0;
		hornPanel.add(playButton, c);

		playButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (!isPlayer) {
						playButton.setText("停止");
					} else {
						playButton.setText("播放");
					}

					URL url = HornPanel.class.getResource("/music/test.wav");
					playerMusic(url);

					System.out.println("music path: " + url.getPath());

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * 播放音乐
	 * 
	 * @param musicUrl
	 *            文件URL
	 */
	private void playerMusic(URL musicUrl) {

		try {

			if (!isPlayer) {
				// File f = new File(path);
				// URL url = f.toURI().toURL();
				au = Applet.newAudioClip(musicUrl);
				au.play();
				isPlayer = true;
				System.out.println("start play music");
			} else {
				if (au != null) {
					au.stop();
					au = null;
					isPlayer = false;
					System.out.println("stop play music");
				} else {
					System.out.println("start failure because au==null");
				}
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			au = null;
			e.printStackTrace();
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
