package com.ludateam.wechat.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ludateam.wechat.dto.SyncUserJobDto;
import com.ludateam.wechat.dto.SyncUserJobResultDto;
import com.ludateam.wechat.entity.FsmdEntity;
import com.ludateam.wechat.entity.FsrwEntity;
import com.ludateam.wechat.entity.SssxTzsEntity;
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
	 * 取得实名办税员列表(同步企业微信通讯录)
	 */
	List<TaxOfficerEntity> getRealNameTaxOfficerList();

	/**
	 * 同步未停用实名办税员与企业的对照关系
	 */
	int updateEnableRelation();

	/**
	 * 同步未停用实名办税员与企业的对照关系
	 */
	int updateDisableRelation();

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

	/**
	 * 取得发送任务列表
	 */
	List<SssxTzsEntity> getTzsList();

	/**
	 * 保存发送任务
	 */
	int saveFsrw(FsrwEntity entity);

	/**
	 * 保存发送名单
	 */
	int saveFsmd(FsmdEntity entity);

	/**
	 * 更新通知书状态
	 */
	int updateTzsStatus(String wsh);

	
	/**
	 * 取得发送任务列表
	 */
	List<SssxTzsEntity> getWsbList();
	
	
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
