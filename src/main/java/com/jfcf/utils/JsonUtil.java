package com.jfcf.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;


/**
 * @date 2016年1月8日
 */
public class JsonUtil {

	/**
	 * 替换Json字符串 如_a为 A
	 * @param jsonText
	 * @return
	 */
	public static String underscoreToCamel(String jsonText) {
		if (StringUtils.isEmpty(jsonText)) {
			return jsonText;
		}
		String regex = "(\")[a-z0-9A-Z]+(_[a-z0-9A-Z]+)+(\":)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(jsonText);
		Map<String, String> tokens = new HashMap<String, String>();
		while(matcher.find()) {
			String group = matcher.group();
			tokens.put(group, camelName(group));
		}
		matcher.reset();
		StringBuffer sb = new StringBuffer();
		while(matcher.find()) {
			matcher.appendReplacement(sb, tokens.get(matcher.group()));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	public static String camelName(String name) {
	    StringBuilder result = new StringBuilder();
	    if (name == null || name.isEmpty()) {
	        return "";
	    } else if (!name.contains("_")) {
	        return name.substring(0, 1).toLowerCase() + name.substring(1);
	    }
	    String camels[] = name.split("_");
	    for (String camel :  camels) {
	        if (camel.isEmpty()) {
	            continue;
	        }
	        if (result.length() == 0) {
	            result.append(camel.toLowerCase());
	        } else {
	            result.append(camel.substring(0, 1).toUpperCase());
	            result.append(camel.substring(1).toLowerCase());
	        }
	    }
	    return result.toString();
	}
	
	public static <T> T transform(Object obj , Class<T> classz) {
		return JSONObject.parseObject(FastJsonUtils.toJson(obj), classz);
	}
	
}
