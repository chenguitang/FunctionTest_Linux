package com.posin.swing;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class WifiPanel {

	static JPanel wifiPanel = null; // 根布局
	JButton playButton = null;
	AudioClip currentSong = null;
	String path = "/mnt/nfs/test.wav";
	public boolean isPlayer = false;
	private AudioClip au;

	public WifiPanel() {
		wifiPanel = new JPanel();
		wifiPanel.setLayout(new GridBagLayout());
		Font f = new Font("隶书", Font.PLAIN, 25);

		playButton = new JButton("播放");
		playButton.setPreferredSize(new Dimension(160, 80));
		playButton.setFont(f);
		// hornPanel.add(playButton);

		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.ipadx = 0;
		c.ipady = 0;
		wifiPanel.add(playButton, c);

		
	}
}
