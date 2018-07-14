package com.posin.swing;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.posin.utils.Eth0Utils;
import com.posin.utils.MacUtils;
import com.posin.utils.ProcessUtils;
import com.posin.utils.PropertiesUtils;
import com.posin.utils.StringUtils;

public class EthernetSettingPanel implements ActionListener {

	private static final String ETHERNET_CONFIGURE_PATH = "/etc/ethernet.prop";
	private static final String ETHERNET_IS_WITHIN_KEY = "ro.posin.eth0";

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

	private String eth0StateTip = "��̫��״̬��";
	private String eth0AddressTip = "       IP��ַ��";
	private String eth0MaskCodeTip = "          ���룺";
	private String eth0MacAddressTip = "  MAC��ַ��";
	private final String eth0CheckBoxTxt = "������̫���������";
	private final String restartButtonTxt = "������̫��";
	private final String eth0SettingButtonTxt = "IP��ַ����";


	private static class EthernetHolder {
		private static final EthernetSettingPanel ETHERNET_SETTING_INSTANCE = new EthernetSettingPanel();
	}

	public static EthernetSettingPanel getInstance() {
		return EthernetHolder.ETHERNET_SETTING_INSTANCE;
	}

	public EthernetSettingPanel() {

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
	}

	public void initSyncView() {
		// ��ȡϵͳ�����ļ����жϲ���ʾ�Ƿ�ʹ�ñ����������̫��
		String eth0Model = PropertiesUtils.getPro(ETHERNET_CONFIGURE_PATH,
				ETHERNET_IS_WITHIN_KEY);
		eth0CheckBox.setSelected(eth0Model.equals("yes"));
		
		try {
			eth0MacAddress.setText(eth0MacAddressTip+Eth0Utils.getEth0Mac());
			eth0Address.setText(eth0AddressTip+Eth0Utils.getIPAddress());
			eth0MaskCode.setText(eth0MaskCodeTip+Eth0Utils.getMask());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����View
	 */
	private void updateSettingView() {
		if (eth0CheckBox.isSelected()) {
			restartButton.setEnabled(true);
			eth0SettingButton.setEnabled(true);

			PropertiesUtils.updatePro(ETHERNET_CONFIGURE_PATH,
					ETHERNET_IS_WITHIN_KEY, "yes");

		} else {
			restartButton.setEnabled(false);
			eth0SettingButton.setEnabled(false);
			PropertiesUtils.updatePro(ETHERNET_CONFIGURE_PATH,
					ETHERNET_IS_WITHIN_KEY, "no");
		}
	}

	private void initConfig() {

		mTitleFont = new Font("����", Font.PLAIN, 25);
		mButtonFont = new Font("����", Font.PLAIN, 20);
		mCheckBoxFont = new Font("����", Font.PLAIN, 22);
		mStateFont = new Font("����", Font.PLAIN, 20);
	}

	private void initView() {
		ethernetPanel = new JPanel();
		contentPanel = new JPanel();
		// ���ò��ַ�ʽ
		ethernetPanel.setLayout(new BorderLayout());

		// �����ָ���
		JLabel topLine = new JLabel();
		topLine.setPreferredSize(new Dimension(1920, 70));
		topLine.setText("������Ϊͬʱʹ��WIFI����̫�������ã�����ͬʱʹ�ã����ÿ��������������̫��");
		topLine.setFont(mTitleFont);
		topLine.setHorizontalAlignment(JLabel.CENTER);
		topLine.setForeground(Color.white);
		topLine.setOpaque(true);
		topLine.setBackground(new Color(77, 111, 113));
		ethernetPanel.add(topLine, BorderLayout.NORTH);

		// ������ʾ������
		ethernetPanel.add(contentPanel, BorderLayout.CENTER);

		// ����������ʾҳ��Ĳ��ַ�ʽ
		contentPanel.setLayout(new GridBagLayout());
		// ���ñ߿�
		contentPanel.setBorder(BorderFactory.createLineBorder(Color.gray));

		// ������̫���������������̫����IP���ø�������
		JPanel actionPanel = new JPanel();
		actionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		actionPanel.setPreferredSize(new Dimension(1000, 80));
		contentPanel.add(actionPanel,
				createGBC(GridBagConstraints.HORIZONTAL, 0, 0, 0, 0, 0, 0));

		// ���û�ȡ�����������ť
		eth0CheckBox = new JCheckBox();
		eth0CheckBox.setPreferredSize(new Dimension(240, 40));
		// eth0CheckBox.setBackground(Color.gray);
		eth0CheckBox.setFont(mCheckBoxFont);
		eth0CheckBox.setText(eth0CheckBoxTxt);
		eth0CheckBox.setFocusable(false);
		actionPanel.add(eth0CheckBox);
		// ������̫����ť
		restartButton = new JButton(restartButtonTxt);
		restartButton.setPreferredSize(new Dimension(140, 60));
		restartButton.setHorizontalAlignment(SwingConstants.CENTER);
		restartButton.setFocusable(false);
		restartButton.setFont(mButtonFont);
		actionPanel.add(restartButton);
		// IP����
		eth0SettingButton = new JButton(eth0SettingButtonTxt);
		eth0SettingButton.setPreferredSize(new Dimension(140, 60));
		eth0SettingButton.setHorizontalAlignment(SwingConstants.CENTER);
		eth0SettingButton.setFocusable(false);
		eth0SettingButton.setFont(mButtonFont);
		actionPanel.add(eth0SettingButton);

		// �ָ���
		addLine(contentPanel, 0, 1, -9, Color.gray);

		eth0StateLabel = new JLabel(eth0StateTip + "�����ã�����������");
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
		JLabel lineLabel = new JLabel();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = gridx;
		c.gridy = gridy;
		c.ipady = ipady;
		linePanel.setBackground(color);
		// lineLabel.setBackground(color);
		fatherJpanel.add(linePanel, c);
		// fatherJpanel.add(lineLabel, c);
	}

	/**
	 * ����GridBagConstraints
	 * 
	 * @param fill
	 *            ������Ļ�ķ���
	 * @param gridx
	 *            x��λ��
	 * @param gridy
	 *            y��λ��
	 * @param ipady
	 *            y��Ŵ����
	 * @param ipadx
	 *            x��Ŵ����
	 * @param weightx
	 *            x�����
	 * @param weighty
	 *            y�����
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

			updateSettingView();

			break;

		case restartButtonTxt:
			System.out.println("restart ...");

			break;
		case eth0SettingButtonTxt:

			System.out.println("ethernet setting ... ");

			break;

		default:
			break;
		}
	}

}
