package com.posin.power;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import view.AlertDialog;

import com.posin.constant.CommandConstant;
import com.posin.utils.DeviceDetect;
import com.posin.utils.HwUtils;
import com.posin.utils.MacUtils;
import com.posin.utils.ModelUtils;
import com.posin.utils.NetworkInfo;
import com.posin.utils.SnUtils;
import com.posin.utils.UsbUtils;
import com.posin.utils.VersionUtils;

import flexjson.JSONSerializer;

public class RegistedMachine extends JFrame {

	private JPanel contentPane;
	private JButton setButton;
	private JButton exitButton;
	private JButton refreshButton;
	private JButton uploadButton;
	private JTextField textField;
	private JLabel messageLabel;

	private NetworkInfo netInfo;
	static final String SERVER_ADDR = "192.168.0.3";
	static final int SERVER_FTP_PORT = 9321;
	static final int SERVER_REG_PORT = 1921;

	private static RegistedMachine registedMachine = null;

	public static synchronized RegistedMachine getInstance() {
		if (registedMachine == null) {
			registedMachine = new RegistedMachine();
		}
		return registedMachine;
	}

	/**
	 * Create the frame.
	 */
	public RegistedMachine() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(250, 80, 1400, 900);
		setUndecorated(true);
//		setAlwaysOnTop(true);
		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		setContentPane(contentPane);

		JPanel setPanel = new JPanel();
		setPanel.setPreferredSize(new Dimension(1350, 60));
		setPanel.setLayout(new BorderLayout());
		setButton = new JButton("设置SN");
		setButton.setFocusable(false);
		setButton.setFont(new Font("宋体", Font.PLAIN, 24));
		setPanel.add(setButton, BorderLayout.WEST);

		exitButton = new JButton("退出");
		exitButton.setFocusable(false);
		exitButton.setFont(new Font("宋体", Font.PLAIN, 26));
		setPanel.add(exitButton, BorderLayout.EAST);

		textField = new JTextField();
		textField.setColumns(1);
		textField.setFont(new Font("Dialog", Font.PLAIN, 22));
		textField.setBackground(Color.WHITE);
		// textField.setPreferredSize(new
		// Dimension((850-50-setButton.WIDTH-500),40));
		setPanel.add(textField, BorderLayout.CENTER);
		contentPane.add(setPanel);

		JPanel messagePanel = new JPanel();
		messagePanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		messagePanel.setPreferredSize(new Dimension(1350, 700));
		messagePanel.setLayout(new BorderLayout());
		refreshButton = new JButton("刷新信息");
		refreshButton.setFocusable(false);
		refreshButton.setFont(new Font("宋体", Font.PLAIN, 24));
		JPanel buttonJPanel = new JPanel();
		buttonJPanel.setBorder(null);
		buttonJPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.ipadx = 10;
		c.ipady = 20;
		buttonJPanel.add(refreshButton, c);
		// buttonJPanel.setBackground(Color.white);
		messagePanel.add(buttonJPanel, BorderLayout.WEST);

		messageLabel = new JLabel();
		messageLabel.setFont(new Font("宋体", Font.BOLD, 18));
		messageLabel.setHorizontalAlignment(SwingConstants.LEFT);
		messageLabel
				.setText(formatStr("SN=EA08HD09A006 ", " model=120.215.321"));
		messagePanel.add(messageLabel, BorderLayout.CENTER);
		contentPane.add(messagePanel);

		JPanel uploadPanel = new JPanel();
		uploadPanel.setPreferredSize(new Dimension(1350, 60));
		uploadPanel.setLayout(new BorderLayout(0, 0));

		uploadButton = new JButton("上传机器记录");
		uploadButton.setFocusable(false);
		uploadButton.setFont(new Font("宋体", Font.PLAIN, 24));

		uploadPanel.add(uploadButton, BorderLayout.WEST);
		contentPane.add(uploadPanel);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowDeactivated(WindowEvent e) {
				System.out.println("RegistedMachine windowDeactivated");
//				setVisible(false);
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				System.out.println("RegistedMachine windowActivated");
			}
		});

		initEent();
		initData();

	}

	/**
	 * 初始化事件
	 */
	public void initEent() {
		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				registedMachine.setVisible(false);
			}
		});

		setButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String sn = textField.getText().toString().trim();
					if (SnUtils.isAvaildSN(sn)) {
						SnUtils.setSN(sn);
						refreshData();
					} else {
						alert("温馨提醒", "SN码无效，请重新设置...");
					}
				} catch (Throwable e1) {
					e1.printStackTrace();
				}

			}
		});

		refreshButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				refreshData();
			}
		});

		uploadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				uploadInfo();
			}
		});
	}

	/**
	 * 初始化数据
	 */
	public void initData() {
		refreshData();
	}

	/**
	 * 上传数据
	 */
	public void uploadInfo() {

		Socket s = null;
		try {
			String sn = SnUtils.getSN();
			if (!SnUtils.isAvaildSN(sn)) {
				alert("温馨提醒", "sn格式不正确");
				return;
			}

			if (netInfo.getEth0_ip() == null || netInfo.getWlan0_mac() == null) {
				alert("温馨提醒", "错误: 信息不全");
				return;
			}

			Map<String, Object> map = new HashMap<String, Object>();
			Map<String, Object> sub = new HashMap<String, Object>();

			sub.put("sn", sn);
			sub.put("model", ModelUtils.getModel());
			sub.put("hw", HwUtils.getHw());
			sub.put("version", VersionUtils.getVersion());
			sub.put("eth0", netInfo.getEth0_mac());
			sub.put("wlan0", netInfo.getWlan0_mac());

			map.put("info", sub);
			sub = DeviceDetect.getSystemInfo();
			map.putAll(sub);

			s = new Socket();
			s.connect(new InetSocketAddress(SERVER_ADDR, SERVER_REG_PORT), 5000);
			s.setSoTimeout(5000);
			OutputStream os = s.getOutputStream();

			OutputStreamWriter wr = new OutputStreamWriter(os);
			JSONSerializer serializer = new JSONSerializer();
			serializer.serialize(map, wr);
			wr.flush();
			os.flush();

			alert("温馨提示", "上传成功");

		} catch (Throwable e) {
			e.printStackTrace();
			alert("温馨提示", ("出错了: " + e.getMessage()));
		} finally {
			if (s != null) {
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 刷新数据
	 */
	public void refreshData() {
		String sn = null;
		String model = null;
		String hw = null;
		String version = null;
		String eth0 = null;
		String wlan0 = null;
		try {

			netInfo = MacUtils.getNetInfo();
			sn = formatStrInLine("SN=", SnUtils.getSN());
			model = formatStrInLine("<br>", "Model=", ModelUtils.getModel(),
					"<br/>");
			hw = formatStrInLine("Hw=", HwUtils.getHw());
			version = formatStrInLine("<br>", "Version=",
					VersionUtils.getVersion(), "<br/>");
			eth0 = formatStrInLine("eth0=", netInfo.getEth0_mac());
			wlan0 = formatStrInLine("<br>", "wlan0=", netInfo.getWlan0_mac());
			String buildRegString = UsbUtils.buildRegString();
			String uploadMessage = formatStr(sn, model, hw, version, eth0,
					wlan0, buildRegString);
			// String uploadMessage = formatStr(sn, eth0, wlan0);

			messageLabel.setText(uploadMessage);
			textField.setText("");

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * 格式化字符串为HTML字符串
	 * 
	 * @param meesage
	 * @return
	 */
	public String formatStr(String... meesage) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		for (int i = 0; i < meesage.length; i++) {
			sb.append(meesage[i]);
			sb.append("<br/>");
		}
		sb.append(" <html/>");
		return sb.toString();
	}

	/**
	 * 提示框
	 * 
	 * @param title
	 * @param message
	 */
	public void alert(String title, String message) {
		AlertDialog alertDialog = new AlertDialog(title, message);
		alertDialog.setVisible(true);
		// AlertDialog.showDialog(title, message);
	}

	/**
	 * 格式化字符串，行内拼接
	 * 
	 * @param meesage
	 * @return
	 */
	public String formatStrInLine(String... meesage) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < meesage.length; i++) {
			sb.append(meesage[i]);
		}
		return sb.toString();
	}

}
