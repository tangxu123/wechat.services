package com.ludateam.wechat.entity;

public class Article {

	public Article() {

	}

	/** 标题 */
	private String title = "";
	/** 描述 */
	private String description = "";
	/** 点击后跳转的链接 */
	private String url = "";
	/** 图文消息的图片链接 */
	private String picurl = "";

	public Article(String title, String description, String url, String picurl) {
		this.title = title;
		this.description = description;
		this.url = url;
		this.picurl = picurl;
	}

	/**
	 * 取得标题的值
	 * 
	 * @return 标题
	 *
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设定标题的值
	 * 
	 * @param title
	 *            标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 取得描述的值
	 * 
	 * @return 描述
	 *
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设定描述的值
	 * 
	 * @param description
	 *            描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 取得点击后跳转的链接的值
	 * 
	 * @return 点击后跳转的链接。
	 *
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设定点击后跳转的链接的值
	 * 
	 * @param url
	 *            点击后跳转的链接。
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 取得图文消息的图片链接的值
	 * 
	 * @return 图文消息的图片链接
	 *
	 */
	public String getPicurl() {
		return picurl;
	}

	/**
	 * 设定图文消息的图片链接的值
	 * 
	 * @param picurl
	 *            图文消息的图片链接
	 */
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

}
