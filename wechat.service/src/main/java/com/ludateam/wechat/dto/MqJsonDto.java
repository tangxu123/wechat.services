package com.ludateam.wechat.dto;

public class MqJsonDto {

	/** 任务id */
	private String rwid;
	/** 手机号码 */
	private String sjhm;
	/** 微信账号 */
	private String wxzh;
	/** 短信内容 */
	private String dxnr;
	/** 企业号ID */
	private int qyhid;
	/** 微信应用ID */
	private int wxyyid;

	public String getRwid() {
		return rwid;
	}

	public void setRwid(String rwid) {
		this.rwid = rwid;
	}

	public String getSjhm() {
		return sjhm;
	}

	public void setSjhm(String sjhm) {
		this.sjhm = sjhm;
	}

	public String getWxzh() {
		return wxzh;
	}

	public void setWxzh(String wxzh) {
		this.wxzh = wxzh;
	}

	public String getDxnr() {
		return dxnr;
	}

	public void setDxnr(String dxnr) {
		this.dxnr = dxnr;
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
