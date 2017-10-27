package com.ludateam.wechat.dao;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface SearchDao {
	/**
	 * 保存企业微信接收的消息
	 */
	int saveReseiceMsg(Map msgMap);

	/**
	 * 取得发送任务列表
	 */
	List<Map<String, Object>> getTaskList();

	/**
	 * 通过任务id取得对应发送名单
	 */
	List<Map<String, Object>> getSendListByTaskid(BigDecimal rwid);

	/**
	 * 更新发送名单表的微信账号信息
	 */
	int updateWechatInfo(Map paramMap);

	/**
	 * 更新任务发送状态
	 */
	int updateTaskStatus(Map paramMap);

	Map getAccountByUserName(String accountUserName);

	int updateAccountLastLogin(int id);

	Map getZsd(String accountUserName);

	Map findSKorder(String jksbh);

	Map findSKorderByOrderId(String orderid);

	Map findSKorderByOrderIdAndResult(String orderid);

	Map findSKorderByOrderIdfail(String orderid);

	int addskorder(Map skorderMap);

	List<LinkedHashMap<String, Object>> querySQL(String sqlContent);

	int updateSKOrderZf(Map paramMap);

	Map findSKorderByCon(Map paramMap);

	int insertSKDetails(Map paramMap);

	int updateSQL(Map paramMap);
}
