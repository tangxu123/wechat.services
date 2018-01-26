package com.ludateam.wechat.services;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ludateam.wechat.amqp.MessageSenderHandler;
import com.ludateam.wechat.dao.SearchDao;
import com.ludateam.wechat.dto.HolidayPeriodDto;
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
			logger.info("no data----sms----tmid=0");
			return;
		}
		
		addTaskToQueue(taskList, SEND_METHOD_SMS);
	}
	
	/**
	 * 发送微信消息任务（多微信张合并成一条）
	 * 
	 */
	@Scheduled(cron = "1 0/1 * * * ?")
	public void executeWechatMultiple() {

		TaskEntity entity = new TaskEntity();
		entity.setTmid(BigDecimal.ZERO);
		entity.setFsfs(SEND_METHOD_WECHAT);
		List<TaskEntity> taskList = searchDao.getTaskList(entity);

		if (taskList == null || taskList.size() == 0) {
			logger.info("no data----wechat----tmid=0");
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
			logger.info("no data----sms----tmid>0");
			return;
		}
		
		MessageSenderHandler messageSender = (MessageSenderHandler) SpringBeanKit
				.getBean("messageSenderHandler");
		for (int i = 0; i < taskList.size(); i++) {
			TaskEntity entityObj = taskList.get(i);
			String mqJson = makeMqJson(entityObj.getRwid(),
					entityObj.getSjhm(), entityObj.getDxnr(),
					entityObj.getQyhid(), entityObj.getWxyyid(),
					SEND_METHOD_SMS);
			messageSender.sendSmsMessage(mqJson);
		}
	}
	
	/**
	 * 发送微信消息任务（单条）
	 * 
	 */
	@Scheduled(cron = "1 0/1 * * * ?")
	public void executeWechatSingle() {

		TaskEntity entity = new TaskEntity();
		entity.setTmid(BigDecimal.ONE);
		entity.setFsfs(SEND_METHOD_WECHAT);
		List<TaskEntity> taskList = searchDao.getTaskList(entity);

		if (taskList == null || taskList.size() == 0) {
			logger.info("no data----wechat----tmid>0");
			return;
		}
		
		MessageSenderHandler messageSender = (MessageSenderHandler) SpringBeanKit
				.getBean("messageSenderHandler");
		for (int i = 0; i < taskList.size(); i++) {
			TaskEntity entityObj = taskList.get(i);
			String mqJson = makeMqJson(entityObj.getRwid(),
					entityObj.getWxzhid(), entityObj.getDxnr(),
					entityObj.getQyhid(), entityObj.getWxyyid(),
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

		for (int i = 0; i < tzsList.size(); i++) {
			SssxTzsEntity tzsEntity = tzsList.get(i);
			String wxzhid = tzsEntity.getWxzhid();
			if (StrKit.isBlank(wxzhid)) {
				continue;
			} else {
				String content = tzsEntity.getNsrmc() + "（纳税人识别号:"
						+ tzsEntity.getShxydm() + "）:\n于" + tzsEntity.getSqsj()
						+ "申请办理的" + tzsEntity.getSssxMc() + "事项（文书号："
						+ tzsEntity.getWsh() + "）结果通知书已经制发完成。";
				
				logger.info(content);
				FsrwEntity fsrwEntity = new FsrwEntity();
				fsrwEntity.setDxnr(content);
				fsrwEntity.setFsdx("0");
				fsrwEntity.setFsfs(SEND_METHOD_WECHAT);
				fsrwEntity.setLk("");
				fsrwEntity.setNwbz("0");
				fsrwEntity.setRydm(tzsEntity.getSlryDm());
				fsrwEntity.setSbdm(tzsEntity.getZgswskfjDm());
				fsrwEntity.setShsx("1");
				fsrwEntity.setTmid(0);
				fsrwEntity.setYxq(1);
				fsrwEntity.setQyhid(1);
				fsrwEntity.setWxyyid(14);
				searchDao.saveFsrw(fsrwEntity);
				
				BigDecimal rwid = fsrwEntity.getRwid();
				String djxh = tzsEntity.getDjxh();
				List<String> wxzhidList = removeDuplicateWxzhid(wxzhid);
				for (int j = 0; j < wxzhidList.size(); j++) {
					FsmdEntity fsmdEntity = new FsmdEntity();
					fsmdEntity.setRwid(rwid);
					fsmdEntity.setDxnr(null);
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
	 * 催报催缴数据提前作成
	 * 
	 */
	@Scheduled(cron = "0 20 9 * * ?")
	public void executeCbcjBefore() {
		logger.info("已申报数据统计开始");
		searchDao.getHxzgYsbtj();
		logger.info("已申报数据统计结束");
	}
	
	/**
	 * 催报催缴消息
	 * 
	 */
	@Scheduled(cron = "0 30 9 * * ?")
	public void executeCbcj() {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String today = format.format(new Date());
		String year = today.substring(0, 4);
		String month = today.substring(4, 6);
		String day = today.substring(6);
		String todayStr = year + "年" + month + "月" + day + "日";
		int tjnd = Integer.parseInt(year + month);
		boolean sendFlag = checkDate(year, month, day);
		if (!sendFlag) {
			return;
		}
		
		// 加工催报催缴数据到临时表中
		searchDao.getHxzgCbcj(tjnd);
		// 取得未申报的数据进行推送
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
				String content = tzsEntity.getNsrmc() + "（纳税人识别号:"
						+ tzsEntity.getShxydm() + "）:\n截止" + todayStr
						+ "，本征期有如下税种尚未申报：" + tzsEntity.getSssxMc()
						+ "，请在规定的申报期内申报纳税，如已申报请忽略。";
				logger.info(content);
				FsrwEntity fsrwEntity = new FsrwEntity();
				fsrwEntity.setDxnr(content);
				fsrwEntity.setFsdx("0");
				fsrwEntity.setFsfs(SEND_METHOD_WECHAT);
				fsrwEntity.setLk("");
				fsrwEntity.setNwbz("0");
				fsrwEntity.setRydm(tzsEntity.getSlryDm());
				fsrwEntity.setSbdm(tzsEntity.getZgswskfjDm());
				fsrwEntity.setShsx("1");
				fsrwEntity.setTmid(0);
				fsrwEntity.setYxq(1);
				fsrwEntity.setQyhid(1);
				fsrwEntity.setWxyyid(15);
				searchDao.saveFsrw(fsrwEntity);

				BigDecimal rwid = fsrwEntity.getRwid();
				String djxh = tzsEntity.getDjxh();
				List<String> wxzhidList = removeDuplicateWxzhid(wxzhid);
				for (int j = 0; j < wxzhidList.size(); j++) {
					FsmdEntity fsmdEntity = new FsmdEntity();
					fsmdEntity.setRwid(rwid);
					fsmdEntity.setDxnr(null);
					fsmdEntity.setFsr(djxh);
					fsmdEntity.setFssx("0");
					fsmdEntity.setSjhm(wxzhidList.get(j));
					fsmdEntity.setWxzhid(wxzhidList.get(j));
					searchDao.saveFsmd(fsmdEntity);
				}
			}
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
	 * @param qyhid
	 *            企业号ID
	 * @param wxyyid
	 *            微信应用ID
	 * @param sendMethod
	 *            发送方式
	 * 
	 * @return 消息队列json
	 */
	private String makeMqJson(BigDecimal rwid, String fsdx, String dxnr,
			int qyhid, int wxyyid, String sendMethod) {
		String mqjson = "";
		if (SEND_METHOD_SMS.equals(sendMethod)) {
			mqjson = "{\"rwid\":\"" + rwid + "\",\"sjhm\":\"" + fsdx + "\",\"dxnr\":\""
					+ dxnr.replace("\\", "\\\\").replace("\"", "\\\"") + "\"}";
		} else if (SEND_METHOD_WECHAT.equals(sendMethod)) {
			mqjson = "{\"rwid\":\"" + rwid + "\",\"wxzh\":\"" + fsdx
					+ "\",\"dxnr\":\"" + dxnr.replace("\\", "\\\\").replace("\"", "\\\"")
					+ "\",\"qyhid\":\"" + qyhid + "\",\"wxyyid\":\"" + wxyyid + "\"}";
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
				int qyhid = 0;
				int wxyyid = 0;
				for (int n = (m - 1) * 1000; n < subTaskList.size() && n < m * 1000; n++) {
					dxnr = subTaskList.get(n).getDxnr();
					qyhid = subTaskList.get(n).getQyhid();
					wxyyid = subTaskList.get(n).getWxyyid();
					if (SEND_METHOD_SMS.equals(sendMethod)) {
						fsdx += subTaskList.get(n).getSjhm() + ",";
					} else if (SEND_METHOD_WECHAT.equals(sendMethod)) {
						fsdx += subTaskList.get(n).getWxzhid() + ",";
					}
				}
				fsdx = fsdx.substring(0, fsdx.length() - 1);
				String mqJson = makeMqJson(rwid, fsdx, dxnr, qyhid, wxyyid, sendMethod);
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
	
	/**
	 * 判断当天是否是申报纳税期限截至日的三个工作日之内(含截至日当天)<br>
	 * 三个工作日之内给纳税人推送催报催缴信息，如果遇到周末或节假日则不计入统计
	 * 
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @param day
	 *            日
	 * 
	 * @return 是否推送
	 * 
	 */
	private boolean checkDate(String year, String month, String day) {
		boolean result = false;
		String thisMonth = year + "-" + month;
		String currMonth = year + month;
		String today = year + "-" + month + "-" + day;
		// 取得每月纳税申报期限
		String nssbqx = searchDao.getHxzgNssbqx(thisMonth);
		// 取得截至日期(含截止日期)前的3个工作日
		List<String> workdayList = getWorkdayList(nssbqx, currMonth, 3);
		if (!CollectionUtils.isEmpty(workdayList) && workdayList.contains(today)) {
			result = true;
		}
		return result;
	}
	
	/**
	 * 取得截至日期(含截止日期)前的若干个工作日
	 * 
	 * @param endDate
	 *            截止日期(yyyy-MM-dd)
	 * @param currMonth
	 *            当月日期字符串
	 * @param days
	 *            天数
	 * 
	 * @return 工作日集合
	 * 
	 */
	private List<String> getWorkdayList(String endDate, String currMonth,
			int days) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		List<String> workdayList = new ArrayList<String>();
		workdayList.add(endDate);

		List<HolidayPeriodDto> periodList = searchDao.getHolidayPeriodList(currMonth);
		List<String> holidayList = getAllHolidays(periodList);

		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(format.parse(endDate));
			for (int i = 0; i < 31; i++) {
				calendar.add(Calendar.DATE, -1);
				String varDate = format.format(calendar.getTime());
				// 这一天是节假日
				if (!holidayList.contains(varDate)) {
					workdayList.add(varDate);
					if (days == workdayList.size()) {
						break;
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return workdayList;
	}
	
	/**
	 * 取得所有节假日
	 * 
	 * @param periodList
	 *            节假日时间段列表
	 * 
	 * @return 所有节假日
	 * 
	 */
	private List<String> getAllHolidays(List<HolidayPeriodDto> periodList) {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		List<String> holidayList = new ArrayList<String>();
		
		try {
			for (HolidayPeriodDto periodDto : periodList) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(periodDto.getStartDate());
				for (int i = 0; i < 31; i++) {
					if (calendar.getTime().compareTo(periodDto.getEndDate()) < 0) {
						holidayList.add(format.format(calendar.getTime()));
						calendar.add(Calendar.DATE, 1);
					} else if (calendar.getTime().compareTo(periodDto.getEndDate()) == 0) {
						holidayList.add(format.format(calendar.getTime()));
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return holidayList;
	}
}
