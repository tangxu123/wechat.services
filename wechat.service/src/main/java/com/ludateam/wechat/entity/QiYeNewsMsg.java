package com.ludateam.wechat.entity;

/**
 * 企业新闻消息
 *
 * @author
 */
public class QiYeNewsMsg extends QiYeBaseMsg {

	/**
	 * 新闻内容
	 */
	private News news;

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

}