package com.jfcf.platform.generate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.beetl.core.BeetlKit;

import com.jfinal.plugin.activerecord.DbKit;

/**
 * 简易辅助开发代码生成器
 * 
 * 描述：根据表，生成对应的.sql.xml文件、Model类、Service类、validator类
 * 
 */
public abstract class GenerateBase {

	/**
	 * 获取表的所有字段信息
	 * @param tableName
	 * @return
	 */
	public abstract List<TableColumnDto> getColunm(String dbName,String tableName) ;
	
	/**
	 * 获取表所有数据类型
	 * @param tableName
	 * @return
	 */
	public Map<String, String> getJavaType(String tableName){
        //  获取字段数
	    Map<String, String> columnJavaTypeMap = new HashMap<String, String>();
	    
	    Connection conn = null;
	    Statement st = null;
	    ResultSet rs = null;
	    
		try {
			conn = DbKit.getConfig().getConnection();
			st = conn.createStatement();    
		    String sql = "select * from " + tableName + " where 1 != 1 ";   
		    rs = st.executeQuery(sql);    
		    ResultSetMetaData rsmd = rs.getMetaData(); 

	        int columns = rsmd.getColumnCount();   
	        for (int i=1; i<=columns; i++){   
	            //获取字段名
	            String columnName = rsmd.getColumnName(i).toLowerCase(); 
	 			String columnClassName = rsmd.getColumnClassName(i);   
	 			if(columnClassName.equals("[B")){
	 				columnClassName = "byte[]";
	 			}
	 			columnJavaTypeMap.put(columnName, columnClassName);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbKit.getConfig().close(rs, st, conn);
		}
		
		return columnJavaTypeMap;
	}

	/**
	 * 获取所有数据类型
	 * @param tableName
	 * @return
	 */
	public Set<String> getJataTypeList(String tableName){
		Map<String, String> map = getJavaType(tableName);
		Set<String> keys = map.keySet();
		Set<String> typeSet = new HashSet<String>();
		for (String key : keys) {
			String type = map.get(key);
			if(type.equals("byte[]") || type.startsWith("java.lang.")){
				continue;
			}
			typeSet.add(map.get(key));
		}
		return typeSet;
	}
	

	/**
	 * 生成model
	 * @param srcFolder
	 * @param namespace
	 * @param packageBase
	 * @param className
	 * @param classNameSmall
	 * @param dataSource
	 * @param tableName 表名称
	 * @param pkName 主键
	 * @param colunmList 字段列表
	 * @param dataSourceName 数据源
	 * @param readOnly
	 */
	public void model(String srcFolder,String namespace,String packageBase,String className, String classNameSmall, String dataSource, String tableName, String pkName, List<TableColumnDto> colunmList,boolean readOnly){
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String packages = packageBase + "." + className.toLowerCase();
		paraMap.put("package", packages);
		if(readOnly){
			className = className +"R";
		}
		paraMap.put("className", className);
		paraMap.put("dataSource", dataSource);
		paraMap.put("tableName", tableName);
		paraMap.put("pkName", pkName);
		paraMap.put("namespace", namespace);
		paraMap.put("classNameSmall", classNameSmall);

		paraMap.put("colunmList", colunmList);
		paraMap.put("dataTypes", getJataTypeList(tableName));
		
		String filePath = System.getProperty("user.dir") + "/"+srcFolder+"/" + packages.replace(".", "/") + "/" + className +".java";
		createFileByTemplete("model.html", paraMap, filePath);
	}
	/**
	 * 生成model
	 * @param srcFolder
	 * @param namespace
	 * @param packageBase
	 * @param className
	 * @param classNameSmall
	 * @param dataSource
	 * @param tableName 表名称
	 * @param pkName 主键
	 * @param colunmList 字段列表
	 * @param dataSourceName 数据源
	 * @param readOnly
	 */
	public void modelNew(String srcFolder,String namespace,String packageBase,String className, String classNameSmall, String dataSource, String tableName, String pkName, List<TableColumnDto> colunmList,boolean readOnly){
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String packages = packageBase + "." + className.toLowerCase();
		paraMap.put("package", packages);
		if(readOnly){
			className = className +"R";
			dataSource = dataSource +"_R";
		}
		paraMap.put("className", className);
		paraMap.put("dataSource", dataSource);
		paraMap.put("tableName", tableName);
		paraMap.put("pkName", pkName);
		paraMap.put("namespace", namespace);
		paraMap.put("classNameSmall", classNameSmall);

		paraMap.put("colunmList", colunmList);
		paraMap.put("dataTypes", getJataTypeList(tableName));
		
		String filePath = System.getProperty("user.dir") + "/"+srcFolder+"/" + packages.replace(".", "/") + "/" + className +".java";
		createFileByTemplete("modelNew.html", paraMap, filePath);
	}
	/**
	 * 生成DTO                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
	 * @param base
	 * @param className
	 * @param classNameSmall
	 * @param dataSource
	 * @param tableName
	 */
	public void dto(String srcFolder,String packageBase,String className, String classNameSmall, String dataSource, String tableName, List<TableColumnDto> colunmList){
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String packages = packageBase + "." + className.toLowerCase();
		paraMap.put("package", packages);
		paraMap.put("className", className);
		paraMap.put("dataSource", dataSource);
		paraMap.put("tableName", tableName);

		paraMap.put("colunmList", colunmList);
		paraMap.put("dataTypes", getJataTypeList(tableName));
		
		String filePath = System.getProperty("user.dir") + "/"+srcFolder+"/" + packages.replace(".", "/") + "/" + className +"Dto.java";
		createFileByTemplete("dto.html", paraMap, filePath);
	}

	/**
	 * 生成.sql.xml
	 * @param classNameSmall
	 * @param tableName
	 * @param colunmList 
	 */
	public void sql(String srcFolder,String namespace,String packageBase,String classNameSmall, String tableName, List<TableColumnDto> colunmList){
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String packages = packageBase + "." + classNameSmall.toLowerCase();
		paraMap.put("namespace", namespace + "." + classNameSmall);
		paraMap.put("tableName", tableName);
		paraMap.put("colunmList", colunmList);
		paraMap.put("prefix", "<");
		paraMap.put("suffix", ">");
		paraMap.put("dl", "$");
		String filePath = System.getProperty("user.dir") + "/"+srcFolder+"/" + packages.replace(".", "/") + "/" + classNameSmall + ".sql.xml";
		createFileByTemplete("sql.html", paraMap, filePath);
	}

	/**
	 * 生成Controller
	 * @param className
	 * @param classNameSmall
	 */
	public void controller(String srcFolder,String basePath,String packageBase,String className, String classNameSmall, String tableName,String dataSource, List<TableColumnDto> colunmList,Boolean isHtml){
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String packages = packageBase + "." + classNameSmall.toLowerCase();
		paraMap.put("package", packages);
		paraMap.put("className", className);
		paraMap.put("classNameSmall", classNameSmall);
		paraMap.put("basePath", basePath);
		paraMap.put("tableName", tableName);
		paraMap.put("dataSource", dataSource);
		paraMap.put("colunmList", colunmList);
		
		String filePath = System.getProperty("user.dir") + "/"+srcFolder+"/" + packages.replace(".", "/") + "/" + className + "Controller.java";
		if(isHtml){
			createFileByTemplete("pageController.html", paraMap, filePath);
		}else{
			createFileByTemplete("controller.html", paraMap, filePath);
		}
	}
	/**
	 * 生成list.html
	 * @author ducongcong
	 * @createDate 2016年7月1日
	 */
	public void html(String srcFolder,String basePath,String packageBase,String className, String classNameSmall, String tableName, List<TableColumnDto> colunmList ){
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String packages = packageBase + "." + classNameSmall.toLowerCase();
		paraMap.put("package", packages);
		paraMap.put("className", className);
		paraMap.put("classNameSmall", classNameSmall);
		paraMap.put("basePath", basePath);
		paraMap.put("tableName", tableName);
		paraMap.put("classNameSmall", classNameSmall);
		paraMap.put("colunmList", colunmList);
		paraMap.put("dataTypes", getJataTypeList(tableName));
		paraMap.put("prefix", "<");
		paraMap.put("suffix", ">");
		paraMap.put("dl", "$");
		String filePath = System.getProperty("user.dir") + "/"+srcFolder+"/" + packages.replace(".", "/") + "/add.html";
		createFileByTemplete("add.html", paraMap, filePath);
		filePath = System.getProperty("user.dir") + "/"+srcFolder+"/" + packages.replace(".", "/") + "/edit.html";
		createFileByTemplete("edit.html", paraMap, filePath);
		filePath = System.getProperty("user.dir") + "/"+srcFolder+"/" + packages.replace(".", "/") + "/view.html";
		createFileByTemplete("view.html", paraMap, filePath);
	}

	/**
	 * 生成validator
	 * @param className
	 * @param classNameSmall
	 */
	public void validator(String srcFolder,String basePath,String packageBase,String className, String classNameSmall){
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String packages = packageBase + "." + classNameSmall.toLowerCase();
		paraMap.put("package", packages);
		paraMap.put("className", className);
		paraMap.put("classNameSmall", classNameSmall);
		paraMap.put("basePath", basePath);
		
		String filePath = System.getProperty("user.dir") + "/"+srcFolder+"/" + packages.replace(".", "/") + "/" + className + "Validator.java";
		createFileByTemplete("validator.html", paraMap, filePath);
	}
	
	/**
	 * 生成Service
	 * @param className
	 * @param classNameSmall
	 */
	public void service(String srcFolder,String namespace,String packageBase,String className, String classNameSmall, List<TableColumnDto> colunmList,Boolean isHtml){
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String packages = packageBase + "." + classNameSmall.toLowerCase();
		paraMap.put("package", packages);
		paraMap.put("className", className);
		paraMap.put("classNameSmall", classNameSmall);
		paraMap.put("namespace", namespace + "." + classNameSmall);
		paraMap.put("colunmList", colunmList);
		String filePath = System.getProperty("user.dir") + "/"+srcFolder+"/" + packages.replace(".", "/") + "/" + className + "Service.java";
		if(isHtml){
			createFileByTemplete("pageService.html", paraMap, filePath);
		}else{
			createFileByTemplete("service.html", paraMap, filePath);
		}
	}
	/**
	 * 生成dao
	 * @param className
	 * @param classNameSmall
	 */
	public void dao(String srcFolder,String namespace,String packageBase,String className, String classNameSmall, List<TableColumnDto> colunmList,Boolean isHtml){
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String packages = packageBase + "." + classNameSmall.toLowerCase();
		paraMap.put("package", packages);
		paraMap.put("className", className);
		paraMap.put("classNameSmall", classNameSmall);
		paraMap.put("namespace", namespace + "." + classNameSmall);
		paraMap.put("colunmList", colunmList);
		String filePath = System.getProperty("user.dir") + "/"+srcFolder+"/" + packages.replace(".", "/") + "/" + className + "Dao.java";
	    createFileByTemplete("dao.html", paraMap, filePath);
	}
	
	
	/**
	 * 根据具体模板生成文件
	 * @param templateFileName
	 * @param paraMap
	 * @param filePath
	 */
	public static void createFileByTemplete(String templateFileName, Map<String, Object> paraMap, String filePath)  {
		try {
			Class<?> classes = Class.forName("com.jfcf.platform.generate.GenerateBase");

			InputStream controllerInputStream = classes.getResourceAsStream(templateFileName);
			int count = 0;
			while (count == 0) {
				count = controllerInputStream.available();
			}
			
			byte[] bytes = new byte[count];
			int readCount = 0; // 已经成功读取的字节的个数
			while (readCount < count) {
				readCount += controllerInputStream.read(bytes, readCount, count - readCount);
			}
			
			String template = new String(bytes);
			
			String javaSrc = BeetlKit.render(template, paraMap);
			
			File file = new File(filePath);
			
			File path = new File(file.getParent());
			if (!path.exists()) {
				path.mkdirs();
			}
			
			BufferedWriter output = new BufferedWriter(new FileWriter(file));   
			output.write(javaSrc);   
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

