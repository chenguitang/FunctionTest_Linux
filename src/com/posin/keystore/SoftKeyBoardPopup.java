package com.posin.keystore;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JToolTip;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import sun.swing.SwingUtilities2;

//软键盘弹出菜单  
public class SoftKeyBoardPopup extends JPopupMenu {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {

		final JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 1000);
		frame.setLocationRelativeTo(null);

		final JPanel passwordPanel = new JPanel(new BorderLayout());
		passwordPanel.setBackground(Color.WHITE);
		passwordPanel.setPreferredSize(new Dimension(202, 30));
		passwordPanel.setLayout(new BorderLayout());
		passwordPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

		frame.add(passwordPanel);

		final TextField password = new TextField();
		// password.setSelectedTextColor(Color.BLACK);//
		// 颜色障眼法，产生不能去选中密码框的任何东西的幻觉，其实已经选中了但你不知道
		// password.setSelectionColor(Color.WHITE);//
		// 颜色障眼法，产生不能去选中密码框的任何东西的幻觉，其实已经选中了但你不知道
		password.setForeground(Color.BLACK);
		// password.setFont(password.getFont().deriveFont(22f));
		// password.setEchoChar('●');
		// password.setBorder(new EmptyBorder(5, 3, 0, 3));// 左间隙
		passwordPanel.add(password, BorderLayout.CENTER);

		final SoftKeyBoardPopup keyPopup = new SoftKeyBoardPopup(password);
		final JLabel keyBoard = new JLabel("软件盘");
		keyBoard.setOpaque(true);
		keyBoard.setBackground(Color.WHITE);
		keyBoard.setBorder(new EmptyBorder(0, 0, 0, 0));
		keyBoard.setToolTipText("打开软键盘");
		keyBoard.setPreferredSize(new Dimension(42, 23));
		keyBoard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		keyBoard.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (!keyPopup.isVisible()) {
					keyPopup.show(passwordPanel, 0,
							passwordPanel.getPreferredSize().height);
					keyPopup.getSoftKeyBoardPanel().reset();
					keyPopup.repaint();
				}
			}
		});
		passwordPanel.add(keyBoard, BorderLayout.EAST);

		frame.setVisible(true);
	}

	private static Color transparentColor = new Color(255, 255, 255, 0);
	// private static Dimension popupSize = new Dimension(360, 110);//QQ软键盘大小
	// private static Dimension popupSize = new Dimension(365, 110);
	private static Dimension popupSize = new Dimension(1138, 343);
	private static Color backColor = new Color(23, 127, 194);
	private static Random random = new Random();
	static Font keyBoardFont = new Font("隶书", Font.PLAIN, 20);

	protected SoftKeyBoardPanel softKeyBoardPanel;

	public SoftKeyBoardPopup(TextField passwordField) {
		softKeyBoardPanel = new SoftKeyBoardPanel(passwordField, this);
		softKeyBoardPanel.setPreferredSize(popupSize);
		softKeyBoardPanel.setBorder(BorderFactory.createEmptyBorder());

		add(softKeyBoardPanel);
		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));// 空边框
		setOpaque(false);// 透明
	}

	public static void gc() {
		popupSize = null;
		backColor = null;
		random = null;
		System.gc();
	}

	public static void resetValue() {
		// popupSize = new Dimension(380, 110);
		popupSize = new Dimension(1138, 343);
		backColor = new Color(23, 127, 194);
		random = new Random();
	}

	public SoftKeyBoardPanel getSoftKeyBoardPanel() {
		return softKeyBoardPanel;
	}

	// 软键盘面板
	public static class SoftKeyBoardPanel extends JPanel implements
			ActionListener {

		TextField passwordField;
		JPopupMenu popupMenu;
		RowPanel[] rows;
		KeyStatus status = KeyStatus.normal;
		Paint[] paints = new Paint[] { new Color(70, 67, 114),
				new Color(62, 192, 238), new Color(138, 180, 231) };

		public SoftKeyBoardPanel(TextField passwordField, JPopupMenu popupMenu) {
			this.passwordField = passwordField;
			this.popupMenu = popupMenu;
			initSoftKeyBoardPanel();
		}

		// 初始化
		private void initSoftKeyBoardPanel() {
			setLayout(null);
			setBackground(backColor);

			JPanel proxyPanel = new JPanel(new GridLayout(4, 1, 0, 1));// 4行一列，0水平间隙，1垂直间隙
			proxyPanel.setBackground(backColor);
			proxyPanel.setLocation(3, 4);
			proxyPanel.setSize(popupSize.width - 6, popupSize.height - 7);
			add(proxyPanel);

			rows = new RowPanel[] { new RowPanel(RowType.first),
					new RowPanel(RowType.second), new RowPanel(RowType.third),
					new RowPanel(RowType.fourth) };
			for (int i = 0; i < rows.length; i++) {
				proxyPanel.add(rows[i]);
			}
		}

		// 重写paint绘制想要的效果
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2d = (Graphics2D) g;
			ImageTool.setAntiAliasing(g2d);// 抗锯齿
			ImageTool.drawRoundRect(g2d, getWidth(), getHeight(), 0, null,
					paints);// 绘制软键盘成圆角和多层颜色边框
		}

		// 处理所有软键盘点击事件
		@Override
		public void actionPerformed(ActionEvent e) {
			KeyLable keyLable = (KeyLable) e.getSource();
			if (keyLable.isShift() || keyLable.isCapsLock()) {
				boolean pressed = keyLable.isPressed();

				if (keyLable.isShift()) {
					clickShift();
				} else if (keyLable.isCapsLock()) {
					clickCapsLock();
				}
				pressed = !pressed;
				keyLable.setPressed(pressed);

				notifyKeyLabel();
			} else if (keyLable.isBackSpace()) {
				clickBackSpace();
			} else if (keyLable.isCommKey()) {
				String key;
				if (status == KeyStatus.shift
						|| status == KeyStatus.shiftAndCapsLock) {
					key = keyLable.getCenterKey();
				} else if (status == KeyStatus.normal
						|| status == KeyStatus.capsLock) {
					key = keyLable.getLowerLeftKey() == null ? keyLable
							.getCenterKey() : keyLable.getLowerLeftKey();
				} else {
					key = "";
				}
				clickCommKey(key);
			}
		}

		// 通知KeyLabel更新状态
		public void notifyKeyLabel() {
			for (RowPanel rowPanel : rows) {
				for (KeyLable keyLable : rowPanel.getKeys()) {
					keyLable.setStatus(status);
				}
			}
		}

		// 重置键盘， 清除按压状态，并将键盘恢复至初始状态
		public void reset() {
			for (RowPanel rowPanel : rows) {
				for (KeyLable keyLable : rowPanel.getKeys()) {
					keyLable.reset();
				}
			}
			status = KeyStatus.normal;
		}

		// 更改状态
		public void clickShift() {
			if (status == KeyStatus.capsLock) {
				status = KeyStatus.shiftAndCapsLock;
			} else if (status == KeyStatus.shiftAndCapsLock) {
				status = KeyStatus.capsLock;
			} else if (status == KeyStatus.shift) {
				status = KeyStatus.normal;
			} else if (status == KeyStatus.normal) {
				status = KeyStatus.shift;
			} else {
				status = KeyStatus.normal;
			}
		}

		// 更改状态
		public void clickCapsLock() {
			if (status == KeyStatus.capsLock) {
				status = KeyStatus.normal;
			} else if (status == KeyStatus.shiftAndCapsLock) {
				status = KeyStatus.shift;
			} else if (status == KeyStatus.shift) {
				status = KeyStatus.shiftAndCapsLock;
			} else if (status == KeyStatus.normal) {
				status = KeyStatus.capsLock;
			} else {
				status = KeyStatus.normal;
			}
		}

		// 点击了删除键， 删除一个字符
		public void clickBackSpace() {
			char[] password = passwordField.getText().toString().trim()
					.toCharArray();
			if (password != null && password.length > 0) {
				char[] copyOf = Arrays.copyOf(password, password.length - 1);
				passwordField.setText(new String(copyOf));
				System.out.println("已删除的字符：" + password[password.length - 1]);
				System.out.println("删除后的的密码：" + new String(copyOf));
			}
		}

		// 点击了普通的键，添加一个字符
		public void clickCommKey(String key) {
			if (key != null) {
				if (key.length() > 1) {// 可有可无的检查
					key = key.substring(0, key.length() - 1);
				}
				char[] password = passwordField.getText().toString().trim()
						.toCharArray();
				String string = (password == null ? "" : new String(password));
				passwordField.setText(string + key);
				System.out.println("新添加的字符：" + key);
				System.out.println("添加后的密码：" + string + key);
			}
		}

		public RowPanel[] getRows() {
			return rows;
		}

		// 创建关闭图标
		public Image createCloseImage(Color fontColor, boolean isFocus) {
			int width = 36;
			BufferedImage bi = new BufferedImage(width, width,
					BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2d = bi.createGraphics();

			ImageTool.setAntiAliasing(g2d);

			// 画背景
			g2d.setPaint(transparentColor);
			g2d.fillRect(0, 0, width, width);

//			int[] xpoints_1 = { 2, 4, 8, 10 };
//			int[] ypoints_1 = { 2, 2, 10, 10 };
			int npoints_1 = 4;
			
			int[] xpoints_1 = { 4, 8, 16, 20 };
			int[] ypoints_1 = { 4, 4, 20, 20 };
//			int npoints_1 = 8;
			Polygon p_left = new Polygon(xpoints_1, ypoints_1, npoints_1);// 左上角到右下角图标

			int[] xpoints_2 = xpoints_1;
//			int[] ypoints_2 = { 10, 10, 2, 2 };
			int[] ypoints_2 = { 20, 20, 4, 4 };
			int npoints_2 = 4;
			Polygon p_right = new Polygon(xpoints_2, ypoints_2, npoints_2);// 右上角到左下角图标

			if (isFocus) {
				g2d.setPaint(new GradientPaint(0, 0, fontColor, 0, width,
						new Color(fontColor.getRed(), fontColor.getGreen(),
								fontColor.getBlue(), 50)));
			} else {
				g2d.setPaint(fontColor);
			}
			// 画关闭图标("x")
			g2d.fillPolygon(p_left);
			g2d.fillPolygon(p_right);

			return bi;
		}

		public class RowPanel extends JPanel {
			RowType rowType;
			KeyLable[] keys;

			public RowPanel(RowType rowType) {
				this.rowType = rowType;
				initRowPanel();
			}

			private void initRowPanel() {
				setOpaque(true);
				setLayout(new FlowLayout(FlowLayout.CENTER, 1, 0));// 水平间隙1，垂直间隙0
				setBackground(backColor);
				if (rowType == RowType.first) {

					KeyLable key1 = new KeyLable("!", "1",
							SoftKeyBoardPanel.this);
					KeyLable key2 = new KeyLable("@", "2",
							SoftKeyBoardPanel.this);
					KeyLable key3 = new KeyLable("#", "3",
							SoftKeyBoardPanel.this);
					KeyLable key4 = new KeyLable("$", "4",
							SoftKeyBoardPanel.this);
					KeyLable key5 = new KeyLable("%", "5",
							SoftKeyBoardPanel.this);
					KeyLable key6 = new KeyLable("^", "6",
							SoftKeyBoardPanel.this);
					KeyLable key7 = new KeyLable("&", "7",
							SoftKeyBoardPanel.this);
					KeyLable key8 = new KeyLable("*", "8",
							SoftKeyBoardPanel.this);
					KeyLable key9 = new KeyLable("(", "9",
							SoftKeyBoardPanel.this);
					KeyLable key10 = new KeyLable(")", "0",
							SoftKeyBoardPanel.this);
					KeyLable key11 = new KeyLable("~", "`",
							SoftKeyBoardPanel.this);// 这个键的位置随机
					KeyLable key12 = new KeyLable("BackSpace", true,
							SoftKeyBoardPanel.this);// 功能键，位置固定在最右

					key1.setPreferredSize(new Dimension(78, 52));
					key2.setPreferredSize(new Dimension(78, 52));
					key3.setPreferredSize(new Dimension(78, 52));
					key4.setPreferredSize(new Dimension(78, 52));
					key5.setPreferredSize(new Dimension(78, 52));
					key6.setPreferredSize(new Dimension(78, 52));
					key7.setPreferredSize(new Dimension(78, 52));
					key8.setPreferredSize(new Dimension(78, 52));
					key9.setPreferredSize(new Dimension(78, 52));
					key10.setPreferredSize(new Dimension(78, 52));
					key11.setPreferredSize(new Dimension(78, 52));
					key12.setPreferredSize(new Dimension(104, 52));

					keys = new KeyLable[] { key1, key2, key3, key4, key5, key6,
							key7, key8, key9, key10, key11, key12 };
					ArrayList<KeyLable> keylist = new ArrayList<KeyLable>(
							keys.length);

					for (KeyLable key : keys) {
						// if (key != key11) {// key11排除在外
						keylist.add(key);
						// }
					}

					// int randomIndex = random.nextInt(keys.length - 1);//
					// 排除最后一个留给key12的位置
					// keylist.add(randomIndex, key11);

					for (KeyLable key : keylist) {
						this.add(key);
					}

					// 关闭Label
					final Image defaImage = createCloseImage(new Color(138,
							180, 231), false);
					final Image focusImage = createCloseImage(new Color(30, 90,
							150), true);
					final JLabel closeLabel = new JLabel(new ImageIcon(
							defaImage)) {
						JToolTip toolTip;

						protected void paintComponent(Graphics g) {
							super.paintComponent(g);
							ImageIcon icon = (ImageIcon) getIcon();
							if (icon != null) {
								g.drawImage(icon.getImage(), 0, 0, 12, 12, 0,
										0, 12, 12, null);
							}
						}

						public JToolTip createToolTip() {
							JToolTip toolTip = new JToolTip();
							Color color = new Color(118, 118, 118);

							toolTip.setComponent(this);
							toolTip.setTipText(this.getToolTipText());
							toolTip.setBackground(Color.WHITE);
							toolTip.setForeground(color);
							toolTip.setFont(new Font(Font.DIALOG, Font.PLAIN,
									12));

							Border outside = BorderFactory
									.createLineBorder(color);
							Border inside = BorderFactory.createEmptyBorder(2,
									3, 2, 3);
							CompoundBorder border = BorderFactory
									.createCompoundBorder(outside, inside);
							toolTip.setBorder(border);
							return toolTip;
						}
					};
					MouseAdapter mouseAdapter = new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							popupMenu.setVisible(false);
						}

						public void mouseEntered(MouseEvent e) {
							closeLabel.setIcon(new ImageIcon(focusImage));
						}

						public void mouseExited(MouseEvent e) {
							closeLabel.setIcon(new ImageIcon(defaImage));
						}
					};
					closeLabel.setToolTipText("关闭");
					closeLabel.addMouseListener(mouseAdapter);
					// closeLabel.setPreferredSize(new Dimension(12, 12));
					closeLabel.setPreferredSize(new Dimension(36, 36));
					add(closeLabel);
				} else if (rowType == RowType.second) {
					// key1至key10是一个闭环顺序，通过随机数决定在哪个位置切断这个环从而使其变成单链
					KeyLable key1 = new KeyLable("+", "=",
							SoftKeyBoardPanel.this);
					KeyLable key2 = new KeyLable("|", "\\",
							SoftKeyBoardPanel.this);
					KeyLable key3 = new KeyLable("{", "[",
							SoftKeyBoardPanel.this);
					KeyLable key4 = new KeyLable("}", "]",
							SoftKeyBoardPanel.this);
					KeyLable key5 = new KeyLable(":", ";",
							SoftKeyBoardPanel.this);
					KeyLable key6 = new KeyLable("\"", "'",
							SoftKeyBoardPanel.this);
					KeyLable key7 = new KeyLable("<", ",",
							SoftKeyBoardPanel.this);
					KeyLable key8 = new KeyLable(">", ".",
							SoftKeyBoardPanel.this);
					KeyLable key9 = new KeyLable("?", "/",
							SoftKeyBoardPanel.this);
					KeyLable key10 = new KeyLable("_", "-",
							SoftKeyBoardPanel.this);
					KeyLable key11 = new KeyLable("Shift", true,
							SoftKeyBoardPanel.this);// 功能键，位置固定在最左
					KeyLable key12 = new KeyLable("CapsLock", true,
							SoftKeyBoardPanel.this);// 功能键，位置固定在最右
					key1.setPreferredSize(new Dimension(78, 52));
					key2.setPreferredSize(new Dimension(78, 52));
					key3.setPreferredSize(new Dimension(78, 52));
					key4.setPreferredSize(new Dimension(78, 52));
					key5.setPreferredSize(new Dimension(78, 52));
					key6.setPreferredSize(new Dimension(78, 52));
					key7.setPreferredSize(new Dimension(78, 52));
					key8.setPreferredSize(new Dimension(78, 52));
					key9.setPreferredSize(new Dimension(78, 52));
					key10.setPreferredSize(new Dimension(78, 52));
					key11.setPreferredSize(new Dimension(104, 52));
					key12.setPreferredSize(new Dimension(104, 52));

					keys = new KeyLable[] { key11, key1, key2, key3, key4,
							key5, key6, key7, key8, key9, key10, key12 };
					ArrayList<KeyLable> keylist = new ArrayList<KeyLable>(
							keys.length);
					// int randomIndex = random.nextInt(keys.length - 2) + 1;//
					// 随机切入点，排除key11和key12的位置

					keylist.add(key11);
					for (int i = 0; i < keys.length; i++) {
						keylist.add(keys[i]);
					}
					// for (int i = 1; i < randomIndex; i++) {
					// keylist.add(keys[i]);
					// }
					// keylist.add(key12);

					for (KeyLable key : keylist) {
						this.add(key);
					}
				} else if (rowType == RowType.third) {
					// key1至key13是一个闭环顺序，通过随机数决定在哪个位置切断这个环从而使其变成单链
					KeyLable key1 = new KeyLable("c", SoftKeyBoardPanel.this);
					KeyLable key2 = new KeyLable("d", SoftKeyBoardPanel.this);
					KeyLable key3 = new KeyLable("e", SoftKeyBoardPanel.this);
					KeyLable key4 = new KeyLable("f", SoftKeyBoardPanel.this);
					KeyLable key5 = new KeyLable("g", SoftKeyBoardPanel.this);
					KeyLable key6 = new KeyLable("h", SoftKeyBoardPanel.this);
					KeyLable key7 = new KeyLable("i", SoftKeyBoardPanel.this);
					KeyLable key8 = new KeyLable("j", SoftKeyBoardPanel.this);
					KeyLable key9 = new KeyLable("k", SoftKeyBoardPanel.this);
					KeyLable key10 = new KeyLable("l", SoftKeyBoardPanel.this);
					KeyLable key11 = new KeyLable("m", SoftKeyBoardPanel.this);
					KeyLable key12 = new KeyLable("a", SoftKeyBoardPanel.this);
					KeyLable key13 = new KeyLable("b", SoftKeyBoardPanel.this);

					key1.setPreferredSize(new Dimension(78, 52));
					key2.setPreferredSize(new Dimension(78, 52));
					key3.setPreferredSize(new Dimension(78, 52));
					key4.setPreferredSize(new Dimension(78, 52));
					key5.setPreferredSize(new Dimension(78, 52));
					key6.setPreferredSize(new Dimension(78, 52));
					key7.setPreferredSize(new Dimension(78, 52));
					key8.setPreferredSize(new Dimension(78, 52));
					key9.setPreferredSize(new Dimension(78, 52));
					key10.setPreferredSize(new Dimension(78, 52));
					key11.setPreferredSize(new Dimension(78, 52));
					key12.setPreferredSize(new Dimension(78, 52));
					key13.setPreferredSize(new Dimension(78, 52));

					keys = new KeyLable[] { key12, key13, key1, key2, key3,
							key4, key5, key6, key7, key8, key9, key10, key11 };
					ArrayList<KeyLable> keylist = new ArrayList<KeyLable>(
							keys.length);
					// int randomIndex = random.nextInt(keys.length);// 随机切入点

					for (int i = 0; i < keys.length; i++) {
						keylist.add(keys[i]);
					}
					// for (int i = 0; i < randomIndex; i++) {
					// keylist.add(keys[i]);
					// }

					for (KeyLable key : keylist) {
						this.add(key);
					}
				} else if (rowType == RowType.fourth) {
					// key1至key13是一个闭环顺序，通过随机数决定在哪个位置切断这个环从而使其变成单链
					KeyLable key1 = new KeyLable("n", SoftKeyBoardPanel.this);
					KeyLable key2 = new KeyLable("o", SoftKeyBoardPanel.this);
					KeyLable key3 = new KeyLable("p", SoftKeyBoardPanel.this);
					KeyLable key4 = new KeyLable("q", SoftKeyBoardPanel.this);
					KeyLable key5 = new KeyLable("r", SoftKeyBoardPanel.this);
					KeyLable key6 = new KeyLable("s", SoftKeyBoardPanel.this);
					KeyLable key7 = new KeyLable("t", SoftKeyBoardPanel.this);
					KeyLable key8 = new KeyLable("u", SoftKeyBoardPanel.this);
					KeyLable key9 = new KeyLable("v", SoftKeyBoardPanel.this);
					KeyLable key10 = new KeyLable("w", SoftKeyBoardPanel.this);
					KeyLable key11 = new KeyLable("x", SoftKeyBoardPanel.this);
					KeyLable key12 = new KeyLable("y", SoftKeyBoardPanel.this);
					KeyLable key13 = new KeyLable("z", SoftKeyBoardPanel.this);

					key1.setPreferredSize(new Dimension(78, 52));
					key2.setPreferredSize(new Dimension(78, 52));
					key3.setPreferredSize(new Dimension(78, 52));
					key4.setPreferredSize(new Dimension(78, 52));
					key5.setPreferredSize(new Dimension(78, 52));
					key6.setPreferredSize(new Dimension(78, 52));
					key7.setPreferredSize(new Dimension(78, 52));
					key8.setPreferredSize(new Dimension(78, 52));
					key9.setPreferredSize(new Dimension(78, 52));
					key10.setPreferredSize(new Dimension(78, 52));
					key11.setPreferredSize(new Dimension(78, 52));
					key12.setPreferredSize(new Dimension(78, 52));
					key13.setPreferredSize(new Dimension(78, 52));

					keys = new KeyLable[] { key1, key2, key3, key4, key5, key6,
							key7, key8, key9, key10, key11, key12, key13 };
					ArrayList<KeyLable> keylist = new ArrayList<KeyLable>(
							keys.length);
					// int randomIndex = random.nextInt(keys.length);// 随机切入点

					for (int i = 0; i < keys.length; i++) {
						keylist.add(keys[i]);
					}
					// for (int i = 0; i < randomIndex; i++) {
					// keylist.add(keys[i]);
					// }

					for (KeyLable key : keylist) {
						this.add(key);
					}
				}
			}

			public KeyLable[] getKeys() {
				return keys;
			}
		}
	}

	// 键标签
	public static class KeyLable extends JLabel {

		// 用String而不是char考虑有功能键显示的是文字，不想再多一个字段了
		String centerKey;
		String lowerLeftKey;
		boolean isBackSpace;
		boolean isCapsLock;
		boolean isShift;
		boolean isPressed;
		KeyStatus status = KeyStatus.normal;
		Dimension size = new Dimension(48, 48);
		Color keyBorderColor = new Color(54, 112, 184);
		Color keyBorderFocusColor = new Color(64, 194, 241);
		Color keyBackColor = new Color(253, 255, 255);
		Color keyBackFocusColor = new Color(28, 159, 228);
		Font boldFont = new Font("微软雅黑", Font.PLAIN, 18);
		Color boldColor = new Color(0, 0, 57);
		Font plainFont = new Font("微软雅黑", Font.PLAIN, 20);
		Color plainColor = new Color(156, 157, 197);

		public KeyLable(String centerKey, ActionListener action) {
			this(centerKey, null, action);
		}

		public KeyLable(String centerKey, String lowerLeftKey,
				ActionListener action) {
			this(centerKey, lowerLeftKey, false, action);
		}

		public KeyLable(String centerKey, boolean isFunctionKey,
				ActionListener action) {
			this(centerKey, null, isFunctionKey, action);
		}

		public KeyLable(String centerKey, String lowerLeftKey,
				boolean isFunctionKey, final ActionListener action) {
			this.centerKey = centerKey;
			this.lowerLeftKey = lowerLeftKey;
			if (isFunctionKey) {// 这个变量主要是提高效率
				if (centerKey.indexOf("Shift") >= 0) {
					isShift = true;
				} else if (centerKey.indexOf("Back") >= 0
						|| centerKey.indexOf("Space") >= 0) {
					isBackSpace = true;
				} else if (centerKey.indexOf("Caps") >= 0
						|| centerKey.indexOf("Lock") >= 0) {
					isCapsLock = true;
				}
			}

			setOpaque(true);// 不透明
			setBackground(keyBackColor);
			setPreferredSize(size);
			setBorder(BorderFactory.createLineBorder(keyBorderColor));
			setFont(boldFont);

			addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					KeyLable.this.setBackground(keyBackFocusColor);// 鼠标悬浮时的背景色
				}

				public void mouseExited(MouseEvent e) {
					// 如果不是Shift和CapsLock键则还原背景色，或者是Shift和CapsLock键但是不是按压状态也要还原背景色
					if ((!KeyLable.this.isShift && !KeyLable.this.isCapsLock)
							|| (!isPressed)) {
						KeyLable.this.setBackground(keyBackColor);
					}
				}

				public void mouseClicked(MouseEvent e) {
					// 创建一个ActionEvent将KeyLable对象作为Source
					action.actionPerformed(new ActionEvent(KeyLable.this,
							ActionEvent.ACTION_PERFORMED, e.getID() + ""));
				}
			});
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);// 完成背景色的绘制

			Graphics2D g2d = (Graphics2D) g;
			ImageTool.setAntiAliasing(g2d);// 抗锯齿
			Container parent = getParent();
			ImageTool.clearAngle(g2d, parent != null ? parent.getBackground()
					: this.getBackground(), this.getWidth(), this.getHeight(),
					4);// 清除角落变圆角

			if (getMousePosition() != null) {// 如果鼠标正在这个键的范围内，绘制圆角边框
				g2d.setPaint(keyBorderFocusColor);
				g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 4, 4);
			}

			if (status == KeyStatus.normal || status == KeyStatus.capsLock) {
				if (lowerLeftKey == null) {
					g2d.setFont(boldFont);
					g2d.setPaint(boldColor);
					// g2d.drawString(centerKey, isCommKey() ? 8 : 4, 17);
					// SwingUtilities2.drawStringUnderlineCharAt(this, g2d,
					// centerKey, -1, isCommKey() ? 8 : 4, 17);
					SwingUtilities2.drawStringUnderlineCharAt(this, g2d,
							centerKey, -1, isCommKey() ? 16 : 8, 34);

				} else {
					g2d.setFont(plainFont);
					g2d.setPaint(plainColor);
					// g2d.drawString(centerKey, 12, 15);
					// SwingUtilities2.drawStringUnderlineCharAt(this, g2d,
					// centerKey, -1, 12, 15);
					SwingUtilities2.drawStringUnderlineCharAt(this, g2d,
							centerKey, -1, 24, 30);

					g2d.setFont(boldFont);
					g2d.setPaint(boldColor);
					// g2d.drawString(lowerLeftKey, 3, 20);
					// SwingUtilities2.drawStringUnderlineCharAt(this, g2d,
					// lowerLeftKey, -1, 3, 20);
					SwingUtilities2.drawStringUnderlineCharAt(this, g2d,
							lowerLeftKey, -1, 6, 40);
				}
			} else if (status == KeyStatus.shift
					|| status == KeyStatus.shiftAndCapsLock) {
				if (lowerLeftKey == null) {
					g2d.setFont(boldFont);
					g2d.setPaint(boldColor);
					// g2d.drawString(centerKey, isCommKey() ? 8 : 4, 17);
					SwingUtilities2.drawStringUnderlineCharAt(this, g2d,
							centerKey, -1, isCommKey() ? 16 : 8, 34);

				} else {
					g2d.setFont(boldFont);
					g2d.setPaint(boldColor);
					// g2d.drawString(centerKey, 10, 15);
					SwingUtilities2.drawStringUnderlineCharAt(this, g2d,
							centerKey, -1, 20, 30);

					g2d.setFont(plainFont);
					g2d.setPaint(plainColor);
					// g2d.drawString(lowerLeftKey, 3, 20);
					SwingUtilities2.drawStringUnderlineCharAt(this, g2d,
							lowerLeftKey, -1, 6, 40);
				}
			}
		}

		public String getCenterKey() {
			return centerKey;
		}

		public String getLowerLeftKey() {
			return lowerLeftKey;
		}

		public boolean isBackSpace() {
			return isBackSpace;
		}

		public boolean isCapsLock() {
			return isCapsLock;
		}

		public boolean isShift() {
			return isShift;
		}

		public void setPressed(boolean isPressed) {
			this.isPressed = isPressed;
		}

		public boolean isPressed() {
			return isPressed;
		}

		public boolean isCommKey() {
			return !isBackSpace && !isCapsLock && !isShift;
		}

		// 重置
		public void reset() {
			this.isPressed = false;
			if (isShift || isCapsLock) {
				KeyLable.this.setBackground(keyBackColor);
			} else if (isCommKey()) {
				if (lowerLeftKey == null) {
					centerKey = centerKey.toLowerCase();
				}
			}
			status = KeyStatus.normal;
			repaint();
		}

		// 设置状态
		public void setStatus(KeyStatus status) {
			if (isCommKey() && this.status != status) {
				if (status == KeyStatus.shift || status == KeyStatus.capsLock) {
					if (lowerLeftKey == null) {
						if (Character.isUpperCase(centerKey.charAt(0))) {
							centerKey = centerKey.toLowerCase();
						} else {
							centerKey = centerKey.toUpperCase();
						}
					}
				} else if (status == KeyStatus.normal
						|| status == KeyStatus.shiftAndCapsLock) {
					if (lowerLeftKey == null) {
						centerKey = centerKey.toLowerCase();
					}
				}
				this.status = status;
				repaint();
			}
		}
	}

	public static enum RowType {
		first, second, third, fourth
	}

	public static enum KeyStatus {
		normal, shift, capsLock, shiftAndCapsLock
	}
}

class ImageTool {

	// 设置Graphics2D抗锯齿,具体请查看RenderingHints类的API
	public static void setAntiAliasing(Graphics2D g2d) {
		setRenderingHint(g2d, RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	}

	public static void setRenderingHint(Graphics2D g2d, Key key, Object value) {
		if (g2d.getRenderingHints() == null) {
			g2d.setRenderingHints(new RenderingHints(key, value));
		} else {
			g2d.setRenderingHint(key, value);
		}
	}

	// 绘制圆角
	public static void drawRoundRect(Graphics2D g2d, int width, int height,
			int r, Paint anglePaint, Paint[] borderPaints) {
		clearAngle(g2d, anglePaint, width, height, r);// 清除角落
		drawMultiBorder(g2d, width, height, r, anglePaint, borderPaints);// 画边框
	}

	// 清除角落
	public static void clearAngle(Graphics2D g2d, Paint anglePaint, int width,
			int height, int r) {
		setAntiAliasing(g2d);
		Composite oldComposite = g2d.getComposite();

		if (anglePaint == null) {
			g2d.setComposite(AlphaComposite.Clear);// 设置Composite为清空
		} else {
			g2d.setPaint(anglePaint);
		}

		int npoints = 5;// 5点定位一个角落轨迹
		// 左上角
		int[] xpoints1 = { r, 0, 0, r / 4, r / 2 };
		int[] ypoints1 = { 0, 0, r, r / 2, r / 4 };
		Polygon polygon = new Polygon(xpoints1, ypoints1, npoints);
		g2d.fillPolygon(polygon);
		// 右上角
		int[] xpoints2 = { width - r, width, width, width - r / 4,
				width - (r / 2) };
		int[] ypoints2 = ypoints1;
		polygon = new Polygon(xpoints2, ypoints2, npoints);
		g2d.fillPolygon(polygon);
		// 右下角
		int[] xpoints3 = xpoints2;
		int[] ypoints3 = { height, height, height - r, height - (r / 2),
				height - r / 4 };
		polygon = new Polygon(xpoints3, ypoints3, npoints);
		g2d.fillPolygon(polygon);
		// 左下角
		int[] xpoints4 = xpoints1;
		int[] ypoints4 = ypoints3;
		polygon = new Polygon(xpoints4, ypoints4, npoints);
		g2d.fillPolygon(polygon);
		// 还原Composite
		g2d.setComposite(oldComposite);
	}

	// 绘制有层次感的边框
	public static void drawMultiBorder(Graphics2D g2d, int width, int height,
			int r, Paint anglePaint, Paint[] borderPaints) {
		setAntiAliasing(g2d);

		int roundx = r * 2;
		int roundy = roundx;
		int grow = 2;
		int x = 0;
		int y = 0;
		int w = width;
		int h = height;

		// 从外层往内层开始画
		for (int i = 0; i < borderPaints.length; i++, x++, y++, w -= grow, h -= grow) {
			g2d.setPaint(borderPaints[i]);
			if (r > 0) {
				g2d.drawRoundRect(x, y, w - 1, h - 1, roundx, roundy);
			} else {
				g2d.drawRect(x, y, w - 1, h - 1);
			}
		}
	}
}