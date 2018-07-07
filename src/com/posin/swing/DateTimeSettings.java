package com.posin.swing;

import io.loli.datepicker.DateFilter;
import io.loli.datepicker.DatePicker;
import io.loli.datepicker.DatePicker.DatePanelClickListener;
import io.loli.datepicker.TimePicker;
import io.loli.datepicker.TimePicker.TimePanelClickListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.posin.utils.ProcessUtils;
import com.posin.utils.ProcessUtils.Callback;
import com.posin.utils.StringUtils;

/**
 * 时间及日期
 * 
 * @author Greetty
 * 
 */
public class DateTimeSettings {

	public JPanel dateSettingPanel;
	private JPanel datePanel; // 设置日期
	private JPanel timePanel; // 设置时间
	private JPanel autoSyncDatePanel; // 自动同步时间
	private TimePicker timePicker = null;
	private DatePicker datePicker = null;
	private JButton syncDataButton = null;
	private JPanel syncShowTimePanel = null;
	private ProcessUtils mProcessUtils = null;
	public static Timer mTimer = new Timer();
	private boolean SyncTimeSuccess = false; // 同步时间是否成功

	// private boolean isAutoSyns;
	private static class DateTimeHolder {
		private static final DateTimeSettings DATE_TIME_SETTINGS_INSTANCE = new DateTimeSettings();
	}

	public static DateTimeSettings getInstance() {
		return DateTimeHolder.DATE_TIME_SETTINGS_INSTANCE;
	}

	private DateTimeSettings() {
		dateSettingPanel = new JPanel();
		dateSettingPanel.setBackground(Color.WHITE);
		dateSettingPanel.setLayout(new GridBagLayout());
		addLine(dateSettingPanel, 0, 0, -8, Color.GRAY);

		mProcessUtils = new ProcessUtils();
		boolean isAutoSyns = StringUtils.isAutoRefreshData();
		if (isAutoSyns) {
			System.out.println("******************* yes *********************");
			initAutoSyncDateUI(true);
		} else {
			System.out.println("******************* no *********************");
			initAutoSyncDateUI(false);
		}
		initDatePanelUI();
		addLine(dateSettingPanel, 0, 3, -9, Color.GRAY);
		initTimePanelUI();
		addLine(dateSettingPanel, 0, 5, -9, Color.GRAY);
		initSyncShowTimePanelUI();
		initEmptyPanelUI();
		initAutoSyncDate(isAutoSyns, syncDataButton);
		checkAutoSyncDateStatus(isAutoSyns);
	}

	/**
	 * 显示自动同步时的时间
	 */
	private void initSyncShowTimePanelUI() {
		Font f = new Font("隶书", Font.PLAIN, 30);
		syncShowTimePanel = new JPanel();
		syncShowTimePanel.setLayout(new BorderLayout());
		final JButton showTimeButton = new JButton();
		syncShowTimePanel.add(showTimeButton, BorderLayout.CENTER);
		showTimeButton.setFocusable(false);
		showTimeButton.setFont(f);
		showTimeButton.setBackground(Color.white);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		// c.weighty = 1;
		c.gridx = 0;
		c.gridy = 6;
		c.ipadx = 10;
		c.ipady = 20;
		dateSettingPanel.add(syncShowTimePanel, c);

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
				new Runnable() {

					public void run() {
						showTimeButton.setText("当前系统时间为： "
								+ simpleDateFormat.format(System
										.currentTimeMillis()));
					}

				}, 1, 1, TimeUnit.SECONDS);
	}

	/**
	 * 自动同步时间UI
	 * 
	 * @param isAutoSyns
	 */
	public void initAutoSyncDateUI(final boolean isAutoSyns) {

		Font fontSyncButton = new Font("隶书", Font.PLAIN, 30);
		autoSyncDatePanel = new JPanel();
		autoSyncDatePanel.setLayout(new BorderLayout());
		syncDataButton = new JButton();
		syncDataButton.setFont(fontSyncButton);
		syncDataButton.setFocusable(false);

		autoSyncDatePanel.add(syncDataButton, BorderLayout.CENTER);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		// c.weighty = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.ipadx = 10;
		c.ipady = 30;
		dateSettingPanel.add(autoSyncDatePanel, c);

		syncDataButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				closeAllDialog();
				try {
					boolean autoRefreshData = StringUtils.isAutoRefreshData();
					initAutoSyncDate(!autoRefreshData, syncDataButton);
					checkAutoSyncDateStatus(!autoRefreshData);
					if (autoRefreshData) {
						mProcessUtils
								.createSuProcess(" echo ro.autorefresh.date=no > /etc/date.prop");
						System.out
								.println("systemctl disable systemd-timesyncd ");
					} else {
						mProcessUtils
								.createSuProcess(" echo ro.autorefresh.date=yes > /etc/date.prop");
						System.out
								.println("systemctl enable systemd-timesyncd ");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}

	/**
	 * 同步时间
	 * 
	 * @param isAutoSyns
	 * @param syncDataButton
	 */
	private void initAutoSyncDate(boolean isAutoSyns, JButton syncDataButton) {

		System.out.println("isAutoSyns: " + isAutoSyns);
		try {
			if (isAutoSyns) {

				mProcessUtils
						.createSuProcess("systemctl enable systemd-timesyncd \n");
				mProcessUtils
						.createSuProcess("systemctl start systemd-timesyncd \n");

				syncDataButton.setText("已开启自动同步时间，点击关闭自动同步");
				syncDataButton.setBackground(new Color(125, 198, 191));
				datePanel.setVisible(false);
				timePanel.setVisible(false);
				syncShowTimePanel.setVisible(true);
			} else {

				mProcessUtils
						.createSuProcess("systemctl disable systemd-timesyncd \n");
				mProcessUtils
						.createSuProcess("systemctl stop systemd-timesyncd \n");

				syncDataButton.setText("已关闭自动同步时间，点击开启自动同步");
				syncDataButton.setBackground(new Color(192, 192, 192));
				datePanel.setVisible(true);
				timePanel.setVisible(true);
				syncShowTimePanel.setVisible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断是否同步完成
	 * 
	 * @param isAutoSyns
	 */
	MyTimerTask myTimerTask = null;

	private void checkAutoSyncDateStatus(boolean isAutoSyns) {
		System.out.println("======= isAutoSyns : " + isAutoSyns);

		if (isAutoSyns) {
			myTimerTask = new MyTimerTask();
			mTimer.schedule(myTimerTask, 5000, 10000);
		} else {
			if (myTimerTask != null) {
				myTimerTask.cancel();
			}
		}
	}

	/**
	 * 循环检查是否同步成功时间
	 * 
	 * @author Greetty
	 * 
	 */
	private class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			try {
				System.out.println("check systemd timesyncd status ...");
				SyncTimeSuccess = false;
				mProcessUtils
						.suExecCallback(
								"timedatectl \nsystemctl status -l systemd-timesyncd \n",
								new Callback() {

									@Override
									public void readLine(String line) {
										try {
											if (line.equals("NTP synchronized: yes")) {
												MyTimerTask.this.cancel();
												myTimerTask = null;
												SyncTimeSuccess = true;
											} else if (line
													.contains("Synchronized to time server")
													&& line.contains("systemd-timesyncd[")) {
												System.out
														.println("Synchronization time success ... ");
												MyTimerTask.this.cancel();
												myTimerTask = null;
												SyncTimeSuccess = true;

											} else if (line
													.equals("**CMD-RESULT**")) {
												if (!SyncTimeSuccess) {
													System.out
															.println("Resynchronization time ... ");
													mProcessUtils
															.createSuProcess("systemctl restart systemd-timesyncd  \n");
												}
											}
										} catch (Exception e) {
											e.printStackTrace();
										}

									}
								}, 3000);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 初始化日期选择布局
	 */
	private void initDatePanelUI() {
		datePanel = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		// c.weighty = 1;
		c.gridx = 0;
		c.gridy = 2;
		c.ipadx = 10;
		c.ipady = 20;
		dateSettingPanel.add(datePanel, c);
		datePanel.setLayout(new GridLayout(0, 1));
		datePanel.setBackground(Color.white);

		Font fontTip = new Font("隶书", Font.BOLD, 25);
		Font fontDate = new Font("隶书", Font.PLAIN, 20);
		// 设置日期
		JLabel dateTipLabel = new JLabel("   设置日期");
		JLabel dateShowLabel = new JLabel("    2018年3月19日");
		dateTipLabel.setFont(fontTip);
		dateShowLabel.setFont(fontDate);

		datePanel.add(dateTipLabel);
		datePanel.add(dateShowLabel);

		modifyDate(dateShowLabel);
	}

	/**
	 * 修改日期
	 * 
	 * @param dateLabel
	 */
	private void modifyDate(final JLabel dateLabel) {

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd");
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
				new Runnable() {

					public void run() {
						dateLabel.setText("    "
								+ simpleDateFormat.format(System
										.currentTimeMillis()));
					}

				}, 1, 1, TimeUnit.SECONDS);

		datePicker = DatePicker.datePicker(datePanel, dateLabel, "yyyy-MM-dd",
				new DateFilter() {
					public boolean filter(Date date) {
						return date.getDay() == 0 || date.getDay() == 6;
					}
				});

		datePicker.setDatePanelClickListener(new DatePanelClickListener() {

			@Override
			public void clickListener() {
				if (timePicker != null) {
					if (timePicker.isShow()) {
						timePicker.close();
					}
				}
			}
		});
	}

	/**
	 * 关闭所有弹框
	 */
	public void closeAllDialog() {
		if (timePicker != null) {
			if (timePicker.isShow()) {
				timePicker.dismissPopup();
			}
		}

		if (datePicker != null) {
			if (datePicker.isShow()) {
				datePicker.dismissPopup();
			}
		}

	}

	/**
	 * 初始化时间选择布局
	 */
	private void initTimePanelUI() {
		timePanel = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		// c.weighty = 1;
		c.gridx = 0;
		c.gridy = 4;
		c.ipadx = 0;
		c.ipady = 20;
		dateSettingPanel.add(timePanel, c);
		timePanel.setLayout(new GridLayout(0, 1));
		timePanel.setBackground(Color.white);

		Font fontTip = new Font("隶书", Font.BOLD, 25);
		Font fontDate = new Font("隶书", Font.PLAIN, 20);
		// 设置日期
		JLabel timeTipLabel = new JLabel("   设置时间");
		JLabel timeShowLabel = new JLabel("    11:01");
		timeTipLabel.setFont(fontTip);
		timeShowLabel.setFont(fontDate);

		timePanel.add(timeTipLabel);
		timePanel.add(timeShowLabel);

		modifyTime(timeShowLabel);
	}

	/**
	 * 修改系统时间
	 * 
	 * @param timeShowLabel
	 */
	private void modifyTime(final JLabel timeLabel) {
		final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		final SimpleDateFormat formatDate = new SimpleDateFormat("HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
				new Runnable() {

					public void run() {
						timeLabel.setText("    "
								+ format.format(System.currentTimeMillis()));
					}

				}, 1, 1, TimeUnit.SECONDS);

		timePicker = DatePicker.timePicker(timePanel, timeLabel, "HH:mm:ss");

		timePicker.setTimePanelClickListener(new TimePanelClickListener() {

			@Override
			public void clickListener() {
				if (datePicker != null) {
					if (datePicker.isShow()) {
						datePicker.close();
					}
				}
			}
		});
	}

	public void initEmptyPanelUI() {
		JPanel emptyPanel = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 7;
		c.ipadx = 0;
		c.ipady = 100;
		dateSettingPanel.add(emptyPanel, c);

		emptyPanel.setBackground(Color.WHITE);
	}

	/**
	 * 添加横线
	 * 
	 * @param fatherJpanel
	 *            父布局
	 * @param gridx
	 *            X轴位置
	 * @param gridy
	 *            Y轴位置
	 * @param ipady
	 *            Y轴内撑大值（Android上的padding）
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
