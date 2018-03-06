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
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class HornPanel {

	static JPanel hornPanel = null; // ������
	JButton playButton = null;
	AudioClip currentSong = null;
	String path = "/mnt/nfs/test.wav";
	public boolean isPlayer = false;
	private AudioClip au;

	public HornPanel() {
		hornPanel = new JPanel();
		hornPanel.setLayout(new GridBagLayout());
		Font f = new Font("����", Font.PLAIN, 25);

		addLine(hornPanel, 0, 0, -8, Color.GRAY);
		
		playButton = new JButton("����");
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
				if (!isPlayer) {
					playButton.setText("ֹͣ");
				} else {
					playButton.setText("����");
				}
				playerMusic(path);
			}
		});
	}

	/**
	 * ��������
	 * 
	 * @param path
	 *            �ļ�·��
	 */
	private void playerMusic(String path) {

		try {

			if (!isPlayer) {

				File f = new File(path);
				URL url = f.toURI().toURL();
				au = Applet.newAudioClip(url);
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

		} catch (MalformedURLException e) {
			System.out.println("Error: " + e.getMessage());
			au = null;
			e.printStackTrace();
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
