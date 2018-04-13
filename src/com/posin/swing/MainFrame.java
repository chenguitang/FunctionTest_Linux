package com.posin.swing;

//��γ��������Ҫ��Ϊ����չʾ���ʹ��CardLayout���ֹ����������������е�������в���
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.posin.global.Appconfig;
import com.posin.global.SocketConstant;
import com.posin.power.PowerManager;
import com.posin.socket.ServerSocketManager;
import com.posin.socket.SockectCallback;

/**
 * ��ҳ�棬����ҳ���л�
 * 
 * @author Greetty
 * 
 */
public class MainFrame extends JFrame {

	private int mButtonWidth = Appconfig.TOP_BUTTON_WIDTH;
	private int mButtonHeight = Appconfig.TOP_BUTTON_HEIGHT;

	private JPanel pane = null; // ��Ҫ��JPanel����JPanel�Ĳ��ֹ��������ó�CardLayout
	private JPanel p = null; // �Ű�ť��JPanel
	private CardLayout card = null; // CardLayout���ֹ�����
	private JButton b_1 = null, b_2 = null, b_3 = null, b_4 = null, b_5 = null,
			b_6 = null, b_7 = null; // ��ֱ�ӷ�ת��JPanel����İ�ť
	private JPanel p_1 = null, p_2 = null, p_3 = null, p_4 = null, p_5 = null,
			p_6 = null; // Ҫ�л���JPanel

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
		b_6 = new JButton("������ʱ��");
		b_7 = new JButton("�˳�");

		// ����Button�����С����ʽ��
		Font f = new Font("����", Font.BOLD, 25);
		b_1.setFont(f);
		b_2.setFont(f);
		b_3.setFont(f);
		b_4.setFont(f);
		b_5.setFont(f);
		b_6.setFont(f);
		b_7.setFont(f);

		// �Ƿ�ɾ۽�
		b_1.setFocusable(false);
		b_2.setFocusable(false);
		b_3.setFocusable(false);
		b_4.setFocusable(false);
		b_5.setFocusable(false);
		b_6.setFocusable(false);
		b_7.setFocusable(false);

		// ����Button��༰Button��С
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

		p.add(b_1);
		p.add(b_2);
		p.add(b_3);
		p.add(b_4);
		p.add(b_5);
		p.add(b_6);
		p.add(b_7);

		p_1 = CashDrawerPanel.getInstance().cashDrawerPanel;
		p_2 = SerialPortPanel.getInstance().serialPortPanel;
		p_3 = HornPanel.getInstance().hornPanel;
		p_4 = WifiPanel.getInstance().wifiPanel;
		p_5 = SecondaryTestPanel.getInstance().secTestPanel;
		p_6 = DateTimeSettings.getInstance().dateSettingPanel;

		pane.add(p_1, "p1");
		pane.add(p_2, "p2");
		pane.add(p_3, "p3");
		pane.add(p_4, "p4");
		pane.add(p_5, "p5");
		pane.add(p_6, "p6");

		initListenerOnclick();

		this.getContentPane().add(pane);
		this.getContentPane().add(p, BorderLayout.NORTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(Appconfig.PANELCARDWIDTH, Appconfig.PANELCARDHEIGHT);
		// this.setVisible(true);
	}

	/**
	 * ��ʼ������¼�
	 */
	private void initListenerOnclick() {
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

		b_6.addActionListener(new ActionListener() {
			// ֱ�ӷ�ת��p_6
			public void actionPerformed(ActionEvent e) {
				card.show(pane, "p6");
			}
		});

		b_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("================== exit =================");
//				System.exit(0);
				setVisible(false);
			}
		});
	}

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					// �����ػ���ť
					PowerManager.getInstance().startPowerListener();

					// ����Socket������������socketָ��
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
											System.out
													.println("open to functiontest now ...");
										}else {
											System.out.println("funtiontest isShowing ... ");
										}
										
									}else if(command.equals(SocketConstant.OPEN_SHUTDOWN_VIEW)){
										PowerManager.getInstance().showShutdownView();
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
