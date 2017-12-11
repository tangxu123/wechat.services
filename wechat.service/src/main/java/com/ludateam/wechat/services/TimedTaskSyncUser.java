package com.ludateam.wechat.services;

import java.util.ArrayList;
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
import com.ludateam.wechat.dto.UserListDto;
import com.ludateam.wechat.entity.TaxOfficerEntity;
import com.ludateam.wechat.entity.UserEntity;
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
	@Scheduled(cron = "0 0/30 * * * ?")
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
		String weburl = requestHost + "/wechat/replaceuser";
		
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
	@Scheduled(cron = "0-59/5 0-1,30-31 * * * ?")
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
	@Scheduled(cron = "0 30 8 * * ?")
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
			String weburl = requestHost + "/wechat/replaceuser";
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
	
	/**
	 * 回写徐汇专管员号、徐汇税务号通讯录成员的关注状态
	 * 
	 * */
	@Scheduled(cron = "0 0/5 * * * ?")
	public void executeFollowStatus() {
		
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-type", "application/json");
		try {
			String xhzgyHost = PropertyUtil.getProperty("1");
			String xhzgyWeburl = xhzgyHost + "/wechat/user/simplelist";
			String xhzgyParam = makeRequestParam("7");
			String xhzgyResult = HttpKit.post(xhzgyWeburl, xhzgyParam, headers);
			UserListDto xhzgyUserDto = JSON.parseObject(xhzgyResult,UserListDto.class);
			if ("0".equals(xhzgyUserDto.getErrcode())) {
				List<UserEntity> userList = xhzgyUserDto.getUserlist();
				int count = searchDao.updateXhzgyFollowStatus(getUseridList(userList));
				logger.info("XHZGY Follow Status Callback--" + count);
			}
			
			String xhswHost = PropertyUtil.getProperty("2");
			String xhswWeburl = xhswHost + "/wechat/user/simplelist";
			String xhswParam = makeRequestParam("1");
			String xhswResuslt = HttpKit.post(xhswWeburl, xhswParam, headers);
			UserListDto xhswUserDto = JSON.parseObject(xhswResuslt,UserListDto.class);
			if ("0".equals(xhswUserDto.getErrcode())) {
				List<UserEntity> userList = xhzgyUserDto.getUserlist();
				int count = searchDao.updateXhswFollowStatus(getUseridList(userList));
				logger.info("XHSW Follow Status Callback--" + count);
			}
		} catch (Exception e) {
			logger.info("execute Follow Status Callback Exception---" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取部门成员
	 * 
	 * @param departmentId
	 *            获取的部门id
	 * @param fetch_child
	 *            1/0：是否递归获取子部门下面的成员
	 * @param status
	 *            0获取全部成员，1获取已关注成员列表，2获取禁用成员列表，4获取未关注成员列表
	 *            
	 * @return 获取部门成员参数
	 */
	private String makeRequestParam(String departmentId) {
		return "{\"department_id\":\"" + departmentId + "\",\"fetch_child\":\"1\",\"status\":\"1\"}";
	}
	
	/**
	 * 提取部门成员的成员id
	 * 
	 * @param userList
	 *            成员列表
	 * 
	 * @return 成员id列表
	 */
	private List<String> getUseridList(List<UserEntity> userList) {
		List<String> useridList = new ArrayList<String>();
		for (UserEntity userEntity : userList) {
			useridList.add(userEntity.getUserid());
		}
		return useridList;
	}
}
