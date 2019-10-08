package com.helun.menu.util;

import static com.google.common.base.Preconditions.checkArgument;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Time util.
 * 
 * @author helun
 *
 */
public class Times {
  private static final Logger log = LoggerFactory.getLogger(Time.class);
  private static String DATE = "\\d{4}?-\\d{2}?-\\d{2}?";
  private static String TIME = "\\d{2}?:\\d{2}?:\\d{2}?";
  public static final String REGEX_TIME = "yyyy-MM-dd HH:mm:ss:SSS";
  public static final String REGEX_DATE = "yyyy-MM-dd";
  public static final Long MILLSEC_OF_DAY = 1000 * 60 * 60 * 24L;

  public static void main(String[] args) throws ParseException {



    System.out.println(Times.getTimeInMillis(REGEX_DATE, "2018-10-24"));
    System.out.println(Times.getTimeInMillis(REGEX_DATE, "2018-10-25"));
    System.out.println(MILLSEC_OF_DAY);
    System.out.println(Times.getTimeInMillis(REGEX_TIME, "2018-10-24 23:59:59")
        - Times.getTimeInMillis(REGEX_TIME, "2018-10-24 00:00:00"));
  }

  public static String getSomeWeeksTime(String current, int offest) throws ParseException {

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Calendar cal = Calendar.getInstance();

    Date dt = df.parse(current);
    cal.setTime(dt);
    cal.add(Calendar.WEEK_OF_MONTH, offest);
    Date date2 = cal.getTime();

    return df.format(date2);
  }

  public static boolean isVaildTime(String regex, String time) {
    Long millis = System.currentTimeMillis();
    try {
      millis = getTimeInMillis(regex, getDataAndTime(time));
    } catch (ParseException e) {
      millis = System.currentTimeMillis();
    }
    return millis <= System.currentTimeMillis();
  }

  /**
   * 判断指定时间是否在以当前为参照到过去的某个时间范围内
   * 
   * @param regex such as yyyy-MM-dd HH:mm:ss ,yyyy-MM-dd
   * @param time 需要判断的时间
   * @param field 偏移单位 just for Calendar.MONTH , Calendar.WEEK_OF_YEAR ,Calendar.DAY_OF_YEAR
   * @param offest 需要指定的时间偏移量 1：向过去偏移一个单位
   * @return true : 在指定范围内 false 超出指定范围
   */
  public static boolean isVaildTime(String regex, String time, int field, int offest) {
    try {
      offest = 0 - offest;
      Long inputTime = getTimeInMillis(regex, time);

      SimpleDateFormat df = new SimpleDateFormat(regex);
      Date dt = df.parse(formatTime(regex, System.currentTimeMillis()));
      Calendar cal = Calendar.getInstance();
      cal.setTime(dt);
      cal.setFirstDayOfWeek(Calendar.SUNDAY);
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);

      Long offestTime = 0L;
      switch (field) {
        case Calendar.WEEK_OF_YEAR: {
          cal.add(Calendar.WEEK_OF_YEAR, offest);
          cal.add(Calendar.DAY_OF_YEAR, 1 - cal.get(Calendar.DAY_OF_WEEK));
          offestTime = cal.getTimeInMillis();
          break;
        }
        case Calendar.DAY_OF_YEAR: {
          cal.add(Calendar.DAY_OF_YEAR, offest);
          offestTime = cal.getTimeInMillis();
          break;
        }
        case Calendar.MONTH: {
          cal.add(Calendar.MONTH, offest);
          cal.set(Calendar.DAY_OF_MONTH, 1);
          offestTime = cal.getTimeInMillis();
          break;
        }
      }

      if (inputTime >= offestTime && inputTime <= System.currentTimeMillis()) {
        return true;
      }

    } catch (Exception e) {
    }
    return false;
  }

  public static String getDataAndTime(String origTime) {

    return parserData(DATE, origTime) + " " + parserData(TIME, origTime);
  }

  public static String parserData(String regex, String origData) {

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(origData);
    if (matcher.find()) {
      return matcher.group();
    }
    return origData;
  }



  /**
   * @return time interval in format, example: 0 Days, 0 Hours, 0 Minutes, 0 Seconds.
   */
  public static String formatInterval(long millis) {
    checkArgument(millis > 0, "Expected time interval to be positive, but mills=%s.", millis);
    long days = millis / (1000 * 60 * 60 * 24);
    long hours = (millis % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
    long minutes = (millis % (1000 * 60 * 60)) / (1000 * 60);
    long seconds = (millis % (1000 * 60)) / 1000;
    StringBuffer ret = new StringBuffer();
    if (days > 0) {
      ret.append(days + "d ");
    }
    ret.append(String.format("%sh %smin %ss", hours, minutes, seconds));
    return ret.toString();
  }

  public static String formatInterval(Date begin, Date end) {
    return formatInterval(end.getTime() - begin.getTime());
  }

  public static String formatInterval(long begin, long end) {
    return formatInterval(end - begin);
  }

  public static String formatInterval(String format, String begin, String end) {
    try {
      Long start = getTimeInMillis(format, begin);
      Long terminal = getTimeInMillis(format, end);
      return Times.formatInterval(start, terminal);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return "";
  }

  /**
   * 
   * @param format e.g. yyyy-MM-dd HH:mm:ss
   * @param time millisecdonds
   * @return time in specified format
   */
  public static String formatTime(String format, long time) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    Date curDate = new Date(time);
    return dateFormat.format(curDate);
  }

  /**
   * @return current time in format, example: 2016-12-08 18:57:38
   */
  public static String currentFormatTime() {
    return currentFormatTime("yyyy-MM-dd HH:mm:ss");
  }

  public static String currentFormatTime(String format) {
    return formatTime(format, System.currentTimeMillis());
  }

  public static String getDataAndTime(int houre, boolean isLast) {
    Calendar now = Calendar.getInstance();
    now.setTime(new Date());
    if (isLast) {
      now.set(Calendar.DATE, now.get(Calendar.DATE) - 1);
    }
    now.set(Calendar.HOUR_OF_DAY, houre);
    now.set(Calendar.MINUTE, 0);
    now.set(Calendar.SECOND, 0);
    return formatTime("yyyy-MM-dd HH:mm:ss", now.getTimeInMillis());
  }

  public static String getWeekData(int week, boolean isLast) {
    Calendar now = Calendar.getInstance();
    now.setTime(new Date());
    if (isLast) {
      now.set(Calendar.WEEK_OF_YEAR, now.get(Calendar.WEEK_OF_YEAR) - 1);
    }
    now.set(Calendar.DAY_OF_WEEK, week + 1);
    return formatTime("yyyy-MM-dd", now.getTimeInMillis());
  }

  public static String getmonthData(int month, boolean isLast) {
    Calendar now = Calendar.getInstance();
    now.setTime(new Date());
    if (isLast) {
      now.set(Calendar.MONTH, now.get(Calendar.MONTH) - 1);
    }
    now.set(Calendar.DAY_OF_MONTH, month);
    return formatTime("yyyy-MM-dd", now.getTimeInMillis());
  }


  /**
   * @des 获取指定天数前的毫秒值
   * @param agoDays
   * @return the Millis of some days ago
   */
  public static long someDayAgoMillis(int agoDays) {
    Calendar now = Calendar.getInstance();
    now.setTime(new Date());
    now.set(Calendar.DATE, now.get(Calendar.DATE) - agoDays);
    return now.getTimeInMillis();
  }

  public static long someWeekAgoMillis(int agoWeeks) {
    Calendar now = Calendar.getInstance();
    now.setTime(new Date());
    now.set(Calendar.WEEK_OF_YEAR, now.get(Calendar.WEEK_OF_YEAR) - agoWeeks);
    return now.getTimeInMillis();
  }

  public static long someMonthAgoMillis(int agoMonths) {
    Calendar now = Calendar.getInstance();
    now.setTime(new Date());
    now.set(Calendar.MONTH, now.get(Calendar.MONTH) - agoMonths);
    return now.getTimeInMillis();
  }

  /**
   * 一年的第几天
   * 
   * @param format
   * @param time
   * @return
   */
  public static Integer getDayInYear(String format, String time) {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    Date date = null;
    try {
      date = sdf.parse(time);
    } catch (ParseException e) {
      log.error("Fail to parse time.Please check your format and time.{},{}", format, time);
      return Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal.get(Calendar.DAY_OF_YEAR);

  }

  /**
   * 一年的第几周
   * 
   * @param format
   * @param time
   * @return
   */
  public static Integer getWeekInYear(String format, String time) {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    Date date = null;
    try {
      date = sdf.parse(time);
    } catch (ParseException e) {
      log.error("Fail to parse time.Please check your format and time.{},{}", format, time);
      return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
    }
    Calendar cal = Calendar.getInstance();
    cal.setFirstDayOfWeek(Calendar.SUNDAY);
    cal.setTime(date);
    return cal.get(Calendar.WEEK_OF_YEAR);

  }


  /**
   * 一年的第几个月
   * 
   * @param format
   * @param time
   * @return
   */
  public static Integer getMonthInYear(String format, String time) {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    Date date = null;
    try {
      date = sdf.parse(time);
    } catch (ParseException e) {
      log.error("Fail to parse time.Please check your format and time.{},{}", format, time);
      return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal.get(Calendar.MONTH) + 1;
  }

  /**
   * 
   * @param format
   * @param time
   * @return the millis for a specified time
   * @throws ParseException
   */
  public static long getTimeInMillis(String format, String time) throws ParseException {

    SimpleDateFormat sdf = new SimpleDateFormat(format);
    Date date = null;
    date = sdf.parse(time);
    return date.getTime();
  }

  /**
   * 
   * @param time
   * @return the millis for a specified format("yyyy-MM-dd HH:mm:ss") time
   * @throws ParseException
   */
  public static long getTimeInMillisFormat(String time) throws ParseException {
    return getTimeInMillis("yyyy-MM-dd HH:mm:ss", time);
  }

  /**
   * 
   * @return return a uuid without "-"
   */
  public static String getUUID() {
    String s = UUID.randomUUID().toString();
    return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23)
        + s.substring(24);
  }

  /**
   * 时区转换
   * 
   * @param date
   * @param type 1. from Loacal to UTC. 2. from UTC to Local.
   * @return
   */
  public static String converterTimeZone(Date date, int type) {

    SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Calendar cal = Calendar.getInstance();

    cal.setTime(date);

    int zoneOffset = cal.get(Calendar.ZONE_OFFSET);

    int dstOffset = cal.get(Calendar.DST_OFFSET); // 取得夏令时差：

    if (type == 1) {
      cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset)); // 从本地时间里扣除这些差量，即可以取得UTC时间
    } else {
      cal.add(Calendar.MILLISECOND, (zoneOffset + dstOffset)); // 从本地时间里加上这些差量，即可以取得CST时间
    }


    return dfs.format(cal.getTime());
  }

}
