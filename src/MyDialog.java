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

public class MyDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			final MyDialog dialog = new MyDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);

			dialog.setOnclikOkListener(new OnClickListener() {
				@Override
				public void onClick(ActionEvent e) {
					dialog.dispose();
					System.out.println("----------close dialog ---------");
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public MyDialog() {
		setBounds(100, 100, 450, 300);
		Font f = new Font("隶书", Font.BOLD, 25);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			JPanel tipPanel = new JPanel();
			JPanel inputPanel = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("确认");
				okButton.setPreferredSize(new Dimension(100, 40));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						mOnClickListener.onClick(e);
					}
				});
			}
			{
				JButton cancelButton = new JButton("取消");
				cancelButton.setPreferredSize(new Dimension(100, 40));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						mOnClickListener.onClick(e);
					}
				});
			}

			// 文本提示
			JLabel tipLabel = new JLabel("请输入密码");
			tipLabel.setFont(f);
			tipPanel.add(tipLabel);
			getContentPane().add(tipPanel, BorderLayout.NORTH);

			TextField inputPassword = new TextField();
			inputPassword.setPreferredSize(new Dimension(500, 40));
			inputPassword.setFont(f);
			inputPanel.setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 1;
			c.gridy = 1;
			c.ipady = 20;
			c.ipadx = 400;
//			serialPortPanel.add(inputPanel, c);
			
			inputPanel.add(inputPassword,c);
			getContentPane().add(inputPanel, BorderLayout.CENTER);
		}
	}

	private OnClickListener mOnClickListener;

	public void setOnclikOkListener(OnClickListener onClickListener) {
		this.mOnClickListener = onClickListener;
	}

	public interface OnClickListener {
		void onClick(ActionEvent e);
	}

}
