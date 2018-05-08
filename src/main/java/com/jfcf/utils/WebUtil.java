package com.jfcf.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ducongcong
 * @date 2016年1月4日
 */
public class WebUtil {
	
	   static String[] mobileAgents = {"iphone", "android", "phone", "mobile", "wap", "netfront", "java", "opera mobi",
           "opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry", "dopod", "nokia",
           "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola", "foma", "docomo",
           "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad", "webos", "techfaith",
           "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips", "sagem", "wellcom", "bunjalloo",
           "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos", "pantech", "gionee", "portalmmm",
           "jig browser", "hiptop", "benq", "haier", "^lct", "320x320", "240x320", "176x220", "w3c ", "acs-", "alav",
           "alca", "amoi", "audi", "avan", "benq", "bird", "blac", "blaz", "brew", "cell", "cldc", "cmd-", "dang",
           "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs", "kddi", "keji", "leno", "lg-c", "lg-d", "lg-g",
           "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi", "mot-", "moto", "mwbp", "nec-", "newt", "noki",
           "oper", "palm", "pana", "pant", "phil", "play", "port", "prox", "qwap", "sage", "sams", "sany", "sch-",
           "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem", "smal", "smar", "sony", "sph-", "symb", "t-mo",
           "teli", "tim-", "tsm-", "upg1", "upsi", "vk-v", "voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc",
           "winw", "winw", "xda", "xda-", "googlebot-mobile"};

   public static boolean isAjaxRequest(HttpServletRequest request) {
       String header = request.getHeader("X-Requested-With");
       return "XMLHttpRequest".equalsIgnoreCase(header);
   }

   public static boolean isMultipartRequest(HttpServletRequest request) {
       String contentType = request.getContentType();
       return contentType != null && contentType.toLowerCase().indexOf("multipart") != -1;
   }

   /**
    * 是否是手机浏览器
    *
    * @return
    */
   public static boolean isMoblieBrowser(HttpServletRequest request) {
       String ua = request.getHeader("User-Agent");
       if (ua == null) {
           return false;
       }
       ua = ua.toLowerCase();
       for (String mobileAgent : mobileAgents) {
           if (ua.indexOf(mobileAgent) >= 0) {
               return true;
           }
       }
       return false;
   }

   /**
    * 是否是微信浏览器
    *
    * @return
    */
   public static boolean isWechatBrowser(HttpServletRequest request) {
       String ua = request.getHeader("User-Agent");
       if (ua == null) {
           return false;
       }
       ua = ua.toLowerCase();
       if (ua.indexOf("micromessenger") > 0) {
           return true;
       }
       return false;
   }


   /**
    * 是否是PC版的微信浏览器
    *
    * @param request
    * @return
    */
   public static boolean isWechatPcBrowser(HttpServletRequest request) {
       String ua = request.getHeader("User-Agent");
       if (ua == null) {
           return false;
       }
       ua = ua.toLowerCase();
       if (ua.indexOf("windowswechat") > 0) {
           return true;
       }
       return false;
   }

   /**
    * 是否是IE浏览器
    *
    * @return
    */
   public static boolean isIEBrowser(HttpServletRequest request) {
       String ua = request.getHeader("User-Agent");
       if (ua == null) {
           return false;
       }

       ua = ua.toLowerCase();
       if (ua.indexOf("msie") > 0) {
           return true;
       }

       if (ua.indexOf("gecko") > 0 && ua.indexOf("rv:11") > 0) {
           return true;
       }
       return false;
   }

   public static String getIpAddress(HttpServletRequest request) {

       String ip = request.getHeader("X-requested-For");
       if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
           ip = request.getHeader("X-Forwarded-For");
       }
       if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
           ip = request.getHeader("Proxy-Client-IP");
       }
       if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
           ip = request.getHeader("WL-Proxy-Client-IP");
       }
       if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
           ip = request.getHeader("HTTP_CLIENT_IP");
       }
       if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
           ip = request.getHeader("HTTP_X_FORWARDED_FOR");
       }
       if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
           ip = request.getRemoteAddr();
       }

       if (ip != null && ip.contains(",")) {
           String[] ips = ip.split(",");
           for (int index = 0; index < ips.length; index++) {
               String strIp = ips[index];
               if (!("unknown".equalsIgnoreCase(strIp))) {
                   ip = strIp;
                   break;
               }
           }
       }

       return ip;
   }

   public static String getUserAgent(HttpServletRequest request) {
       return request.getHeader("User-Agent");
   }


   public static String getReferer(HttpServletRequest request) {
       return request.getHeader("Referer");
   }
	
	/**
	 * 获取客户端IP地址,
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		/**
		 * 在转发的时候，将原客户的请求ip放入到header中 字段reuestSourceIp
		 * 如果有该reuestSourceIp，直接使用这个ip，（慎用）
		 */
		String requestIp = StringUtils.isNotNull(request.getHeader("reuestSourceIp"))? request.getHeader("reuestSourceIp"):request.getHeader("requestSourceIp");
		if(StringUtils.isNotNull(requestIp)){
			if(requestIp.contains(",") ){
				return requestIp.split(",")[0];
			}
			return requestIp;
		}
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	/**
	 * 获取用户请求域名
	 * @author ducongcong
	 * @createDate 2016年5月4日
	 * @updateDate 
	 * @param request
	 * @return
	 */
	public static String getDomain(HttpServletRequest request){
		String domain = request.getRemoteHost();
		if(StringUtils.isEmpty(domain)){
			domain =  request .getHeader("Referer");
		}
		return domain;
	}
}
