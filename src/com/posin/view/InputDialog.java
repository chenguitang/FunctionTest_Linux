package com.posin.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.posin.keystore.SoftKeyBoardPopup;

public class InputDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private String title;
	private TextField inputPassword;

	private OnClickListener mOnClickListener;

	private final JPanel contentPanel = new JPanel();
	private SoftKeyBoardPopup keyPopup = null;

	private JButton okButton;

	public static void main(String[] args) {
		new InputDialog("测试1").setVisible(true);
	}

	/**
	 * Create the dialog.
	 */
	public InputDialog(String title) {
		this.title = title;
		setUndecorated(true);
		setAlwaysOnTop(true);
		setBounds(700, 300, 450, 300);
		Font f = new Font("隶书", Font.BOLD, 25);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		JPanel buttonPane = new JPanel();
		JPanel tipPanel = new JPanel();
		JPanel inputPanel = new JPanel();

		inputPassword = new TextField();
		keyPopup = new SoftKeyBoardPopup(inputPassword);

		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		okButton = new JButton("确认");
		okButton.setPreferredSize(new Dimension(100, 40));
		okButton.setFocusable(false);
		buttonPane.add(okButton);
		okButton.setEnabled(false);
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				setEnabled(false);
				dispose();
				if (mOnClickListener != null) {
					mOnClickListener.onClickOk(inputPassword.getText()
							.toString().trim());
				}
			}
		});
		JButton cancelButton = new JButton("取消");
		cancelButton.setPreferredSize(new Dimension(100, 40));
		cancelButton.setFocusable(false);
		buttonPane.add(cancelButton);

		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				setEnabled(false);
				dispose();
				if (mOnClickListener != null) {
					mOnClickListener.onCancel();
				}
			}
		});

		// 文本提示
		JLabel tipLabel = new JLabel(title, JLabel.CENTER);
		tipLabel.setPreferredSize(new Dimension(450, 70));
		tipLabel.setOpaque(true);
		tipLabel.setForeground(Color.white);
		tipLabel.setBackground(new Color(77, 111, 113));
		tipLabel.setFont(f);
		tipPanel.add(tipLabel);
		getContentPane().add(tipPanel, BorderLayout.NORTH);

		inputPassword.setPreferredSize(new Dimension(500, 40));
		inputPassword.setFont(f);
		inputPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		c.ipady = 20;
		c.ipadx = 400;
		// serialPortPanel.add(inputPanel, c);

		inputPanel.add(inputPassword, c);
		getContentPane().add(inputPanel, BorderLayout.CENTER);
		initListenerKeyBoard();
	}

	/**
	 * 初始化键盘
	 */
	private void initListenerKeyBoard() {

		inputPassword.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!keyPopup.isVisible()) {
					keyPopup.show(inputPassword, -300,
							inputPassword.getPreferredSize().height + 200);
					keyPopup.getSoftKeyBoardPanel().reset();
					keyPopup.repaint();
				}
				System.out.println("onclick input passwork show keyboard");
			}
		});

		inputPassword.addTextListener(new TextListener() {

			@Override
			public void textValueChanged(TextEvent e) {
				if (rootPaneCheckingEnabled) {
					if (inputPassword.getText().toString().trim().length() > 7) {
						okButton.setEnabled(true);
					} else {
						okButton.setEnabled(false);
					}
				}
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
				System.out.println("InputDialog windowActivated");
			}
		});
	}

	public void setOnComfirmclikListener(OnClickListener onClickListener) {
		this.mOnClickListener = onClickListener;
	}

	public interface OnClickListener {
		/**
		 * 确认
		 * @param password 密码内容
		 */
		void onClickOk(String password);
		
		/**
		 * 取消
		 */
		void onCancel();
	}

}
