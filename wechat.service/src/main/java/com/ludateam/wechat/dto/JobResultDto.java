package com.ludateam.wechat.dto;

public class JobResultDto extends ResultDto {

	/** 成员UserID */
	private String userid;
	/** 操作类型（按位或）：1 新建部门 ，2 更改部门名称， 4 移动部门， 8 修改部门排序 */
	private int action;
	/** 部门ID */
	private int partyid;

	/**
	 * 取得成员UserID的值
	 * 
	 * @return 成员UserID
	 *
	 */
	public String getUserid() {
		return userid;
	}

	/**
	 * 设定成员UserID的值
	 * 
	 * @param userid
	 *            成员UserID
	 * 
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	/**
	 * 取得操作类型的值
	 * 
	 * @return 操作类型
	 *
	 */
	public int getAction() {
		return action;
	}

	/**
	 * 设定操作类型的值
	 * 
	 * @param action
	 *            操作类型
	 * 
	 */
	public void setAction(int action) {
		this.action = action;
	}

	/**
	 * 取得部门ID的值
	 * 
	 * @return 部门ID
	 *
	 */
	public int getPartyid() {
		return partyid;
	}

	/**
	 * 设定部门ID的值
	 * 
	 * @param partyid
	 *            部门ID
	 * 
	 */
	public void setPartyid(int partyid) {
		this.partyid = partyid;
	}
}
