package com.posin.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;

public class AlertDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private static AlertDialog mAlertDialog;
	private static JLabel topLabel;
	private static Label messageLabel;

//	public static void showDialog(String title, String message) {
//		if (mAlertDialog == null) {
//			mAlertDialog = new AlertDialog(title, message);
//		} else {
//			topLabel.setText(title);
//			messageLabel.setText(message);
//		}
//		mAlertDialog.setVisible(true);
//	}
//	
//	public static void main(String[] args) {
//		new AlertDialog("警告 ！！ ", "IP地址无效").setVisible(true);
//	}

	/**
	 * Create the dialog.
	 */
	public AlertDialog(String title, String message) {
		setFont(new Font("Dialog", Font.PLAIN, 18));
		setDefaultLookAndFeelDecorated(false);
		setUndecorated (true);
		setAlwaysOnTop(true);
		setBounds(700, 300, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		Label topLine = new Label();
		Label leftLine = new Label();
		Label rightLine = new Label();
		Label bottomLine = new Label();
		topLine.setPreferredSize(new Dimension(450, 2));
		leftLine.setPreferredSize(new Dimension(2, 300));
		rightLine.setPreferredSize(new Dimension(2, 300));
		bottomLine.setPreferredSize(new Dimension(450, 2));
		topLine.setBackground(Color.black);
		leftLine.setBackground(Color.black);
		rightLine.setBackground(Color.black);
		bottomLine.setBackground(Color.black);
		getContentPane().add(topLine, BorderLayout.NORTH);
		getContentPane().add(leftLine, BorderLayout.WEST);
		getContentPane().add(rightLine, BorderLayout.EAST);
		getContentPane().add(bottomLine, BorderLayout.SOUTH);

		contentPanel.setLayout(new BorderLayout());
		topLabel = new JLabel();
		topLabel.setHorizontalAlignment(SwingConstants.CENTER);
		topLabel.setText(title);
		topLabel.setOpaque(true);
		topLabel.setForeground(Color.white);
		topLabel.setBackground(new Color(77, 111, 113));
		topLabel.setFont(new Font("宋体", Font.BOLD, 28));
		topLabel.setPreferredSize(new Dimension(450, 50));
		// topLabel.setHorizontalAlignment(SwingConstants.LEFT);
		contentPanel.add(topLabel, BorderLayout.NORTH);

		messageLabel = new Label(message);
		messageLabel.setAlignment(Label.CENTER);
		messageLabel.setFont(new Font("宋体", Font.PLAIN, 26));
		messageLabel.setBackground(Color.white);
		contentPanel.add(messageLabel, BorderLayout.CENTER);

		JPanel confirmPanel = new JPanel();
		confirmPanel.setLayout(new BorderLayout());
		JButton confirmButton = new JButton("确认");
		confirmButton.setFont(new Font("宋体", Font.PLAIN, 20));
		confirmButton.setFocusable(false);
		confirmPanel.setPreferredSize(new Dimension(450, 50));
		confirmPanel.add(confirmButton, BorderLayout.CENTER);

		contentPanel.add(confirmPanel, BorderLayout.SOUTH);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[] {
				getContentPane(), contentPanel, topLabel, messageLabel,
				confirmPanel, confirmButton }));

		confirmButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				setEnabled(false);
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
				System.out.println("alert windowActivated");
			}
		});
	}

}
