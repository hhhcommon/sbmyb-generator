/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: 玖富时代</p>
 * @author ducongcong
 * @version 1.0
 */
package com.jfcf.platform.generate;

public class GenConfig {

	/**
	 * 数据库驱动配置
	 */
	private String driverClass;
	/**
	 * 数据库链接地址
	 */
	private String jdbcUrl;
	/**
	 * 数据库用户名
	 */
	private String jdbcUser;
	/**
	 * 数据库密码
	 */
	private String jdbcPwd;
	/**
	 * 代码生成包配置目录
	 */
	private String srcFolder;
	/**
	 * 代码生成的包
	 */
	private String packageBase;
	private String basePath;
	/**
	 * 是否生成html,true生成html，false 不
	 */
	private Boolean isHtml;
	/**
	 * sql文件命名空间,自己修改为对应的 loan或risk
	 */
	private String namespace;
	/**
	 * 数据源名称
	 */
	private String dataSourceName;
	public String getDriverClass() {
		return driverClass;
	}
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	public String getJdbcUser() {
		return jdbcUser;
	}
	public void setJdbcUser(String jdbcUser) {
		this.jdbcUser = jdbcUser;
	}
	public String getJdbcPwd() {
		return jdbcPwd;
	}
	public void setJdbcPwd(String jdbcPwd) {
		this.jdbcPwd = jdbcPwd;
	}
	public String getSrcFolder() {
		return srcFolder;
	}
	public void setSrcFolder(String srcFolder) {
		this.srcFolder = srcFolder;
	}
	public String getPackageBase() {
		return packageBase;
	}
	public void setPackageBase(String packageBase) {
		this.packageBase = packageBase;
	}
	public String getBasePath() {
		return basePath;
	}
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getDataSourceName() {
		return dataSourceName;
	}
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
	public Boolean getIsHtml() {
		return isHtml;
	}
	public void setIsHtml(Boolean isHtml) {
		this.isHtml = isHtml;
	}
	
	
}
