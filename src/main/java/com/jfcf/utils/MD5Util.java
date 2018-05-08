package com.jfcf.utils;

import java.security.MessageDigest;

public class MD5Util {
	
    /**编码格式*/
    public static final String CHARSET = "UTF-8";
	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}
	/**
	 * md5加密字符串，以utf-8
	 * @author ducongcong
	 * @createDate 2016年1月5日
	 * @updateDate 
	 * @param origin
	 * @return
	 * @throws Exception 
	 */
    public static String MD5Encode(String origin) throws Exception{
    	return MD5Encode(origin, CHARSET);
    }
	/**
	 * md5加密
	 * @author ducongcong
	 * @createDate 2016年1月5日
	 * @updateDate 
	 * @param origin 要加密的字符串
	 * @param charsetname 编码
	 * @return
	 * @throws Exception 
	 */
	public static String MD5Encode(String origin, String charsetname) throws Exception {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
		} catch (Exception e) {
			throw e;
		}
		return resultString;
	}

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };
	/**
	 * 16进制MD5加密
	 * @param content
	 * @return
	 * @throws Exception 
	 */
	public static String MD5Encode16(String content) throws Exception{
		StringBuffer sb = new StringBuffer();
		try {
			byte[] chars = MessageDigest.getInstance("MD5").digest(content.getBytes());
			for (int i = 0; i < chars.length; i++) {
				String str = Integer.toHexString(0xFF & chars[i]);
				if (str.length() == 1) {
					sb.append("0").append(str);
				} else{
					sb.append(str);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return sb.toString();
	}
}
