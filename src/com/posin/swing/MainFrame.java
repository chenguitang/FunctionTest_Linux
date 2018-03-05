package com.posin.swing;

//��γ��������Ҫ��Ϊ����չʾ���ʹ��CardLayout���ֹ����������������е�������в���
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.posin.global.Appconfig;

public class MainFrame extends JFrame {
	
	private int mButtonWidth=Appconfig.TOP_BUTTON_WIDTH;
	private int mButtonHeight=Appconfig.TOP_BUTTON_HEIGHT;
	
	private JPanel pane = null; // ��Ҫ��JPanel����JPanel�Ĳ��ֹ��������ó�CardLayout
	private JPanel p = null; // �Ű�ť��JPanel
	private CardLayout card = null; // CardLayout���ֹ�����
	private JButton b_1 = null, b_2 = null, b_3 = null, b_4 = null, b_5 = null; // �����ֱ�ӷ�ת��JPanel����İ�ť
	private JPanel p_1 = null, p_2 = null, p_3 = null, p_4 = null, p_5 = null; // Ҫ�л������JPanel

	public MainFrame() {
		super("CardLayout Test");
		card = new CardLayout(5, 5); // ����һ������ָ����ˮƽ�ʹ�ֱ��϶���¿�Ƭ����
		pane = new JPanel(card); // JPanel�Ĳ��ֹ��������ó�CardLayout
		p = new JPanel(); // ����Ű�ť��JPanel
		
		b_1 = new JButton("Ǯ�����");
		b_2 = new JButton("���ڲ���");
		b_3 = new JButton("���Ȳ���");
		b_4 = new JButton("wifi����");
		b_5 = new JButton("��������");
		
		//����Button�����С����ʽ��
		Font f = new Font("����",Font.BOLD,25);  
		b_1.setFont(f);
		b_2.setFont(f);
		b_3.setFont(f);
		b_4.setFont(f);
		b_5.setFont(f);
		
		//����Button��༰Button��С
		b_1.setMargin(new Insets(2, 2, 2, 2));
//		b_1.setSize(100, 200);
		b_1.setPreferredSize(new Dimension(mButtonWidth,mButtonHeight));
		b_2.setMargin(new Insets(2, 2, 2, 2));
		b_2.setPreferredSize(new Dimension(mButtonWidth,mButtonHeight));
		b_3.setMargin(new Insets(2, 2, 2, 2));
		b_3.setPreferredSize(new Dimension(mButtonWidth,mButtonHeight));
		b_4.setMargin(new Insets(2, 2, 2, 2));
		b_4.setPreferredSize(new Dimension(mButtonWidth,mButtonHeight));
		b_5.setMargin(new Insets(2, 2, 2, 2));
		b_5.setPreferredSize(new Dimension(mButtonWidth,mButtonHeight));
		
		p.add(b_1);
		p.add(b_2);
		p.add(b_3);
		p.add(b_4);
		p.add(b_5);
//		p_1 = new JPanel();
		CashDrawerPanel mCashDrawerPanel=new CashDrawerPanel();
		p_1=CashDrawerPanel.cashDrawerPanel;
//		p_2 = new JPanel();
		SerialPortPanel mSerialPortPanel = new SerialPortPanel();
		p_2=mSerialPortPanel.serialPortPanel;
		p_3 = new JPanel();
		p_4 = new JPanel();
		p_5 = new JPanel();
//		p_1.setBackground(Color.RED);
//		p_2.setBackground(Color.BLUE);
		p_3.setBackground(Color.GREEN);
		p_5.setBackground(Color.GRAY);
		
//		p_1.add(new JLabel("JPanel_1"));
//		p_2.add(new JLabel("JPanel_2"));
		p_3.add(new JLabel("JPanel_3"));
		p_4.add(new JLabel("JPanel_4"));
		p_5.add(new JLabel("JPanel_5"));
		
		pane.add(p_2, "p2");
		pane.add(p_1, "p1");
		
		pane.add(p_3, "p3");
		pane.add(p_4, "p4");
		pane.add(p_5, "p5");
		
		b_1.addActionListener(new ActionListener() {
			// ֱ�ӷ�ת��p_1
			public void actionPerformed(ActionEvent e) {
				card.show(pane, "p1");
			}
		});
		b_2.addActionListener(new ActionListener() {
			// ֱ�ӷ�ת��p_2
			public void actionPerformed(ActionEvent e) {
				card.show(pane, "p2");
			}
		});
		b_3.addActionListener(new ActionListener() {
			// ֱ�ӷ�ת��p_3
			public void actionPerformed(ActionEvent e) {
				card.show(pane, "p3");
			}
		});
		
		b_4.addActionListener(new ActionListener() {
			// ֱ�ӷ�ת��p_4
			public void actionPerformed(ActionEvent e) {
				card.show(pane, "p4");
			}
		});
		
		b_5.addActionListener(new ActionListener() {
			// ֱ�ӷ�ת��p_5
			public void actionPerformed(ActionEvent e) {
				card.show(pane, "p5");
			}
		});
		
		this.getContentPane().add(pane);
		this.getContentPane().add(p, BorderLayout.NORTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(Appconfig.PANELCARDWIDTH, Appconfig.PANELCARDHEIGHT);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new MainFrame();
	}

	
}
