package com.ludateam.wechat.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ludateam.wechat.amqp.MessageSenderHandler;
import com.ludateam.wechat.dao.SearchDao;
import com.ludateam.wechat.dto.TaskListDto;
import com.ludateam.wechat.entity.TaskEntity;
import com.ludateam.wechat.kit.SpringBeanKit;

@Component
public class TimedSendingTask {

	/** 发送方式：短信 */
	private static final String SEND_METHOD_SMS = "1";
	/** 发送方式：微信 */
	private static final String SEND_METHOD_WECHAT = "2";
	
	
	private static Logger logger = Logger.getLogger(TimedSendingTask.class);

	@Autowired
	private SearchDao searchDao;

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
		
		for (int i = 0; i < taskList.size(); i++) {
			TaskEntity entityObj = taskList.get(i);
			String mqJson = makeMqJson(entityObj.getRwid(),
					entityObj.getSjhm(), entityObj.getDxnr(), SEND_METHOD_SMS);
			putQueue(mqJson, SEND_METHOD_SMS);
		}
	}
	
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

		for (int i = 0; i < taskList.size(); i++) {
			TaskEntity entityObj = taskList.get(i);
			String mqJson = makeMqJson(entityObj.getRwid(),
					entityObj.getSjhm(), entityObj.getDxnr(),
					SEND_METHOD_WECHAT);
			putQueue(mqJson, SEND_METHOD_WECHAT);
		}
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
					+ "\",\"dxnr\":\"" + dxnr + "\"}";
		} else if (SEND_METHOD_WECHAT.equals(sendMethod)) {
			mqjson = "{\"rwid\":\"" + rwid + "\",\"wxzh\":\"" + fsdx
					+ "\",\"dxnr\":\"" + dxnr + "\"}";
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
				logger.info(mqJson);
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
	
}
