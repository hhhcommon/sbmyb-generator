package com.jfcf.utils.mail;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfcf.utils.ConstantConfig;
import com.jfinal.kit.PropKit;

/**
 * 邮件发送
 * 
 * 描述：使用javamail出现java.net.SocketException: Network is unreachable: connect异常 解决方法 
 * 
 * 1. main方法加入System.setProperty("java.net.preferIPv4Stack", "true");  
 * 2. tomcat服务器加上启动参数 -Djava.net.preferIPv4Stack=true   
 */
public class EMailUtil {
	
	private static  Logger logger =  LoggerFactory.getLogger(EMailUtil.class);

	public static final String sendType_text = "text";

	public static final String sendType_html = "html";
	private static List<String> toUsers;
	private static List<String> towallet;
	public static List<String> getToUsers(String mailToUserKey){
		if(towallet==null){
			towallet=new ArrayList<String>();
			String str = PropKit.get(mailToUserKey);
			if(str!=null){
				String[] users = str.split(";");
				for(String user:users){
					towallet.add(user);
				}
			}
		}
		return towallet;
	}
	
	public static List<String> getToUsers(){
		if(toUsers==null){
			toUsers=new ArrayList<String>();
			String str = PropKit.get(ConstantConfig.CONFIG_MAIL_TOUSERS);
			if(str!=null){
				String[] users = str.split(";");
				for(String user:users){
					toUsers.add(user);
				}
			}
		}
		return toUsers;
	}
	/**
	 * 发送文本邮件
	 * @param host
	 * @param port
	 * @param validate
	 * @param userName
	 * @param password
	 * @param from
	 * @param to
	 * @param subject
	 * @param content
	 * @param attachFileNames
	 */
	public static void sendTextMail(
			String host, String port, boolean validate, String userName, String password,
			String from, List<String> to, 
			String subject, String content, String[] attachFileNames) {
		if(logger.isInfoEnabled()){
			logger.info("发送文本邮件");
		}
		SendMail mail = new SendMail(sendType_text, host, port, validate, userName, password, from, to, subject, content, attachFileNames);
		mail.start();
	}
	
	/**
	 * 发送html邮件
	 * @param host
	 * @param port
	 * @param validate
	 * @param userName
	 * @param password
	 * @param from
	 * @param to
	 * @param subject
	 * @param content
	 * @param attachFileNames
	 */
	public static void sendHtmlMail(
			String host, String port, boolean validate, String userName, String password,
			String from, List<String> to, 
			String subject, String content, String[] attachFileNames) {
		SendMail mail = new SendMail(sendType_html, host, port, validate, userName, password, from, to, subject, content, attachFileNames);
		mail.start();
	}
	/**
	 * 发送邮件对象
	 * @param sendType 发送邮件的类型：text 、html
	 * @param to	接收者
	 * @param subject 邮件标题
	 * @param content 邮件的文本内容
	 * @param attachFileNames 附件
	 */
	private static void sendTextMail(String sendType, List<String> to, String subject, String content, String[] attachFileNames){
		String host = PropKit.get(ConstantConfig.CONFIG_MAIL_HOST).trim();		// 发送邮件的服务器的IP
		String port = PropKit.get(ConstantConfig.CONFIG_MAIL_PORT).trim();	// 发送邮件的服务器的端口

		boolean validate = true;	// 是否需要身份验证
		
		String from = PropKit.get(ConstantConfig.CONFIG_MAIL_FROM).trim();		// 邮件发送者的地址
		String userName = PropKit.get(ConstantConfig.CONFIG_MAIL_USERNAME).trim();	// 登陆邮件发送服务器的用户名
		String password = PropKit.get(ConstantConfig.CONFIG_MAIL_PASSWORD).trim();	// 登陆邮件发送服务器的密码
		
		if(sendType != null && sendType.equals(EMailUtil.sendType_text)){
			EMailUtil.sendTextMail(host, port, validate, userName, password, from, to, subject, content, attachFileNames);
			
		} else if(sendType != null && sendType.equals(EMailUtil.sendType_html)){
			EMailUtil.sendHtmlMail(host, port, validate, userName, password, from, to, subject, content, attachFileNames);
			
		} else {
			if(logger.isErrorEnabled()){
				logger.error("发送邮件参数sendType错误");
			}
		}
	}
	
	/**
	 * 
	 * @param subject
	 * @param toUser
	 * @param content
	 */
	public static void sendHtmlMail( String subject,String mailToUserKey, String content){
		sendTextMail(EMailUtil.sendType_html, EMailUtil.getToUsers(mailToUserKey), subject, content, null);
	}
	
	/**
	 * 发送邮件对象
	 * @param subject 邮件标题
	 * @param content 邮件的文本内容
	 */
	public static void sendHtmlMail( String subject, String content){
		sendTextMail(EMailUtil.sendType_html, EMailUtil.getToUsers(), subject, content, null);
	}
	/**
	 * 发送邮件对象
	 * @param subject 邮件标题
	 * @param content 邮件的文本内容
	 */
	public static void sendTextMail( String subject, String content){
		sendTextMail(EMailUtil.sendType_text, EMailUtil.getToUsers(), subject, content, null);
	}

}




