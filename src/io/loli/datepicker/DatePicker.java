package io.loli.datepicker;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.PopupFactory;

import com.posin.utils.ProcessUtils;
import com.posin.utils.ProcessUtils.MySuProcess;

public class DatePicker extends AbstractPicker {
	/*
	 * Default date format
	 */

	{
		format = "yyyy-MM-dd";
	}

	private SimpleDateFormat simpleDateFormat;

	private boolean setTimeAtSetup = false;

	/**
	 * Get text of text field
	 * 
	 * @return text of text field
	 */
	public String getDateText() {
		return this.field.getText();
	}

	/**
	 * @param field
	 *            to add to
	 * @param format
	 *            date format
	 */
	public DatePicker(JPanel clickPanel, final JLabel field, String format,
			DateFilter filter) {
		this.field = field;
		if (format != null) {
			this.format = format;
		}
		this.filter = filter;
		simpleDateFormat = new SimpleDateFormat(this.format);
		if (setTimeAtSetup)
			this.field.setText(simpleDateFormat.format(new Date()));

		clickPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (mPanelClickListener!=null) {
					mPanelClickListener.clickListener();
				}
				
				if (popup != null) {
					popup.hide();
					popup = null;
					return;
				}
				DatePanel datePanel = new DatePanel(DatePicker.this);
				PopupFactory factory = PopupFactory.getSharedInstance();

				// popup = factory.getPopup(field, timePanel, (int) field
				// .getLocationOnScreen().getX(), (int) field
				// .getLocationOnScreen().getY() + field.getHeight());

				datePanel.setPreferredSize(new Dimension(600, 400));
				popup = factory.getPopup(field, datePanel, 600, 400);
				popup.show();
			}
		});
	}

	public DatePicker(JPanel panel, JLabel field) {
		this(panel, field, null, new BasicDateFilter());
	}

	public static SimpleDateFormat getDateFormat(String format) {
		return new SimpleDateFormat(format);
	}

	/**
	 * Get year val from text field
	 * 
	 * @return year val
	 */
	public int getShowYear() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDate());
		return cal.get(Calendar.YEAR);
	}

	/**
	 * Get month val from text field
	 * 
	 * @return month val
	 */
	public int getShowMonth() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDate());
		return cal.get(Calendar.MONDAY);
	}

	/**
	 * Set date to text field
	 * 
	 * @param year
	 *            year to set
	 * @param month
	 *            month to set
	 * @param day
	 *            day to set
	 */
	void selectDay(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		Date date = cal.getTime();
		this.field.setText(simpleDateFormat.format(date));
		if (popup != null) {
			popup.hide();
			popup = null;
		}
	}

	/**
	 * Add a date picker to a text field
	 * 
	 * @param field
	 *            the text field you want to add to
	 */
	public static void datePicker(JPanel panel, JLabel field) {
		datePicker(panel, field, null);
	}

	/**
	 * Add a date picker to a text field with date format
	 * 
	 * @param field
	 *            the text field you want to add to
	 * @param format
	 *            date format string
	 */
	public static void datePicker(JPanel panel, JLabel field, String format) {
		new DatePicker(panel, field, format, new BasicDateFilter());
	}

	/**
	 * Add a date picker to a text field with date format
	 * 
	 * @param field
	 *            the text field you want to add to
	 * @param format
	 *            date format string
	 * @param filter
	 *            filter
	 */
	public static DatePicker datePicker(JPanel panel, JLabel field, String format,
			DateFilter filter) {
		return new DatePicker(panel, field, format, filter);
	}

	/**
	 * Add a time picker to a text field
	 * 
	 * @param field
	 *            the text field you want to add to
	 */
	public static void timePicker(JPanel panel, JLabel field) {
		timePicker(panel, field, null);
	}

	/**
	 * Add a time picker to a text field with time format
	 * 
	 * @param field
	 *            the text field you want to add to
	 * @param format
	 *            time format string
	 */
	public static TimePicker timePicker(JPanel panel, JLabel field,
			String format) {
		return new TimePicker(panel, field, format);
	}

	/**
	 * Add a date time picker to a text field
	 * 
	 * @param field
	 *            the text field you want to add to
	 */
	public static void dateTimePicker(JLabel field) {
		dateTimePicker(field, null);
	}

	/**
	 * Add a date time picker to a text field with time format
	 * 
	 * @param field
	 *            the text field you want to add to
	 * @param format
	 *            date time format string
	 */
	public static void dateTimePicker(JLabel field, String format) {
		dateTimePicker(field, format, new BasicDateFilter());
	}

	/**
	 * Add a time picker to a text field with time format
	 * 
	 * @param field
	 *            the text field you want to add to
	 * @param format
	 *            time format string
	 * @param filter
	 *            to make some days unclickable
	 */
	public static void dateTimePicker(JLabel field, String format,
			DateFilter filter) {
		new DateTimePicker(field, format, filter);
	}

	public DateFilter getDateFilter() {
		return filter;
	}

	public void set(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.getDate());

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date);

		cal.set(Calendar.YEAR, cal2.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, cal2.get(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, cal2.get(Calendar.DAY_OF_MONTH));

		long currentTimeMillis = System.currentTimeMillis();
		// new日期对象
		Date mDate = new Date(currentTimeMillis);
		// 转换提日期输出格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		String mTime = dateFormat.format(mDate);
		// String dateCmd = "date -s \"" +
		// simpleDateFormat.format(cal.getTime())
		// + " " + getDate().getHours() + ":" + getDate().getMinutes()
		// + ":" + getDate().getSeconds()+"\"";
		String dateCmd = "date -s \"" + simpleDateFormat.format(cal.getTime())
				+ " " + mTime + "\"";
		System.out.println("dateCmd: ====" + dateCmd + "====");

		try {
			System.out.println("dateCmd: " + dateCmd);
			// 修改系统时间
			new ProcessUtils().createSuProcess(dateCmd);
			new ProcessUtils().createSuProcess("busybox hwclock -w -u");
			
			Thread.sleep(400);
			// 更新系统显示时间
			this.field.setText("    "
					+ simpleDateFormat.format(new Date(System
							.currentTimeMillis())));
			close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * 是否显示选择框
	 * 
	 * @return boolean
	 */
	public boolean isShow() {
		if (popup != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 隐藏选择框
	 */
	public void dismissPopup() {
		if (popup != null) {
			popup.hide();
			popup=null;
		}
	}
	
	private DatePanelClickListener mPanelClickListener;
	
	public void setDatePanelClickListener(DatePanelClickListener panelClickListener) {
		this.mPanelClickListener=panelClickListener;
	}
	
	public interface DatePanelClickListener{
		void clickListener();
	}


}
