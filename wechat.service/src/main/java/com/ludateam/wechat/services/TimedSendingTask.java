package com.ludateam.wechat.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ludateam.wechat.amqp.MessageSenderHandler;
import com.ludateam.wechat.dao.SearchDao;
import com.ludateam.wechat.dto.TaskListDto;
import com.ludateam.wechat.entity.FsmdEntity;
import com.ludateam.wechat.entity.FsrwEntity;
import com.ludateam.wechat.entity.SssxTzsEntity;
import com.ludateam.wechat.entity.TaskEntity;
import com.ludateam.wechat.kit.SpringBeanKit;
import com.ludateam.wechat.kit.StrKit;

@Component
public class TimedSendingTask {

	/** 发送方式：短信 */
	private static final String SEND_METHOD_SMS = "1";
	/** 发送方式：微信 */
	private static final String SEND_METHOD_WECHAT = "2";
	
	
	private static Logger logger = Logger.getLogger(TimedSendingTask.class);

	@Autowired
	private SearchDao searchDao;

	/**
	 * 发送短信任务（多手机号码合并成一条）
	 * 
	 */
	@Scheduled(cron = "0 0/1 * * * ?")
	public void executeSmsMultiple() {

		TaskEntity entity = new TaskEntity();
		entity.setTmid(BigDecimal.ZERO);
		entity.setFsfs(SEND_METHOD_SMS);
		List<TaskEntity> taskList = searchDao.getTaskList(entity);

		if (taskList == null || taskList.size() == 0) {
			logger.info("暂无数据-----发送方式=sms-----条目id=0");
			return;
		}
		
		addTaskToQueue(taskList, SEND_METHOD_SMS);
	}
	
	/**
	 * 发送微信消息任务（多微信张合并成一条）
	 * 
	 */
	@Scheduled(cron = "0 0/1 * * * ?")
	public void executeWechatMultiple() {

		TaskEntity entity = new TaskEntity();
		entity.setTmid(BigDecimal.ZERO);
		entity.setFsfs(SEND_METHOD_WECHAT);
		List<TaskEntity> taskList = searchDao.getTaskList(entity);

		if (taskList == null || taskList.size() == 0) {
			logger.info("暂无数据-----发送方式=wechat-----条目id=0");
			return;
		}

		addTaskToQueue(taskList, SEND_METHOD_WECHAT);
	}
	
	/**
	 * 发送短信任务（单条）
	 * 
	 */
	@Scheduled(cron = "0 0/1 * * * ?")
	public void executeSmsSingle() {

		TaskEntity entity = new TaskEntity();
		entity.setTmid(BigDecimal.ONE);
		entity.setFsfs(SEND_METHOD_SMS);
		List<TaskEntity> taskList = searchDao.getTaskList(entity);

		if (taskList == null || taskList.size() == 0) {
			logger.info("暂无数据-----发送方式=sms-----条目id>0");
			return;
		}
		
		MessageSenderHandler messageSender = (MessageSenderHandler) SpringBeanKit
				.getBean("messageSenderHandler");
		for (int i = 0; i < taskList.size(); i++) {
			TaskEntity entityObj = taskList.get(i);
			String mqJson = makeMqJson(entityObj.getRwid(),
					entityObj.getSjhm(), entityObj.getDxnr(), SEND_METHOD_SMS);
			messageSender.sendSmsMessage(mqJson);
		}
	}
	
	/**
	 * 发送微信消息任务（单条）
	 * 
	 */
	@Scheduled(cron = "0 0/1 * * * ?")
	public void executeWechatSingle() {

		TaskEntity entity = new TaskEntity();
		entity.setTmid(BigDecimal.ONE);
		entity.setFsfs(SEND_METHOD_WECHAT);
		List<TaskEntity> taskList = searchDao.getTaskList(entity);

		if (taskList == null || taskList.size() == 0) {
			logger.info("暂无数据-----发送方式=wechat-----条目id>0");
			return;
		}
		
		MessageSenderHandler messageSender = (MessageSenderHandler) SpringBeanKit
				.getBean("messageSenderHandler");
		for (int i = 0; i < taskList.size(); i++) {
			TaskEntity entityObj = taskList.get(i);
			String mqJson = makeMqJson(entityObj.getRwid(),
					entityObj.getSjhm(), entityObj.getDxnr(),
					SEND_METHOD_WECHAT);
			messageSender.sendWechatMessage(mqJson);
		}
	}
	
	/**
	 * 事项通知书制件完成
	 * 
	 */
	@Scheduled(cron = "0 30 10,14 * * ?")
	public void executeSxtzs() {

		List<SssxTzsEntity> tzsList = searchDao.getTzsList();
		if (tzsList == null || tzsList.size() == 0) {
			logger.info("暂无制件完成的工作流通知书");
			return;
		}

		FsrwEntity fsrwEntity = new FsrwEntity();
		fsrwEntity.setDxnr(null);
		fsrwEntity.setFsdx("0");
		fsrwEntity.setFsfs(SEND_METHOD_WECHAT);
		fsrwEntity.setLk("");
		fsrwEntity.setNwbz("0");
		fsrwEntity.setRydm("");
		fsrwEntity.setSbdm("");
		fsrwEntity.setShsx("1");
		fsrwEntity.setTmid(99);
		fsrwEntity.setYxq(0);
		fsrwEntity.setQyhid(1);
		fsrwEntity.setWxyyid(4);
		
		searchDao.saveFsrw(fsrwEntity);
		
		for (int i = 0; i < tzsList.size(); i++) {
			SssxTzsEntity tzsEntity = tzsList.get(i);
			String wxzhid = tzsEntity.getWxzhid();
			if (StrKit.isBlank(wxzhid)) {
				continue;
			} else {
				String content = tzsEntity.getNsrmc() + ":（纳税人识别号："
						+ tzsEntity.getShxydm() + "）\n你单位于"
						+ tzsEntity.getSqsj() + "申请办理的" + tzsEntity.getSssxMc()
						+ "事项（文书号：" + tzsEntity.getWsh() + "），结果通知书已经制发完成。";

				BigDecimal rwid = fsrwEntity.getRwid();
				String djxh = tzsEntity.getDjxh();
				List<String> wxzhidList = removeDuplicateWxzhid(wxzhid);
				for (int j = 0; j < wxzhidList.size(); j++) {
					FsmdEntity fsmdEntity = new FsmdEntity();
					fsmdEntity.setRwid(rwid);
					fsmdEntity.setDxnr(content);
					fsmdEntity.setFsr(djxh);
					fsmdEntity.setFssx("0");
					fsmdEntity.setSjhm(wxzhidList.get(j));
					fsmdEntity.setWxzhid(wxzhidList.get(j));
					searchDao.saveFsmd(fsmdEntity);
				}
				searchDao.updateTzsStatus(tzsEntity.getWsh());
			}
		}
	}

	/**
	 * 催报催缴消息
	 * 
	 */
	@Scheduled(cron = "0 30 9 10 * ?")
	public void executeCbcj() {
		
		searchDao.deleteCbcjTmpData();
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		String strTjnd = String.valueOf(year) + String.valueOf(month);
		int tjnd = Integer.parseInt(strTjnd);
		searchDao.saveCbcjTmpData(tjnd);
		List<SssxTzsEntity> wsbList = searchDao.getWsbList();
		if (wsbList == null || wsbList.size() == 0) {
			logger.info("暂无催报催缴信息");
			return;
		}
		
		for (int i = 0; i < wsbList.size(); i++) {
			SssxTzsEntity tzsEntity = wsbList.get(i);
			String wxzhid = tzsEntity.getWxzhid();
			if (StrKit.isBlank(wxzhid)) {
				continue;
			} else {
				String content = tzsEntity.getNsrmc() + ":（纳税人识别号："
						+ tzsEntity.getShxydm() + "）\n你单位于" + strTjnd
						+ "有如下税种需要申报：" + tzsEntity.getSssxMc()
						+ "，请在规定的申报期内申报纳税。";
				logger.info(content);
				FsrwEntity fsrwEntity = new FsrwEntity();
				fsrwEntity.setDxnr(content);
				fsrwEntity.setFsdx("0");
				fsrwEntity.setFsfs(SEND_METHOD_WECHAT);
				fsrwEntity.setLk("【徐汇税务局】");
				fsrwEntity.setNwbz("0");
				fsrwEntity.setRydm(tzsEntity.getSlryDm());
				fsrwEntity.setSbdm(tzsEntity.getZgswskfjDm());
				fsrwEntity.setShsx("0");
				fsrwEntity.setTmid(99);
				fsrwEntity.setYxq(0);
				searchDao.saveFsrw(fsrwEntity);

				BigDecimal rwid = fsrwEntity.getRwid();
				String djxh = tzsEntity.getDjxh();
				List<String> wxzhidList = removeDuplicateWxzhid(wxzhid);
				for (int j = 0; j < wxzhidList.size(); j++) {
					FsmdEntity fsmdEntity = new FsmdEntity();
					fsmdEntity.setRwid(rwid);
					fsmdEntity.setDxnr(content);
					fsmdEntity.setFsr(djxh);
					fsmdEntity.setFssx("0");
					fsmdEntity.setSjhm(wxzhidList.get(j));
					fsmdEntity.setWxzhid(wxzhidList.get(j));
					searchDao.saveFsmd(fsmdEntity);
				}
				//searchDao.updateTzsStatus(tzsEntity.getWsh());
			}
		}
		
		searchDao.deleteCbcjTmpData();
	}
	
	/**
	 * 根据任务id分隔任务列表
	 * 
	 * @param taskList
	 *            任务列表
	 * 
	 * @return 任务列表
	 */
	private List<TaskListDto> splitTaskListByTaskid(List<TaskEntity> taskList) {

		BigDecimal rwidIndex = taskList.get(0).getRwid();
		List<TaskEntity> subTaskList = new ArrayList<TaskEntity>();
		List<TaskListDto> taskDtoList = new ArrayList<TaskListDto>();
		for (int i = 0; i < taskList.size(); i++) {
			TaskEntity entityObj = taskList.get(i);
			if (rwidIndex.compareTo(entityObj.getRwid()) == 0) {
				subTaskList.add(entityObj);
			} else {
				TaskListDto dto = new TaskListDto();
				dto.setRwid(rwidIndex);
				dto.setTaskList(subTaskList);
				taskDtoList.add(dto);

				// 重置任务id（任务id变化了）
				rwidIndex = entityObj.getRwid();
				// 重置任务子列表（任务id变化了）
				subTaskList = new ArrayList<TaskEntity>();
				subTaskList.add(entityObj);
			}
		}

		TaskListDto dto = new TaskListDto();
		dto.setRwid(rwidIndex);
		dto.setTaskList(subTaskList);
		taskDtoList.add(dto);

		return taskDtoList;
	}

	/**
	 * 将任务添加到相应的队列中
	 * 
	 * @param rwid
	 *            任务id
	 * @param fsdx
	 *            发送对象
	 * @param dxnr
	 *            短信内容
	 * @param sendMethod
	 *            发送方式
	 * 
	 * @return 消息队列json
	 */
	private String makeMqJson(BigDecimal rwid, String fsdx, String dxnr,
			String sendMethod) {
		String mqjson = "";
		if (SEND_METHOD_SMS.equals(sendMethod)) {
			mqjson = "{\"rwid\":\"" + rwid + "\",\"sjhm\":\"" + fsdx
					+ "\",\"dxnr\":\"" + dxnr.replace("\"", "\\\"") + "\"}";
		} else if (SEND_METHOD_WECHAT.equals(sendMethod)) {
			mqjson = "{\"rwid\":\"" + rwid + "\",\"wxzh\":\"" + fsdx
					+ "\",\"dxnr\":\"" + dxnr.replace("\"", "\\\"") + "\"}";
		}
		return mqjson;
	}
	
	/**
	 * 将任务添加到相应的队列中
	 * 
	 * @param rwid
	 *            任务列表
	 * @param sendMethod
	 *            发送方式
	 * 
	 * @return 无
	 */
	private void addTaskToQueue(List<TaskEntity> taskList, String sendMethod) {
		
		logger.info("发送方式：" + sendMethod + "-----条目id=0----" + taskList.size());
		List<TaskListDto> taskDtoList = splitTaskListByTaskid(taskList);
		logger.info("根据任务id分隔任务列表大小----" + taskDtoList.size());
		
		for (TaskListDto taskDto : taskDtoList) {
			BigDecimal rwid = taskDto.getRwid();
			List<TaskEntity> subTaskList = taskDto.getTaskList();
			logger.info("任务id----" + rwid + "----任务列表大小----" + subTaskList.size());
			
			int count = subTaskList.size() % 1000 == 0 ? subTaskList.size() / 1000 : subTaskList.size() / 1000 + 1;
			for (int m = 1; m <= count; m++) {
				String fsdx = "";
				String dxnr = "";
				for (int n = (m - 1) * 1000; n < subTaskList.size() && n < m * 1000; n++) {
					dxnr = subTaskList.get(n).getDxnr();
					if (SEND_METHOD_SMS.equals(sendMethod)) {
						fsdx += subTaskList.get(n).getSjhm() + ",";
					} else if (SEND_METHOD_WECHAT.equals(sendMethod)) {
						fsdx += subTaskList.get(n).getWxzhid() + ",";
					}
				}
				fsdx = fsdx.substring(0, fsdx.length() - 1);
				String mqJson = makeMqJson(rwid, fsdx, dxnr, sendMethod);
				logger.info("添加第" + m + "组1000条消息到队列开始");
				putQueue(mqJson, sendMethod);
				logger.info("添加第" + m + "组1000条消息到队列结束");
			}
		}
	}
	
	/**
	 * 将单条的任务添加到相应的队列中
	 * 
	 * @param mqJson
	 *            任务列表
	 * @param sendMethod
	 *            发送方式
	 * 
	 * @return 无
	 */
	private void putQueue(String mqJson, String sendMethod) {
		MessageSenderHandler messageSender = (MessageSenderHandler) SpringBeanKit
				.getBean("messageSenderHandler");
		if (SEND_METHOD_SMS.equals(sendMethod)) {
			messageSender.sendSmsMessage(mqJson);
		} else if (SEND_METHOD_WECHAT.equals(sendMethod)) {
			messageSender.sendWechatMessage(mqJson);
		}
	}
	
	/**
	 * 去除重复的微信账号id（场景：同一家企业法人、财务、办税员为同一人，避免同一人收到三条同意的消息）
	 * 
	 * @param wxzhid
	 *            微信账号id
	 * 
	 * @return 不重复的微信账号id
	 */
	private List<String> removeDuplicateWxzhid(String wxzhid) {
		String[] wxzhidArray = wxzhid.split(",");
		List<String> arrList = new ArrayList<String>();
		for (int i = 0; i < wxzhidArray.length; i++) {
			if (!arrList.contains(wxzhidArray[i]))
				arrList.add(wxzhidArray[i]);
		}
		return arrList;
	}
	
}
