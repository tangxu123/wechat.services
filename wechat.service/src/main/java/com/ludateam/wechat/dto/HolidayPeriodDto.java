package com.ludateam.wechat.dto;

import java.util.Date;

/** 节假日（期间） */
public class HolidayPeriodDto {

	/** 节假日起始日期 */
	private Date startDate;
	/** 节假日终止日期 */
	private Date endDate;

	/**
	 * 取得节假日起始日期的值
	 * 
	 * @return 节假日起始日期
	 *
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * 设定节假日起始日期的值
	 * 
	 * @param startDate
	 *            节假日起始日期
	 * 
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * 取得节假日终止日期的值
	 * 
	 * @return 节假日终止日期
	 *
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * 设定节假日终止日期的值
	 * 
	 * @param endDate
	 *            节假日终止日期
	 * 
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
