package com.posin.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.posin.ethernet.EthernetUtils;
import com.posin.keystore.SoftKeyBoardPopup;

public class EthernetInputDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -1522811503308636826L;
	private JPanel contentPanel;
	private JLabel titleLabel;
	private SoftKeyBoardPopup keyPopup = null;
	private ComfirmListener mComfirmListener;

	private JButton cancelButton;
	private JButton okButton;
	private TextField ipTextField;
	private TextField maskCodeTextField;

	private Font mTitleFont;
	private Font mButtonFont;
	private Font mTxtFont;

	private final String cancelButtonTxt = "取消";
	private final String okButtonTxt = "确认";

	public static void main(String[] args) {
		new EthernetInputDialog("以太网IP地址").setVisible(true);
	}

	/**
	 * Create the dialog.
	 */
	public EthernetInputDialog(String title) {
		setUndecorated(true);
		setAlwaysOnTop(true);
		setBounds(700, 300, 500, 340);
		// getContentPane().setLayout(new BorderLayout());

		initConfig();

		initView(title);

		initListener();

	}

	private void initConfig() {
		mTitleFont = new Font("楷体", Font.PLAIN, 28);
		mButtonFont = new Font("楷体", Font.PLAIN, 22);
		mTxtFont = new Font("楷体", Font.PLAIN, 25);
	}

	private void initView(String title) {

		contentPanel = new JPanel();
		getContentPane().add(contentPanel);
		contentPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPanel.setLayout(new GridBagLayout());

		// 顶部标题
		titleLabel = new JLabel(title, JLabel.CENTER);
		titleLabel.setOpaque(true);
		titleLabel.setBackground(new Color(77, 111, 113));
		titleLabel.setForeground(Color.white);
		titleLabel.setFont(mTitleFont);

		contentPanel.add(titleLabel,
				createGBC(GridBagConstraints.HORIZONTAL, 0, 0, 40, 0, 1, 0));

		// 空布局，占位
		contentPanel.add(new JPanel(),
				createGBC(GridBagConstraints.HORIZONTAL, 0, 1, 25, 0, 1, 0));

		// IP地址
		JPanel ipAddressPanel = new JPanel();
		ipAddressPanel.setLayout(new FlowLayout());
		contentPanel.add(ipAddressPanel,
				createGBC(GridBagConstraints.HORIZONTAL, 0, 2, 30, 0, 1, 0));

		// IP地址输入提示
		JLabel ipAddressTip = new JLabel("IP地址:", JLabel.LEFT);
		ipAddressTip.setOpaque(true);
		ipAddressTip.setFont(mTxtFont);
		ipAddressPanel.add(ipAddressTip, FlowLayout.LEFT);

		// IP地址输入框
		ipTextField = new TextField();
		ipTextField.setFont(mTxtFont);
		ipTextField.setPreferredSize(new Dimension(300, 50));
		ipAddressPanel.add(ipTextField, FlowLayout.CENTER);

		// 掩码
		JPanel maskCodePanel = new JPanel();
		maskCodePanel.setLayout(new FlowLayout());
		contentPanel.add(maskCodePanel,
				createGBC(GridBagConstraints.HORIZONTAL, 0, 3, 30, 0, 1, 0));

		// 掩码输入提示
		JLabel maskCodeTip = new JLabel("  掩码：", JLabel.CENTER);
		maskCodeTip.setOpaque(true);
		maskCodeTip.setFont(mTxtFont);
		// maskCodePanel.add(maskCodeTip,
		// createGBC(GridBagConstraints.VERTICAL, 0, 0, 10, 10, 1, 0));
		maskCodePanel.add(maskCodeTip, FlowLayout.LEFT);

		// 掩码输入框
		maskCodeTextField = new TextField();
		maskCodeTextField.setFont(mTxtFont);
		maskCodeTextField.setPreferredSize(new Dimension(300, 50));
		maskCodeTextField.setText("255.255.255.0");

		// maskCodePanel.add(maskCodeTextField,
		// createGBC(GridBagConstraints.VERTICAL, 1, 0, 20, 300, 1, 0));
		maskCodePanel.add(maskCodeTextField, FlowLayout.CENTER);

		// 空布局，占位
		contentPanel.add(new JPanel(),
				createGBC(GridBagConstraints.HORIZONTAL, 0, 4, 10, 0, 1, 0));

		// 分割线
		JLabel labelLine = new JLabel();
		labelLine.setOpaque(true);
		labelLine.setPreferredSize(new Dimension(450, 1));
		labelLine.setBackground(Color.black);
		contentPanel.add(labelLine,
				createGBC(GridBagConstraints.HORIZONTAL, 0, 5, 1, 0, 1, 0));

		// 取消确认按钮
		JPanel comfirmPanel = new JPanel();
		comfirmPanel.setLayout(new GridBagLayout());
		contentPanel.add(comfirmPanel,
				createGBC(GridBagConstraints.HORIZONTAL, 0, 6, 10, 0, 1, 0));

		// 取消
		cancelButton = new JButton(cancelButtonTxt);
		cancelButton.setFocusable(false);
		cancelButton.setFont(mButtonFont);
		cancelButton.setHorizontalAlignment(SwingConstants.CENTER);
		comfirmPanel.add(cancelButton,
				createGBC(GridBagConstraints.VERTICAL, 0, 0, 25, 225, 1, 0));

		okButton = new JButton(okButtonTxt);
		okButton.setFocusable(false);
		okButton.setFont(mButtonFont);
		// okButton.setBackground(new Color(77, 111, 113));
		// okButton.setForeground(Color.white);
		okButton.setHorizontalAlignment(SwingConstants.CENTER);
		comfirmPanel.add(okButton,
				createGBC(GridBagConstraints.VERTICAL, 1, 0, 25, 225, 1, 0));
	}

	private void initListener() {
		cancelButton.addActionListener(this);
		okButton.addActionListener(this);

		ipTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (keyPopup != null) {
					if (keyPopup.isShowing()) {
						keyPopup.setVisible(false);
						keyPopup.setEnabled(false);
					}
				}

				keyPopup = new SoftKeyBoardPopup(ipTextField);
				keyPopup.show(ipTextField, -300,
						ipTextField.getPreferredSize().height + 220);
				keyPopup.getSoftKeyBoardPanel().reset();
				keyPopup.repaint();
			}
		});
		maskCodeTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (keyPopup != null) {
					if (keyPopup.isShowing()) {
						keyPopup.setVisible(false);
						keyPopup.setEnabled(false);
					}
				}
				keyPopup = new SoftKeyBoardPopup(maskCodeTextField);
				keyPopup.show(maskCodeTextField, -300,
						maskCodeTextField.getPreferredSize().height + 150);
				keyPopup.getSoftKeyBoardPanel().reset();
				keyPopup.repaint();
			}
		});
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowDeactivated(WindowEvent e) {
				if (isShowing()) {
					setVisible(false);
					dispose();
				}
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				System.out.println("EthernetInputDialog windowActivated");
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		switch (event.getActionCommand()) {
		case cancelButtonTxt:
			// System.exit(0);
			setVisible(false);
			setEnabled(false);
			dispose();
			System.out.println("cannel ... ");
			break;
		case okButtonTxt:
			System.out.println("ok ... ");
			String ipAddress = ipTextField.getText().toString().trim();
			String maskCode = maskCodeTextField.getText().toString().trim();

			if (ipAddress == null || maskCode == null) {
				new AlertDialog("警告！！", "IP不能为空").setVisible(true);
				return;
			} else if (!EthernetUtils.isIpv4Adress(ipAddress)) {
				new AlertDialog("警告！！", "IP无效，请重新设置IP地址").setVisible(true);
				return;
			} else if (!EthernetUtils.isIpv4Adress(maskCode)) {
				new AlertDialog("警告！！", "掩码无效，请重新设置掩码").setVisible(true);
				return;
			}

			if (mComfirmListener != null) {
				mComfirmListener.comfirm(ipAddress, maskCode);
				System.out.println("mComfirmListener  != null  ");
				setVisible(false);
				setEnabled(false);
				dispose();
			}else{
				System.out.println("mComfirmListener  == null  ");
			}
			break;
		default:
			break;
		}
	}

	public void setComfirmListener(ComfirmListener comfirmListener) {
		this.mComfirmListener = comfirmListener;
	}

	public interface ComfirmListener {
		void comfirm(String ipAddress, String maskCode);
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

}
