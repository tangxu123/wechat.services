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

}
