package com.posin.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.posin.constant.EthernetConstant;
import com.posin.ethernet.Eth0Utils;
import com.posin.ethernet.EthernetStateThread;
import com.posin.ethernet.EthernetStateThread.EthernetStateListener;
import com.posin.ethernet.EthernetThread;
import com.posin.ethernet.EthernetThread.EthernetListener;
import com.posin.ethernet.EthernetUtils;
import com.posin.ethernet.ProcessEthernetUtils;
import com.posin.utils.PropertiesUtils;
import com.posin.view.EthernetInputDialog;
import com.posin.view.EthernetInputDialog.ComfirmListener;

public class EthernetSettingPanel implements ActionListener, ComfirmListener,
		EthernetListener, EthernetStateListener {

	// private static final String ETHERNET_CONFIGURE_PATH =
	// "/etc/ethernet.prop";
	// private static final String ETHERNET_IS_WITHIN_KEY = "ro.posin.eth0";

	public JPanel ethernetPanel;
	private JPanel contentPanel;

	private JCheckBox eth0CheckBox;
	private JButton restartButton;
	private JButton eth0SettingButton;
	private JLabel eth0StateLabel;
	private JLabel eth0Address;
	private JLabel eth0MaskCode;
	private JLabel eth0MacAddress;

	private Font mTitleFont;
	private Font mButtonFont;
	private Font mCheckBoxFont;
	private Font mStateFont;

	private String eth0StateTip = "以太网状态：";
	private String eth0AddressTip = "       IP地址：";
	private String eth0MaskCodeTip = "          掩码：";
	private String eth0MacAddressTip = "  MAC地址：";
	private final String eth0CheckBoxTxt = "启用以太网服务程序";
	private final String restartButtonTxt = "重启以太网";
	private final String eth0SettingButtonTxt = "IP地址设置";

	private static class EthernetHolder {
		private static final EthernetSettingPanel ETHERNET_SETTING_INSTANCE = new EthernetSettingPanel();
	}

	public static EthernetSettingPanel getInstance() {
		return EthernetHolder.ETHERNET_SETTING_INSTANCE;
	}

	public EthernetSettingPanel() {

		// EthernetThread.getInstance().start();
		EthernetStateThread.getInstance().start();
		initConfig();
		initView();
		// updateSettingView();
		initSyncView();
		initListener();

	}

	private void initListener() {
		eth0CheckBox.addActionListener(this);
		restartButton.addActionListener(this);
		eth0SettingButton.addActionListener(this);
		EthernetThread.getInstance().setEthernetListener(this);
		EthernetStateThread.getInstance().setEthernetStateListener(this);
	}

	public void initSyncView() {
		// 读取系统保存文件，判断并显示是否使用本程序管理以太网
		String eth0Model = PropertiesUtils.getPro(
				EthernetConstant.ETHERNET_CONFIGURE_PATH,
				EthernetConstant.ETHERNET_IS_WITHIN_KEY);
		eth0CheckBox.setSelected(eth0Model.equals("yes"));

		if (eth0CheckBox.isSelected()) {
			restartButton.setEnabled(true);
			eth0SettingButton.setEnabled(true);
			eth0StateLabel.setText(eth0StateTip + "已启用，网线未连接");
			EthernetStateThread.getInstance().isUseThisManager(true);
		} else {
			restartButton.setEnabled(false);
			eth0SettingButton.setEnabled(false);
			eth0StateLabel.setText(eth0StateTip + "未启用，网线未连接");
			EthernetStateThread.getInstance().isUseThisManager(false);
		}

		try {
			eth0MacAddress.setText(eth0MacAddressTip + Eth0Utils.getEth0Mac());
			eth0Address.setText(eth0AddressTip + Eth0Utils.getIPAddress());
			eth0MaskCode.setText(eth0MaskCodeTip + Eth0Utils.getMask());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void initConfig() {

		mTitleFont = new Font("楷体", Font.PLAIN, 25);
		mButtonFont = new Font("楷体", Font.PLAIN, 20);
		mCheckBoxFont = new Font("隶书", Font.PLAIN, 22);
		mStateFont = new Font("楷体", Font.PLAIN, 20);
	}

	private void initView() {
		ethernetPanel = new JPanel();
		contentPanel = new JPanel();
		// 设置布局方式
		ethernetPanel.setLayout(new BorderLayout());

		// 顶部分割线
		JLabel topLine = new JLabel();
		topLine.setPreferredSize(new Dimension(1920, 70));
		topLine.setText("本程序为同时使用WIFI和以太网而设置，如需同时使用，请用开启本程序管理以太网");
		topLine.setFont(mTitleFont);
		topLine.setHorizontalAlignment(JLabel.CENTER);
		topLine.setForeground(Color.white);
		topLine.setOpaque(true);
		topLine.setBackground(new Color(77, 111, 113));
		ethernetPanel.add(topLine, BorderLayout.NORTH);

		// 内容显示父布局
		ethernetPanel.add(contentPanel, BorderLayout.CENTER);

		// 设置内容显示页面的布局方式
		contentPanel.setLayout(new GridBagLayout());
		// 设置边框
		contentPanel.setBorder(BorderFactory.createLineBorder(Color.gray));

		// 启用以太网服务程序，重启以太网，IP设置父亲容器
		JPanel actionPanel = new JPanel();
		actionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		actionPanel.setPreferredSize(new Dimension(1000, 80));
		contentPanel.add(actionPanel,
				createGBC(GridBagConstraints.HORIZONTAL, 0, 0, 0, 0, 0, 0));

		// 启用或取消本服务程序按钮
		eth0CheckBox = new JCheckBox();
		eth0CheckBox.setPreferredSize(new Dimension(240, 40));
		// eth0CheckBox.setBackground(Color.gray);
		eth0CheckBox.setFont(mCheckBoxFont);
		eth0CheckBox.setText(eth0CheckBoxTxt);
		eth0CheckBox.setFocusable(false);
		actionPanel.add(eth0CheckBox);
		// 重启以太网按钮
		restartButton = new JButton(restartButtonTxt);
		restartButton.setPreferredSize(new Dimension(140, 60));
		restartButton.setHorizontalAlignment(SwingConstants.CENTER);
		restartButton.setFocusable(false);
		restartButton.setFont(mButtonFont);
		actionPanel.add(restartButton);
		// IP设置
		eth0SettingButton = new JButton(eth0SettingButtonTxt);
		eth0SettingButton.setPreferredSize(new Dimension(140, 60));
		eth0SettingButton.setHorizontalAlignment(SwingConstants.CENTER);
		eth0SettingButton.setFocusable(false);
		eth0SettingButton.setFont(mButtonFont);
		actionPanel.add(eth0SettingButton);

		// 分割线
		addLine(contentPanel, 0, 1, -9, Color.gray);
		eth0StateLabel = new JLabel(eth0StateTip + "未启用，网线未连接");
		eth0Address = new JLabel(eth0AddressTip + "null");
		eth0MaskCode = new JLabel(eth0MaskCodeTip + "null");
		eth0MacAddress = new JLabel(eth0MacAddressTip + "null");

		eth0StateLabel.setPreferredSize(new Dimension(1000, 25));
		eth0Address.setPreferredSize(new Dimension(1000, 25));
		eth0MaskCode.setPreferredSize(new Dimension(1000, 25));
		eth0MacAddress.setPreferredSize(new Dimension(1000, 25));

		eth0StateLabel.setFont(mStateFont);
		eth0Address.setFont(mStateFont);
		eth0MaskCode.setFont(mStateFont);
		eth0MacAddress.setFont(mStateFont);

		contentPanel.add(eth0StateLabel,
				createGBC(GridBagConstraints.HORIZONTAL, 0, 2, 0, 0, 0, 0));
		contentPanel.add(eth0Address,
				createGBC(GridBagConstraints.HORIZONTAL, 0, 3, 0, 0, 0, 0));
		contentPanel.add(eth0MaskCode,
				createGBC(GridBagConstraints.HORIZONTAL, 0, 4, 0, 0, 0, 0));
		contentPanel.add(eth0MacAddress,
				createGBC(GridBagConstraints.HORIZONTAL, 0, 5, 0, 0, 0, 0));
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
	 * 生成GridBagConstraints
	 * 
	 * @param fill
	 *            布满屏幕的方向
	 * @param gridx
	 *            x轴位置
	 * @param gridy
	 *            y轴位置
	 * @param ipady
	 *            y轴撑大距离
	 * @param ipadx
	 *            x轴撑大距离
	 * @param weightx
	 *            x轴比重
	 * @param weighty
	 *            y轴比重
	 * @return
	 */
	public GridBagConstraints createGBC(int fill, int gridx, int gridy,
			int ipady, int ipadx, int weightx, int weighty) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = fill;
		c.gridx = gridx;
		c.gridy = gridy;
		c.ipady = ipady;
		c.ipadx = ipadx;
		c.weightx = weightx;
		c.weighty = weighty;
		return c;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		switch (event.getActionCommand()) {
		case eth0CheckBoxTxt:

			if (eth0CheckBox.isSelected()) {
				restartButton.setEnabled(true);
				eth0SettingButton.setEnabled(true);

				PropertiesUtils.updatePro(
						EthernetConstant.ETHERNET_CONFIGURE_PATH,
						EthernetConstant.ETHERNET_IS_WITHIN_KEY, "yes");
				EthernetStateThread.getInstance().isUseThisManager(true);

			} else {
				restartButton.setEnabled(false);
				eth0SettingButton.setEnabled(false);
				PropertiesUtils.updatePro(
						EthernetConstant.ETHERNET_CONFIGURE_PATH,
						EthernetConstant.ETHERNET_IS_WITHIN_KEY, "no");
				EthernetStateThread.getInstance().isUseThisManager(false);
			}
			break;

		case restartButtonTxt:
			System.out.println("restart ...");

			try {


//				String ipAddress = PropertiesUtils.getPro(
//						EthernetConstant.ETHERNET_CONFIGURE_PATH,
//						EthernetConstant.ETHERNET_IP_ADDRESS);
//				if (ipAddress==null || ipAddress.equals("") ) {
//					EthernetStateThread.getInstance().restartEth0("192.168.100.123");
//				}else{
//					EthernetStateThread.getInstance().restartEth0(ipAddress);
//				}
				
				eth0StateLabel.setText(eth0StateTip + "未启用，网线未连接");
				eth0Address.setText(eth0AddressTip + null);
				eth0MaskCode.setText(eth0MaskCodeTip + null);
				eth0MacAddress.setText(eth0MacAddressTip + null);

			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		case eth0SettingButtonTxt:
			System.out.println("ethernet setting ... ");
			EthernetInputDialog iPDialog = new EthernetInputDialog("以太网IP地址");
			iPDialog.setVisible(true);
			iPDialog.setEnabled(true);
			iPDialog.setComfirmListener(this);
			break;

		default:
			break;
		}
	}

	@Override
	public void comfirm(String ipAddress, String maskCode) {
		// EthernetUtils ethernetUtils = new EthernetUtils();
		try {
			PropertiesUtils.updatePro(EthernetConstant.ETHERNET_CONFIGURE_PATH,
					EthernetConstant.ETHERNET_IP_ADDRESS, ipAddress);

			EthernetStateThread.getInstance().setIpAddress(ipAddress);
			// EthernetThread.getInstance().setIpAddress(ipAddress);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void refreshEthernet() {
		// updateSettingView();
	}

	/**
	 * 更新View
	 */
	private void updateSettingView(boolean is_use_Ethernet) {

		// System.out.println("  ====  updateSettingView .");
		// // 读取系统保存文件，判断并显示是否使用本程序管理以太网
		// String eth0Model = PropertiesUtils.getPro(
		// EthernetConstant.ETHERNET_CONFIGURE_PATH,
		// EthernetConstant.ETHERNET_IS_WITHIN_KEY);
		// boolean is_use_Ethernet = eth0Model.equals("yes");
		eth0CheckBox.setSelected(is_use_Ethernet);

		if (is_use_Ethernet) {
			restartButton.setEnabled(true);
			eth0SettingButton.setEnabled(true);
		} else {
			restartButton.setEnabled(false);
			eth0SettingButton.setEnabled(false);
		}

		// try {
		// eth0MacAddress.setText(eth0MacAddressTip + Eth0Utils.getEth0Mac());
		// eth0Address.setText(eth0AddressTip + Eth0Utils.getIPAddress());
		// eth0MaskCode.setText(eth0MaskCodeTip + Eth0Utils.getMask());
		// } catch (Throwable e) {
		// e.printStackTrace();
		// }
	}

	@Override
	public void refreshEthernet(boolean isConnect, String mac,
			String ipAddress, String bcast, String mask) {

		String eth0Model = PropertiesUtils.getPro(
				EthernetConstant.ETHERNET_CONFIGURE_PATH,
				EthernetConstant.ETHERNET_IS_WITHIN_KEY);
		boolean is_use_Ethernet = eth0Model.equals("yes");

		if (is_use_Ethernet) {
			if (isConnect) {
				eth0StateLabel.setText(eth0StateTip + "已启用，网线已连接");
			} else {
				eth0StateLabel.setText(eth0StateTip + "已启用，网线未连接");
			}
		} else {
			if (isConnect) {
				eth0StateLabel.setText(eth0StateTip + "未启用,网线已连接");
			} else {
				eth0StateLabel.setText(eth0StateTip + "未启用，网线未连接");
			}
		}
		eth0Address.setText(eth0AddressTip + ipAddress);
		eth0MaskCode.setText(eth0MaskCodeTip + mask);
		eth0MacAddress.setText(eth0MacAddressTip + mac);

		updateSettingView(is_use_Ethernet);
	}

}
