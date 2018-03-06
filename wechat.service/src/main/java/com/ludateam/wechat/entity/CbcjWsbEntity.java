package com.ludateam.wechat.entity;

import java.io.Serializable;

public class CbcjWsbEntity implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -3085480000246257651L;

	/** 登记序号 */
	private String djxh = "";
	/** 纳税人名称 */
	private String nsrmc = "";
	/** 社会信用代码 */
	private String shxydm = "";
	/** 征收项目名称 */
	private String zsxmmcs = "";
	/** 主管税务所（科、分局）代码 */
	private String zgswskfjDm = "";
	/** 受理人员代码 */
	private String slryDm = "";
	/** 微信账号id */
	private String wxzhid = "";
	/** 手机号码 */
	private String sjhm = "";
	/** 办税员类型 */
	private String bsylx = "";

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
	 * 取得手机号码的值
	 * 
	 * @return 手机号码
	 *
	 */
	public String getSjhm() {
		return sjhm;
	}

	/**
	 * 设定手机号码的值
	 * 
	 * @param sjhm
	 *            手机号码
	 */
	public void setSjhm(String sjhm) {
		this.sjhm = sjhm;
	}

	/**
	 * 取得微信账号id的值
	 * 
	 * @return 微信账号id
	 *
	 */
	public String getWxzhid() {
		return wxzhid;
	}

	/**
	 * 设定微信账号id的值
	 * 
	 * @param wxzhid
	 *            微信账号id
	 */
	public void setWxzhid(String wxzhid) {
		this.wxzhid = wxzhid;
	}

	/**
	 * 取得办税员类型的值
	 * 
	 * @return 办税员类型
	 *
	 */
	public String getBsylx() {
		return bsylx;
	}

	/**
	 * 设定办税员类型的值
	 * 
	 * @param bsylx
	 *            办税员类型
	 */
	public void setBsylx(String bsylx) {
		this.bsylx = bsylx;
	}
}
