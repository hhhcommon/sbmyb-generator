package com.jfcf.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP POST和GET处理工具类
 * 
 */
public class HttpUtils {
	private static  Logger logger =  LoggerFactory.getLogger("httplogs");
	public static final String UTF8 = "UTF-8";

	/**
	 * get请求获取ResultData
	 *
	 * @param url    发送请求的URL
	 * @param params 请求参数
	 * @return 返回封装的ResultData对象
	 * @throws Exception
	 */
//	public static ResultData getResultData(String url, Map<String, String> params) throws Exception {
//		String sendGet = sendGet(url, params);
//		if (sendGet != null && sendGet.contains("message") && sendGet.contains("status")) {
//			return FastJsonUtils.fromJson(sendGet, ResultData.class);
//		} else {
//			return null;
//		}
//	}

	/**
	 * get请求获取ResultData
	 *
	 * @param url            发送请求的URL
	 * @param params         请求参数
	 * @param connectTimeout 连接超时时间（毫秒）
	 * @param readTimeout    读取超时时间（毫秒）
	 * @return 返回封装的ResultData对象
	 * @throws Exception
	 */
//	public static ResultData getResultData(String url, Map<String, String> params, int connectTimeout, int readTimeout) throws Exception {
//		String sendGet = sendGet(url, params);
//		if (sendGet != null && sendGet.contains("message") && sendGet.contains("status")) {
//			return FastJsonUtils.fromJson(sendGet, ResultData.class);
//		} else {
//			return null;
//		}
//	}

	/**
	 * 向指定URL发送GET方法的请求
	 *
	 * @param url    发送请求的URL
	 * @param params 请求参数
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, Map<String, String> params) throws Exception {
		return sendGet(url, params, ConstantConfig.DEFAULT_CONNECT_TIMEOUT, ConstantConfig.DEFAULT_READ_TIMEOUT, UTF8);
	}

	/**
	 * 向指定URL发送GET方法的请求
	 *
	 * @param url            发送请求的URL
	 * @param params         请求参数
	 * @param connectTimeout 连接超时时间（毫秒）
	 * @param readTimeout    读取超时时间（毫秒）
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, Map<String, String> params, int connectTimeout, int readTimeout, String charset) throws Exception {
		String result = "";
		BufferedReader in = null;
		long start = System.currentTimeMillis();
		try {
			/** 组装参数 **/
			String param = parseParams(params, charset);
			String urlNameString = url ;
			if(param.length()>0){
				if(url.indexOf("?")==-1){
					urlNameString = url + "?" + param;
				}else{
					urlNameString = url + "&" + param;
				}
			}
			URL realUrl = new URL(urlNameString);
			/** 打开和URL之间的连接 **/
			URLConnection connection = realUrl.openConnection();
			/** 设置通用的请求属性 **/
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			/** 建立实际的连接 **/
			connection.connect();
			/** 定义 BufferedReader输入流来读取URL的响应 **/
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("@@@发送httpGet请求：URL=【" + url + "】;params【" + FastJsonUtils.toJson(params) + "】;connectTimeout=【" + connectTimeout + "】；readTimeout=【" + readTimeout + "】;异常信息：", e);
			}
			throw new RuntimeException("发送GET请求出现异常！", e);
		} finally {/** 使用finally块来关闭输入流 **/
			if (!url.contains("/ds/")) {
				if(logger.isWarnEnabled()){
					logger.warn("url:" + url + " 请求时间：" + (System.currentTimeMillis() - start)  + "毫秒");
				}
			}
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception ex) {
				if(logger.isErrorEnabled()){
					logger.error("@@@发送httpGetIO请求：URL=【" + url + "】;params【" + FastJsonUtils.toJson(params) + "】;connectTimeout=【" + connectTimeout + "】；readTimeout=【" + readTimeout + "】;异常信息：", ex);
				}
				throw new RuntimeException("发送GET请求出现异常！", ex);
			}
		}
		return result;
	}

	/**
	 * post请求获取ResultData
	 *
	 * @param url    发送请求的URL
	 * @param params 请求参数
	 * @return 返回封装的ResultData对象
	 * @throws Exception
	 */
//	public static ResultData postResultData(String url, Map<String, String> params) throws Exception {
//		String sendPost = sendPost(url, params);
//		if (sendPost != null && sendPost.contains("message") && sendPost.contains("status")) {
//			return FastJsonUtils.fromJson(sendPost, ResultData.class);
//		} else {
//			return null;
//		}
//	}

	/**
	 * post请求获取ResultData
	 *
	 * @param url            发送请求的URL
	 * @param params         请求参数
	 * @param connectTimeout 连接超时时间（毫秒）
	 * @param readTimeout    读取超时时间（毫秒）
	 * @return 返回封装的ResultData对象
	 * @throws Exception
	 */
//	public static ResultData postResultData(String url, Map<String, String> params, int connectTimeout, int readTimeout) throws Exception {
//		String sendPost = sendPost(url, params);
//		if (sendPost != null && sendPost.contains("message") && sendPost.contains("status")) {
//			return FastJsonUtils.fromJson(sendPost, ResultData.class);
//		} else {
//			return null;
//		}
//	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param url   发送请求的 URL
	 * @param param 请求参数
	 * @return 所代表远程资源的响应结果
	 */
//	public static String sendPost(String url, Map<String, String> params) throws Exception {
//		return sendPost(url, params, Constants.DEFAULT_CONNECT_TIMEOUT, Constants.DEFAULT_READ_TIMEOUT, HttpUtils.UTF8);
//	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param url            发送请求的 URL
	 * @param param          请求参数
	 * @param connectTimeout 连接超时时间（毫秒）
	 * @param readTimeout    读取超时时间（毫秒）
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, Map<String, String> params, int connectTimeout, int readTimeout, String charset) throws Exception {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		long start = System.currentTimeMillis();
		try {
			URL realUrl = new URL(url);
			/** 打开和URL之间的连接 **/
			URLConnection conn = realUrl.openConnection();
			/** 设置通用的请求属性 **/
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			conn.setRequestProperty("Accept-Charset", charset);
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			/** 发送POST请求必须设置如下两行 **/
			conn.setDoOutput(true);
			conn.setDoInput(true);
			/** 获取URLConnection对象对应的输出流 **/
			out = new PrintWriter(conn.getOutputStream());
			/** 发送请求参数 **/
			String param = parseParams(params, charset);
			out.print(param);
			/** flush输出流的缓冲 **/
			out.flush();
			/** 定义BufferedReader输入流来读取URL的响应 **/
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("@@@发送httpPost请求：URL=【" + url + "】;params【" + FastJsonUtils.toJson(params) + "】;connectTimeout=【" + connectTimeout + "】；readTimeout=【" + readTimeout + "】;异常信息：", e);
			}
			throw new RuntimeException("发送 POST 请求出现异常！", e);
		} finally { /** 使用finally块来关闭输出流、输入流 **/
			if (!url.contains("/ds/")) {
				if(logger.isWarnEnabled()){
					logger.warn("url:" + url + " 请求时间：" + (System.currentTimeMillis() - start)  + "毫秒");
				}
			}
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				if(logger.isErrorEnabled()){
					logger.error("@@@发送httpPostIO异常：URL=【" + url + "】;params【" + FastJsonUtils.toJson(params) + "】;connectTimeout=【" + connectTimeout + "】；readTimeout=【" + readTimeout + "】;异常信息：", ex);
				}
				throw new RuntimeException("http post IOException", ex);
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param url            发送请求的 URL
	 * @param param          请求参数
	 * @param connectTimeout 连接超时时间（毫秒）
	 * @param readTimeout    读取超时时间（毫秒）
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, Map<String, String> headParams, Map<String, String> params, int connectTimeout, int readTimeout, String charset) throws Exception {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		long start = System.currentTimeMillis();
		try {
			URL realUrl = new URL(url);
			/** 打开和URL之间的连接 **/
			URLConnection conn = realUrl.openConnection();
			/** 设置通用的请求属性 **/
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			conn.setRequestProperty("Accept-Charset", charset);
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			/** 发送POST请求必须设置如下两行 **/
			conn.setDoOutput(true);
			conn.setDoInput(true);
			if (headParams != null) {
				for (String key : headParams.keySet()) {
					conn.setRequestProperty(key, headParams.get(key));
				}
			}
			/** 获取URLConnection对象对应的输出流 **/
			out = new PrintWriter(conn.getOutputStream());
			/** 发送请求参数 **/
			String param = parseParams(params, charset);
			out.print(param);
			/** flush输出流的缓冲 **/
			out.flush();
			/** 定义BufferedReader输入流来读取URL的响应 **/
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("@@@发送httpPost请求：URL=【" + url + "】;params【" + FastJsonUtils.toJson(params) + "】;connectTimeout=【" + connectTimeout + "】；readTimeout=【" + readTimeout + "】;异常信息：", e);
			}
			throw new RuntimeException("发送 POST 请求出现异常！", e);
		} finally { /** 使用finally块来关闭输出流、输入流 **/
			if (!url.contains("/ds/")) {
				if(logger.isWarnEnabled()){
					logger.warn("url:" + url + " 请求时间：" + (System.currentTimeMillis() - start) + "毫秒");
				}
			}
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				if(logger.isErrorEnabled()){
					logger.error("@@@发送httpPostIO异常：URL=【" + url + "】;params【" + FastJsonUtils.toJson(params) + "】;connectTimeout=【" + connectTimeout + "】；readTimeout=【" + readTimeout + "】;异常信息：", ex);
				}
				throw new RuntimeException("http post IOException", ex);
			}
		}
		return result;
	}

	/**
	 * 将HashMap参数组装成字符串
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static String parseParams(Map<String, String> map, String charset) throws Exception {
		StringBuffer sb = new StringBuffer();
		if (map != null && map.size() > 0) {
			for (Entry<String, String> e : map.entrySet()) {
				sb.append(e.getKey());
				sb.append("=");
				if (StringUtils.isEmpty(e.getValue()) || "null".equals(e.getValue())) {
					sb.append("");
				} else {
					sb.append(URLEncoder.encode(e.getValue(), charset));
				}
				sb.append("&");
			}
			return sb.substring(0, sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 将HashMap参数组装成字符串
	 * 
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public static String parseParamsForBank(Map<String, String> map,String charset) throws Exception {
		StringBuffer sb = new StringBuffer();
		if (map != null) {
			sb.append("{\"params\":{");
			for (Entry<String, String> e : map.entrySet()) {
				sb.append("\""+e.getKey()+"\"");
				sb.append(":");
				if(StringUtils.isEmpty(e.getValue()) || "null".equals(e.getValue())){
					sb.append("");
				}else{
					sb.append("\""+e.getValue()+"\"");
				}
				sb.append(",");
			}
			sb.append("}}");
			return sb.deleteCharAt(sb.lastIndexOf(",")).toString();
		}
		return  sb.toString();
	}
	
	/**
	 * 返回结果解压
	 *
	 * @param url    url地址
	 * @param params 参数
	 * @param heads  请求头
	 * @return
	 * @throws Exception
	 * @author ducongcong
	 * @createDate 2016年12月14日
	 * @updateDate
	 */
	public static String sendGetUnCompress(String url, Map<String, String> params, Map<String, String> heads) throws Exception {
		return sendGetUnCompress(url, params, heads, ConstantConfig.DEFAULT_CONNECT_TIMEOUT, ConstantConfig.DEFAULT_READ_TIMEOUT, HttpUtils.UTF8);
	}

	/**
	 * 向指定URL发送GET方法的请求
	 *
	 * @param url            发送请求的URL
	 * @param params         请求参数
	 * @param connectTimeout 连接超时时间（毫秒）
	 * @param readTimeout    读取超时时间（毫秒）
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, Map<String, String> params, Map<String, String> heads) throws Exception {
		String result = "";
		BufferedReader in = null;
		long start = System.currentTimeMillis();
		try {
			/** 组装参数 **/
			String param = parseParams(params, HttpUtils.UTF8);
			String urlNameString = url;
			if(param.length()>0){
				if(url.indexOf("?")==-1){
					urlNameString = url + "?" + param;
				}else{
					urlNameString = url + "&" + param;
				}
			}
			URL realUrl = new URL(urlNameString);
			/** 打开和URL之间的连接 **/
			URLConnection connection = realUrl.openConnection();
			/** 设置通用的请求属性 **/
			connection.setRequestProperty("Accept-Charset", HttpUtils.UTF8);
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			if (heads != null && !heads.isEmpty()) {//头文件不为空，设置
				for (String key : heads.keySet()) {
					connection.setRequestProperty(key, heads.get(key));
				}
			}
			/** 建立实际的连接 **/
			connection.connect();
			InputStream is = connection.getInputStream();
			/** 定义 BufferedReader输入流来读取URL的响应 **/
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			StringBuilder sb = new StringBuilder();

			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
			} catch (IOException e) {
				throw new RuntimeException("发送GET请求出现异常！", e);
			} finally {
				if (null != is) {
					try {
						is.close();
					} catch (IOException e) {
						if(logger.isErrorEnabled()){
							logger.error("close InputStream is error", e);
						}
					}
				}
			}
			result = sb.toString();
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("@@@发送httpGet请求：URL=【" + url + "】;params【" + FastJsonUtils.toJson(params) + "】;connectTimeout=【" + ConstantConfig.DEFAULT_CONNECT_TIMEOUT + "】；readTimeout=【" + ConstantConfig.DEFAULT_READ_TIMEOUT + "】;异常信息：", e);
			}
			throw new RuntimeException("发送GET请求出现异常！", e);
		} finally {/** 使用finally块来关闭输入流 **/
			if (!url.contains("/ds/")) {
				if(logger.isWarnEnabled()){
					logger.warn("url:" + url + " 请求时间：" + (System.currentTimeMillis() - start) + "毫秒");
				}
			}
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception ex) {
				if(logger.isErrorEnabled()){
					logger.error("@@@发送httpGetIO请求：URL=【" + url + "】;params【" + FastJsonUtils.toJson(params) + "】;connectTimeout=【" + ConstantConfig.DEFAULT_CONNECT_TIMEOUT + "】；readTimeout=【" + ConstantConfig.DEFAULT_READ_TIMEOUT + "】;异常信息：", ex);
				}
				throw new RuntimeException("发送GET请求出现异常！", ex);
			}
		}
		return result;
	}

	/**
	 * 向指定URL发送GET方法的请求
	 *
	 * @param url            发送请求的URL
	 * @param params         请求参数
	 * @param connectTimeout 连接超时时间（毫秒）
	 * @param readTimeout    读取超时时间（毫秒）
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGetUnCompress(String url, Map<String, String> params, Map<String, String> heads, int connectTimeout, int readTimeout, String charset) throws Exception {
		String result = "";
		BufferedReader in = null;
		long start = System.currentTimeMillis();
		try {
			/** 组装参数 **/
			String param = parseParams(params, charset);
			String urlNameString = url + "?" + param;
			if(logger.isInfoEnabled()){
				logger.info("@@@魔蝎请求url：" + urlNameString);
			}
			URL realUrl = new URL(urlNameString);
			/** 打开和URL之间的连接 **/
			URLConnection connection = realUrl.openConnection();
			/** 设置通用的请求属性 **/
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			if (heads != null && !heads.isEmpty()) {//头文件不为空，设置
				for (String key : heads.keySet()) {
					connection.setRequestProperty(key, heads.get(key));
				}
			}
			/** 建立实际的连接 **/
			connection.connect();
			/** 定义 BufferedReader输入流来读取URL的响应 **/
			result = ZipUtils.uncompress(new BufferedInputStream(connection.getInputStream()));
			if(logger.isDebugEnabled()){
				logger.debug("@@@魔蝎请求返回:" + result);
			}
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("@@@发送httpGet请求：URL=【" + url + "】;params【" + FastJsonUtils.toJson(params) + "】;connectTimeout=【" + connectTimeout + "】；readTimeout=【" + readTimeout + "】;异常信息：", e);
			}
			throw new RuntimeException("发送GET请求出现异常！", e);
		} finally {/** 使用finally块来关闭输入流 **/
			if (!url.contains("/ds/")) {
				if(logger.isInfoEnabled()){
					logger.info("url:" + url + " 请求时间：" + (System.currentTimeMillis() - start)  + "毫秒");
				}
			}
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception ex) {
				if(logger.isErrorEnabled()){
					logger.error("@@@发送httpGetIO请求：URL=【" + url + "】;params【" + FastJsonUtils.toJson(params) + "】;connectTimeout=【" + connectTimeout + "】；readTimeout=【" + readTimeout + "】;异常信息：", ex);
				}
				throw new RuntimeException("发送GET请求出现异常！", ex);
			}
		}
		return result;
	}

	/**
	 * HttpPost方式上传文件
	 *
	 * @param url
	 * @param valueMap
	 * @param filePath
	 * @throws Exception
	 */
	public static String sendHttpPostAndUploadFile(String url, Map<String, String> valueMap, String fileParam, File filePath) throws Exception {
		String re = null;
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		try {
			httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
			httpPost.setConfig(requestConfig);
			MultipartEntity multipartEntity = new MultipartEntity();
			for (Map.Entry<String, String> entry : valueMap.entrySet()) {
				StringBody value = new StringBody(entry.getValue(), Charset.forName("UTF-8"));
				multipartEntity.addPart(entry.getKey(), value);
			}
			// 把文件转换成流对象FileBody
			FileBody file = new FileBody(filePath);
			multipartEntity.addPart(fileParam, file);
			httpPost.setEntity(multipartEntity);
			// 发起请求 并返回请求的响应
			response = httpClient.execute(httpPost);
			// 获取响应对象
			HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				re = EntityUtils.toString(resEntity, Charset.forName("UTF-8"));
			}
			// 销毁
			EntityUtils.consume(resEntity);
			return re;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				throw new Exception(e);
			}
		}
	}


	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param url            发送请求的 URL
	 * @param jsonParams     请求参数json
	 * @param connectTimeout 连接超时时间（毫秒）
	 * @param readTimeout    读取超时时间（毫秒）
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String jsonParams, int connectTimeout, int readTimeout, String charset) throws Exception {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		long start = System.currentTimeMillis();
		try {
			URL realUrl = new URL(url);
			/** 打开和URL之间的连接 **/
			URLConnection conn = realUrl.openConnection();
			/** 设置通用的请求属性 **/
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			conn.setRequestProperty("Accept-Charset", charset);
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-type", "application/json");
//			conn.setRequestProperty("Content-encoding", "application/json");
			/** 发送POST请求必须设置如下两行 **/
			conn.setDoOutput(true);
			conn.setDoInput(true);
			/** 获取URLConnection对象对应的输出流 **/
			out = new PrintWriter(conn.getOutputStream());
			/** 发送请求参数 **/
			out.print(jsonParams);
			/** flush输出流的缓冲 **/
			out.flush();
			/** 定义BufferedReader输入流来读取URL的响应 **/
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("@@@发送httpPost请求：URL=【" + url + "】;params【" + jsonParams + "】;connectTimeout=【" + connectTimeout + "】；readTimeout=【" + readTimeout + "】;异常信息：", e);
			}
			throw new RuntimeException("发送 POST 请求出现异常！", e);
		} finally { /** 使用finally块来关闭输出流、输入流 **/
			if (!url.contains("/ds/")) {
				if(logger.isWarnEnabled()){
					logger.warn("url:" + url + " 请求时间：" + (System.currentTimeMillis() - start)  + "毫秒");
				}
			}
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				if(logger.isErrorEnabled()){
					logger.error("@@@发送httpPostIO异常：URL=【" + url + "】;params【" + "】;connectTimeout=【" + connectTimeout + "】；readTimeout=【" + readTimeout + "】;异常信息：", ex);
				}
				throw new RuntimeException("http post IOException", ex);
			}
		}
		return result;
	}


	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param url            发送请求的 URL
	 * @param jsonParams     请求参数json
	 * @param connectTimeout 连接超时时间（毫秒）
	 * @param readTimeout    读取超时时间（毫秒）
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost2(String url, String jsonParams, int connectTimeout, int readTimeout, String charset) throws Exception {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		long start = System.currentTimeMillis();
		try {
			URL realUrl = new URL(url);
			/** 打开和URL之间的连接 **/
			URLConnection conn = realUrl.openConnection();
			/** 设置通用的请求属性 **/
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			conn.setRequestProperty("Accept-Charset", charset);
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-type", "application/json");
			conn.setRequestProperty("Content-encoding", "application/json");
			/** 发送POST请求必须设置如下两行 **/
			conn.setDoOutput(true);
			conn.setDoInput(true);
			/** 获取URLConnection对象对应的输出流 **/
			out = new PrintWriter(conn.getOutputStream());
			/** 发送请求参数 **/
			out.print(jsonParams);
			/** flush输出流的缓冲 **/
			out.flush();
			/** 定义BufferedReader输入流来读取URL的响应 **/
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("@@@发送httpPost请求：URL=【" + url + "】;params【" + "】;connectTimeout=【" + connectTimeout + "】；readTimeout=【" + readTimeout + "】;异常信息：", e);
			}
			throw new RuntimeException("发送 POST 请求出现异常！", e);
		} finally { /** 使用finally块来关闭输出流、输入流 **/
			if (!url.contains("/ds/")) {
				if(logger.isWarnEnabled()){
					logger.warn("url:" + url + " 请求时间：" + (System.currentTimeMillis() - start) + "毫秒");
				}
			}
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				if(logger.isErrorEnabled()){
					logger.error("@@@发送httpPostIO异常：URL=【" + url + "】;params【" + "】;connectTimeout=【" + connectTimeout + "】；readTimeout=【" + readTimeout + "】;异常信息：", ex);
				}
				throw new RuntimeException("http post IOException", ex);
			}
		}
		return result;
	}


	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param url   发送请求的 URL
	 * @param params 请求参数
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String params) throws Exception {
		return sendPost(url, params, ConstantConfig.DEFAULT_CONNECT_TIMEOUT, ConstantConfig.DEFAULT_READ_TIMEOUT, UTF8);
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * @param url
	 * @param jsonParams
	 * @param heads
	 * @param connectTimeout
	 * @param readTimeout
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static String sendPost(String url, String jsonParams, Map<String, String> heads, int connectTimeout, int readTimeout, String charset) throws Exception {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		long start = System.currentTimeMillis();
		try {
			URL realUrl = new URL(url);
			/** 打开和URL之间的连接 **/
			URLConnection conn = realUrl.openConnection();
			/** 设置通用的请求属性 **/
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			conn.setRequestProperty("Accept-Charset", charset);
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-type", "application/json");
			if (heads != null && !heads.isEmpty()) {//头文件不为空，设置
				for (String key : heads.keySet()) {
					conn.setRequestProperty(key, heads.get(key));
				}
			}
//			conn.setRequestProperty("Content-encoding", "application/json");
			/** 发送POST请求必须设置如下两行 **/
			conn.setDoOutput(true);
			conn.setDoInput(true);
			/** 获取URLConnection对象对应的输出流 **/
			out = new PrintWriter(conn.getOutputStream());
			/** 发送请求参数 **/
			out.print(jsonParams);
			/** flush输出流的缓冲 **/
			out.flush();
			/** 定义BufferedReader输入流来读取URL的响应 **/
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("@@@发送httpPost请求：URL=【" + url + "】;params【" + jsonParams + "】;connectTimeout=【" + connectTimeout + "】；readTimeout=【" + readTimeout + "】;异常信息：", e);
			}
			throw new RuntimeException("发送 POST 请求出现异常！", e);
		} finally { /** 使用finally块来关闭输出流、输入流 **/
			if (!url.contains("/ds/")) {
				if(logger.isInfoEnabled()){
					logger.info("url:" + url + " 请求时间：" + (System.currentTimeMillis() - start)  + "毫秒");
				}
			}
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				if(logger.isErrorEnabled()){
					logger.error("@@@发送httpPostIO异常：URL=【" + url + "】;params【" + "】;connectTimeout=【" + connectTimeout + "】；readTimeout=【" + readTimeout + "】;异常信息：", ex);
				}
				throw new RuntimeException("http post IOException", ex);
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * @param url  远程url地址
	 * @param jsonParams  json字符串
	 * @param heads  头信息
	 * @return
	 * @throws Exception
	 */
	public static String sendPost(String url, String jsonParams, Map<String, String> heads) throws Exception {
		return sendPost(url, jsonParams, heads, ConstantConfig.DEFAULT_CONNECT_TIMEOUT, ConstantConfig.DEFAULT_READ_TIMEOUT, UTF8);
	}
}