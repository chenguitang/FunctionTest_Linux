package com.posin.power;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.posin.command.OpenFunctionTest;

public class PowerPage extends JDialog {

	private final JPanel contentPanel = new JPanel();
	Font f = new Font("隶书", Font.BOLD, 30);
	Color frameColor = null;
	Color backgroundColor = null;

	private static final PowerPage INPUTDIALOG_INSTANCE = new PowerPage();

	public static PowerPage getInstance() {
		return INPUTDIALOG_INSTANCE;
	}

	/**
	 * Create the dialog.
	 */
	private PowerPage() {

		// 边框颜色
		// frameColor = new Color(0x3AF9F9);
		setBounds(600, 250, 800, 490);
		setDefaultLookAndFeelDecorated(false);
		setUndecorated(true);
		requestFocusInWindow();
		setAlwaysOnTop(true);
		frameColor = Color.BLACK;
		// 背景颜色
		// backgroundColor = new Color(0xE3F7F7);
		// backgroundColor = new Color(0xF7F7F6);
		backgroundColor = new Color(0xE6E7E4);
		// 添加到主Panel中
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		// 边框
		Label northLabel = new Label();
		Label southLabel = new Label();
		Label eastLabel = new Label();
		Label westhLabel = new Label();

		northLabel.setPreferredSize(new Dimension(800, 3));
		southLabel.setPreferredSize(new Dimension(800, 3));
		eastLabel.setPreferredSize(new Dimension(3, 400));
		westhLabel.setPreferredSize(new Dimension(3, 400));

		northLabel.setBackground(frameColor);
		southLabel.setBackground(frameColor);
		eastLabel.setBackground(frameColor);
		westhLabel.setBackground(frameColor);

		getContentPane().add(southLabel, BorderLayout.SOUTH);
		getContentPane().add(northLabel, BorderLayout.NORTH);
		getContentPane().add(eastLabel, BorderLayout.EAST);
		getContentPane().add(westhLabel, BorderLayout.WEST);

		contentPanel.setLayout(new GridBagLayout());
		getContentPane().setBackground(backgroundColor);

		contentPanel.setOpaque(false);

		initShutdown();
		initReboot();
		initFunctionTest();
	}

	/**
	 * 关机
	 */
	private void initShutdown() {

		JPanel shutdownPanel = new JPanel();
		JLabel shutdownButton = new JLabel("关机");
		shutdownButton.setPreferredSize(new Dimension(250,50));
		shutdownButton.setBackground(Color.white);
		shutdownPanel.setBackground(Color.white);
		shutdownButton.setFont(f);
		shutdownButton.setFocusable(false);
		shutdownPanel.setLayout(new GridBagLayout());

		ImageIcon shutdownIcon = new ImageIcon(
				PowerPage.class.getResource("/image/shutdown.png"));
		JLabel shutdownLabel = new JLabel();
		shutdownLabel.setPreferredSize(new Dimension(shutdownIcon
				.getIconWidth(), shutdownIcon.getIconHeight()));
		shutdownLabel.setOpaque(true);
		shutdownLabel.setBackground(Color.white);
		shutdownLabel.setIcon(shutdownIcon);
		shutdownPanel.add(
				shutdownLabel,
				createGridBagConstraints2(GridBagConstraints.VERTICAL, 1, 1, 0,
						0, 2, 1));
		
		shutdownPanel.add(
				shutdownButton,
				createGridBagConstraints2(GridBagConstraints.VERTICAL, 2, 1, 0,
						0, 6, 1));

		contentPanel.add(
				shutdownPanel,
				createGridBagConstraints(GridBagConstraints.HORIZONTAL, 1, 0,
						50, 400));
		shutdownPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				try {
					ProcessUtils.getInstance().createSuProcess(
							"systemctl poweroff");
					System.out.println("system poweroff");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * 重启
	 */
	private void initReboot() {
		// 间隙
		Label label = new Label();
		label.setBackground(backgroundColor);
		contentPanel.add(
				label,
				createGridBagConstraints(GridBagConstraints.HORIZONTAL, 1, 1,
						0, 650));
		// 重启
		JPanel rebootPanel = new JPanel();
		JLabel rebootButton = new JLabel("重启",JLabel.LEFT);
		rebootButton.setPreferredSize(new Dimension(250,50));
		rebootButton.setBackground(Color.white);
		rebootPanel.setBackground(Color.white);
		rebootButton.setFont(f);
		rebootButton.setFocusable(false);
		rebootPanel.setLayout(new GridBagLayout());

		ImageIcon rebootIcon = new ImageIcon(
				PowerPage.class.getResource("/image/reboot.png"));
		JLabel rebootLabel = new JLabel();
		// rebootLabel.setPreferredSize(new Dimension(rebootIcon.getIconWidth(),
		// rebootIcon.getIconHeight()));
		rebootLabel.setBounds(500, 500, rebootIcon.getIconWidth(),
				rebootIcon.getIconHeight());
		rebootLabel.setOpaque(true);
		rebootLabel.setBackground(Color.white);
		rebootLabel.setIcon(rebootIcon);

		// rebootPanel.add(new Label());
		rebootPanel.add(
				rebootLabel,
				createGridBagConstraints2(GridBagConstraints.VERTICAL, 1, 1, 0,
						0, 2, 1));
		rebootPanel.add(
				rebootButton,
				createGridBagConstraints2(GridBagConstraints.VERTICAL, 2, 1, 0,
						0, 6, 1));

		contentPanel.add(
				rebootPanel,
				createGridBagConstraints(GridBagConstraints.HORIZONTAL, 1, 2,
						50, 600));
		rebootPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				try {
					ProcessUtils.getInstance().createSuProcess(
							"systemctl reboot");
					System.out.println("system reboot");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 打开功能测试
	 */
	private void initFunctionTest() {
		// 间隙
		Label label = new Label();
		label.setBackground(backgroundColor);
		contentPanel.add(
				label,
				createGridBagConstraints(GridBagConstraints.HORIZONTAL, 1, 3,
						0, 650));
		JPanel openFcPanel = new JPanel();
		JLabel openFcButton = new JLabel("打开功能测试");
		openFcButton.setPreferredSize(new Dimension(350,50));
		openFcButton.setBackground(Color.white);
		openFcPanel.setBackground(Color.white);
		openFcButton.setFont(f);
		openFcButton.setFocusable(false);
		openFcPanel.setLayout(new GridBagLayout());

		ImageIcon rebootIcon = new ImageIcon(
				PowerPage.class.getResource("/image/function.png"));
		JLabel openFcabel = new JLabel();
		openFcabel.setBounds(500, 500, rebootIcon.getIconWidth(),
				rebootIcon.getIconHeight());
		openFcabel.setOpaque(true);
		openFcabel.setBackground(Color.white);
		openFcabel.setIcon(rebootIcon);

		openFcPanel.add(
				openFcabel,
				createGridBagConstraints2(GridBagConstraints.VERTICAL, 1, 1, 0,
						0, 3, 1));
		openFcPanel.add(
				openFcButton,
				createGridBagConstraints2(GridBagConstraints.VERTICAL, 2, 1, 0,
						0, 6, 1));

		contentPanel.add(
				openFcPanel,
				createGridBagConstraints(GridBagConstraints.HORIZONTAL, 1, 4,
						50, 600));
		openFcPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				try {
					ProcessUtils.getInstance().createSuProcess(
							"/usr/bin/functiontest.sh");

					setVisible(false);
					System.out.println("open fucntion test ... ");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 生成GridBagConstraints
	 * 
	 * @param fill
	 * @param gridx
	 * @param gridy
	 * @param ipady
	 * @param ipadx
	 * @return
	 */
	public GridBagConstraints createGridBagConstraints(int fill, int gridx,
			int gridy, int ipady, int ipadx) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = fill;
		c.gridx = gridx;
		c.gridy = gridy;
		c.ipady = ipady;
		c.ipadx = ipadx;
		return c;
	}

	/**
	 * 生成GridBagConstraints
	 * 
	 * @param fill
	 * @param gridx
	 * @param gridy
	 * @param ipady
	 * @param ipadx
	 * @param weightx
	 * @param weighty
	 * @return
	 */
	public GridBagConstraints createGridBagConstraints2(int fill, int gridx,
			int gridy, int ipady, int ipadx, int weightx, int weighty) {
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
