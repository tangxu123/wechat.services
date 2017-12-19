package com.ludateam.wechat.dto;

import java.util.List;

public class SyncUserJobResultDto extends SyncUserJobDto {

	/** 任务状态 1表示任务开始，2表示任务进行中，3表示任务已完成 */
	private int status;
	/** 操作类型 1. sync_user(增量更新成员) 2. replace_user(全量覆盖成员)3. replace_party(全量覆盖部门) */
	private String type;
	/** 任务运行总条数 */
	private int total;
	/** 目前运行百分比，当任务完成时为100 */
	private int percentage;
	/** 详细的处理结果 */
	private List<JobResultDto> result;
	

	/**
	 * 取得任务状态的值
	 * 
	 * @return 任务状态
	 *
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设定任务状态的值
	 * 
	 * @param status
	 *            任务状态
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 取得操作类型的值
	 * 
	 * @return 操作类型
	 *
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设定操作类型的值
	 * 
	 * @param type
	 *            操作类型
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 取得任务运行总条数的值
	 * 
	 * @return 任务运行总条数
	 *
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * 设定任务运行总条数的值
	 * 
	 * @param total
	 *            任务运行总条数
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * 取得目前运行百分比的值
	 * 
	 * @return 目前运行百分比
	 *
	 */
	public int getPercentage() {
		return percentage;
	}

	/**
	 * 设定目前运行百分比的值
	 * 
	 * @param percentage
	 *            目前运行百分比
	 */
	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	/**
	 * 取得详细的处理结果的值
	 * 
	 * @return 详细的处理结果
	 *
	 */
	public List<JobResultDto> getResult() {
		return result;
	}

	/**
	 * 设定详细的处理结果的值
	 * 
	 * @param result
	 *            详细的处理结果
	 */
	public void setResult(List<JobResultDto> result) {
		this.result = result;
	}
}
