package com.ludateam.wechat.entity;

public class TaxOfficerEntity {

	/** 姓名 */
	private String name = "";
	/** 帐号 */
	private String userid = "";
	/** 微信号 */
	private String wxid = "";
	/** 手机号 */
	private String mobile = "";
	/** 邮箱 */
	private String email = "";
	/** 所在部门 */
	private String department = "";
	/** 职位 */
	private String position = "";

	/**
	 * 取得姓名的值
	 * 
	 * @return 姓名
	 *
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设定姓名的值
	 * 
	 * @param name
	 *            姓名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 取得帐号的值
	 * 
	 * @return 帐号
	 *
	 */
	public String getUserid() {
		return userid;
	}

	/**
	 * 设定帐号的值
	 * 
	 * @param userid
	 *            帐号
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	/**
	 * 取得微信号的值
	 * 
	 * @return 微信号
	 *
	 */
	public String getWxid() {
		return wxid;
	}

	/**
	 * 设定微信号的值
	 * 
	 * @param wxid
	 *            微信号
	 */
	public void setWxid(String wxid) {
		this.wxid = wxid;
	}

	/**
	 * 取得手机号的值
	 * 
	 * @return 手机号
	 *
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * 设定手机号的值
	 * 
	 * @param mobile
	 *            手机号
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * 取得邮箱的值
	 * 
	 * @return 邮箱
	 *
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 设定邮箱的值
	 * 
	 * @param email
	 *            邮箱
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 取得所在部门的值
	 * 
	 * @return 所在部门
	 *
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * 设定所在部门的值
	 * 
	 * @param department
	 *            所在部门
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * 取得职位的值
	 * 
	 * @return 职位
	 *
	 */
	public String getPosition() {
		String positionEx = "";
		if (position != null) {
			if (position.contains("1") || position.contains("2")
					|| position.contains("3")) {
				if (position.contains("1")) {
					positionEx = "法定代表人";
				}
				if (position.contains("2")) {
					positionEx = "".equals(positionEx) ? "财务负责人" : positionEx
							+ "|财务负责人";
				}
				if (position.contains("3")) {
					positionEx = "".equals(positionEx) ? "办税员" : positionEx
							+ "|办税员";
				}
			} else {
				positionEx = position;
			}
		}
		return positionEx;
	}

	/**
	 * 设定职位的值
	 * 
	 * @param position
	 *            职位
	 */
	public void setPosition(String position) {
		this.position = position;
	}

}
