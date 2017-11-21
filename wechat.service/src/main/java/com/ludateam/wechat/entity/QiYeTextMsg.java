package com.ludateam.wechat.entity;

/**
 * 企业文本消息
 *
 * @author Javen
 */
public class QiYeTextMsg extends QiYeBaseMsg {

	/**
	 * 消息内容
	 */
	private Text text;

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

}