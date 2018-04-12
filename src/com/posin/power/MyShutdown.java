package com.posin.power;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;

public class MyShutdown extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Create the frame.
	 */
	public MyShutdown() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		textField = new JTextField("olajdf;lja;jf;aj;fja;jf;aj");
		contentPane.add(textField, BorderLayout.CENTER);
		textField.setColumns(10);
		
		JLabel label = new JLabel("\u6211\u662F\u663E\u793A\u9875\u9762");
		label.setBackground(Color.LIGHT_GRAY);
		label.setForeground(Color.CYAN);
		label.setFont(new Font("ËÎÌו", Font.PLAIN, 16));
		contentPane.add(label, BorderLayout.NORTH);
	}

}
