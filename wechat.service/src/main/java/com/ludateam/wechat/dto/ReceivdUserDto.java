package com.ludateam.wechat.dto;

public class ReceivdUserDto {

	/** 办税员类型 */
	private String bsylx = "";
	/** 微信账号id */
	private String wxzhid = "";
	/** 手机号码 */
	private String sjhm = "";

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
