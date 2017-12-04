package com.ludateam.wechat.dto;

public class SyncUserJobDto extends ResultDto {

	/** 异步任务id */
	private String jobid;
	/** 执行数量 */
	private int zxsl;
	/** 微信企业号ID */
	private String wxqyhId;

	/**
	 * 取得异步任务id的值
	 * 
	 * @return 异步任务id
	 *
	 */
	public String getJobid() {
		return jobid;
	}

	/**
	 * 设定异步任务id的值
	 * 
	 * @param jobid
	 *            异步任务id
	 * 
	 */
	public void setJobid(String jobid) {
		this.jobid = jobid;
	}

	/**
	 * 取得执行数量的值
	 * 
	 * @return 执行数量
	 *
	 */
	public int getZxsl() {
		return zxsl;
	}

	/**
	 * 设定执行数量的值
	 * 
	 * @param zxsl
	 *            执行数量
	 * 
	 */
	public void setZxsl(int zxsl) {
		this.zxsl = zxsl;
	}

	/**
	 * 取得微信企业号ID的值
	 * 
	 * @return 微信企业号ID
	 *
	 */
	public String getWxqyhId() {
		return wxqyhId;
	}

	/**
	 * 设定微信企业号ID的值
	 * 
	 * @param wxqyhId
	 *            微信企业号ID
	 * 
	 */
	public void setWxqyhId(String wxqyhId) {
		this.wxqyhId = wxqyhId;
	}
}
