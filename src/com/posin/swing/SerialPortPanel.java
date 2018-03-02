package com.posin.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.posin.device.CashDrawer;
import com.posin.global.Appconfig;
import com.posin.utils.Proc;

/**
 * @author Greetty
 * 
 * @desc 钱箱测试
 */
public class SerialPortPanel {

	private static final long serialVersionUID = 1L;
	static JPanel serialPortPanel = null; // 根布局
	private JPanel mButtonJPanel = null; // 承载button的按钮
	private JButton portButton = null, baudrateButton = null,
			switchButton = null;

	public SerialPortPanel() {
		serialPortPanel = new JPanel();
		serialPortPanel.setSize(new Dimension(Appconfig.PANELCARDWIDTH,
				Appconfig.PANELCARDHEIGHT));
		serialPortPanel.setBackground(Color.WHITE);
		serialPortPanel.setLayout(new BorderLayout());
		serialPortPanel.setLayout(new GridLayout(1, 1)); // 使子JPanel填满父布局

		mButtonJPanel = new JPanel();
		mButtonJPanel.setBackground(Color.WHITE);
		// mButtonJPanel.setLayout(new BorderLayout());
		serialPortPanel.setLayout(new BorderLayout());
		serialPortPanel.add(mButtonJPanel, BorderLayout.CENTER);
		// mButtonJPanel.setBackground(Color.RED);

		portButton = new JButton("端口");
		baudrateButton = new JButton("波特率");
		switchButton = new JButton("打开");

		mButtonJPanel.add(portButton);
		mButtonJPanel.add(baudrateButton);
		mButtonJPanel.add(switchButton);

		// 设置Button大小
		portButton.setPreferredSize(new Dimension(
				Appconfig.CASHDRAWER_BUTTON_WIDTH,
				Appconfig.CASHDRAWER_BUTTON_HEIGHT));
		baudrateButton.setPreferredSize(new Dimension(
				Appconfig.CASHDRAWER_BUTTON_WIDTH,
				Appconfig.CASHDRAWER_BUTTON_HEIGHT));
		switchButton.setPreferredSize(new Dimension(
				Appconfig.CASHDRAWER_BUTTON_WIDTH,
				Appconfig.CASHDRAWER_BUTTON_HEIGHT));

		// 设置Button字体大小及样式等
		Font f = new Font("隶书", Font.PLAIN, 15);
		portButton.setFont(f);
		baudrateButton.setFont(f);
		switchButton.setFont(f);
		initListener();
	}

	/**
	 * 按钮的点击事件
	 */
	private void initListener() {
		// 端口开钱箱
		portButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectPort();
			}
		});

		// 波特率开钱箱
		baudrateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectBaudrate();
			}
		});
		// 打开or关闭
		switchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				switchListener();
			}
		});
	}

	/**
	 * 选择接口
	 */
	private void selectPort() {
		try {
			Proc.createSuProcess("cashdrawer kickout pin2 100");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * 选择波特率
	 */
	private void selectBaudrate() {
		try {
			Proc.createSuProcess("cashdrawer kickout pin5 100");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 开始测试或关闭测试
	 */
	private void switchListener() {
		
	}

}
