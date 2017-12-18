package com.ludateam.wechat.entity;

/**
 * 企业绑定对照关系实体类
 * */
public class BindingEntity {

	/** 登记序号 */
	private String djxh = "";
	/** 纳税人名称 */
	private String nsrmc = "";
	/** 关系ID */
	private String gxid = "";
	/** 是否使用 */
	private String isUse = "";

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
	 * 取得关系ID的值
	 * 
	 * @return 关系ID
	 *
	 */
	public String getGxid() {
		return gxid;
	}

	/**
	 * 设定关系ID的值
	 * 
	 * @param gxid
	 *            关系ID
	 */
	public void setGxid(String gxid) {
		this.gxid = gxid;
	}

	/**
	 * 取得是否使用的值
	 * 
	 * @return 是否使用
	 *
	 */
	public String getIsUse() {
		return isUse;
	}

	/**
	 * 设定是否使用的值
	 * 
	 * @param isUse
	 *            是否使用
	 */
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
}
