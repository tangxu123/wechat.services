package com.ludateam.wechat.entity;

import java.math.BigDecimal;

public class FsrwEntity {

	/** 任务ID */
	private BigDecimal rwid;
	/** 所别 */
	private String sbdm;
	/** 审核属性 */
	private String shsx;
	/** 条目ID */
	private int tmid;
	/** 短信内容 */
	private String dxnr;
	/** 落款 */
	private String lk;
	/** 发送对象 */
	private String fsdx;
	/** 有效期（天） */
	private int yxq;
	/** 内网标志 */
	private String nwbz;
	/** 人员代码 */
	private String rydm;
	/** 发送方式 */
	private String fsfs;

	/**
	 * 取得任务ID的值
	 * 
	 * @return 任务ID
	 *
	 */
	public BigDecimal getRwid() {
		return rwid;
	}

	/**
	 * 设定任务ID的值
	 * 
	 * @param rwid
	 *            任务ID
	 */
	public void setRwid(BigDecimal rwid) {
		this.rwid = rwid;
	}

	/**
	 * 取得所别的值
	 * 
	 * @return 所别
	 *
	 */
	public String getSbdm() {
		return sbdm;
	}

	/**
	 * 设定所别的值
	 * 
	 * @param sbdm
	 *            所别
	 */
	public void setSbdm(String sbdm) {
		this.sbdm = sbdm;
	}

	/**
	 * 取得审核属性的值
	 * 
	 * @return 审核属性
	 *
	 */
	public String getShsx() {
		return shsx;
	}

	/**
	 * 设定审核属性的值
	 * 
	 * @param shsx
	 *            审核属性
	 */
	public void setShsx(String shsx) {
		this.shsx = shsx;
	}

	/**
	 * 取得条目ID的值
	 * 
	 * @return 条目ID
	 *
	 */
	public int getTmid() {
		return tmid;
	}

	/**
	 * 设定条目ID的值
	 * 
	 * @param tmid
	 *            条目ID
	 */
	public void setTmid(int tmid) {
		this.tmid = tmid;
	}

	/**
	 * 取得短信内容的值
	 * 
	 * @return 短信内容
	 *
	 */
	public String getDxnr() {
		return dxnr;
	}

	/**
	 * 设定短信内容的值
	 * 
	 * @param dxnr
	 *            短信内容
	 */
	public void setDxnr(String dxnr) {
		this.dxnr = dxnr;
	}

	/**
	 * 取得落款的值
	 * 
	 * @return 落款
	 *
	 */
	public String getLk() {
		return lk;
	}

	/**
	 * 设定落款的值
	 * 
	 * @param lk
	 *            落款
	 */
	public void setLk(String lk) {
		this.lk = lk;
	}

	/**
	 * 取得发送对象的值
	 * 
	 * @return 发送对象
	 *
	 */
	public String getFsdx() {
		return fsdx;
	}

	/**
	 * 设定发送对象的值
	 * 
	 * @param fsdx
	 *            发送对象
	 */
	public void setFsdx(String fsdx) {
		this.fsdx = fsdx;
	}

	/**
	 * 取得有效期（天）的值
	 * 
	 * @return 有效期（天）
	 *
	 */
	public int getYxq() {
		return yxq;
	}

	/**
	 * 设定有效期（天）的值
	 * 
	 * @param yxq
	 *            有效期（天）
	 */
	public void setYxq(int yxq) {
		this.yxq = yxq;
	}

	/**
	 * 取得内网标志的值
	 * 
	 * @return 内网标志
	 *
	 */
	public String getNwbz() {
		return nwbz;
	}

	/**
	 * 设定内网标志的值
	 * 
	 * @param nwbz
	 *            内网标志
	 */
	public void setNwbz(String nwbz) {
		this.nwbz = nwbz;
	}

	/**
	 * 取得人员代码的值
	 * 
	 * @return 人员代码
	 *
	 */
	public String getRydm() {
		return rydm;
	}

	/**
	 * 设定人员代码的值
	 * 
	 * @param rydm
	 *            人员代码
	 */
	public void setRydm(String rydm) {
		this.rydm = rydm;
	}

	/**
	 * 取得发送方式的值
	 * 
	 * @return 发送方式
	 *
	 */
	public String getFsfs() {
		return fsfs;
	}

	/**
	 * 设定发送方式的值
	 * 
	 * @param fsfs
	 *            发送方式
	 */
	public void setFsfs(String fsfs) {
		this.fsfs = fsfs;
	}
}
