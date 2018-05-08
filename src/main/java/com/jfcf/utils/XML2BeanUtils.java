package com.jfcf.utils;


import java.util.Iterator;
import java.util.Map;

import com.thoughtworks.xstream.XStream;

/**
 * 改造使用XStream转换xml
 * @author ducongcong
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class XML2BeanUtils {
	private static final String xmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n";

	/** 
     * 将Bean转换为XML 
     * @param clazzMap 别名-类名映射Map 
     * @param bean     要转换为xml的bean对象 
     * @return XML字符串 
     */ 
	public static String bean2xml(Map<String, Class> clazzMap, Object bean) { 
        XStream xstream = new XStream(); 
        for (Iterator it = clazzMap.entrySet().iterator(); it.hasNext();) { 
            Map.Entry<String, Class> m = (Map.Entry<String, Class>) it.next(); 
            xstream.alias(m.getKey(), m.getValue()); 
        } 
        return xmlHead+xstream.toXML(bean); 
    } 
	/**
	 * 将javaBean对象转换成xml文件，对于没有设置的属性将不会生成xml标签
	 * @param obj 待转换的javabean对象
	 * @param rootName 根节点名称,不写默认类路径
	 * @return 转换后的xml 字符串
	 * @author ducongcong
	 * @createDate 2017年4月12日
	 * @updateDate
	 */
	public static String bean2XmlString(Object obj,String rootName) {
		XStream xstream = new XStream(); 
		if(StringUtils.isNotNull(rootName)){
			xstream.alias(rootName, obj.getClass());
		}
		return xmlHead+xstream.toXML(obj);
	}
    /** 
     * 将XML转换为Bean 
     * 
     * @param clazzMap 别名-类名映射Map 
     * @param xml      要转换为bean对象的xml字符串 
     * @return Java Bean对象 
     */ 
    public static Object xml2Bean(Map<String, Class> clazzMap, String xml) { 
        XStream xstream = new XStream(); 
        for (Iterator it = clazzMap.entrySet().iterator(); it.hasNext();) { 
            Map.Entry<String, Class> m = (Map.Entry<String, Class>) it.next(); 
            xstream.alias(m.getKey(), m.getValue()); 
        } 
        Object bean = xstream.fromXML(xml); 
        return bean; 
    } 

    /** 
     * 获取XStream对象 
     * 
     * @param clazzMap 别名-类名映射Map 
     * @return XStream对象 
     */ 
    public static XStream getXStreamObject(Map<String, Class> clazzMap) { 
        XStream xstream = new XStream(); 
        for (Iterator it = clazzMap.entrySet().iterator(); it.hasNext();) { 
            Map.Entry<String, Class> m = (Map.Entry<String, Class>) it.next(); 
            xstream.alias(m.getKey(), m.getValue()); 
        } 
        return xstream; 
    } 
    /**
     * 将xml转成javaBean
     * demo: XML2BeanUtils.xmlSring2Bean(Response.class, "<?xml version='1.0' encoding='UTF-8'?><response><string>141214919811963860130545</string></response>", "response");
     * @param beanClass  要转换类的class
     * @param xmlFile xml字符串
     * @param rootName 根节点名称
     * @return 转换后的对象
     * @throws Exception
     * @author ducongcong
     * @createDate 2017年4月12日
     * @updateDate
     */
	public static <T> T xmlSring2Bean(Class<T> beanClass, String xmlFile,String rootName) throws Exception{
    	XStream xstream = new XStream(); 
		if (beanClass == null || xmlFile == null || xmlFile.isEmpty())
		throw new IllegalArgumentException("给定的参数不能为null！");
		xstream.alias(rootName, beanClass);
		return (T) xstream.fromXML(xmlFile);
    }
//    public static void main(String[] args) {
//    	Response res = new Response();
//    	res.setString("123");
//    	System.err.println(XML2BeanUtils.bean2XmlString(res, "response"));
//    	
//    	try {
//			Response resp = XML2BeanUtils.xmlSring2Bean(Response.class, "<?xml version='1.0' encoding='UTF-8'?><response><string>141214919811963860130545</string></response>", "response");
//			System.err.println(resp.getString());
//		} catch (Exception e) {
//		}
//	}
}