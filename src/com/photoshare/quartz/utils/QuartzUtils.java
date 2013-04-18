package com.photoshare.quartz.utils;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.quartz.CronExpression;

public final class QuartzUtils {
	public static final String JOB_PACKAGE_NAME = "com.june.quartz.jobs";

	public static final String KEY_DOT = ".";

	public static final String SPECIAL_CHARACTER_INCRE = "/";

	public static final String SPECIAL_CHARACTER_AND = ",";

	public static final String SPECIAL_CHARACTER_WILD_CARD = "*";

	public static final String SPECIAL_CHARACTER_WILD_CARD_CN = "每";

	public static final String SPECIAL_CHARACTER_NONDETERMINE = "?";

	public static final String SPECIAL_CHARACTER_WEEKDAY = "W";

	public static final String SPECIAL_CHARACTER_LAST = "L";

	public static final String SPECIAL_CHARACTER_LAST_CN = "最后";

	public static final String SPECIAL_CHARACTER_DAY = "#";

	public static final String MSG_START_TIME_AFTER_SCHEDULE_DATE = "开始时间晚于调度时间";

	public static final String MSG_NO_DAY_WEEK_OF_MONTH_IN_THIS_MONTH = "这个月没有这一天";

	public static final String MSG_NO_WEEK_OF_MONTH_IN_THIS_MONTH = "这个月只有四周";

	public static final String MSG_NO_JOB_ID_SELECTED = "请选择一个任务";

	public static final String MSG_NO_START_TIME = "请选择执行时间";

	public static final String MSG_NO_START_YEAR = "请选择执行年份";

	public static final String MSG_NO_START_MONTH = "请选择执行月份";

	public static final String MSG_NO_START_WEEK_OF_MONTH = "请选择第几周执行";

	public static final String MSG_NO_START_DAY_OF_WEEK = "请选择星期几执行";

	public static final String MSG_NO_NEXT_FIRE_TIME = "调度时间设置错误，该任务不会被调度";

	public static final String MSG_NEXT_FIRE_TIME = "下次执行时间：\n";

	public static final String MSG_NEXT_FIRE_TIME_LATER_THAN_END_TIME = "任务结束时间早于执行时间";

	public static final String MSG_NO_TRIGGER_PLUG_IN = "没有设置调度时间";

	public static final String MSG_NO_END_TIME_PLUG_IN = "没有设置结束时间";

	public static final String MSG_NO_SELECT_JOBS = "请选择任务";

	public static boolean isJobKey(String key) {
		if (key == null)
			return false;
		if ("".equals(key))
			return false;
		if (key.length() < 35)
			return false;
		if (key.startsWith("job"))
			return true;
		return false;
	}

	public static boolean isBlank(String str) {
		if (str == null)
			return true;
		if ("".equals(str))
			return true;
		if ("空".equals(str)) {
			return true;
		}
		return false;
	}

	public static boolean before(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return true;
		return date1.before(date2);
	}

	public static boolean after(Date date1, Date date2) {
		return date1.after(date2);
	}

	public static int change(String date) {
		int retVal = 0;
		if ("星期日".equals(date) || "一月".equals(date) || "第一周".equals(date)) {
			retVal = 1;
		} else if ("星期一".equals(date) || "二月".equals(date)
				|| "第二周".equals(date)) {
			retVal = 2;
		} else if ("星期二".equals(date) || "三月".equals(date)
				|| "第三周".equals(date)) {
			retVal = 3;
		} else if ("星期三".equals(date) || "四月".equals(date)
				|| "第四周".equals(date)) {
			retVal = 4;
		} else if ("星期四".equals(date) || "五月".equals(date)
				|| "第五周".equals(date)) {
			retVal = 5;
		} else if ("星期五".equals(date) || "六月".equals(date)
				|| "第六周".equals(date)) {
			retVal = 6;
		} else if ("星期六".equals(date) || "七月".equals(date)
				|| "第七周".equals(date)) {
			retVal = 7;
		}

		if ("八月".equals(date)) {
			retVal = 8;
		} else if ("九月".equals(date)) {
			retVal = 9;
		} else if ("十月".equals(date)) {
			retVal = 10;
		} else if ("十一月".equals(date)) {
			retVal = 11;
		} else if ("十二月".equals(date)) {
			retVal = 12;
		}

		return retVal;
	}

	public static boolean isLastDayOfWeek(String date) {
		if ("最后一个".equals(date)) {
			return true;
		}
		return false;
	}

	public static boolean isLastDayOfMonth(String date) {
		if ("最后一天".equals(date)) {
			return true;
		}
		return false;
	}

	public static Date getDate(int year, int month, int weekOfMonth,
			int dayOfWeek, int hour, int minute, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.WEEK_OF_MONTH, weekOfMonth);
		calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		return calendar.getTime();
	}

	public static Date getDate(int year, int month, int day, int hour,
			int minute, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		return calendar.getTime();
	}

	public static Date getDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}

	public static Date getLastDayOfMonth(int year, int month, int hour,
			int minute, int second) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int value = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, value);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		return cal.getTime();
	}

	public static Date getLastDayOfWeek(int year, int month, int dayOfWeek,
			int hour, int minute, int second) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int value = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, value);
		int lastDay = cal.getTime().getDate();
		int lastDayOfWeek = cal.getTime().getDay();
		int back = lastDayOfWeek - dayOfWeek;
		if (back < 0) {
			back += 7;
		}
		lastDay -= back;
		cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		return cal.getTime();
	}

	public static int getFirstDayOfWeekInMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static String format(Date date) {
		String retVal = "";
		if (date != null) {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy年-MM月-dd天 a hh:mm:ss E");
			retVal = format.format(date);
		}
		return retVal;
	}

	public static String quartzFormat(Date date) {
		String retVal = "";
		if (date != null) {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd!HH:mm:ss");
			retVal = format.format(date);
			retVal = retVal.replaceAll("!", "T");
		}
		return retVal;
	}

	public static int getWeeksOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		return cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
	}

	public static boolean isMonthValid(String month) {
		if (!isBlank(month)) {
			int m = Integer.parseInt(month);
			if (m >= 1 && m <= 12) {
				return true;
			}
		}
		return false;
	}

	public static boolean isDayValid(String day) {
		if (!isBlank(day)) {
			int m = Integer.parseInt(day);
			if (m >= 1 && m <= 12) {
				return true;
			}
		}
		return false;
	}

	public static boolean isSecondValid(int second) {
		if (second <= 0)
			return false;
		return true;
	}

	public static boolean isRepeatCountValid(int repeatCnt) {
		if (repeatCnt < 0)
			return false;
		return true;
	}

	public static boolean isCronExpressionValid(String cronExpression) {
		return CronExpression.isValidExpression(cronExpression);
	}

	public static boolean isPast(Date date) {
		if (date == null)
			return true;
		if (date.before(new Date()))
			return true;
		return false;
	}

	public static boolean checkUrl(String urlStr) {
		try {
			new URL(urlStr);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isUrl(String url) {
		if (url == null)
			return false;
		if ("".equals(url))
			return false;
		String regEx = "^(http|https|ftp)//://([a-zA-Z0-9//.//-]+(//:[a-zA-"
				+ "Z0-9//.&%//$//-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{"
				+ "2}|[1-9]{1}[0-9]{1}|[1-9])//.(25[0-5]|2[0-4][0-9]|[0-1]{1}"
				+ "[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)//.(25[0-5]|2[0-4][0-9]|"
				+ "[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)//.(25[0-5]|2[0-"
				+ "4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0"
				+ "-9//-]+//.)*[a-zA-Z0-9//-]+//.[a-zA-Z]{2,4})(//:[0-9]+)?(/"
				+ "[^/][a-zA-Z0-9//.//,//?//'///////+&%//$//=~_//-@]*)*$";
		Pattern p = Pattern.compile(regEx);
		Matcher matcher = p.matcher(url);
		return matcher.matches();
	}

	public static void main(String[] args) {
		System.out.println(!QuartzUtils.checkUrl("http://localhost:8080")
				|| QuartzUtils.isBlank("qwe")
				|| !QuartzUtils.isCronExpressionValid("* * * * * ?")
				|| !QuartzUtils.before(new Date(), new Date()));
	}

	public static boolean isCharacterWildCard(String str) {
		if (str == null)
			return false;
		if (SPECIAL_CHARACTER_WILD_CARD.equals(str) || "0/1".equals(str))
			return true;
		return false;
	}

	public static boolean isCharacterLast(String str) {
		if (str == null)
			return false;
		if (SPECIAL_CHARACTER_LAST.equals(str))
			return true;
		return false;
	}

	public static boolean isCharacterIncre(String str) {
		if (str == null)
			return false;
		if (SPECIAL_CHARACTER_INCRE.equals(str))
			return true;
		return false;
	}

	public static boolean isCharacterNondetermine(String str) {
		if (str == null)
			return false;
		if (SPECIAL_CHARACTER_NONDETERMINE.equals(str))
			return true;
		return false;
	}

	public static boolean isCharacterDay(String str) {
		if (str == null)
			return false;
		if (SPECIAL_CHARACTER_DAY.equals(str))
			return true;
		return false;
	}

}
