package com.ludateam.wechat.services;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.ludateam.wechat.dao.SearchDao;
import com.ludateam.wechat.dto.SyncUserJobDto;
import com.ludateam.wechat.dto.SyncUserJobResultDto;
import com.ludateam.wechat.entity.TaxOfficerEntity;
import com.ludateam.wechat.kit.HttpKit;
import com.ludateam.wechat.utils.PropertyUtil;

@Component
public class TimedTaskSyncUser {

	private static Logger logger = Logger.getLogger(TimedTaskSyncUser.class);

	@Autowired
	private SearchDao searchDao;

	/**
	 * 根据市局下放至徐汇分局的实名办税信息，采用增量更新的方式，定期同步企业微信通讯录，并把实名信息与企业绑定关系存入对照表中。
	 * */
	@Scheduled(cron = "0 30 9,11,13,15,17 * * ?")
	public void executeSyncNsUser() {

		List<TaxOfficerEntity> resultList = searchDao.getRealNameTaxOfficerList();
		String sendParam = "{\"content\" : \"姓名,帐号,微信号,手机号,邮箱,所在部门,职位\n";
		for (int i = 0; i < resultList.size(); i++) {
			TaxOfficerEntity entity = resultList.get(i);
			sendParam += entity.getMobile() + "," + entity.getUserid() + ","
					+ entity.getWxid() + "," + entity.getMobile() + ","
					+ entity.getEmail() + "," + entity.getDepartment() + ",\n";
		}
		sendParam += "\"}";

		logger.info("post sync user:" + sendParam);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-type", "application/json");
		String requestHost = PropertyUtil.getProperty("1");
		String weburl = requestHost + "/wechat/syncuser";
		
		try {
			String result = HttpKit.post(weburl, sendParam, headers);
			logger.info("sync user result:" + result);

			SyncUserJobDto jobDto = JSON.parseObject(result, SyncUserJobDto.class);
			if ("0".equals(jobDto.getErrcode())) {
				int count = searchDao.updateEnableRelation();
				logger.info("updateEnableRelation count:" + count);
				count = searchDao.updateDisableRelation();
				logger.info("updateDisableRelation count:" + count);
			}
			jobDto.setZxsl(resultList.size());
			jobDto.setWxqyhId("1");
			searchDao.saveSyncUserJob(jobDto);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 取得异步任务的执行结果（9:30:00 开始--9:31:59 每隔5秒监听一次）
	 * 
	 * */
	@Scheduled(cron = "0-59/5 30-31 9,11,13,15,17 * * ?")
	public void executeJobResult() {
		
		List<SyncUserJobDto> jobidList = searchDao.getJobidList();
		if (!CollectionUtils.isEmpty(jobidList)) {
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("Content-type", "application/json");
			for (int i = 0; i < jobidList.size(); i++) {
				SyncUserJobDto jobDto = jobidList.get(i);
				String jobid = jobDto.getJobid();
				String requestHost = PropertyUtil.getProperty(jobDto.getWxqyhId());
				String weburl = requestHost + "/wechat/batch/result";
				String sendParam = "{\"jobid\":\"" + jobid + "\"}";
				String result = HttpKit.post(weburl, sendParam, headers);
				logger.info("get batch result:" + result);
				SyncUserJobResultDto jobResultDto = JSON.parseObject(result,SyncUserJobResultDto.class);
				jobResultDto.setJobid(jobid);
				searchDao.saveSyncUserJobResult(jobResultDto);
			}
		}else{
			logger.info("no data ");
		}
	}
	
	/**
	 * 同步税务人员
	 * */
	@Scheduled(cron = "0 30 11,17 * * ?")
	public void executeSyncSwryUser() {

		List<TaxOfficerEntity> resultList = searchDao.getXhSwryList();
		String sendParam = "{\"content\" : \"姓名,帐号,微信号,手机号,邮箱,所在部门,职位\n";
		for (int i = 0; i < resultList.size(); i++) {
			TaxOfficerEntity entity = resultList.get(i);
			sendParam += entity.getName() + "," + entity.getUserid() + ","
					+ entity.getWxid() + "," + entity.getMobile() + ","
					+ entity.getEmail() + "," + entity.getDepartment() + ","
					+ entity.getPosition() + "\n";
		}
		sendParam += "\"}";

		try {
			logger.info("post sync user:" + sendParam);
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("Content-type", "application/json");
			String requestHost = PropertyUtil.getProperty("2");
			String weburl = requestHost + "/wechat/syncuser";
			String result = HttpKit.post(weburl, sendParam, headers);
			logger.info("sync user result:" + result);

			SyncUserJobDto jobDto = JSON.parseObject(result, SyncUserJobDto.class);
			if ("0".equals(jobDto.getErrcode())) {
				jobDto.setZxsl(resultList.size());
				jobDto.setWxqyhId("2");
				searchDao.saveSyncUserJob(jobDto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
