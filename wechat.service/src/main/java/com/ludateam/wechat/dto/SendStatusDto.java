package com.ludateam.wechat.dto;

import java.util.ArrayList;
import java.util.List;

/** 信息发送状态 */
public class SendStatusDto {

	/** 任务ID */
	private String rwid = "";
	/** 发送属性 */
	private String fssx = "";
	/** 消息组 */
	private String msgGroup = "";
	/** 手机号码 */
	private List<String> sjhmList = new ArrayList<String>();
	/** 微信账号ID */
	private List<String> wxzhidList = new ArrayList<String>();

	/**
	 * 取得任务ID的值
	 * 
	 * @return 任务ID
	 * 
	 */
	public String getRwid() {
		return rwid;
	}

	/**
	 * 设定任务ID的值
	 * 
	 * @param rwid
	 *            任务ID
	 */
	public void setRwid(String rwid) {
		this.rwid = rwid;
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
	public List<String> getSjhmList() {
		return sjhmList;
	}

	/**
	 * 设定手机号码的值
	 * 
	 * @param sjhmList
	 *            手机号码
	 */
	public void setSjhmList(List<String> sjhmList) {
		this.sjhmList = sjhmList;
	}

	/**
	 * 取得微信账号ID的值
	 * 
	 * @return 微信账号ID
	 * 
	 */
	public List<String> getWxzhidList() {
		return wxzhidList;
	}

	/**
	 * 设定微信账号ID的值
	 * 
	 * @param wxzhidList
	 *            微信账号ID
	 */
	public void setWxzhidList(List<String> wxzhidList) {
		this.wxzhidList = wxzhidList;
	}

	/**
	 * 取得消息组的值
	 * 
	 * @return 消息组
	 * 
	 */
	public String getMsgGroup() {
		return msgGroup;
	}

	/**
	 * 设定消息组的值
	 * 
	 * @param msgGroup
	 *            消息组
	 */
	public void setMsgGroup(String msgGroup) {
		this.msgGroup = msgGroup;
	}

}
