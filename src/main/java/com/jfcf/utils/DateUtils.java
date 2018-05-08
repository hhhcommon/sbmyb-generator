package com.jfcf.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期处理工具类
 * 
 * @author jiang.li
 * @date 2013-12-18 11:22
 */
public class DateUtils {
	private static  Logger logger =  LoggerFactory.getLogger(DateUtils.class);

    /** 定义常量 **/
    public static final String DATE_FULL_STR = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_LONG_STR = "yyyy-MM-dd kk:mm:ss.SSS";
    public static final String DATE_SMALL_STR = "yyyy-MM-dd";
    public static final String DATE_FORMART_STR = "yyyy/MM/dd";
    public static final String DATE_MONTH = "yyyy-MM";
    public static final String DATE_TIME = "HH:mm:ss";
    public static final String DATE_KEY_STR = "yyMMddHHmmss";
    public static final String DATE_All_KEY_STR = "yyyyMMddHHmmss";
    public static final String DATE_SIMPLE_STR = "yyyyMMdd";

  

    /**
     * 给指定的日期加上(减去)月份
     * 
     * @param date
     * @param pattern
     * @param num
     * @return
     */
    public static String addMoth(Date date, String pattern, int num) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(Calendar.MONTH, num);
        return simpleDateFormat.format(calender.getTime());
    }

    /**
     * 给制定的时间加上(减去)天
     * 
     * @param date
     * @param pattern
     * @param num
     * @return
     */
    public static String addDay(Date date, String pattern, int num) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(Calendar.DATE, num);
        return simpleDateFormat.format(calender.getTime());
    }

    /**
     * 给制定的时间加上(减去)分钟
     * 
     * @param date
     * @return
     */
    public static String addMinute(Date date, String pattern, int num) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(Calendar.MINUTE, num);
        return simpleDateFormat.format(calender.getTime());
    }

    /**
     * 获取系统当前时间
     * 
     * @return
     */
    public static String getNowTime() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FULL_STR);
        return df.format(new Date());
    }

    /**
     * 获取指定格式的date
     * 
     * @param pattern
     * @return
     * @author ducongcong
     * @createDate 2017年3月20日
     * @updateDate
     */
    public static Date getDate(String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        String dateStr = df.format(new Date());
        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
        	if(logger.isErrorEnabled()){
        		logger.error("格式化pattern" + pattern + "时间异常", e);
        	}
        }
        return new Date();
    }

    /**
     * 获取系统当前时间(指定返回类型)
     * 
     * @return
     */
    public static String getNowTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    /**
     * 使用预设格式提取字符串日期
     * 
     * @param date
     *            日期字符串
     * @return
     */
    public static Date parse(String date) {
        return parse(date, DATE_FULL_STR);
    }

    /**
     * 指定指定日期字符串
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static Date parse(String date, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(date);
        } catch (ParseException e) {
        	if(logger.isErrorEnabled()){
        		logger.error("解析时间" + date + " pattern:" + pattern + "异常", e);
        	}
            return null;
        }
    }

    /**
     * 两个时间比较
     * 
     * @param
     * @return
     */
    public static int compareDateWithNow(Date date) {
        Date now = new Date();
        int rnum = date.compareTo(now);
        return rnum;
    }

    /**
     * 两个时间比较(时间戳比较)
     * 
     * @param
     * @return
     */
    public static int compareDateWithNow(long date) {
        long now = dateToUnixTimestamp();
        if (date > now) {
            return 1;
        } else if (date < now) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * 将指定的日期转换成Unix时间戳
     * 
     * @param date
     *            需要转换的日期 yyyy-MM-dd HH:mm:ss
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp(String date) {
        long timestamp = 0;
        try {
            timestamp = new SimpleDateFormat(DATE_FULL_STR).parse(date).getTime();
        } catch (ParseException e) {
        	if(logger.isErrorEnabled()){
        		logger.error("时间date" + date + "转化unixTime异常：", e);
        	}
        }

        return timestamp;
    }

    /*
     * 将指定的日期转换成Unix时间戳
     * 
     * @param date 需要转换的日期 yyyy-MM-dd
     * 
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp(String date, String dateFormat) {
        long timestamp = 0;
        try {
            timestamp = new SimpleDateFormat(dateFormat).parse(date).getTime();
        } catch (ParseException e) {
        	if(logger.isErrorEnabled()){
        		logger.error("时间date" + date + "格式：" + dateFormat + "转化unixTime异常：", e);
        	}
        }
        return timestamp;
    }

    /**
     * 将当前日期转换成Unix时间戳
     * 
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp() {
        long timestamp = new Date().getTime();
        return timestamp;
    }

    /**
     * 将Unix时间戳转换成日期
     * 
     * @param timestamp
     *            时间戳
     * @return String 日期字符串
     */
    public static String unixTimestampToDate(long timestamp) {
        SimpleDateFormat sd = new SimpleDateFormat(DATE_FULL_STR);
        sd.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sd.format(new Date(timestamp));
    }

    /**
     * 将Unix时间戳转换成日期
     * 
     * @param timestamp
     *            时间戳
     * @return String 日期字符串
     */
    public static String TimeStamp2Date(long timestamp, String dateFormat) {
        String date = new SimpleDateFormat(dateFormat).format(new Date(timestamp));
        return date;
    }

    /**
     * 将Unix时间戳转换成日期
     * 
     * @param timestamp
     *            时间戳
     * @return String 日期字符串
     */
    public static String TimeStamp2Date(long timestamp) {
        String date = new SimpleDateFormat(DATE_FULL_STR).format(new Date(timestamp));
        return date;
    }

    /**
     * 时间转字符串
     * 
     * @param date
     *            日期
     * @param format
     *            格式
     * @return
     */
    public static String date2Str(Date date, String format) {
        if (null != date && !"".equals(date)) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        }
        return null;
    }

    /**
     * 获取系统当前日期和时间，格式为yyyy-MM-dd HH:mm:ss
     * 
     * @return 返回计算后的日期时间（String）
     */
    public static String getCurrentDateTime() {
        return date2Str(new Date(), DATE_FULL_STR);
    }

    /**
     * sql.date 转字符串
     * 
     * @param time
     * @return
     */
    public static String timestamp2Str(Timestamp time) {
        if (null != time && !"".equals(time)) {
            Date date = new Date(time.getTime());
            return date2Str(date, DATE_FULL_STR);
        }
        return null;
    }

    /**
     * sql.date 转字符串
     * 
     * @param time
     * @return
     */
    public static String timestamp2Str(Timestamp time, String format) {
        if (null != time && !"".equals(time)) {
            Date date = new Date(time.getTime());
            return date2Str(date, format);
        }
        return null;
    }

    /**
     * 字符串转sql.date时间
     * 
     * @param str
     *            yyyy-MM-dd HH:mm:ss 格式时间
     * @return
     */
    public static Timestamp str2Timestamp(String str) {
        if (null != str && !"".equals(str)) {
            Date date = parse(str, DATE_FULL_STR);
            return new Timestamp(date.getTime());
        }
        return null;
    }

    /**
     * 将Timestamp类型对象转换为Date类型对象
     * 
     * @param timestamp
     *            Timestamp类型对象
     * @return Date类型对象
     */
    public static Date timestampToDate(Timestamp timestamp) {
        Date date = new Date();
        date = timestamp;
        return date;
    }

    /**
     * 将Date类型对象转换为Timestamp类型对象
     * 
     * @param date
     *            Date类型对象
     * @return Timestamp类型对象
     */
    public static Timestamp dateToTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * 获取当前时间的Timestamp
     * 
     * @param date
     *            Date类型对象
     * @return Timestamp类型对象
     */
    public static Timestamp getCurTimestamp() {
        return new Timestamp(new Date().getTime());
    }

    /**
     * 字符串转sql.date时间
     * 
     * @param str
     *            yyyy-MM-dd 格式时间
     * @return
     */
    public static java.sql.Date getSqlDate(String str) {
        if (null != str && !"".equals(str)) {
            Date date = parse(str, DATE_SMALL_STR);
            return new java.sql.Date(date.getTime());
        }
        return null;
    }
    /**
     * 获取两个时间的差值，分钟，取整
     * 
     * @author ducongcong
     * @createDate 2015年12月29日
     * @updateDate
     * @param end
     * @param start
     * @return
     * @throws Exception
     */
    public static int getTimeDiff(Timestamp end, Timestamp start) throws ParseException {
        long intDiff = diff(timestamp2Str(start), timestamp2Str(end), DATE_FULL_STR)/(1000 * 60);
        return (int) intDiff;
    }

    
    /**
     * 获取两个时间的差值，天，取整
     * 
     * @author LiuPengYuan
     * @date 2017年3月28日
     * @param end
     * @param start
     * @return
     * @throws Exception
     */
    public static int getTimeDiffDay(Timestamp end, Timestamp start) throws ParseException {
        long intDiff = diff(timestamp2Str(start), timestamp2Str(end), DATE_FULL_STR)/(1000 * 60 * 60 * 24);
        return (int) intDiff;
    }

    /**
     * 两个时间之间的天数差
     * 
     * @param startDate
     * @param endDate
     * @return
     * @author ducongcong
     * @createDate 2016年12月13日
     * @updateDate
     */
    public static int getDateDiff(String startDate, String endDate) throws ParseException  {
        long intDiff = diff(startDate, endDate, DATE_FULL_STR)/(1000 * 60 * 60 * 24);
        return (int) intDiff;
    }
    /**
     * 两日期的间隔天数
     * 
     * @param strDate1
     *            需要进行计较的日期1(yyyy-MM-dd)
     * @param strDate2
     *            需要进行计较的日期2(yyyy-MM-dd)
     * @return 间隔天数（int）
     * @throws ParseException
     */
    public static int diffDay(String strDateBegin, String strDateEnd) throws ParseException {
        long intDiff = diff(strDateBegin, strDateEnd, DATE_SMALL_STR)/(1000*60*60*24);
        return (int) intDiff;
    }

    /**
     * 两日期的间隔天数
     * 
     * @param strDate1
     *            需要进行计较的日期1(yyyy-MM-dd HH:mm:ss)
     * @param strDate2
     *            需要进行计较的日期2(yyyy-MM-dd HH:mm:ss)
     * @return 间隔天数（int）
     * @throws ParseException
     */
    public static int diffDay(String strDateBegin, String strDateEnd, String pattern) throws ParseException {
        long intDiff = diff(strDateBegin, strDateEnd, pattern)/(1000*60*60*24);
        return (int) intDiff;
    }

    /**
     * 两日期的间隔天数
     * 
     * @param strDate1
     *            需要进行计较的日期1(yyyy-MM-dd HH:mm:ss)
     * @param strDate2
     *            需要进行计较的日期2(yyyy-MM-dd HH:mm:ss)
     * @return 间隔秒数（int）
     * @throws ParseException
     */
    public static long diffSecond(String strDateBegin, String strDateEnd) throws ParseException {
        return diffSecond(strDateBegin, strDateEnd, DATE_FULL_STR);
    }

    /**
     * 两日期的间隔天数
     * 
     * @param strDate1
     *            需要进行计较的日期1
     * @param strDate2
     *            需要进行计较的日期2
     * @param formart
     *            指定日期格式
     * @return 间隔秒数（int）
     * @throws ParseException
     */
    public static long diffSecond(String strDateBegin, String strDateEnd, String formart) throws ParseException {
        return diff(strDateBegin, strDateEnd, formart)/1000;
    }
    /**
     * 两个时间之间的时间差（毫秒级）
     * @param strDateBegin
     * @param strDateEnd
     * @param formart
     * @return
     * @throws ParseException
     * @author ducongcong
     * @createDate 2017年7月31日
     * @updateDate
     */
    public static long diff(String strDateBegin, String strDateEnd, String formart)throws ParseException{
    	 SimpleDateFormat sdf = new SimpleDateFormat(formart);
         Date dateBegin = sdf.parse(strDateBegin);
         Date dateEnd = sdf.parse(strDateEnd);
         return dateEnd.getTime() - dateBegin.getTime();
    }

    /**
     * @desc: 获取时间差
     * @param startTime
     * @param endTime
     * @return long
     */
    public static long diffSecond(long startTime, long endTime) {
        long milliSencods = endTime - startTime;
        long intDiff = milliSencods / 1000;
        return intDiff;
    }

    /**
     * 获取字符串格式日期的天
     * 
     * @param dateStr
     *            yyyy-MM-dd HH:mm:ss
     * @param format
     *            格式 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static int getCurDayFromStr(String dateStr, String format) {
        int day = 0;
        SimpleDateFormat sf = new SimpleDateFormat(format);
        Date dtDate;
        try {
            dtDate = sf.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dtDate);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            calendar = null;
        } catch (ParseException e) {
        	if(logger.isErrorEnabled()){
        		logger.error("获取字符串时间" + dateStr + "格式" + format + "中的day异常", e);
        	}
        }
        return day;
    }

    /**
     * 返回yyyy-MM-dd HH:mm:ss格式的字符串日期
     * 
     * @param date
     * @return
     * @author ducongcong
     * @createDate 2016年12月13日
     * @updateDate
     */
    public static String formateDateStr(Date date) {
        SimpleDateFormat sf = new SimpleDateFormat(DATE_FULL_STR);
        return sf.format(date);
    }

    /**
     * 指定格式返回日期
     * 
     * @param date
     * @param farmat
     * @return
     * @author ducongcong
     * @createDate 2016年12月13日
     * @updateDate
     */
    public static String formateDateStr(Date date, String farmat) {
        SimpleDateFormat sf = new SimpleDateFormat(farmat);
        return sf.format(date);
    }

    public static long diffMinute(String strDateBegin, String strDateEnd) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FULL_STR);
        Date dateBegin = sdf.parse(strDateBegin);
        Date dateEnd = sdf.parse(strDateEnd);

        long milliSencods = dateEnd.getTime() - dateBegin.getTime();
        long intDiff = milliSencods / 60000;
        return intDiff;
    }

    /**
     * @desc: 获取最近的时间
     * @param time1
     * @param time2
     * @return Timestamp
     */
    public static Timestamp getLastTime(Timestamp time1, Timestamp time2) {
        if (time1 != null && time2 != null) {
            if (time1.after(time2)) {
                return time1;
            } else {
                return time2;
            }
        } else {
            if (time1 != null) {
                return time1;
            } else if (time2 != null) {
                return time2;
            }
        }
        return null;
    }

    /**
     * 日期比较
     * 
     * @param strDate1
     *            需要进行计较的日期1(yyyy-MM-dd)
     * @param strDate2
     *            需要进行计较的日期2(yyyy-MM-dd)
     * @return 比较的结果（int） -1：strDate1 < strDate2 0：strDate1 = strDate2 1：strDate1 > strDate2
     * @throws ParseException
     */
    public static int compareDate(String strDate1, String strDate2) throws ParseException {
        java.util.Date dtDate1 = null;
        java.util.Date dtDate2 = null;
        int intCom = 0;
        SimpleDateFormat myFormatter = new SimpleDateFormat(DATE_SMALL_STR);
        myFormatter.setLenient(false);
        dtDate1 = myFormatter.parse(strDate1);
        dtDate2 = myFormatter.parse(strDate2);

        intCom = dtDate1.compareTo(dtDate2);
        if (intCom > 0)
            return 1;
        if (intCom < 0)
            return -1;
        return 0;
    }

    /**
     * 日期比较
     * 
     * @param strDate1
     *            需要进行计较的日期1(yyyy-MM-dd)
     * @param strDate2
     *            需要进行计较的日期2(yyyy-MM-dd)
     * @return 比较的结果（int） -1：strDate1 < strDate2 0：strDate1 = strDate2 1：strDate1 > strDate2
     * @throws ParseException
     */
    public static int msCompareDate(String strDate1, String strDate2) throws ParseException {
        java.util.Date dtDate1 = null;
        java.util.Date dtDate2 = null;
        int intCom = 0;
        SimpleDateFormat myFormatter = new SimpleDateFormat(DATE_SMALL_STR);
        myFormatter.setLenient(false);
        dtDate1 = myFormatter.parse(strDate1);
        dtDate2 = myFormatter.parse(strDate2);

        intCom = dtDate1.compareTo(dtDate2);
        if (intCom > 0)
            return 1;
        if (intCom < 0)
            return -1;
        return 0;
    }

    /**
     * 获取当前日期、时间
     * 
     * @return 系统当前的日期/时间（Date）
     */
    public static java.util.Date msGetCurrentDate() {
        java.util.Date dtDate = new Date();
        return dtDate;
    }

    /**
     * 日期/时间格式化显示（年、月、日、时、分、秒、毫秒、星期）
     * 
     * @param dtmDate
     *            需要格式化的日期（java.util.Date）
     * @param strFormat
     *            该日期的格式串
     * @return 格式化后的字符串（String）
     */
    public static String msFormatDateTime(java.util.Date dtmDate, String strFormat) {

        if (strFormat.equals(""))
            strFormat = DATE_FULL_STR;

        SimpleDateFormat myFormat = new SimpleDateFormat(strFormat);

        return myFormat.format(dtmDate.getTime());
    }

    /**
     * 返回格式化的当前日期/时间
     * 
     * @param strFormat
     *            格式串
     * @return 当前日期/时间格式化后的字符串（String）
     */
    public static String getFormatCurrentDate(String strFormat) {
        return msFormatDateTime(msGetCurrentDate(), strFormat);
    }

    /**
     * 获取当前日期
     * 
     * @return yyyyMMdd
     */
    public static String getCurrentDateSplit() {
        return DateUtils.getFormatCurrentDate("yyyyMMdd");
    }
}
