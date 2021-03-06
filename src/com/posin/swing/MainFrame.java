package com.posin.swing;

//这段程序代码主要是为读者展示如何使用CardLayout布局管理器针对内容面板中的组件进行布局
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.posin.global.Appconfig;
import com.posin.global.SocketConstant;
import com.posin.power.PowerManager;
import com.posin.power.RegistedMachine;
import com.posin.socket.ServerSocketManager;
import com.posin.socket.SockectCallback;

/**
 * 主页面，控制页面切换
 * 
 * @author Greetty
 * 
 */
public class MainFrame extends JFrame {


	private static final long serialVersionUID = 1L;
	private int mButtonWidth = Appconfig.TOP_BUTTON_WIDTH;
	private int mButtonHeight = Appconfig.TOP_BUTTON_HEIGHT;

	private JPanel pane = null; // 主要的JPanel，该JPanel的布局管理将被设置成CardLayout
	private JPanel p = null; // 放按钮的JPanel
	private CardLayout card = null; // CardLayout布局管理器
	private JButton b_1 = null, b_2 = null, b_3 = null, b_4 = null, b_5 = null,
			b_6 = null, b_7 = null, b_8 = null, b_eth0 = null; // 可直接翻转到JPanel组件的按钮
	private JPanel p_1 = null, p_2 = null, p_3 = null, p_4 = null, p_5 = null,
			p_6, p_7 = null, p_eth0 = null; // 要切换的JPanel

	public MainFrame() {
		super("CardLayout Test");
		setUndecorated(true);
		setAlwaysOnTop(true);
		card = new CardLayout(5, 5); // 创建一个具有指定的水平和垂直间隙的新卡片布局
		pane = new JPanel(card); // JPanel的布局管理将被设置成CardLayout
		p = new JPanel(); // 构造放按钮的JPanel

		b_1 = new JButton("钱箱测试");
		b_2 = new JButton("串口测试");
		b_3 = new JButton("喇叭测试");
		b_4 = new JButton("wifi管理");
		b_5 = new JButton("副屏测试");
		b_6 = new JButton("日期与时间");
		b_7 = new JButton("关于");
		b_8 = new JButton("退出");
		b_eth0 = new JButton("以太网设置");

		// 设置Button字体大小及样式等
		Font f = new Font("隶书", Font.BOLD, 25);
		b_1.setFont(f);
		b_2.setFont(f);
		b_3.setFont(f);
		b_4.setFont(f);
		b_5.setFont(f);
		b_6.setFont(f);
		b_7.setFont(f);
		b_8.setFont(f);
		b_eth0.setFont(f);

		// 是否可聚焦
		b_1.setFocusable(false);
		b_2.setFocusable(false);
		b_3.setFocusable(false);
		b_4.setFocusable(false);
		b_5.setFocusable(false);
		b_6.setFocusable(false);
		b_7.setFocusable(false);
		b_8.setFocusable(false);
		b_eth0.setFocusable(false);

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
		b_6.setMargin(new Insets(2, 2, 2, 2));
		b_6.setPreferredSize(new Dimension(mButtonWidth, mButtonHeight));
		b_7.setMargin(new Insets(2, 2, 2, 2));
		b_7.setPreferredSize(new Dimension(mButtonWidth, mButtonHeight));
		b_8.setMargin(new Insets(2, 2, 2, 2));
		b_8.setPreferredSize(new Dimension(mButtonWidth, mButtonHeight));
		b_eth0.setMargin(new Insets(2, 2, 2, 2));
		b_eth0.setPreferredSize(new Dimension(mButtonWidth, mButtonHeight));

		p.add(b_1);
		p.add(b_2);
		p.add(b_3);
		p.add(b_4);
		p.add(b_5);
		p.add(b_6);
		p.add(b_eth0);
		p.add(b_7);
		p.add(b_8);

		p_1 = CashDrawerPanel.getInstance().cashDrawerPanel;
		p_2 = SerialPortPanel.getInstance().serialPortPanel;
		p_3 = HornPanel.getInstance().hornPanel;
		p_4 = WifiPanel.getInstance().wifiPanel;
		p_5 = SecondaryTestPanel.getInstance().secTestPanel;
		p_6 = DateTimeSettings.getInstance().dateSettingPanel;
		p_7 = AboutPanel.getInstance().aboutPanel;
		p_eth0 = EthernetSettingPanel.getInstance().ethernetPanel;

		pane.add(p_1, "p1");
		pane.add(p_2, "p2");
		pane.add(p_3, "p3");
		pane.add(p_4, "p4");
		pane.add(p_5, "p5");
		pane.add(p_6, "p6");
		pane.add(p_7, "p7");
		pane.add(p_eth0, "p_eth0");

		initListenerOnclick();

		this.getContentPane().add(pane);
		this.getContentPane().add(p, BorderLayout.NORTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(Appconfig.PANELCARDWIDTH, Appconfig.PANELCARDHEIGHT);
		// this.setVisible(true);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				System.out.println("windowActivated");
			}
		});
	}

	/**
	 * 初始化点击事件
	 */
	private void initListenerOnclick() {
		b_1.addActionListener(new ActionListener() {
			// 直接翻转到p_1
			public void actionPerformed(ActionEvent e) {
				card.show(pane, "p1");
				DateTimeSettings.getInstance().closeAllDialog();
			}
		});
		b_2.addActionListener(new ActionListener() {
			// 直接翻转到p_2
			public void actionPerformed(ActionEvent e) {
				card.show(pane, "p2");
				DateTimeSettings.getInstance().closeAllDialog();
			}
		});
		b_3.addActionListener(new ActionListener() {
			// 直接翻转到p_3
			public void actionPerformed(ActionEvent e) {
				card.show(pane, "p3");
				DateTimeSettings.getInstance().closeAllDialog();
			}
		});

		b_4.addActionListener(new ActionListener() {
			// 直接翻转到p_4
			public void actionPerformed(ActionEvent e) {
				card.show(pane, "p4");
				DateTimeSettings.getInstance().closeAllDialog();
			}
		});

		b_5.addActionListener(new ActionListener() {
			// 直接翻转到p_5
			public void actionPerformed(ActionEvent e) {
				card.show(pane, "p5");
			}
		});

		b_6.addActionListener(new ActionListener() {
			// 直接翻转到p_6
			public void actionPerformed(ActionEvent e) {
				card.show(pane, "p6");
				DateTimeSettings.getInstance().closeAllDialog();
			}
		});

		b_7.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				card.show(pane, "p7");
				DateTimeSettings.getInstance().closeAllDialog();
			}
		});

		b_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("================== exit =================");
				setVisible(false);
				// 关闭副屏显示页面
				SecondaryTestPanel.getInstance().closeSecPage();

			}
		});

		b_eth0.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				card.show(pane, "p_eth0");
				DateTimeSettings.getInstance().closeAllDialog();
			}
		});
	}

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// 监听关机按钮
					PowerManager.getInstance().startPowerListener();

					// 创建Socket服务器，监听socket指令
					ServerSocketManager.getInstance().startSocketServer(
							new SockectCallback() {
								MainFrame mainFrame = null;

								@Override
								public void receiveCommad(String command) {
									System.out.println("command: " + command);
									if (command
											.equals(SocketConstant.OPEN_FUNCTIONTEST)) {
										if (mainFrame == null) {
											mainFrame = new MainFrame();
										}

										if (!mainFrame.isShowing()) {
											mainFrame.setVisible(true);
											mainFrame.setEnabled(true);
											mainFrame.setFocusable(true);
											// mainFrame.setFocusableWindowState(true);
											System.out
													.println("open to functiontest now ...");
										} else {
											System.out
													.println("funtiontest isShowing ... ");
										}

									} else if (command
											.equals(SocketConstant.OPEN_SHUTDOWN_VIEW)) {
										PowerManager.getInstance()
												.showShutdownView();
									} else if (command
											.equals(SocketConstant.OPEN_MINIPOS_SETTINGS)) {
										RegistedMachine.getInstance()
												.setVisible(true);
									}

								}
							});

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
