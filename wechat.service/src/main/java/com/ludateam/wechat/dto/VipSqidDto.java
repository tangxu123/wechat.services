package com.ludateam.wechat.dto;

public class VipSqidDto extends ResultDto {

	/** 授权id */
	private String sqid = "";

	/**
	 * 取得授权id的值
	 * 
	 * @return 授权id
	 *
	 */
	public String getSqid() {
		return sqid;
	}

	/**
	 * 设定授权id的值
	 * 
	 * @param sqid
	 *            授权id
	 * 
	 */
	public void setSqid(String sqid) {
		this.sqid = sqid;
	}
}
