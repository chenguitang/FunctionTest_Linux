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
 * @desc Ǯ�����
 */
public class CashDrawerPanel {

	private static final long serialVersionUID = 1L;
	static JPanel cashDrawerPanel = null; // ������
	private JPanel mButtonJPanel = null; // ���ز���Ǯ������button�İ�ť
	private JButton pin2Button = null, pin5Button = null; // ��Ǯ�䰴ť

	public CashDrawerPanel() {
		cashDrawerPanel = new JPanel();
		cashDrawerPanel.setSize(new Dimension(Appconfig.PANELCARDWIDTH,
				Appconfig.PANELCARDHEIGHT));
		cashDrawerPanel.setBackground(Color.WHITE);
		cashDrawerPanel.setLayout(new BorderLayout());
		cashDrawerPanel.setLayout(new GridLayout(1, 1)); // ʹ��JPanel����������

		mButtonJPanel = new JPanel();
		mButtonJPanel.setBackground(Color.WHITE);
		// mButtonJPanel.setLayout(new BorderLayout());
		cashDrawerPanel.setLayout(new BorderLayout());
		cashDrawerPanel.add(mButtonJPanel, BorderLayout.CENTER);
//		mButtonJPanel.setBackground(Color.RED);
		
		pin2Button = new JButton("��Ǯ��(pin2)");
		pin5Button = new JButton("��Ǯ��(pin5)");

		mButtonJPanel.add(pin2Button);
		mButtonJPanel.add(pin5Button);

		pin2Button.setPreferredSize(new Dimension(Appconfig.CASHDRAWER_BUTTON_WIDTH,
				Appconfig.CASHDRAWER_BUTTON_HEIGHT));
		pin5Button.setPreferredSize(new Dimension(Appconfig.CASHDRAWER_BUTTON_WIDTH,
				Appconfig.CASHDRAWER_BUTTON_HEIGHT));

		// ����Button�����С����ʽ��
		Font f = new Font("����", Font.PLAIN, 15);
		pin2Button.setFont(f);
		pin5Button.setFont(f);
		initListener();
	}

	/**
	 * ��ť�ĵ���¼�
	 */
	private void initListener() {
		// pin2��Ǯ��
		pin2Button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openByPin2();
			}
		});

		// pin5��Ǯ��
		pin5Button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openByPin5();
			}
		});
	}

	/**
	 * pin2��Ǯ��
	 */
	private void openByPin2() {
		try {
			Proc.createSuProcess("cashdrawer kickout pin2 100");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * pin5��Ǯ��
	 */
	private void openByPin5() {
		try {
			Proc.createSuProcess("cashdrawer kickout pin5 100");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
