package com.ludateam.wechat.dto;

public class ResultDto {
	
	/** 返回码 */
	private String errcode = "";
	/** 对返回码的文本描述内容 */
	private String errmsg = "";

	/**
	 * 取得返回码的值
	 * 
	 * @return 返回码
	 *
	 */
	public String getErrcode() {
		return errcode;
	}

	/**
	 * 设定返回码的值
	 * 
	 * @param errcode
	 *            返回码
	 * 
	 */
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	/**
	 * 取得对返回码的文本描述内容的值
	 * 
	 * @return 对返回码的文本描述内容
	 *
	 */
	public String getErrmsg() {
		return errmsg;
	}

	/**
	 * 设定对返回码的文本描述内容的值
	 * 
	 * @param errmsg
	 *            对返回码的文本描述内容
	 * 
	 */
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
}
