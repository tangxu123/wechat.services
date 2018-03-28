package com.ludateam.wechat.entity;

import java.util.List;

public class News {

	private List<Article> articles;

	public News(List<Article> articles) {
		this.articles = articles;
	}

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

}
