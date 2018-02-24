package com.ludateam.wechat.dto;

public class RecordWechatDto {

	/** 手机号码 */
	private String djxh;
	/** 微信账号 */
	private String wxzhid;
	/** 短信内容 */
	private String fsnr;
	/** 微信应用ID */
	private int wxyyid;

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

	public String getDjxh() {
		return djxh;
	}

	public void setDjxh(String djxh) {
		this.djxh = djxh;
	}

	public String getWxzhid() {
		return wxzhid;
	}

	public void setWxzhid(String wxzhid) {
		this.wxzhid = wxzhid;
	}

	public String getFsnr() {
		return fsnr;
	}

	public void setFsnr(String fsnr) {
		this.fsnr = fsnr;
	}
}
