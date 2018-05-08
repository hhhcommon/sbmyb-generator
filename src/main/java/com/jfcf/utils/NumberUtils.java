package com.jfcf.utils;

import java.math.BigDecimal;

public class NumberUtils {

	private static final String NUM_FORMAT = "#";
	private static final String NUM_FORMAT_ZERO = "#.00";
	
	public static String formatDouble(Double num) {
		if(num == null) {
			return null;
		}
		java.text.DecimalFormat   df   =new   java.text.DecimalFormat(NUM_FORMAT); 
		return df.format(num);
	}
	
	public static String formatBigDecimal(BigDecimal num) {
		if(num == null) {
			return null;
		}
		java.text.DecimalFormat   df   =new   java.text.DecimalFormat(NUM_FORMAT); 
		return df.format(num);
	}
	
    /**
     * 保留小数点后两位00
     * @param num
     * @return String
     */
	public static String formatBigDecimalTwoZero(BigDecimal num) {
		if(num == null) {
			return null;
		}
		java.text.DecimalFormat   df   =new   java.text.DecimalFormat(NUM_FORMAT_ZERO); 
		return df.format(num);
	}
}
