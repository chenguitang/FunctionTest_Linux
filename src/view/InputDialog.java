package view;

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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class InputDialog extends JDialog {

	private String title;

	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public InputDialog(String title) {
		this.title = title;
		setBounds(700, 300, 450, 300);
		Font f = new Font("隶书", Font.BOLD, 25);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			JPanel tipPanel = new JPanel();
			JPanel inputPanel = new JPanel();

			final TextField inputPassword = new TextField();

			buttonPane.setLayout(new BorderLayout());
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("确认");
				okButton.setPreferredSize(new Dimension(100, 40));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton,BorderLayout.WEST);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						mOnClickListener.onClick(e, inputPassword.getText()
								.toString().trim());
					}
				});
			}
			{
				JButton cancelButton = new JButton("取消");
				cancelButton.setPreferredSize(new Dimension(100, 40));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton,BorderLayout.EAST);
				cancelButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						mOnClickListener.onClick(e, inputPassword.getText()
								.toString().trim());
					}
				});
			}

			// 文本提示
			JLabel tipLabel = new JLabel(title);
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
		}
	}

	private OnClickListener mOnClickListener;

	public void setOnComfirmclikListener(OnClickListener onClickListener) {
		this.mOnClickListener = onClickListener;
	}

	public interface OnClickListener {
		void onClick(ActionEvent event, String password);
	}

}
