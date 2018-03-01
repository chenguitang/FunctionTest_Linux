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
 * @desc Ǯ�����
 */
public class CashDrawerPanel {

	private static final long serialVersionUID = 1L;
	static JPanel cashDrawerPanel = null; // ������
	private JPanel mButtonJPanel = null; // ���ز���Ǯ������button�İ�ť
	private JButton pin2Button = null, pin5Button = null; // ��Ǯ�䰴ť

	public CashDrawerPanel() {
		cashDrawerPanel = new JPanel();
		cashDrawerPanel.setBackground(Color.WHITE);
		cashDrawerPanel.setLayout(new GridLayout(1, 1));  //ʹ��JPanel����������
		
		mButtonJPanel = new JPanel();
		mButtonJPanel.setLayout(new BorderLayout());  
		
		cashDrawerPanel.add(mButtonJPanel);
		
		pin2Button = new JButton("��Ǯ��(pin2)");
		pin5Button = new JButton("��Ǯ��(pin5)");
		
		mButtonJPanel.add(pin2Button,BorderLayout.SOUTH);
		mButtonJPanel.add(pin5Button,BorderLayout.NORTH);
		
	}

}
