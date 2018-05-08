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
 * @desc Ǯ�����
 */
public class SerialPortPanel {

	private final Object mRecvLock = new Object();
	private final ByteArrayOutputStream mRecvStream = new ByteArrayOutputStream();
	final SerialPortDataReceiver mDataReceiver = new SerialPortDataReceiver();
	Font inputTypeFont = new Font("����", Font.PLAIN, 20);

	private static final long serialVersionUID = 1L;
	public JPanel serialPortPanel = null; // ������
	private JPanel mButtonJPanel = null; // ��������JPanel
	private JPanel sendTipJpanel = null; // ������ʾ����ťJPanel
	private JPanel sendDataJpanel = null; // ��������JPanel
	private JPanel receiverTipJpanel = null; // ������ʾ����ťJPanel
	private JScrollPane receiverDataJpanel = null; // ��������JPanel
	private JPanel linePanel = null; // �и���
	private JButton portButton = null, baudrateButton = null,
			switchButton = null;

	private TextField sendDatainput = null; // �ı����͵������
	private JTextArea receiverDatainput = null; // �ı����յ���ʾ��

	private String mSelectPort = null; // ѡ��Ķ˿�
	private String mSelectBaudrate = null; // ѡ��Ĳ�����
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

		// �м�����������ÿ����ʾ������
		// ��Ļ�ָ���
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
	 * ��������������
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
	 * ��������
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
	 * �������ݱ�����ʾ
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

		JLabel receiverLableJLabel = new JLabel("����:");
		final JRadioButton textradioButton = new JRadioButton("�ı�");
		final JRadioButton hexradioButton = new JRadioButton("ʮ������");
		JButton receiverClearButton = new JButton("���");

		// ����Button�����С����ʽ��
		Font f = new Font("����", Font.PLAIN, 20);
		Font fontType = new Font("����", Font.PLAIN, 20);

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

		// ������������
		textradioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				onRecvTypeChanged(true);
				hexradioButton.setSelected(false);
			}
		});
		// ������������
		hexradioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				onRecvTypeChanged(false);
				textradioButton.setSelected(false);
			}
		});

		// ���
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
	 * ����������������
	 * 
	 * @param recvText
	 *            ���������Ƿ�Ϊ�ı�����
	 */
	void onRecvTypeChanged(boolean recvText) {
		if (mRecvText == recvText)
			return;
		mRecvText = recvText;
		updateRecvView();
	}

	/**
	 * ��������
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
	 * �������ݱ�����ʾ
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

		JLabel sendLableJLabel = new JLabel("����:");
		final JRadioButton textradioButton = new JRadioButton("�ı�");
		final JRadioButton hexradioButton = new JRadioButton("ʮ������");
		JButton sendButton = new JButton("����");

		// ����Button�����С����ʽ��
		Font f = new Font("����", Font.PLAIN, 20);
		Font fontType = new Font("����", Font.PLAIN, 20);

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

		textradioButton.setSelected(true); // Ĭ��Ϊ�ı�

		JPanel sendTypeJPanel = new JPanel();
		sendTypeJPanel.add(textradioButton);
		sendTypeJPanel.add(hexradioButton);
		sendTypeJPanel.setLayout(new GridBagLayout());
		// sendTipJpanel.add(textradioButton,BorderLayout.NORTH);
		sendTipJpanel.add(sendTypeJPanel, BorderLayout.CENTER);

		// ������Ͱ�ť
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sendData();
			}
		});

		// ������������
		textradioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// mSendText = true;
				onSendTypeChanged(true);
				hexradioButton.setSelected(false);
			}
		});
		// ������������
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
	 * ������������
	 * 
	 * @param sendText
	 *            ���������Ƿ�Ϊ�ı�
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
	 * ����ѡ��ť
	 */
	private void initButtonJpanel() {
		mButtonJPanel = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 0;
		serialPortPanel.add(mButtonJPanel, c);

		portButton = new JButton("�˿�");
		baudrateButton = new JButton("������");
		switchButton = new JButton("��");

		mButtonJPanel.add(portButton);
		mButtonJPanel.add(baudrateButton);
		mButtonJPanel.add(switchButton);
	}

	/**
	 * ��ʼ��Button��ʽ
	 */
	private void initButtonStyle() {
		// ����Button��С
		portButton.setPreferredSize(new Dimension(140, 60));
		baudrateButton.setPreferredSize(new Dimension(140, 60));
		switchButton.setPreferredSize(new Dimension(140, 60));

		// ����Button�����С����ʽ��
		Font f = new Font("����", Font.PLAIN, 20);
		portButton.setFont(f);
		baudrateButton.setFont(f);
		switchButton.setFont(f);
	}

	/**
	 * ��ť�ĵ���¼�
	 */
	private void initListener() {
		// �˿�
		portButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectPort();
			}
		});

		// ������
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
			String[] allDevices = SerialUtil.findAllDevices(); // ���д��ڶ˿�

			mSelectPort = (String) JOptionPane.showInputDialog(null, "��ѡ�񴮿ڶ˿�",
					"�˿�", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(
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
	 * ѡ������
	 */
	private void selectBaudrate() {
		try {
			Object[] possibleValues = { "9600", "19200", "38400", "115200" }; // �û���ѡ������
			mSelectBaudrate = (String) JOptionPane.showInputDialog(null,
					"��ѡ������", "������", JOptionPane.INFORMATION_MESSAGE, null,
					possibleValues, possibleValues[0]);
			if (mSelectBaudrate != null) {
				baudrateButton.setText(mSelectBaudrate);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ʼ���Ի�رղ���
	 */
	private void switchListener() {
		if (mSelectPort == null) {
			JOptionPane.showMessageDialog(null, "�˿ڲ���Ϊ�գ���ѡ��˿�");// ��ʾ�û�ѡ��˿�
			return;
		}

		if (mSelectBaudrate == null) {
			JOptionPane.showMessageDialog(null, "�����ʲ���Ϊ�գ���ѡ��������");// ��ʾ�û�ѡ������
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
				switchButton.setText("�ر�");

				// ��ʼ���ռ�����Ϣ
				mDataReceiver.start(sp.getInputStream());
			} else {
				sp.close();
				sp = null;
				switchButton.setText("��");
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
	 * ��������
	 */
	private void sendData() {
		byte[] data = null;
		try {
			if (sp == null) {
				JOptionPane.showMessageDialog(null, "��򿪴��ڣ��ٷ������ݲ���");
				return;
			}

			String sendData = sendDatainput.getText().toString();
			if (sendData == null) {
				JOptionPane.showMessageDialog(null, "�������ݲ���Ϊ��");
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
			JOptionPane.showMessageDialog(null, "�����ˣ� " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * ��Ӻ���
	 * 
	 * @param fatherJpanel
	 *            ������
	 * @param gridx
	 *            X��λ��
	 * @param gridy
	 *            Y��λ��
	 * @param ipady
	 *            Y���ڳŴ�ֵ��Android�ϵ�padding��
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
	 * ���������߳�
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

							// ��������
							synchronized (mRecvLock) {
								mRecvStream.write(bb.array());
							}
							// ����������ʾ
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
	 * ���½��մ��ڵ���ʾ����
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
