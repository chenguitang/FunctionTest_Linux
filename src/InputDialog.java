

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InputDialog {

	/**
	 * ��ʾһ���Զ���ĶԻ���
	 * 
	 * @param owner
	 *            �Ի����ӵ����
	 * @param parentComponent
	 *            �Ի���ĸ������
	 */
	public static void showCustomDialog(Frame owner, Component parentComponent) {
		// ����һ��ģ̬�Ի���
		final JDialog dialog = new JDialog(owner, "��ʾ", true);
		// ���öԻ���Ŀ��
		dialog.setSize(250, 150);
		// ���öԻ����С���ɸı�
		dialog.setResizable(false);
		// ���öԻ��������ʾ��λ��
		dialog.setLocationRelativeTo(parentComponent);

		// ����һ����ǩ��ʾ��Ϣ����
		JLabel messageLabel = new JLabel("�Ի�����Ϣ����");

		// ����һ����ť���ڹرնԻ���
		JButton okBtn = new JButton("ȷ��");
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// �رնԻ���
				dialog.dispose();
			}
		});

		// �����Ի�����������, ������ڿ��Ը����Լ�����Ҫ����κ�������������ǲ���
		JPanel panel = new JPanel();

		// �����������
		panel.add(messageLabel);
		panel.add(okBtn);

		// ���öԻ�����������
		dialog.setContentPane(panel);
		// ��ʾ�Ի���
		dialog.setVisible(true);
	}
}
