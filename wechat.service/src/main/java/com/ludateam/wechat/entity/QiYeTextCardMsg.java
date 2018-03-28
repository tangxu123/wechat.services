package com.ludateam.wechat.entity;

/**
 * 企业文本卡片消息
 *
 * @author Javen
 */
public class QiYeTextCardMsg extends QiYeBaseMsg {

	/**
	 * 消息内容
	 */
	private TextCard textcard;

	public TextCard getTextcard() {
		return textcard;
	}

	public void setTextcard(TextCard textcard) {
		this.textcard = textcard;
	}

}