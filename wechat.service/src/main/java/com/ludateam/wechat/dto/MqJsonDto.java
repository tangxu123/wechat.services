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
	/** 标题 */
	private String title = "";
	/** 点击后跳转的链接。 */
	private String url = "";

	public String getFsnrlx() {
		return fsnrlx;
	}

	public void setFsnrlx(String fsnrlx) {
		this.fsnrlx = fsnrlx;
	}

	/** 发送内容类型。 */
	private String fsnrlx = "";

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

	/**
	 * 取得点击后跳转的链接。的值
	 * 
	 * @return 点击后跳转的链接。
	 *
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设定点击后跳转的链接。的值
	 * 
	 * @param url
	 *            点击后跳转的链接。
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 取得标题的值
	 * 
	 * @return 标题
	 *
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设定标题的值
	 * 
	 * @param title
	 *            标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}
}
