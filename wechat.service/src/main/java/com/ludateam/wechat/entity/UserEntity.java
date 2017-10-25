package com.ludateam.wechat.entity;

import java.io.Serializable;

public class UserEntity implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -3085480000246257651L;

	/** 成员UserID */
	private String userid;
	/** 成员名称 */
	private String name;
	/** 手机号码 */
	private String mobile;
	/** 成员所属部门id列表 */
	private int[] department;
	/** 部门内的排序值 */
	private int[] order;
	/** 职位信息 */
	private String position;
	/** 性别 */
	private String gender;
	/** 邮箱 */
	private String email;
	/** 标示是否为上级 */
	private int isleader;
	/** 头像url */
	private String avatar;
	/** 座机 */
	private String telephone;
	/** 英文名 */
	private String english_name;
	/** 激活状态 */
	private int status;
	/** 扩展属性 */
	private String extattr;

	/**
	 * 取得成员UserID的值
	 * 
	 * @return 成员UserID
	 *
	 */
	public String getUserid() {
		return userid;
	}

	/**
	 * 设定成员UserID的值
	 * 
	 * @param userid
	 *            成员UserID
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	/**
	 * 取得成员名称的值
	 * 
	 * @return 成员名称
	 *
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设定成员名称的值
	 * 
	 * @param name
	 *            成员名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 取得手机号码的值
	 * 
	 * @return 手机号码
	 *
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * 设定手机号码的值
	 * 
	 * @param mobile
	 *            手机号码
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * 取得成员所属部门id列表的值
	 * 
	 * @return 成员所属部门id列表
	 *
	 */
	public int[] getDepartment() {
		return department;
	}

	/**
	 * 设定成员所属部门id列表的值
	 * 
	 * @param department
	 *            成员所属部门id列表
	 */
	public void setDepartment(int[] department) {
		this.department = department;
	}

	/**
	 * 取得部门内的排序值的值
	 * 
	 * @return 部门内的排序值
	 *
	 */
	public int[] getOrder() {
		return order;
	}

	/**
	 * 设定部门内的排序值的值
	 * 
	 * @param order
	 *            部门内的排序值
	 */
	public void setOrder(int[] order) {
		this.order = order;
	}

	/**
	 * 取得职位信息的值
	 * 
	 * @return 职位信息
	 *
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * 设定职位信息的值
	 * 
	 * @param position
	 *            职位信息
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * 取得性别的值
	 * 
	 * @return 性别
	 *
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * 设定性别的值
	 * 
	 * @param gender
	 *            性别
	 */
	public void setGender(String gender) {
		this.gender = gender;
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
	 * 取得标示是否为上级的值
	 * 
	 * @return 标示是否为上级
	 *
	 */
	public int getIsleader() {
		return isleader;
	}

	/**
	 * 设定标示是否为上级的值
	 * 
	 * @param isleader
	 *            标示是否为上级
	 */
	public void setIsleader(int isleader) {
		this.isleader = isleader;
	}

	/**
	 * 取得头像url的值
	 * 
	 * @return 头像url
	 *
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * 设定头像url的值
	 * 
	 * @param avatar
	 *            头像url
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	/**
	 * 取得座机的值
	 * 
	 * @return 座机
	 *
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * 设定座机的值
	 * 
	 * @param telephone
	 *            座机
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * 取得英文名的值
	 * 
	 * @return 英文名
	 *
	 */
	public String getEnglish_name() {
		return english_name;
	}

	/**
	 * 设定英文名的值
	 * 
	 * @param english_name
	 *            英文名
	 */
	public void setEnglish_name(String englishName) {
		this.english_name = englishName;
	}

	/**
	 * 取得激活状态的值
	 * 
	 * @return 激活状态
	 *
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设定激活状态的值
	 * 
	 * @param status
	 *            激活状态
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 取得扩展属性的值
	 * 
	 * @return 扩展属性
	 *
	 */
	public String getExtattr() {
		return extattr;
	}

	/**
	 * 设定扩展属性的值
	 * 
	 * @param extattr
	 *            扩展属性
	 */
	public void setExtattr(String extattr) {
		this.extattr = extattr;
	}

}
