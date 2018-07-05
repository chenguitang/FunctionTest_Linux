package com.posin.swing;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import sun.org.mozilla.javascript.internal.ast.NewExpression;

/**
 * ��������
 * 
 * @author Greetty
 * 
 */
public class SecondaryTestPanel {

	public JPanel secTestPanel = null; // ������
	JButton showText = null;
	JButton showImage = null;

	private JFrame secFrame; // ������ʾ����

	private static class SecondaryTestHolder {
		private static final SecondaryTestPanel SECONDARY_TEST_PANEL_INSTANCE = new SecondaryTestPanel();
	}

	public static SecondaryTestPanel getInstance() {
		return SecondaryTestHolder.SECONDARY_TEST_PANEL_INSTANCE;
	}

	private SecondaryTestPanel() {
		secTestPanel = new JPanel();
		secTestPanel.setLayout(new GridBagLayout());

		addLine(secTestPanel, 0, 0, -8, Color.GRAY);

		final Font f = new Font("����", Font.PLAIN, 25);
		showText = new JButton("������ʾ����");
		showImage = new JButton("������ʾͼƬ");

		showText.setPreferredSize(new Dimension(200, 80));
		showImage.setPreferredSize(new Dimension(200, 80));

		showText.setFocusable(false);
		showImage.setFocusable(false);

		showText.setFont(f);
		showImage.setFont(f);

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(showText);
		buttonPanel.add(showImage);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.ipadx = 0;
		c.ipady = 0;
		secTestPanel.add(buttonPanel, c);

		listenerClick();
		closeSecPage();

	}

	/**
	 * ����л�������ʾ����
	 */
	private void listenerClick() {

		// ��ʾ�ı�
		showText.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame jf = new JFrame();
				jf.setUndecorated(true);
				jf.setSize(1920, 1080);
				// jf.setDefaultCloseOperation(3);
				jf.setVisible(true);

				final Font secTxtfont = new Font("����", Font.PLAIN, 42);
				JPanel mPanle = new JPanel();
				JLabel mLabel = new JLabel("���Ǹ�����ʾҳ��");
				mLabel.setFont(secTxtfont);
				mLabel.setSize(new Dimension(1920, 1080));
				mLabel.setForeground(Color.RED);
				mPanle.setBackground(Color.WHITE);
				mPanle.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				// c.fill = GridBagConstraints.HORIZONTAL;
				c.weightx = 1;
				c.weighty = 1;
				c.gridx = 0;
				c.gridy = 1;
				c.ipadx = 0;
				c.ipady = 0;
				mPanle.add(mLabel, c);
				jf.setContentPane(mPanle);
				showOnScreen2(1, jf);
			}
		});

		// ��ʾͼƬ
		showImage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame jf = new JFrame();
				jf.setUndecorated(true);
				jf.setSize(1920, 1080);
				jf.setDefaultCloseOperation(3);
				jf.setVisible(true);

				JPanel mPanle = new JPanel();
				JLabel imagelable = new JLabel();
				ImageIcon icon = new ImageIcon(WifiPanel.class
						.getResource("/image/food.png"));
				imagelable.setIcon(icon);

				mPanle.setLayout(new BorderLayout());
				mPanle.add(imagelable, BorderLayout.CENTER);
				jf.setContentPane(mPanle);
				showOnScreen2(1, jf);
			}
		});
	}

	/**
	 * 
	 * @param screen
	 *            ��ʾ�����
	 * @param frame
	 *            ��ʾ�Ĳ���
	 */
	private void showOnScreen2(int screen, final JFrame frame) {

		// ���Ƹ�����ʾ��ǰҳ��
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice[] gd = ge.getScreenDevices();
		System.out.println("screen size: " + gd.length);
		if (screen > -1 && screen < gd.length) {
			frame.setLocation(
					gd[screen].getDefaultConfiguration().getBounds().x,
					frame.getY());

		} else if (gd.length > 0) {
			frame.setLocation(gd[0].getDefaultConfiguration().getBounds().x,
					frame.getY());
		} else {
			throw new RuntimeException("No Screens Found");
		}

		// ��������о�ҳ�棬�رո�����ʾ�ľ�ҳ��
		if (secFrame != null) {
			secFrame.setVisible(false);
			secFrame.dispose();
			System.out.println("close old secondary display frame ... ");
		}
		secFrame = frame;

	}

	/**
	 * �رո�������ҳ��
	 */
	public void closeSecPage() {
		if (secFrame != null) {
			secFrame.setVisible(false);
			secFrame.dispose();
			System.out.println("close secondary display frame ... ");
		} else {
			System.out.println("secondary display frame is null ... ");
		}
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
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = gridx;
		c.gridy = gridy;
		c.ipady = ipady;
		linePanel.setBackground(color);
		fatherJpanel.add(linePanel, c);
	}
}
