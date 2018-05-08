package com.jfcf.platform.generate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;

/**
 * 定制MySQL下的代码生成
 */
public class GenMainMySQL extends GenerateBase {
	private static Log log = Log.getLog(GenMainMySQL.class);

	/**
	 * 表中字段为驼峰的代码生成工具类
	 * @param config 配置项信息
	 * @param tableArr 对应的表配置
	 * 	 * 数据源（默认可以是null）、
	 *     表名、
	 *     主键名（默认可以是null）、
	 *     类名（不包含后缀.java）
	 *      String[][] tableArr = {
		      {"ConstantConfig.DB_DATABASE_LOAN", "t_sys_ipwhite", "id", "SysIpWhite"}
	        }
	 */
	public GenMainMySQL(GenConfig config,String[][] tableArr) {
		String basePath=config.getBasePath();
		
		/**
		 * 生成的包和类所在的源码根目录，比如src
		 */
		String srcFolder = config.getSrcFolder();
		/**
		 * 生成的文件存放的包，公共基础包
		 * 描述：比如
		 * 	platform所在的包就是com.platform
		 * 	weixin所在的包就是com.weixin
		 */
		String packageBase =config.getPackageBase();
		
		log.info("configPlugin 配置Druid数据库连接池连接属性");
		String username = config.getJdbcUser().trim();
		String password = config.getJdbcPwd().trim();
		String jdbcUrl = config.getJdbcUrl();
		String driverClass=config.getDriverClass().trim();
		String namespace=config.getNamespace();
		String dataSourceName= config.getDataSourceName();
		/**
		 * 配置链接的数据源
		 */
		DruidPlugin druidPluginMain = new DruidPlugin(jdbcUrl, username, password, driverClass);
		druidPluginMain.set(1, 1, 1);
		druidPluginMain.start();
		ActiveRecordPlugin arpMain = new ActiveRecordPlugin("main",druidPluginMain);
		arpMain.setDevMode(true); // 设置开发模式
		arpMain.setShowSql(true); // 是否显示SQL
		arpMain.setContainerFactory(new CaseInsensitiveContainerFactory(true));// 大小写不敏感
		arpMain.setDialect(new MysqlDialect());
		arpMain.start();
		
		/**
		 * 从jdbcurl中获取数据库名称
		 */
		String dbName = jdbcUrl.substring(jdbcUrl.indexOf("//") + 2);
		dbName = dbName.substring(dbName.indexOf("/") + 1, dbName.indexOf("?"));
		
		/**
		 * 创建  information_schema 表的数据源
		 *   用于读取表的元数据
		 */
		jdbcUrl = jdbcUrl.replace(dbName, "information_schema");
		DruidPlugin druidPluginIS = new DruidPlugin(jdbcUrl, username, password, driverClass);
		druidPluginIS.set(1, 1, 1);
		druidPluginIS.start();
		
		log.info("configPlugin 配置ActiveRecord插件");
		ActiveRecordPlugin arpIS = new ActiveRecordPlugin("information_schema", druidPluginIS);
		arpIS.setDevMode(true); // 设置开发模式
		arpIS.setShowSql(true); // 是否显示SQL
		arpIS.setContainerFactory(new CaseInsensitiveContainerFactory(true));// 大小写不敏感
		arpIS.setDialect(new MysqlDialect());
		arpIS.start();
		
		for (int i = 0; i < tableArr.length; i++) {
			// 数据源名称
			String dataSource = tableArr[i][0]; 
			// 表名
			String tableName = tableArr[i][1]; 
			// 主键
			String pkName = tableArr[i][2]; 
			// 类名
			String className = tableArr[i][3]; 
			// 类名首字母小写
			String classNameSmall = toLowerCaseFirstOne(className); 
			
			List<TableColumnDto> colunmList = getColunm(dbName,tableName);
			
			// 1.生成sql文件
			sql(srcFolder,namespace,packageBase,classNameSmall, tableName,colunmList); 
			
			// 2.生成model
			modelNew(srcFolder,namespace,packageBase,className, classNameSmall, dataSource, tableName, pkName, colunmList,false); 
			//生成只读model
			modelNew(srcFolder,namespace,packageBase,className, classNameSmall, dataSource, tableName, pkName, colunmList,true); 
			
    	    dto(srcFolder, packageBase, className, classNameSmall, dataSource, tableName, colunmList);
            if(config.getIsHtml()){
            	html(srcFolder, basePath, packageBase, className, classNameSmall, tableName,colunmList);
            }
        	// 4.生成controller
			controller(srcFolder,basePath,packageBase,className, classNameSmall, tableName,dataSource, colunmList,config.getIsHtml()); 
		
			// 5.生成service
			service(srcFolder,namespace,packageBase,className, classNameSmall, colunmList,config.getIsHtml()); 
			// 生成dao
			dao(srcFolder,namespace,packageBase,className, classNameSmall, colunmList,config.getIsHtml()); 
		}
		
		System.exit(0);
	}
	
	@Override
	public List<TableColumnDto> getColunm(String dbName,String tableName)  {
		List<TableColumnDto> list = new ArrayList<TableColumnDto>();
		
		/**
		 * 查询对应的表名称，获取表描述
		 */
		String tableDesc = Db.use("information_schema").findFirst("select * from tables where table_schema = ? and table_name = ?", dbName, tableName).getStr("table_COMMENT");
		/**
		 * 查询表对应的列
		 */
		List<Record> listColumn = Db.use("information_schema").find("select * from columns where table_schema = ? and table_name = ?", dbName, tableName);
		
		Map<String, String> columnJavaTypeMap = getJavaType(tableName);
				
		for (Record record : listColumn) {
			String column_name = record.getStr("column_name");
			String column_type = record.getStr("column_type");
			String character_maximum_length = String.valueOf(record.getBigInteger("CHARACTER_MAXIMUM_LENGTH"));
			String column_comment = record.getStr("COLUMN_COMMENT");

			// 需要跳过的字段
			if("xxx".equals(column_name) || "yyy".equals(column_name) || "zzz".equals(column_name)){
				continue;
			}
			
			TableColumnDto table = new TableColumnDto();
			table.setTable_name(tableName);
			table.setTable_desc(tableDesc);
			String[] arr = column_name.split("_");
			StringBuffer columnSb=new StringBuffer();
			
			if(arr.length>0){
				for(int i=0;i<arr.length;i++){
					if(i!=0){
						columnSb.append(toUpperCaseFirstOne(arr[i].toLowerCase()));
					}else{
						columnSb.append(arr[i].toLowerCase());
					}
				}
			}
			table.setColumn_name_t(columnSb.toString());
			table.setColumn_name(column_name);
			table.setColumn_name_upperCaseFirstOne(toUpperCaseFirstOne(columnSb.toString()));
			table.setColumn_type(column_type);
			table.setColumn_length(character_maximum_length);
			table.setColumn_desc(column_comment);
			
			table.setColumn_className(columnJavaTypeMap.get(column_name.toLowerCase()));
			
			list.add(table);
		}
		
		return list;
	}
	/**
	 * 首字母转小写
	 * @param s
	 * @return
	 */
	public static String toLowerCaseFirstOne(String s) {
        if(Character.isLowerCase(s.charAt(0))){
        	return s;
        } else {
        	return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }
	/**
	 * 首字母转大写
	 * @param s
	 * @return
	 */
    public static String toUpperCaseFirstOne(String s) {
        if(Character.isUpperCase(s.charAt(0))){
        	return s;
        } else {
        	return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }
}
