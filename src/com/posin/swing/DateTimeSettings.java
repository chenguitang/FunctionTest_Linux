package com.posin.swing;

import io.loli.datepicker.DateFilter;
import io.loli.datepicker.DatePicker;
import io.loli.datepicker.TimePicker;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import view.InputDialog.OnClickListener;

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
	private TimePicker timePicker = null;
	private static final DateTimeSettings DATE_TIME_SETTINGS_INSTANCE = new DateTimeSettings();

	public static DateTimeSettings getInstance() {
		return DATE_TIME_SETTINGS_INSTANCE;
	}

	private DateTimeSettings() {
		dateSettingPanel = new JPanel();
		dateSettingPanel.setBackground(Color.WHITE);
		dateSettingPanel.setLayout(new GridBagLayout());
		addLine(dateSettingPanel, 0, 0, -8, Color.GRAY);

		initDatePanelUI();
		addLine(dateSettingPanel, 0, 2, -9, Color.GRAY);
		initTimePanelUI();
		addLine(dateSettingPanel, 0, 4, -9, Color.GRAY);
		initEmptyPanelUI();
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
		c.gridy = 1;
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

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateLabel.setText("    " + simpleDateFormat.format(new Date()));

		DatePicker.datePicker(datePanel, dateLabel, "yyyy-MM-dd",
				new DateFilter() {
					public boolean filter(Date date) {
						return date.getDay() == 0 || date.getDay() == 6;
					}
				});
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
		c.gridy = 3;
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

		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
				new Runnable() {

					public void run() {
						timeLabel.setText("    " + format.format(new Date()));
					}

				}, 1, 1, TimeUnit.SECONDS);

		timePicker = DatePicker.timePicker(timePanel, timeLabel, "HH:mm:ss");
	}

	public void initEmptyPanelUI() {
		JPanel emptyPanel = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 5;
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
