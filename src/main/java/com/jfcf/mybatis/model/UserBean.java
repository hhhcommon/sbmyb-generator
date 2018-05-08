package com.jfcf.mybatis.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author wuchp
 * @createDate 2018年5月3日下午10:32:34
 */
public class UserBean implements Serializable {

	/**
	 *
	 * @author wuchp
	 * @createDate 2018年5月3日下午10:36:20
	 */
	private static final long serialVersionUID = 2453700889040208869L;
	
	private Long id;
	private Long customerId;
	private String mobile;
	private Integer whiteStatus;
	private Timestamp createTime;
	private Timestamp updateTime;
	private String remark;
	
	public UserBean(){
		
	}
	
	public UserBean(Long customerId, String mobile, Integer whiteStatus, String remark) {
		this.customerId = customerId;
		this.mobile = mobile;
		this.whiteStatus = whiteStatus;
		/*this.createTime = DateUtils.getCurTimestamp();
		this.updateTime = DateUtils.getCurTimestamp();*/
		this.remark = remark;
	}
	
	public UserBean(Long id, Long customerId, String mobile, Integer whiteStatus, String remark) {
		this.id = id;
		this.customerId = customerId;
		this.mobile = mobile;
		this.whiteStatus = whiteStatus;
		this.remark = remark;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getWhiteStatus() {
		return whiteStatus;
	}
	public void setWhiteStatus(Integer whiteStatus) {
		this.whiteStatus = whiteStatus;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
