package com.jfcf.utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;

public class DBInfoUtil {

	private static  Logger logger =  LoggerFactory.getLogger(DBInfoUtil.class);

	private static final Map<String, DataBase> dbMap = new LinkedHashMap<String, DataBase>();
	
	/**
	 * 获取所有数据源配置对象
	 * @return
	 */
	public static Map<String, DataBase> getDbMap(){
		if(dbMap.isEmpty()){
			synchronized ("DB_INIT_MAP") {
				if(dbMap.isEmpty()){
					parseDbInfo();
				}
			}
		}
		return dbMap;
	}

	/**
	 * 获取指定数据源对象
	 * @param name
	 * @return
	 */
	public static DataBase getDbMap(String name){
		if(dbMap.isEmpty()){
			parseDbInfo();
		}
		return dbMap.get(name);
	}
	
	/**
	 * 分解数据库连接url
	 * @return
	 */
	private static void parseDbInfo(){
		String driverClass = null;
		String jdbcUrl = null;
		String userName = null;
		String passWord = null;
		String ip = null;
		String port = null;
		String dbName = null;

		StringBuilder sb = null;
		DataBase db = null;
		if(null != PropKit.getInt(ConstantConfig.CONFIG_DB_NUM)){
			int count = PropKit.getInt(ConstantConfig.CONFIG_DB_NUM,1);
			for (int i = 1; i <= count; i++) {
				sb = new StringBuilder();
				sb.append("db[").append(i).append("]").append(".");
				// 数据源名称
				String name = PropKit.get(sb.toString() + ConstantConfig.CONFIG_DB_NAME_SUFFIX );
				
				// 判断数据库类型
				String dbType = PropKit.get(sb.toString() + ConstantConfig.CONFIG_DB_TYPE_SUFFIX );
				String prefix = sb.append(dbType).toString();
				driverClass = PropKit.get(prefix + ConstantConfig.DB_DRIVERCLASS_SUFFIX );
				jdbcUrl = PropKit.get(prefix + ConstantConfig.DB_JDBCURL_SUFFIX).trim();
				userName = PropKit.get(prefix + ConstantConfig.DB_USER_SUFFIX).trim();
				passWord = PropKit.get(prefix + ConstantConfig.DB_PASSWORD_SUFFIX).trim();
				
				// 解析数据库连接URL，获取数据库名称
				dbName = jdbcUrl.substring(jdbcUrl.indexOf("//") + 2);
				dbName = dbName.substring(dbName.indexOf("/") + 1);
				
				// 解析数据库连接URL，获取数据库地址IP
				ip = jdbcUrl.substring(jdbcUrl.indexOf("//") + 2);
				ip = ip.substring(0, ip.indexOf(":"));
				
				// 解析数据库连接URL，获取数据库地址端口
				port = jdbcUrl.substring(jdbcUrl.indexOf("//") + 2);
				port = port.substring(port.indexOf(":") + 1, port.indexOf("/"));
		        Pattern pattern = Pattern.compile("[0-9]+");  
				Matcher matcher = pattern.matcher(port);  
				if(!matcher.matches()){
					port = "3306";
				}
				// 把数据库连接信息写入常用map
				db = new DataBase();
				
				db.setName(name);
				
				db.setType(dbType);
				
				db.setDriverClass(driverClass);
				db.setJdbcUrl(jdbcUrl);
				db.setUserName(userName);
				passWord = SignUtil.getDecode(passWord); 
				db.setPassWord(passWord);
				db.setIp(ip);
				db.setPort(port);
				db.setDbName(dbName);
				
				db.setInitialSize(PropKit.getInt(ConstantConfig.DB_INITIALSIZE));
				db.setMinIdle(PropKit.getInt(ConstantConfig.DB_MINIDLE));
				db.setMaxActive(PropKit.getInt(ConstantConfig.DB_MAXACTIVE));
				dbMap.put(name, db);
			}
		}
	}
	
	/**
	 * 数据库导出
	 * @param dbName
	 * @param exportPath
	 * @throws IOException
	 */
	public static void exportSql(String dbName, String exportPath) throws IOException {
		DataBase db = getDbMap(dbName);
		String username = db.getUserName();
		String password = db.getPassWord();
		String ip = db.getIp();
		String port = db.getPort();
		String database = db.getDbName();
		
		StringBuilder command = new StringBuilder();

		String dbType = db.getType();
		if(dbType.equals(ConstantConfig.DB_TYPE_POSTGRESQL)){ // pg
			// pg_dump --host 127.0.0.1 --port 5432 --username "postgres" --role "postgres" --no-password  --format custom --blobs --encoding UTF8 --verbose --file "D:/jfinaluibv3.backup" "jfinaluibv3"
			command.append(PathKit.getWebRootPath()).append("/WEB-INF/database/pg/bin/pg_dump ");
			command.append(" --host ").append(ip).append(" --port ").append(port).append(" --username ").append(" \"postgres\" ");
			command.append(" --role \"postgres\" --no-password  --format custom --blobs --encoding UTF8 --verbose --file ").append(exportPath).append(" \"").append(database).append("\" ");
			
		}else if(dbType.equals(ConstantConfig.DB_TYPE_MYSQL)){ // mysql
			command.append("cmd /c mysqldump -u").append(username).append(" -p").append(password)//密码是用的小p，而端口是用的大P。  
					.append(" -h").append(ip).append(" -P").append(port).append(" ").append(database).append(" -r \"").append(exportPath+"\"");
		} 
		try {
			Process process = Runtime.getRuntime().exec(command.toString(), null, new File(exportPath));
			process.waitFor();
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("exportSql is error:",e);
			}
		}
	}
	
	/**
	 * 数据库导入
	 * @param dbName
	 * @param filePath
	 * @throws IOException
	 */
	public static void importSql(String dbName, String filePath) throws IOException {
		DataBase db = getDbMap(dbName);
		String username = db.getUserName();
		String password = db.getPassWord();
		String ip = db.getIp();
		String port = db.getPort();
		String database = db.getDbName();
		
		String dbType = db.getType();
		if(dbType.equals(ConstantConfig.DB_TYPE_MYSQL)){ // mysql
			//第一步，获取登录命令语句  
			String loginCommand = new StringBuilder().append("mysql -u").append(username).append(" -p").append(password).append(" -h").append(ip).append(" -P").append(port).toString();
			//第二步，获取切换数据库到目标数据库的命令语句  
			String switchCommand = new StringBuilder("use ").append(database).toString();
			//第三步，获取导入的命令语句  
			String importCommand = new StringBuilder("source ").append(filePath).toString();
			//需要返回的命令语句数组  
			String[] commands = new String[] { loginCommand, switchCommand, importCommand };
			
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(commands[0]);
			//执行了第一条命令以后已经登录到mysql了，所以之后就是利用mysql的命令窗口  
			//进程执行后面的代码  
			OutputStream os = process.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(os);
			//命令1和命令2要放在一起执行  
			writer.write(commands[1] + "\r\n" + commands[2]);
			writer.flush();
			writer.close();
			os.close();
		}
		
	}
}
