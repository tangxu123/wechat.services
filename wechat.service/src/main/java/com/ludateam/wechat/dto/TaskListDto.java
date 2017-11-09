package com.ludateam.wechat.dto;

import java.math.BigDecimal;
import java.util.List;

import com.ludateam.wechat.entity.TaskEntity;

public class TaskListDto {

	/** 任务id */
	private BigDecimal rwid;
	/** 任务列表 */
	private List<TaskEntity> taskList;

	/**
	 * 取得任务id的值
	 * 
	 * @return 任务id
	 *
	 */
	public BigDecimal getRwid() {
		return rwid;
	}

	/**
	 * 设定任务id的值
	 * 
	 * @param rwid
	 *            任务id
	 */
	public void setRwid(BigDecimal rwid) {
		this.rwid = rwid;
	}

	public List<TaskEntity> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<TaskEntity> taskList) {
		this.taskList = taskList;
	}
}
