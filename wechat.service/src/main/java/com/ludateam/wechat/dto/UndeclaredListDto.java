package com.ludateam.wechat.dto;

import java.util.ArrayList;
import java.util.List;

/** 未申报企业 */
public class UndeclaredListDto {

	/** 登记序号 */
	private String djxh = "";
	/** 纳税人名称 */
	private String nsrmc = "";
	/** 社会信用代码 */
	private String shxydm = "";
	/** 主管税务所（科、分局）代码 */
	private String zgswskfjDm = "";
	/** 受理人员代码 */
	private String slryDm = "";
	/** 征收项目名称 */
	private String zsxmmcs = "";
	/** 发送对象 */
	private String fsdx = "";
	/** 消息接收者列表 */
	private List<ReceivdUserDto> userList = new ArrayList<ReceivdUserDto>();

	/**
	 * 取得登记序号的值
	 * 
	 * @return 登记序号
	 *
	 */
	public String getDjxh() {
		return djxh;
	}

	/**
	 * 设定登记序号的值
	 * 
	 * @param djxh
	 *            登记序号
	 */
	public void setDjxh(String djxh) {
		this.djxh = djxh;
	}

	/**
	 * 取得纳税人名称的值
	 * 
	 * @return 纳税人名称
	 *
	 */
	public String getNsrmc() {
		return nsrmc;
	}

	/**
	 * 设定纳税人名称的值
	 * 
	 * @param nsrmc
	 *            纳税人名称
	 */
	public void setNsrmc(String nsrmc) {
		this.nsrmc = nsrmc;
	}

	/**
	 * 取得社会信用代码的值
	 * 
	 * @return 社会信用代码
	 *
	 */
	public String getShxydm() {
		return shxydm;
	}

	/**
	 * 设定社会信用代码的值
	 * 
	 * @param shxydm
	 *            社会信用代码
	 */
	public void setShxydm(String shxydm) {
		this.shxydm = shxydm;
	}

	/**
	 * 取得主管税务所（科、分局）代码的值
	 * 
	 * @return 主管税务所（科、分局）代码
	 *
	 */
	public String getZgswskfjDm() {
		return zgswskfjDm;
	}

	/**
	 * 设定主管税务所（科、分局）代码的值
	 * 
	 * @param zgswskfjDm
	 *            主管税务所（科、分局）代码
	 */
	public void setZgswskfjDm(String zgswskfjDm) {
		this.zgswskfjDm = zgswskfjDm;
	}

	/**
	 * 取得受理人员代码的值
	 * 
	 * @return 受理人员代码
	 *
	 */
	public String getSlryDm() {
		return slryDm;
	}

	/**
	 * 设定受理人员代码的值
	 * 
	 * @param slryDm
	 *            受理人员代码
	 */
	public void setSlryDm(String slryDm) {
		this.slryDm = slryDm;
	}

	/**
	 * 取得征收项目名称的值
	 * 
	 * @return 征收项目名称
	 *
	 */
	public String getZsxmmcs() {
		return zsxmmcs;
	}

	/**
	 * 设定征收项目名称的值
	 * 
	 * @param zsxmmcs
	 *            征收项目名称
	 */
	public void setZsxmmcs(String zsxmmcs) {
		this.zsxmmcs = zsxmmcs;
	}

	/**
	 * 取得发送对象的值
	 * 
	 * @return 发送对象
	 *
	 */
	public String getFsdx() {
		return fsdx;
	}

	/**
	 * 设定发送对象的值
	 * 
	 * @param fsdx
	 *            发送对象
	 */
	public void setFsdx(String fsdx) {
		this.fsdx = fsdx;
	}

	/**
	 * 取得消息接收者列表的值
	 * 
	 * @return 消息接收者列表
	 *
	 */
	public List<ReceivdUserDto> getUserList() {
		return userList;
	}

	/**
	 * 设定消息接收者列表的值
	 * 
	 * @param userList
	 *            消息接收者列表
	 */
	public void setUserList(List<ReceivdUserDto> userList) {
		this.userList = userList;
	}

}
