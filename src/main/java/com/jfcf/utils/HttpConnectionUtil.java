package com.jfcf.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP访问工具类
 */
@SuppressWarnings("deprecation")
public class HttpConnectionUtil {
	private static  Logger logger =  LoggerFactory.getLogger("httplogs");
	/**无编码*/
	private static final int RESPONSE_TYPE_NO=0;
	/**utf-8*/
	private static final int RESPONSE_TYPE_UTF8=1;
	/**gbk*/
	private static final int RESPONSE_TYPE_GBK=2;
	/**
	 * 从Servlet中解析json串
	 * 
	 * @param request
	 * @return
	 */
	public static String analyzeJson(HttpServletRequest request) {
		if (null == request) {
			return "";
		}
		Map<String, String[]> param = request.getParameterMap();
		Set<String> keySet = param.keySet();
		Iterator<String> keyIt = keySet.iterator();
		while (keyIt.hasNext()) {
			return keyIt.next();
		}
		return "";
	}
	
	public static String sslPost (String url, Map<String, String> params) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
		String body = null;
		HttpPost post = postForm(url, params);
		body = invokeWithoutEncoding(httpclient, post);
		httpclient.getConnectionManager().shutdown();
		return body;
	}

	/**
	 * 从Servlet中解析对象
	 * 
	 * @param <T>
	 * @param <T>
	 * 
	 * @param request
	 * @param obj
	 * @return
	 */
	public static <T> Object analyzeObject(HttpServletRequest request, T obj) {
		if (null == request) {
			return "";
		}
		Map<String, String[]> param = request.getParameterMap();
		Set<String> keySet = param.keySet();
		Iterator<String> keyIt = keySet.iterator();
		String json = "";
		while (keyIt.hasNext()) {
			json = keyIt.next();

		}
		return FastJsonUtils.fromJson(json, obj.getClass());
	}

	/**
	 * 从Servlet中解析Map
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> analyzeMap(HttpServletRequest request) {
		if (null == request) {
			return null;
		}
		Map<String, String> result = new HashMap<String, String>();
		Map<String, String[]> param = request.getParameterMap();
		Set<String> keySet = param.keySet();
		Iterator<String> keyIt = keySet.iterator();
		while (keyIt.hasNext()) {
			String key = keyIt.next();
			result.put(key, param.get(key)[0]);
		}
		return result;
	}

	/**
	 * post 请求,默认无编码
	 * @param url 请求url
	 * @param params 参数map
	 * @param conntimeout 连接时间
	 * @param readTime  读取时间
	 * @return
	 * @throws Exception
	 */
	public static String post(String url,Map<String,String> params,int conntimeout,int readTime){
	   return basepost(url, params, conntimeout, readTime);
	}
	private static String basepost(String url,Map<String,String> params,int conntimeout,int readTime){
		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, conntimeout);//链接超时
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,readTime);//读取超时
		HttpPost post = postForm(url, params);
		String body = null;
		try {
			body = invokeWithoutEncoding(httpclient, post);
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("@@@HttpConnectionUtil发送post请求：URL=【"+url+"】;params=【"+FastJsonUtils.toJson(params)+"】；conntimeout=【"+conntimeout+"】；readTime=【"+readTime+"】;异常信息：",e);
			}
		}finally{
			httpclient.getConnectionManager().shutdown();
		}
		return body;
	}
	/**
	 * post方式访问
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String post(String url, Map<String, String> params) {
		return post(url, params,ConstantConfig.DEFAULT_CONNECT_TIMEOUT,ConstantConfig.DEFAULT_READ_TIMEOUT);
	}

	/**
	 * post方式发送data
	 * @param url 只有一个参数data
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String postData(String url, Object param) throws Exception {
		// 组装Data
		Map<String, String> params = new HashMap<String, String>();
		if (param instanceof String) {
			params.put("data", param.toString());
		} else {
			params.put("data", FastJsonUtils.toJson(param));
		}
		return post(url, params);
	}
	 /**
	 * get方式访问
	 * @param url 
	 *        访问的url
	 * @return 默认格式的返回值
	 */
    public static String get(String url){
    	return get(url, ConstantConfig.DEFAULT_CONNECT_TIMEOUT, ConstantConfig.DEFAULT_READ_TIMEOUT, RESPONSE_TYPE_NO);
    }
    /**
	 * get方式访问
	 * @param url 
	 *        访问的url
	 * @return utf-8格式的返回值
	 */
    public static String getUtf8(String url){
    	return get(url, ConstantConfig.DEFAULT_CONNECT_TIMEOUT, ConstantConfig.DEFAULT_READ_TIMEOUT, RESPONSE_TYPE_UTF8);
    }
	/**
	 * get方式访问
	 * @param url 
	 *        访问的url
	 * @return gbk格式的返回值
	 */
    public static String getGbk(String url){
    	return get(url, ConstantConfig.DEFAULT_CONNECT_TIMEOUT, ConstantConfig.DEFAULT_READ_TIMEOUT, RESPONSE_TYPE_GBK);
    }
	/**
	 * get方式访问
	 * @param url 
	 *        访问的url
	 * @param conntimeout 
	 *         连接超时时长
	 * @param readTime 
	 *        读取超时时长
	 * @param type  
	 *        返回请求的类型，不传递是无编码转换
	 * @return
	 */
	public static String get(String url,int conntimeout,int readTime,Integer type) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, conntimeout);//链接超时
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,readTime);//读取超时
		String body = null;
		HttpGet get = new HttpGet(url);
		try {
			body = invoke(httpclient, get,type);
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("@@@HttpConnectionUtil发送httpGet请求：URL=【"+url+"】;conntimeout=【"+conntimeout+"】；readTime=【"+readTime+"】;异常信息：",e);
			}
		}finally{
			httpclient.getConnectionManager().shutdown();
		}
		
		return body;
	}
	 /**
	 * get方式访问
	 * @param url 
	 *        访问的url
	 * @param head
	 *        请求头 
	 * @return utf-8格式的返回值
	 */
    public static String getUtf8(String url,Map<String,String> head){
    	return get(url, ConstantConfig.DEFAULT_CONNECT_TIMEOUT, ConstantConfig.DEFAULT_READ_TIMEOUT, RESPONSE_TYPE_UTF8,head);
    }
	/**
	 * get方式访问
	 * @param url 
	 *        访问的url
	 * @param conntimeout 
	 *         连接超时时长
	 * @param readTime 
	 *        读取超时时长
	 * @param head
	 *        请求头
	 * @param type  
	 *        返回请求的类型，不传递是无编码转换
	 * @return
	 */
	public static String get(String url,int conntimeout,int readTime,Integer type,Map<String,String> head) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, conntimeout);//链接超时
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,readTime);//读取超时
		String body = null;
		HttpGet get = new HttpGet(url);
		if(!head.isEmpty()){//头文件不为空，设置
			for (String key : head.keySet()) {
				get.setHeader(key, head.get(key));
			}
		}
		try {
			body = invoke(httpclient, get,RESPONSE_TYPE_NO);
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("@@@HttpConnectionUtil发送httpGet请求：URL=【"+url+"】;head=【"+FastJsonUtils.toJson(head)+"】conntimeout=【"+conntimeout+"】；readTime=【"+readTime+"】;异常信息：",e);
			}
		}finally{
			httpclient.getConnectionManager().shutdown();
		}
		return body;
	}

	private static String invokeWithoutEncoding(DefaultHttpClient httpclient, HttpUriRequest httpReq) throws Exception {
		return invoke(httpclient, httpReq, RESPONSE_TYPE_NO);
	}
	@SuppressWarnings("unused")
	private static String invokeGBK(DefaultHttpClient httpclient, HttpUriRequest httpReq) throws Exception{
		return invoke(httpclient, httpReq, RESPONSE_TYPE_GBK);
	}

	private static String invoke(DefaultHttpClient httpclient, HttpUriRequest httpReq,Integer type) throws Exception {
		HttpResponse response = sendRequest(httpclient, httpReq);
		return paseResponse(response,type);
	}
	private static String paseResponse(HttpResponse response,Integer type) {
		String body = null;
		if (null == response) {
			return body;
		}
		HttpEntity entity = response.getEntity();
		try {
			if(RESPONSE_TYPE_UTF8==type){
				body = EntityUtils.toString(entity,"UTF-8");
			}else if(RESPONSE_TYPE_UTF8==type){
				body = new String(EntityUtils.toString(response.getEntity()).getBytes("ISO-8859-1"), "GBK");
			}else{
				body = EntityUtils.toString(response.getEntity());
			}
		} catch (ParseException e) {
			if(logger.isErrorEnabled()){
				logger.error("http 请求 解析异常",e);
			}
		} catch (IOException e) {
			if(logger.isErrorEnabled()){
				logger.error("http 请求 IO异常",e);
			}
		}
		return body;
	}


	private static HttpResponse sendRequest(DefaultHttpClient httpclient, HttpUriRequest httpost) throws ClientProtocolException, IOException {
		return	 httpclient.execute(httpost);
	}

	private static HttpPost postForm(String url, Map<String, String> params) {
		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}
		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			if(logger.isErrorEnabled()){
				logger.error("@@@HttpConnectionUtil发送postForm请求：URL=【"+url+"】;params=【"+FastJsonUtils.toJson(params)+"】异常信息：",e);
			}
		}
		return httpost;
	}

	public static String postJsonStr(String url, String jsonStr,int conntimeout,int readTime,Integer type ) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, conntimeout);//链接超时
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,readTime);//读取超时
		HttpPost httpost = new HttpPost(url);
		StringEntity s = new StringEntity(jsonStr, ContentType.create("application/json", "UTF-8"));
		httpost.setEntity(s);
		String body = null;
		try {
			body = invoke(httpclient, httpost,type);
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("@@@HttpConnectionUtil发送postJsonStr请求：URL=【"+url+"】;jsonStr=【"+jsonStr+"】;conntimeout=【"+conntimeout+"】;readTime=【"+readTime+"】异常信息：",e);
			}
		}finally{
			httpclient.getConnectionManager().shutdown();
		}
		
		return body;
	}

	public static String postJsonStrUrlencoded(String url, String jsonStr,int conntimeout,int readTime) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, conntimeout);//链接超时
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,readTime);//读取超时
		HttpPost httpost = new HttpPost(url);
		StringEntity s = new StringEntity(jsonStr, ContentType.create("application/x-www-form-urlencoded", "UTF-8"));
		httpost.setEntity(s);
		String body = null;
		try {
			body = invoke(httpclient, httpost,null);
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("@@@HttpConnectionUtil发送postJsonStrUrlencoded请求：URL=【"+url+"】;jsonStr=【"+jsonStr+"】;conntimeout=【"+conntimeout+"】;readTime=【"+readTime+"】异常信息：",e);
			}
		}finally{
			httpclient.getConnectionManager().shutdown();
		}
		return body;
	}

	
	/** 
	 * 
	 * 包含请求头部的信息 获取原始数据时用到
     * @author weichunxia
     * @param url 路径
     * @param headers  头信息
     * @param jsonStr json格式的参数
     */  
    public static String postWithHeader(String url,Map<String, String> headers,String  jsonStr){  
    	HttpPost httpPost  = new HttpPost(url);    
        if (headers != null) {  
            Set<String> keys = headers.keySet();  
            for (Iterator<String> i = keys.iterator(); i.hasNext();) {  
                String key = (String) i.next();  
                httpPost.addHeader(key, headers.get(key));  
            }  
        }  
  
        StringEntity s = new StringEntity(jsonStr, ContentType.create("application/json", "UTF-8"));
    	DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
        httpPost.setEntity(s);
		String body = null;
		try {
			body = invoke(httpclient, httpPost);
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("@@@HttpConnectionUtil发送postWithHeader请求：URL=【"+url+"】;headers=【"+FastJsonUtils.toJson(headers)+"】;jsonStr=【"+jsonStr+"】异常信息：",e);
			}
		}finally{
			httpclient.getConnectionManager().shutdown();
		}
		return body;
    } 
	private static String invoke(DefaultHttpClient httpclient, HttpUriRequest httpost) throws Exception {
		HttpResponse response = sendRequest(httpclient, httpost);
		String body = paseResponse(response);
		return body;
	}
	
	private static String paseResponse(HttpResponse response) {
		String body = null;
		if (null == response) {
			return body;
		}
		HttpEntity entity = response.getEntity();
		try {
			body = EntityUtils.toString(entity);
//			body = new String(body.getBytes("ISO-8859-1"), "UTF-8");
		} catch (ParseException e) {
			if(logger.isErrorEnabled()){
				logger.error("paseResponse  ParseException ", e);
			}
		} catch (IOException e) {
			if(logger.isErrorEnabled()){
				logger.error("paseResponse IOException", e);
			}
		}
		return body;
	}
}
