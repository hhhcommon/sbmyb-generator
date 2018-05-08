/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: 玖富时代</p>
 * @author ducongcong
 * @version 1.0
 */
package com.jfcf.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;
/**
 * spring上下文对象工具类
 * @author ducongcong
 * @date 2017年12月21日
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {
	private SpringContextUtil() {}
	private static ApplicationContext context;
	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		SpringContextUtil.context = context;

	}
	
	public static <T> T getBean(Class<T> clazz){
		if (clazz == null) return null;
		return context.getBean(clazz);
	}

	public static <T> T getBean(String beanName, Class<T> clazz) {
		if (null == beanName || "".equals(beanName.trim())) {
			return null;
		}
		if (clazz == null) return null;
		return (T) context.getBean(beanName, clazz);
	}

	public static ApplicationContext getContext(){
		if (context == null) return null;
		return context;
	}

	public static void publishEvent(ApplicationEvent event) {
		if (context == null) return;
		context.publishEvent(event);
	}

}
