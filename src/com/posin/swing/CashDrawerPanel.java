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

import com.posin.global.Appconfig;
import com.posin.utils.Proc;

/**
 * @author Greetty
 * 
 * @desc 钱箱测试
 */
public class CashDrawerPanel {

	private static final long serialVersionUID = 1L;
	static JPanel cashDrawerPanel = null; // 根布局
	private JPanel mButtonJPanel = null; // 承载测试钱箱两个button的按钮
	private JButton pin2Button = null, pin5Button = null; // 开钱箱按钮

	public CashDrawerPanel() {
		cashDrawerPanel = new JPanel();
		cashDrawerPanel.setSize(new Dimension(Appconfig.PANELCARDWIDTH,
				Appconfig.PANELCARDHEIGHT));
		cashDrawerPanel.setBackground(Color.WHITE);
		cashDrawerPanel.setLayout(new BorderLayout());
		cashDrawerPanel.setLayout(new GridLayout(1, 1)); // 使子JPanel填满父布局

		mButtonJPanel = new JPanel();
		mButtonJPanel.setBackground(Color.WHITE);
		// mButtonJPanel.setLayout(new BorderLayout());
		cashDrawerPanel.setLayout(new BorderLayout());
		cashDrawerPanel.add(mButtonJPanel, BorderLayout.CENTER);
//		mButtonJPanel.setBackground(Color.RED);
		
		pin2Button = new JButton("开钱箱(pin2)");
		pin5Button = new JButton("开钱箱(pin5)");

		mButtonJPanel.add(pin2Button);
		mButtonJPanel.add(pin5Button);

		pin2Button.setPreferredSize(new Dimension(Appconfig.CASHDRAWER_BUTTON_WIDTH,
				Appconfig.CASHDRAWER_BUTTON_HEIGHT));
		pin5Button.setPreferredSize(new Dimension(Appconfig.CASHDRAWER_BUTTON_WIDTH,
				Appconfig.CASHDRAWER_BUTTON_HEIGHT));

		// 设置Button字体大小及样式等
		Font f = new Font("隶书", Font.PLAIN, 15);
		pin2Button.setFont(f);
		pin5Button.setFont(f);
		initListener();
	}

	/**
	 * 按钮的点击事件
	 */
	private void initListener() {
		// pin2开钱箱
		pin2Button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openByPin2();
			}
		});

		// pin5开钱箱
		pin5Button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openByPin5();
			}
		});
	}

	/**
	 * pin2开钱箱
	 */
	private void openByPin2() {
		try {
			Proc.createSuProcess("cashdrawer kickout pin2 100");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * pin5开钱箱
	 */
	private void openByPin5() {
		try {
			Proc.createSuProcess("cashdrawer kickout pin5 100");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
