package com.posin.swing;

//这段程序代码主要是为读者展示如何使用CardLayout布局管理器针对内容面板中的组件进行布局
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import view.InputDialog;

import com.posin.global.Appconfig;
import com.posin.utils.ProcessUtils;
import com.posin.utils.ProcessUtils.Callback;

public class MainFrame extends JFrame {

	private int mButtonWidth = Appconfig.TOP_BUTTON_WIDTH;
	private int mButtonHeight = Appconfig.TOP_BUTTON_HEIGHT;

	private JPanel pane = null; // 主要的JPanel，该JPanel的布局管理将被设置成CardLayout
	private JPanel p = null; // 放按钮的JPanel
	private CardLayout card = null; // CardLayout布局管理器
	private JButton b_1 = null, b_2 = null, b_3 = null, b_4 = null, b_5 = null; // 五个可直接翻转到JPanel组件的按钮
	private JPanel p_1 = null, p_2 = null, p_3 = null, p_4 = null, p_5 = null; // 要切换的五个JPanel

	private static MainFrame mainFrame;

	public MainFrame() {
		super("CardLayout Test");
		card = new CardLayout(5, 5); // 创建一个具有指定的水平和垂直间隙的新卡片布局
		pane = new JPanel(card); // JPanel的布局管理将被设置成CardLayout
		p = new JPanel(); // 构造放按钮的JPanel

		b_1 = new JButton("钱箱测试");
		b_2 = new JButton("串口测试");
		b_3 = new JButton("喇叭测试");
		b_4 = new JButton("wifi管理");
		b_5 = new JButton("其他测试");

		// 设置Button字体大小及样式等
		Font f = new Font("隶书", Font.BOLD, 25);
		b_1.setFont(f);
		b_2.setFont(f);
		b_3.setFont(f);
		b_4.setFont(f);
		b_5.setFont(f);

		// 是否可聚焦
		b_1.setFocusable(false);
		b_2.setFocusable(false);
		b_3.setFocusable(false);
		b_4.setFocusable(false);
		b_5.setFocusable(false);

		// 设置Button间距及Button大小
		b_1.setMargin(new Insets(2, 2, 2, 2));
		// b_1.setSize(100, 200);
		b_1.setPreferredSize(new Dimension(mButtonWidth, mButtonHeight));
		b_2.setMargin(new Insets(2, 2, 2, 2));
		b_2.setPreferredSize(new Dimension(mButtonWidth, mButtonHeight));
		b_3.setMargin(new Insets(2, 2, 2, 2));
		b_3.setPreferredSize(new Dimension(mButtonWidth, mButtonHeight));
		b_4.setMargin(new Insets(2, 2, 2, 2));
		b_4.setPreferredSize(new Dimension(mButtonWidth, mButtonHeight));
		b_5.setMargin(new Insets(2, 2, 2, 2));
		b_5.setPreferredSize(new Dimension(mButtonWidth, mButtonHeight));

		p.add(b_1);
		p.add(b_2);
		p.add(b_3);
		p.add(b_4);
		// p.add(b_5);
		// p_1 = new JPanel();
		CashDrawerPanel mCashDrawerPanel = new CashDrawerPanel();
		p_1 = CashDrawerPanel.cashDrawerPanel;
		// p_2 = new JPanel();
		SerialPortPanel mSerialPortPanel = new SerialPortPanel();
		p_2 = SerialPortPanel.serialPortPanel;
		// p_3 = new JPanel();
		HornPanel hornPanel = new HornPanel();
		p_3 = HornPanel.hornPanel;
		// p_4 = new JPanel();
		WifiPanel wifiPanel = new WifiPanel(mainFrame);
		p_4 = WifiPanel.wifiPanel;
		p_5 = new JPanel();

		pane.add(p_4, "p4");
		pane.add(p_1, "p1");
		pane.add(p_2, "p2");
		pane.add(p_3, "p3");

		// pane.add(p_5, "p5");

		b_1.addActionListener(new ActionListener() {
			// 直接翻转到p_1
			public void actionPerformed(ActionEvent e) {
				card.show(pane, "p1");
			}
		});
		b_2.addActionListener(new ActionListener() {
			// 直接翻转到p_2
			public void actionPerformed(ActionEvent e) {
				card.show(pane, "p2");
			}
		});
		b_3.addActionListener(new ActionListener() {
			// 直接翻转到p_3
			public void actionPerformed(ActionEvent e) {
				card.show(pane, "p3");
				
			}
		});

		b_4.addActionListener(new ActionListener() {
			// 直接翻转到p_4
			public void actionPerformed(ActionEvent e) {
				card.show(pane, "p4");
			}
		});

		b_5.addActionListener(new ActionListener() {
			// 直接翻转到p_5
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
		mainFrame = new MainFrame();
	}

}
