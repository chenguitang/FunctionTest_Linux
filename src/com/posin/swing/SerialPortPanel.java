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
 * @desc Ǯ�����
 */
public class SerialPortPanel {

	private static final long serialVersionUID = 1L;
	static JPanel serialPortPanel = null; // ������
	private JPanel mButtonJPanel = null; // ����button�İ�ť
	private JButton portButton = null, baudrateButton = null,
			switchButton = null;

	public SerialPortPanel() {
		serialPortPanel = new JPanel();
		serialPortPanel.setSize(new Dimension(Appconfig.PANELCARDWIDTH,
				Appconfig.PANELCARDHEIGHT));
		serialPortPanel.setBackground(Color.WHITE);
		serialPortPanel.setLayout(new BorderLayout());
		serialPortPanel.setLayout(new GridLayout(1, 1)); // ʹ��JPanel����������

		mButtonJPanel = new JPanel();
		mButtonJPanel.setBackground(Color.WHITE);
		// mButtonJPanel.setLayout(new BorderLayout());
		serialPortPanel.setLayout(new BorderLayout());
		serialPortPanel.add(mButtonJPanel, BorderLayout.CENTER);
		// mButtonJPanel.setBackground(Color.RED);

		portButton = new JButton("�˿�");
		baudrateButton = new JButton("������");
		switchButton = new JButton("��");

		mButtonJPanel.add(portButton);
		mButtonJPanel.add(baudrateButton);
		mButtonJPanel.add(switchButton);

		// ����Button��С
		portButton.setPreferredSize(new Dimension(
				Appconfig.CASHDRAWER_BUTTON_WIDTH,
				Appconfig.CASHDRAWER_BUTTON_HEIGHT));
		baudrateButton.setPreferredSize(new Dimension(
				Appconfig.CASHDRAWER_BUTTON_WIDTH,
				Appconfig.CASHDRAWER_BUTTON_HEIGHT));
		switchButton.setPreferredSize(new Dimension(
				Appconfig.CASHDRAWER_BUTTON_WIDTH,
				Appconfig.CASHDRAWER_BUTTON_HEIGHT));

		// ����Button�����С����ʽ��
		Font f = new Font("����", Font.PLAIN, 15);
		portButton.setFont(f);
		baudrateButton.setFont(f);
		switchButton.setFont(f);
		initListener();
	}

	/**
	 * ��ť�ĵ���¼�
	 */
	private void initListener() {
		// �˿ڿ�Ǯ��
		portButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectPort();
			}
		});

		// �����ʿ�Ǯ��
		baudrateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectBaudrate();
			}
		});
		// ��or�ر�
		switchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				switchListener();
			}
		});
	}

	/**
	 * ѡ��ӿ�
	 */
	private void selectPort() {
		try {
			Proc.createSuProcess("cashdrawer kickout pin2 100");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * ѡ������
	 */
	private void selectBaudrate() {
		try {
			Proc.createSuProcess("cashdrawer kickout pin5 100");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ʼ���Ի�رղ���
	 */
	private void switchListener() {
		
	}

}
