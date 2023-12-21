package com.android.speaker.base.component.wheel.adapter;

import android.content.Context;

import com.android.speaker.util.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * 日期底部滚轮控件适配器
 *
 * @author dingmj@guahao.com
 *
 */
public class DateWheelAdapter extends AbstractWheelTextAdapter {
	/**
	 * 默认 日期起始时间
	 */
	private static final String START_CALENDER = "1970-01-01";
	/**
	 * 默认 日期时间格式
	 */
	private static final String FORMAT_DEFAULT = "yyyy-MM-dd";
	private static final String TAG = "DateWheelAdapter";

	private Calendar startTime;
	/**
	 * 默认居中日期
	 */
	private Calendar currentTime;
	/**
	 * 日期展示格式
	 */
	private String format;

	private int dayOffset;

	public DateWheelAdapter(Context context) {
		this(context, defaultCalendar());
	}

	/**
	 *
	 * @param context
	 * @param startTime
	 *            日期滚轮的起始时间
	 */
	public DateWheelAdapter(Context context, Calendar startTime) {
		this(context, startTime, Calendar.getInstance());
	}

	public DateWheelAdapter(Context context, Calendar startTime,
			Calendar currentTime) {
		this(context, startTime, currentTime, FORMAT_DEFAULT);
	}

	public DateWheelAdapter(Context context, Calendar startTime,
			Calendar currentTime, String format) {
		super(context);
		this.startTime = startTime;
		this.currentTime = currentTime;
		this.format = format;
		// 开始日期与默认居中日期的offset
		dayOffset = daysBetween(startTime.getTime(), currentTime.getTime());
	}

	public int getDayOffset() {
		return dayOffset;
	}

	public void setDayOffset(int dayOffset) {
		this.dayOffset = dayOffset;
	}

	/**
	 * 计算两个日期之间相差的天数
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public int daysBetween(Date date1, Date date2) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	public static Calendar defaultCalendar() {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DEFAULT);
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(START_CALENDER));
		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage());
		}
		return cal;
	}

	public Calendar getDefaultTime(String parseDate) {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DEFAULT);
		Calendar cal = null;
		try {
			Date date = sdf.parse(parseDate);
			cal = Calendar.getInstance();
			cal.setTime(date);
		} catch (ParseException e) {
			LogUtil.e(TAG, e.getMessage());
		}
		return cal;
	}

	@Override
	public int getItemsCount() {
		return Integer.MAX_VALUE;
	}


	public String getCurrentText(int pos)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentTime.getTime());
		cal.add(Calendar.DATE, pos - dayOffset);
		return format(cal.getTime());
	}

	@Override
	protected CharSequence getItemText(int index) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startTime.getTime());
		cal.add(Calendar.DATE, index);
		Date date = cal.getTime();
		return format(date);
	}

	/**
	 *
	 * @param date
	 *            日期格式化
	 * @return 返回字符串
	 */
	public String format(Date date) {
		SimpleDateFormat sdf = null;
		if (null == format) {
			sdf = new SimpleDateFormat(format);
		} else {
			sdf = new SimpleDateFormat(FORMAT_DEFAULT);
		}
		return sdf.format(date);
	}

}
