package com.ludateam.wechat.entity;

import java.math.BigDecimal;

public class FsmdEntity {

	/** 任务ID */
	private BigDecimal rwid;
	/** 发送任 */
	private String fsr;
	/** 发送属性 */
	private String fssx;
	/** 手机号码 */
	private String sjhm;
	/** 短信内容 */
	private String dxnr;
	/** 微信账号ID */
	private String wxzhid;

	/**
	 * 取得任务ID的值
	 * 
	 * @return 任务ID
	 *
	 */
	public BigDecimal getRwid() {
		return rwid;
	}

	/**
	 * 设定任务ID的值
	 * 
	 * @param rwid
	 *            任务ID
	 */
	public void setRwid(BigDecimal rwid) {
		this.rwid = rwid;
	}

	/**
	 * 取得发送任的值
	 * 
	 * @return 发送任
	 *
	 */
	public String getFsr() {
		return fsr;
	}

	/**
	 * 设定发送任的值
	 * 
	 * @param fsr
	 *            发送任
	 */
	public void setFsr(String fsr) {
		this.fsr = fsr;
	}

	/**
	 * 取得发送属性的值
	 * 
	 * @return 发送属性
	 *
	 */
	public String getFssx() {
		return fssx;
	}

	/**
	 * 设定发送属性的值
	 * 
	 * @param fssx
	 *            发送属性
	 */
	public void setFssx(String fssx) {
		this.fssx = fssx;
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
	 * 取得微信账号ID的值
	 * 
	 * @return 微信账号ID
	 *
	 */
	public String getWxzhid() {
		return wxzhid;
	}

	/**
	 * 设定微信账号ID的值
	 * 
	 * @param wxzhid
	 *            微信账号ID
	 */
	public void setWxzhid(String wxzhid) {
		this.wxzhid = wxzhid;
	}

}
