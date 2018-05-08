package com.posin.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.posin.device.SerialPort;
import com.posin.global.Appconfig;
import com.posin.keystore.SoftKeyBoardPopup;
import com.posin.utils.ByteUtils;
import com.posin.utils.Proc;
import com.posin.utils.SerialUtil;

/**
 * @author Greetty
 * 
 * @desc 钱箱测试
 */
public class SerialPortPanel {

	private final Object mRecvLock = new Object();
	private final ByteArrayOutputStream mRecvStream = new ByteArrayOutputStream();
	final SerialPortDataReceiver mDataReceiver = new SerialPortDataReceiver();
	Font inputTypeFont = new Font("隶书", Font.PLAIN, 20);

	private static final long serialVersionUID = 1L;
	public JPanel serialPortPanel = null; // 根布局
	private JPanel mButtonJPanel = null; // 顶部操作JPanel
	private JPanel sendTipJpanel = null; // 发送提示及按钮JPanel
	private JPanel sendDataJpanel = null; // 发送数据JPanel
	private JPanel receiverTipJpanel = null; // 接收提示及按钮JPanel
	private JScrollPane receiverDataJpanel = null; // 接收数据JPanel
	private JPanel linePanel = null; // 切割线
	private JButton portButton = null, baudrateButton = null,
			switchButton = null;

	private TextField sendDatainput = null; // 文本发送的输入框
	private JTextArea receiverDatainput = null; // 文本接收的显示框

	private String mSelectPort = null; // 选择的端口
	private String mSelectBaudrate = null; // 选择的波特率
	private SerialPort sp = null;
	private boolean mRecvText = true;
	private boolean mSendText = true;

	private static final SerialPortPanel SERIALPORT_PANEL_INSTANCE=new SerialPortPanel();
	
	public static SerialPortPanel getInstance() {
		return SERIALPORT_PANEL_INSTANCE;
	}
	
	private SerialPortPanel() {
		serialPortPanel = new JPanel();
		serialPortPanel.setSize(new Dimension(Appconfig.PANELCARDWIDTH,
				Appconfig.PANELCARDHEIGHT));
		serialPortPanel.setBackground(Color.WHITE);
		// GridLayout gird = new GridLayout(6,0);
		serialPortPanel.setLayout(new GridBagLayout());

		// 中间容器，控制每行显示的内容
		// 屏幕分割线
		addLine(serialPortPanel, 0, 0, -8, Color.GRAY);
		initButtonJpanel();
		addLine(serialPortPanel, 0, 2, -8, Color.GRAY);
		initSendTipJanel();

		initSendDataJanel();

		initreceiverTipJpanel();

		initreceiverDataJpanel();

		// sendTipJpanel.setBackground(Color.BLUE);
		// sendDataJpanel.setBackground(Color.GREEN);
		// receiverTipJpanel.setBackground(Color.BLACK);
		receiverDataJpanel.setBackground(Color.GRAY);

		initButtonStyle();
		initListener();
		initListenerInput();
	}

	/**
	 * 监听输入框，软键盘
	 */
	private void initListenerInput() {

		final SoftKeyBoardPopup keyPopup = new SoftKeyBoardPopup(sendDatainput);
		
		sendDatainput.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out
						.println("=====================focusLost====================");
			}

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out
						.println("=====================focusGained====================");
				if (!keyPopup.isVisible()) {
					keyPopup.show(sendDatainput, 50,
							sendDatainput.getPreferredSize().height + 50);
					keyPopup.getSoftKeyBoardPanel().reset();
					keyPopup.repaint();
				}
			}
		});
	}

	/**
	 * 接收数据
	 */
	private void initreceiverDataJpanel() {
		GridBagConstraints c = new GridBagConstraints();
		receiverDataJpanel = new JScrollPane();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 6;
		c.ipadx = 0;
		c.ipady = 700;
		serialPortPanel.add(receiverDataJpanel, c);

		receiverDatainput = new JTextArea();
		receiverDatainput.setLineWrap(true);
		receiverDatainput.setWrapStyleWord(true);
		receiverDatainput.setFont(inputTypeFont);

		receiverDataJpanel.setViewportView(receiverDatainput);
		// receiverDataJpanel.add(receiverDatainput, BorderLayout.CENTER);
	}

	/**
	 * 接收数据标题提示
	 */
	private void initreceiverTipJpanel() {
		GridBagConstraints c = new GridBagConstraints();
		receiverTipJpanel = new JPanel();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 5;
		c.ipady = 0;
		c.ipadx = 0;
		serialPortPanel.add(receiverTipJpanel, c);
		receiverTipJpanel.setLayout(new BorderLayout());

		JLabel receiverLableJLabel = new JLabel("发送:");
		final JRadioButton textradioButton = new JRadioButton("文本");
		final JRadioButton hexradioButton = new JRadioButton("十六进制");
		JButton receiverClearButton = new JButton("清空");

		// 设置Button字体大小及样式等
		Font f = new Font("隶书", Font.PLAIN, 20);
		Font fontType = new Font("隶书", Font.PLAIN, 20);

		receiverLableJLabel.setFont(f);
		receiverLableJLabel.setBackground(Color.RED);
		receiverLableJLabel.setPreferredSize(new Dimension(100, 15));
		receiverTipJpanel.add(receiverLableJLabel, BorderLayout.WEST);

		textradioButton.setFont(fontType);
		textradioButton.setPreferredSize(new Dimension(80, 40));
		hexradioButton.setFont(fontType);
		hexradioButton.setPreferredSize(new Dimension(120, 40));

		receiverClearButton.setPreferredSize(new Dimension(100, 50));
		receiverTipJpanel.add(receiverClearButton, BorderLayout.EAST);

		JPanel sendTypeJPanel = new JPanel();
		sendTypeJPanel.add(textradioButton);
		sendTypeJPanel.add(hexradioButton);
		sendTypeJPanel.setLayout(new GridBagLayout());
		// sendTipJpanel.add(textradioButton,BorderLayout.NORTH);
		receiverTipJpanel.add(sendTypeJPanel, BorderLayout.CENTER);
		textradioButton.setSelected(true);

		// 监听接收类型
		textradioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				onRecvTypeChanged(true);
				hexradioButton.setSelected(false);
			}
		});
		// 监听接收类型
		hexradioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				onRecvTypeChanged(false);
				textradioButton.setSelected(false);
			}
		});

		// 清空
		receiverClearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				receiverDatainput.setText("");
				mRecvStream.reset();
				System.out.println("clear receiver data ... ");
			}
		});
	}

	/**
	 * 监听接收数据类型
	 * 
	 * @param recvText
	 *            接收数据是否为文本类型
	 */
	void onRecvTypeChanged(boolean recvText) {
		if (mRecvText == recvText)
			return;
		mRecvText = recvText;
		updateRecvView();
	}

	/**
	 * 发送数据
	 */
	private void initSendDataJanel() {
		GridBagConstraints c = new GridBagConstraints();
		sendDataJpanel = new JPanel();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		c.ipady = 20;
		c.ipadx = 0;
		serialPortPanel.add(sendDataJpanel, c);

		sendDataJpanel.setLayout(new BorderLayout());
		sendDatainput = new TextField();
		sendDatainput.setFont(inputTypeFont);
		sendDataJpanel.add(sendDatainput, BorderLayout.CENTER);

	}

	/**
	 * 发送数据标题提示
	 */
	private void initSendTipJanel() {
		sendTipJpanel = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		c.ipady = 0;
		c.ipadx = 0;
		serialPortPanel.add(sendTipJpanel, c);
		sendTipJpanel.setLayout(new BorderLayout());

		JLabel sendLableJLabel = new JLabel("发送:");
		final JRadioButton textradioButton = new JRadioButton("文本");
		final JRadioButton hexradioButton = new JRadioButton("十六进制");
		JButton sendButton = new JButton("发送");

		// 设置Button字体大小及样式等
		Font f = new Font("隶书", Font.PLAIN, 20);
		Font fontType = new Font("隶书", Font.PLAIN, 20);

		sendLableJLabel.setFont(f);
		sendLableJLabel.setBackground(Color.RED);
		sendLableJLabel.setPreferredSize(new Dimension(100, 15));
		sendTipJpanel.add(sendLableJLabel, BorderLayout.WEST);

		textradioButton.setFont(fontType);
		textradioButton.setPreferredSize(new Dimension(80, 40));
		hexradioButton.setFont(fontType);
		hexradioButton.setPreferredSize(new Dimension(120, 40));

		sendButton.setPreferredSize(new Dimension(100, 50));
		sendTipJpanel.add(sendButton, BorderLayout.EAST);

		textradioButton.setSelected(true); // 默认为文本

		JPanel sendTypeJPanel = new JPanel();
		sendTypeJPanel.add(textradioButton);
		sendTypeJPanel.add(hexradioButton);
		sendTypeJPanel.setLayout(new GridBagLayout());
		// sendTipJpanel.add(textradioButton,BorderLayout.NORTH);
		sendTipJpanel.add(sendTypeJPanel, BorderLayout.CENTER);

		// 点击发送按钮
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sendData();
			}
		});

		// 监听发送类型
		textradioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// mSendText = true;
				onSendTypeChanged(true);
				hexradioButton.setSelected(false);
			}
		});
		// 监听发送类型
		hexradioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// mSendText = false;
				onSendTypeChanged(false);
				textradioButton.setSelected(false);
			}
		});
	}

	/**
	 * 监听发送类型
	 * 
	 * @param sendText
	 *            发送类型是否为文本
	 */
	private void onSendTypeChanged(boolean sendText) {
		System.out.println("send type is text: " + sendText);
		if (mSendText == sendText)
			return;
		mSendText = sendText;
		String txt = sendDatainput.getText().toString();
		if (txt == null || txt.length() == 0)
			return;

		try {
			if (mSendText)
				sendDatainput.setText(new String(ByteUtils
						.hexStringToBytes(" ",txt)));
			else
				sendDatainput
						.setText(ByteUtils.bytesToHexString(txt.getBytes()));
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println("Error: " + e.getMessage());
		}
	}

	/**
	 * 顶部选择按钮
	 */
	private void initButtonJpanel() {
		mButtonJPanel = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 0;
		serialPortPanel.add(mButtonJPanel, c);

		portButton = new JButton("端口");
		baudrateButton = new JButton("波特率");
		switchButton = new JButton("打开");

		mButtonJPanel.add(portButton);
		mButtonJPanel.add(baudrateButton);
		mButtonJPanel.add(switchButton);
	}

	/**
	 * 初始化Button样式
	 */
	private void initButtonStyle() {
		// 设置Button大小
		portButton.setPreferredSize(new Dimension(140, 60));
		baudrateButton.setPreferredSize(new Dimension(140, 60));
		switchButton.setPreferredSize(new Dimension(140, 60));

		// 设置Button字体大小及样式等
		Font f = new Font("隶书", Font.PLAIN, 20);
		portButton.setFont(f);
		baudrateButton.setFont(f);
		switchButton.setFont(f);
	}

	/**
	 * 按钮的点击事件
	 */
	private void initListener() {
		// 端口
		portButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectPort();
			}
		});

		// 波特率
		baudrateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectBaudrate();
			}
		});
		// 打开or关闭
		switchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				switchListener();
			}
		});
	}

	/**
	 * 选择接口
	 */
	private void selectPort() {
		try {
			String[] allDevices = SerialUtil.findAllDevices(); // 所有串口端口

			mSelectPort = (String) JOptionPane.showInputDialog(null, "请选择串口端口",
					"端口", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(
							"blue.gif"), allDevices, allDevices[0]);

			for (int i = 0; i < allDevices.length; i++) {
				System.out.println("devices: " + allDevices[i]);
			}
			System.out.println("select port: " + mSelectPort);

			if (mSelectPort != null) {
				portButton.setText(mSelectPort);
			}
		} catch (Throwable e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 选择波特率
	 */
	private void selectBaudrate() {
		try {
			Object[] possibleValues = { "9600", "19200", "38400", "115200" }; // 用户的选择波特率
			mSelectBaudrate = (String) JOptionPane.showInputDialog(null,
					"请选择波特率", "波特率", JOptionPane.INFORMATION_MESSAGE, null,
					possibleValues, possibleValues[0]);
			if (mSelectBaudrate != null) {
				baudrateButton.setText(mSelectBaudrate);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * 开始测试或关闭测试
	 */
	private void switchListener() {
		if (mSelectPort == null) {
			JOptionPane.showMessageDialog(null, "端口不能为空，请选择端口");// 提示用户选择端口
			return;
		}

		if (mSelectBaudrate == null) {
			JOptionPane.showMessageDialog(null, "波特率不能为空，请选择择波特率");// 提示用户选择波特率
			return;
		}
		try {
			if (sp == null) {
				sp = new SerialPort();
				int mBaudrate = Integer.parseInt(mSelectBaudrate);
				sp.open(mSelectPort, mBaudrate, SerialPort.DATABITS_8,
						SerialPort.PARITY_NONE, SerialPort.STOPBITS_1,
						SerialPort.FLOWCONTROL_NONE);
				System.out.println();
				switchButton.setText("关闭");

				// 开始接收监听信息
				mDataReceiver.start(sp.getInputStream());
			} else {
				sp.close();
				sp = null;
				switchButton.setText("打开");
				mDataReceiver.stop();
			}
		} catch (IOException e) {
			if (sp != null) {
				sp.close();
				sp = null;
			}
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 发送数据
	 */
	private void sendData() {
		byte[] data = null;
		try {
			if (sp == null) {
				JOptionPane.showMessageDialog(null, "请打开串口，再发送数据测试");
				return;
			}

			String sendData = sendDatainput.getText().toString();
			if (sendData == null) {
				JOptionPane.showMessageDialog(null, "发送数据不能为空");
				return;
			}
			System.out.println("send data string is: " + sendData);
			System.out.println("mSendText: " + mSendText);

			if (mSendText) {
				data = sendData.getBytes();
			} else {
				data = ByteUtils.hexStringToBytes(" ",sendData);
			}

			if (data != null) {
				sp.getOutputStream().write(data);
			}

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			JOptionPane.showMessageDialog(null, "出错了： " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 添加横线
	 * 
	 * @param fatherJpanel
	 *            父布局
	 * @param gridx
	 *            X轴位置
	 * @param gridy
	 *            Y轴位置
	 * @param ipady
	 *            Y轴内撑大值（Android上的padding）
	 * @param color
	 */
	public void addLine(JPanel fatherJpanel, int gridx, int gridy, int ipady,
			Color color) {
		JPanel linePanel = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = gridx;
		c.gridy = gridy;
		c.ipady = ipady;
		linePanel.setBackground(color);
		fatherJpanel.add(linePanel, c);
	}

	/**
	 * 接收数据线程
	 * 
	 * @author Greetty
	 * 
	 */
	private class SerialPortDataReceiver implements Runnable {
		volatile Thread mThread = null;
		private InputStream mInput = null;
		volatile boolean mExitRequest = false;

		public synchronized boolean isRunning() {
			return mThread != null;
		}

		public synchronized void start(InputStream is) {
			if (mThread != null)
				return;

			mInput = is;
			mExitRequest = false;

			mThread = new Thread(this);
			mThread.start();
		}

		public synchronized void stop() {
			if (mThread == null)
				return;

			mExitRequest = true;
			if (mInput != null) {
				try {
					mInput.close();
				} catch (Throwable e) {
				}
				mInput = null;
			}

			// try {
			// mThread.join();
			// } catch (Throwable e) {
			// }
			// mThread = null;

			join();
		}

		public void join() {
			while (mThread != null) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}
		}

		@Override
		public void run() {
			System.out.println("receiver thread : start...");
			try {
				read();
			} finally {
				mInput = null;
				mThread = null;
				System.out.println("receiver thread : stop...");
			}
		}

		private void read() {
			int size;
			final byte[] data = new byte[128];

			try {
				while (mInput != null && !mExitRequest) {
					if (mInput.available() > 0) {
						size = mInput.read(data);
						System.out.println("recv : " + size + " bytes ");
						if (size > 0) {
							ByteBuffer bb = ByteBuffer.allocate(size);
							bb.put(data, 0, size);

							// 更新数据
							synchronized (mRecvLock) {
								mRecvStream.write(bb.array());
							}
							// 更新数据显示
							updateRecvView();
						} else {
							break;
						}
					} else {
						Thread.sleep(50);
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * 更新接收窗口的显示数据
	 */
	public void updateRecvView() {
		String txt;
		try {
			if (mRecvText) {
				synchronized (mRecvLock) {
					txt = new String(mRecvStream.toByteArray(), "utf-8");
					// txt = mRecvStream.toString();
				}
			} else {
				synchronized (mRecvLock) {
					txt = ByteUtils.bytesToHexString(mRecvStream.toByteArray());
				}
			}

			if (txt != null) {
				if (txt.length() > 0) {
					System.out.println("recerver data is: " + txt);
					receiverDatainput.setText(txt);
				} else {
					System.out.println("recerver data length is 0 ");
					receiverDatainput.append("recerver data length is 0 \n");
				}
			} else {
				System.out
						.println("receiver data is null ,please check you code ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error: " + e.getMessage());
		}
	}
}
