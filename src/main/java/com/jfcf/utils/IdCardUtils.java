package com.jfcf.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdCardUtils {
	private static  Logger logger =  LoggerFactory.getLogger(IdCardUtils.class);
	
	/** 中国公民身份证号码最小长度。 */
	public static final int CHINA_ID_MIN_LENGTH = 15;
	/** 中国公民身份证号码最大长度。 */
	public static final int CHINA_ID_MAX_LENGTH = 18;
	/** 省、直辖市代码表 */
	public static final String cityCode[] = { "11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36", "37",
			"41", "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71", "81", "82", "91" };
	public static final Map<String, String> cityCodes = new HashMap<String, String>();
	/** 每位加权因子 */
	public static final int power[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
	/** 最低年限 */
	public static final int MIN = 1930;
	/** 台湾身份首字母对应数字 */
	public static Map<String, Integer> twFirstCode = new HashMap<String, Integer>();
	/** 香港身份首字母对应数字 */
	public static Map<String, Integer> hkFirstCode = new HashMap<String, Integer>();
	/**
	 * 功能：把15位身份证转换成18位
	 * 
	 * @param id
	 * @return newid or id
	 */
	public static final String getIDCard_18bit(String id) {
		// 若是15位，则转换成18位；否则直接返回ID
		if (15 == id.length()) {
			final int[] W = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };
			final String[] A = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
			int i, j, s = 0;
			String newid;
			newid = id;
			newid = newid.substring(0, 6) + "19" + newid.substring(6, id.length());
			for (i = 0; i < newid.length(); i++) {
				j = Integer.parseInt(newid.substring(i, i + 1)) * W[i];
				s = s + j;
			}
			s = s % 11;
			newid = newid + A[s];
			return newid;
		} else {
			return id;
		}
	}

	/**
	 * 从身份证获取出生日期
	 * 
	 * @param cardNumber
	 *            已经校验合法的身份证号
	 * @return trig YYYY-MM-DD 出生日期
	 */
	public static String getBirthDateFromCard(String cardNumber) {
		return getBirthDateFromCard(cardNumber, "-");
	}
	/**
	 * 返回指定格式的生日
	 * @param cardNumber 身份证号
	 * @param format /  -
	 * @return
	 * @author ducongcong
	 * @createDate 2016年12月16日
	 * @updateDate
	 */
	public static String getBirthDateFromCard(String cardNumber,String format) {
		String card = cardNumber.trim();
		String year;
		String month;
		String day;
		if (card.length() == 18) { // 处理18位身份证
			year = card.substring(6, 10);
			month = card.substring(10, 12);
			day = card.substring(12, 14);
		} else { // 处理非18位身份证
			year = card.substring(6, 8);
			month = card.substring(8, 10);
			day = card.substring(10, 12);
			year = "19" + year;
		}
		if (month.length() == 1) {
			month = "0" + month; // 补足两位
		}
		if (day.length() == 1) {
			day = "0" + day; // 补足两位
		}
		return year + format + month + format + day;
	}

	/**
	 * 从身份证获取性别
	 * 
	 * @param cardNumber
	 *            已经校验合法的身份证号
	 * @return String Sex 性别
	 */
	public static String getSexFromCard(String cardNumber) {
		String inputStr = cardNumber.toString();
		int sex;
		if (inputStr.length() == 18) {
			sex = inputStr.charAt(16);
			if (sex % 2 == 0) {
				return ConstantConfig.FEMALE;
			} else {
				return ConstantConfig.MALE;
			}
		} else {
			sex = inputStr.charAt(14);
			if (sex % 2 == 0) {
				return ConstantConfig.FEMALE;
			} else {
				return ConstantConfig.MALE;
			}
		}
	}
	/**
	 * 根据身份编号获取年龄（周岁）
	 *
	 * @param idCard
	 *            身份编号
	 * @return 年龄 -1 为不合法身份证
	 */
	public static int getAgeByIdCard(String idCard) {
		int iAge = 0;
		if (org.apache.commons.lang3.StringUtils.isEmpty(idCard)) {
			return iAge;
		}
		if (validateCard(idCard)) { // 验证身份证是否合法
			if (idCard.length() == CHINA_ID_MIN_LENGTH) {
				idCard = conver15CardTo18(idCard);
			}
			int year = Integer.valueOf(idCard.substring(6, 10)) ;
			int month = Integer.valueOf(idCard.substring(10,12));
			int day = Integer.valueOf(idCard.substring(12,14));
			
			Calendar cal = Calendar.getInstance();
			int iCurrYear = cal.get(Calendar.YEAR);
			int iCurrMonth = cal.get(Calendar.MONTH)+1;
			int iCurrDay = cal.get(Calendar.DAY_OF_MONTH);
			
			iAge = iCurrYear - Integer.valueOf(year);
			if (month<iCurrMonth) {
				return iAge;
			}else if (month == iCurrMonth) {
				if (day > iCurrDay) {
					return iAge-1;
				}else {
					return iAge;
				}
			}else {
				return iAge-1;
			}
		} else {
			return -1;
		}
	}
	/**
	 * 验证身份证是否合法
	 */
	public static boolean validateCard(String idCard) {
		String card = idCard.trim();
		if (validateIdCard18(card)) {
			return true;
		}
		if (validateIdCard15(card)) {
			return true;
		}
		String[] cardval = validateIdCard10(card);
		if (cardval != null) {
			if (cardval[2].equals("true")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 将15位身份证号码转换为18位
	 *
	 * @param idCard
	 *            15位身份编码
	 * @return 18位身份编码
	 */
	public static String conver15CardTo18(String idCard) {
		String idCard18 = "";
		if (idCard.length() != CHINA_ID_MIN_LENGTH) {
			return null;
		}
		if (isNum(idCard)) {
			// 获取出生年月日
			String birthday = idCard.substring(6, 12);
			Date birthDate = null;
			try {
				birthDate = new SimpleDateFormat("yyMMdd").parse(birthday);
			} catch (ParseException e) {
				if(logger.isErrorEnabled()){
					logger.error("解析省份中日期异常：", e);;
				}
			}
			Calendar cal = Calendar.getInstance();
			if (birthDate != null)
				cal.setTime(birthDate);
			// 获取出生年(完全表现形式,如：2010)
			String sYear = String.valueOf(cal.get(Calendar.YEAR));
			idCard18 = idCard.substring(0, 6) + sYear + idCard.substring(8);
			// 转换字符数组
			char[] cArr = idCard18.toCharArray();
			if (cArr != null) {
				int[] iCard = converCharToInt(cArr);
				int iSum17 = getPowerSum(iCard);
				// 获取校验位
				String sVal = getCheckCode18(iSum17);
				if (sVal.length() > 0) {
					idCard18 += sVal;
				} else {
					return null;
				}
			}
		} else {
			return null;
		}
		return idCard18;
	}
	/**
	 * 验证18位身份编码是否合法
	 *
	 * @param idCard
	 *            身份编码
	 * @return 是否合法
	 */
	public static boolean validateIdCard18(String idCard) {
		boolean bTrue = false;
		if (idCard.length() == CHINA_ID_MAX_LENGTH) {
			// 前17位
			String code17 = idCard.substring(0, 17);
			// 第18位
			String code18 = idCard.substring(17, CHINA_ID_MAX_LENGTH);
			if (isNum(code17)) {
				char[] cArr = code17.toCharArray();
				if (cArr != null) {
					int[] iCard = converCharToInt(cArr);
					int iSum17 = getPowerSum(iCard);
					// 获取校验位
					String val = getCheckCode18(iSum17);
					if (val.length() > 0) {
						if (val.equalsIgnoreCase(code18)) {
							bTrue = true;
						}
					}
				}
			}
		}
		return bTrue;
	}
	/**
	 * 验证15位身份编码是否合法
	 *
	 * @param idCard
	 *            身份编码
	 * @return 是否合法
	 */
	public static boolean validateIdCard15(String idCard) {
		if (idCard.length() != CHINA_ID_MIN_LENGTH) {
			return false;
		}
		if (isNum(idCard)) {
			String proCode = idCard.substring(0, 2);
			if (cityCodes.get(proCode) == null) {
				return false;
			}
			String birthCode = idCard.substring(6, 12);
			Date birthDate = null;
			try {
				birthDate = new SimpleDateFormat("yy").parse(birthCode.substring(0, 2));
			} catch (ParseException e) {
				if(logger.isErrorEnabled()){
					logger.error("解析时间异常：", e);
				}
			}
			Calendar cal = Calendar.getInstance();
			if (birthDate != null)
				cal.setTime(birthDate);
			if (!valiDate(cal.get(Calendar.YEAR), Integer.valueOf(birthCode.substring(2, 4)), Integer.valueOf(birthCode.substring(4, 6)))) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}	
	/**
	 * 验证10位身份编码是否合法
	 *
	 * @param idCard
	 *            身份编码
	 * @return 身份证信息数组
	 *         <p>
	 *         [0] - 台湾、澳门、香港 [1] - 性别(男M,女F,未知N) [2] - 是否合法(合法true,不合法false)
	 *         若不是身份证件号码则返回null
	 *         </p>
	 */
	public static String[] validateIdCard10(String idCard) {
		String[] info = new String[3];
		String card = idCard.replaceAll("[\\(|\\)]", "");
		if (card.length() != 8 && card.length() != 9 && idCard.length() != 10) {
			return null;
		}
		if (idCard.matches("^[a-zA-Z][0-9]{9}$")) { // 台湾
			info[0] = "台湾";
			String char2 = idCard.substring(1, 2);
			if (char2.equals("1")) {
				info[1] = "M";
			} else if (char2.equals("2")) {
				info[1] = "F";
			} else {
				info[1] = "N";
				info[2] = "false";
				return info;
			}
			info[2] = validateTWCard(idCard) ? "true" : "false";
		} else if (idCard.matches("^[1|5|7][0-9]{6}\\(?[0-9A-Z]\\)?$")) { // 澳门
			info[0] = "澳门";
			info[1] = "N";
			// TODO
		} else if (idCard.matches("^[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?$")) { // 香港
			info[0] = "香港";
			info[1] = "N";
			info[2] = validateHKCard(idCard) ? "true" : "false";
		} else {
			return null;
		}
		return info;
	}
	/**
	 * 数字验证
	 *
	 * @param val
	 * @return 提取的数字。
	 */
	public static boolean isNum(String val) {
		return val == null || "".equals(val) ? false : val.matches("^[0-9]*$");
	}
	
	/**
	 * 将字符数组转换成数字数组
	 *
	 * @param ca
	 *            字符数组
	 * @return 数字数组
	 */
	public static int[] converCharToInt(char[] ca) {
		int len = ca.length;
		int[] iArr = new int[len];
		try {
			for (int i = 0; i < len; i++) {
				iArr[i] = Integer.parseInt(String.valueOf(ca[i]));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return iArr;
	}

	/**
	 * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
	 *
	 * @param iArr
	 * @return 身份证编码。
	 */
	public static int getPowerSum(int[] iArr) {
		int iSum = 0;
		if (power.length == iArr.length) {
			for (int i = 0; i < iArr.length; i++) {
				for (int j = 0; j < power.length; j++) {
					if (i == j) {
						iSum = iSum + iArr[i] * power[j];
					}
				}
			}
		}
		return iSum;
	}
	/**
	 * 将power和值与11取模获得余数进行校验码判断
	 *
	 * @param iSum
	 * @return 校验位
	 */
	public static String getCheckCode18(int iSum) {
		String sCode = "";
		switch (iSum % 11) {
		case 10:
			sCode = "2";
			break;
		case 9:
			sCode = "3";
			break;
		case 8:
			sCode = "4";
			break;
		case 7:
			sCode = "5";
			break;
		case 6:
			sCode = "6";
			break;
		case 5:
			sCode = "7";
			break;
		case 4:
			sCode = "8";
			break;
		case 3:
			sCode = "9";
			break;
		case 2:
			sCode = "x";
			break;
		case 1:
			sCode = "0";
			break;
		case 0:
			sCode = "1";
			break;
		}
		return sCode;
	}
	/**
	 * 验证小于当前日期 是否有效
	 *
	 * @param iYear
	 *            待验证日期(年)
	 * @param iMonth
	 *            待验证日期(月 1-12)
	 * @param iDate
	 *            待验证日期(日)
	 * @return 是否有效
	 */
	public static boolean valiDate(int iYear, int iMonth, int iDate) {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int datePerMonth;
		if (iYear < MIN || iYear >= year) {
			return false;
		}
		if (iMonth < 1 || iMonth > 12) {
			return false;
		}
		switch (iMonth) {
		case 4:
		case 6:
		case 9:
		case 11:
			datePerMonth = 30;
			break;
		case 2:
			boolean dm = ((iYear % 4 == 0 && iYear % 100 != 0) || (iYear % 400 == 0)) && (iYear > MIN && iYear < year);
			datePerMonth = dm ? 29 : 28;
			break;
		default:
			datePerMonth = 31;
		}
		return (iDate >= 1) && (iDate <= datePerMonth);
	}
	/**
	 * 验证台湾身份证号码
	 *
	 * @param idCard
	 *            身份证号码
	 * @return 验证码是否符合
	 */
	public static boolean validateTWCard(String idCard) {
		String start = idCard.substring(0, 1);
		String mid = idCard.substring(1, 9);
		String end = idCard.substring(9, 10);
		Integer iStart = twFirstCode.get(start);
		Integer sum = iStart / 10 + (iStart % 10) * 9;
		char[] chars = mid.toCharArray();
		Integer iflag = 8;
		for (char c : chars) {
			sum = sum + Integer.valueOf(c + "") * iflag;
			iflag--;
		}
		return (sum % 10 == 0 ? 0 : (10 - sum % 10)) == Integer.valueOf(end) ? true : false;
	}
	/**
	 * 验证香港身份证号码(存在Bug，部份特殊身份证无法检查)
	 * <p>
	 * 身份证前2位为英文字符，如果只出现一个英文字符则表示第一位是空格，对应数字58 前2位英文字符A-Z分别对应数字10-35
	 * 最后一位校验码为0-9的数字加上字符"A"，"A"代表10
	 * </p>
	 * <p>
	 * 将身份证号码全部转换为数字，分别对应乘9-1相加的总和，整除11则证件号码有效
	 * </p>
	 *
	 * @param idCard
	 *            身份证号码
	 * @return 验证码是否符合
	 */
	public static boolean validateHKCard(String idCard) {
		String card = idCard.replaceAll("[\\(|\\)]", "");
		Integer sum = 0;
		if (card.length() == 9) {
			sum = (Integer.valueOf(card.substring(0, 1).toUpperCase().toCharArray()[0]) - 55) * 9
					+ (Integer.valueOf(card.substring(1, 2).toUpperCase().toCharArray()[0]) - 55) * 8;
			card = card.substring(1, 9);
		} else {
			sum = 522 + (Integer.valueOf(card.substring(0, 1).toUpperCase().toCharArray()[0]) - 55) * 8;
		}
		String mid = card.substring(1, 7);
		String end = card.substring(7, 8);
		char[] chars = mid.toCharArray();
		Integer iflag = 7;
		for (char c : chars) {
			sum = sum + Integer.valueOf(c + "") * iflag;
			iflag--;
		}
		if (end.toUpperCase().equals("A")) {
			sum = sum + 10;
		} else {
			sum = sum + Integer.valueOf(end);
		}
		return (sum % 11 == 0) ? true : false;
	}

}