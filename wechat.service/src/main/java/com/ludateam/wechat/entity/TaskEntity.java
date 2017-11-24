package com.ludateam.wechat.entity;

import java.math.BigDecimal;

public class TaskEntity {

	/** 任务id */
	private BigDecimal rwid = null;
	/** 手机号码 */
	private String sjhm = "";
	/** 短信内容 */
	private String dxnr = "";
	/** 条目id */
	private BigDecimal tmid = null;
	/** 发送方式 */
	private String fsfs = "";
	/** 微信账号id */
	private String wxzhid = "";
	/** 企业号ID */
	private int qyhid;
	/** 微信应用ID */
	private int wxyyid;

	/**
	 * 取得任务id的值
	 * 
	 * @return 任务id
	 *
	 */
	public BigDecimal getRwid() {
		return rwid;
	}

	/**
	 * 设定任务id的值
	 * 
	 * @param rwid
	 *            任务id
	 */
	public void setRwid(BigDecimal rwid) {
		this.rwid = rwid;
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
	 * 取得短信内容的值
	 * 
	 * @return 短信内容
	 *
	 */
	public String getDxnr() {
		return dxnr;
	}

	/**
	 * 设定短信内容的值
	 * 
	 * @param dxnr
	 *            短信内容
	 */
	public void setDxnr(String dxnr) {
		this.dxnr = dxnr;
	}

	/**
	 * 取得条目id的值
	 * 
	 * @return 条目id
	 *
	 */
	public BigDecimal getTmid() {
		return tmid;
	}

	/**
	 * 设定条目id的值
	 * 
	 * @param tmid
	 *            条目id
	 */
	public void setTmid(BigDecimal tmid) {
		this.tmid = tmid;
	}

	/**
	 * 取得发送方式的值
	 * 
	 * @return 发送方式
	 *
	 */
	public String getFsfs() {
		return fsfs;
	}

	/**
	 * 设定发送方式的值
	 * 
	 * @param fsfs
	 *            发送方式
	 */
	public void setFsfs(String fsfs) {
		this.fsfs = fsfs;
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
	 * 取得企业号ID的值
	 * 
	 * @return 企业号ID
	 *
	 */
	public int getQyhid() {
		return qyhid;
	}

	/**
	 * 设定企业号ID的值
	 * 
	 * @param qyhid
	 *            企业号ID
	 */
	public void setQyhid(int qyhid) {
		this.qyhid = qyhid;
	}

	/**
	 * 取得微信应用ID的值
	 * 
	 * @return 微信应用ID
	 *
	 */
	public int getWxyyid() {
		return wxyyid;
	}

	/**
	 * 设定微信应用ID的值
	 * 
	 * @param wxyyid
	 *            微信应用ID
	 */
	public void setWxyyid(int wxyyid) {
		this.wxyyid = wxyyid;
	}
}
