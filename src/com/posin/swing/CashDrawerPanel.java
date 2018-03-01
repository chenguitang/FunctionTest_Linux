package com.posin.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
		cashDrawerPanel.setBackground(Color.WHITE);
		cashDrawerPanel.setLayout(new GridLayout(1, 1));  //使子JPanel填满父布局
		
		mButtonJPanel = new JPanel();
		mButtonJPanel.setLayout(new BorderLayout());  
		
		cashDrawerPanel.add(mButtonJPanel);
		
		pin2Button = new JButton("开钱箱(pin2)");
		pin5Button = new JButton("开钱箱(pin5)");
		
		mButtonJPanel.add(pin2Button,BorderLayout.SOUTH);
		mButtonJPanel.add(pin5Button,BorderLayout.NORTH);
		
	}

}
