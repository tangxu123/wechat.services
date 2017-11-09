package com.ludateam.wechat.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ludateam.wechat.dto.SyncUserJobDto;
import com.ludateam.wechat.dto.SyncUserJobResultDto;
import com.ludateam.wechat.entity.TaskEntity;
import com.ludateam.wechat.entity.TaxOfficerEntity;

public interface SearchDao {

	/**
	 * 保存企业微信接收的消息
	 */
	int saveReseiceMsg(Map msgMap);

	/**
	 * 取得发送任务列表
	 */
	List<TaskEntity> getTaskList(TaskEntity entity);

	/**
	 * 取得实名办税员列表
	 */
	List<TaxOfficerEntity> getRealNameTaxOfficerList();

	/**
	 * 更新企业办税员实名对照关系
	 */
	int updateRealNameRelation();

	/**
	 * 保存同步用户任务信息
	 */
	int saveSyncUserJob(SyncUserJobDto jobDto);

	/**
	 * 保存同步用户任务的结果信息
	 */
	int saveSyncUserJobResult(SyncUserJobResultDto jobResultDto);

	/**
	 * 取得jobid列表
	 */
	List<String> getJobidList();

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
