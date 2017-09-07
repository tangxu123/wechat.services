package com.ludateam.wechat.dao;

import java.util.Map;

public interface SearchDao {
	/** 保存企业微信接受的消息 */
    int saveReseiceMsg(Map msgMap);
}
