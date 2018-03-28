package com.ludateam.wechat.entity;

public class TextCard {

	public TextCard() {

	}

	/** 标题 */
	private String title;
	/** 描述 */
	private String description;
	/** 点击后跳转的链接 */
	private String url;
	/** 按钮文字 */
	private String btntxt;

	public TextCard(String title, String description, String url, String btntxt) {
		this.title = title;
		this.description = description;
		this.url = url;
		this.btntxt = btntxt;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBtntxt() {
		return btntxt;
	}

	public void setBtntxt(String btntxt) {
		this.btntxt = btntxt;
	}

}
