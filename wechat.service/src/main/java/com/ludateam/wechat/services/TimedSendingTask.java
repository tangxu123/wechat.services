package com.ludateam.wechat.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.ludateam.wechat.dao.SearchDao;
import com.ludateam.wechat.dto.ResultDto;
import com.ludateam.wechat.dto.UserListDto;
import com.ludateam.wechat.entity.UserEntity;
import com.ludateam.wechat.kit.HttpKit;
import com.ludateam.wechat.kit.StrKit;
import com.ludateam.wechat.utils.PropertyUtil;

@Component
public class TimedSendingTask {

	/** 已激活 */
	private static final String STATUS_ACTIVE = "1";
	/** 已禁用 */
	private static final String STATUS_DISABLED = "2";
	/** 未激活 */
	private static final String STATUS_INACTIVE = "4";
	/** 发送状态：发送成功 */
	private static final String SEND_STATUS_SUCCESS = "0";
	/** 发送状态：发送失败 */
	private static final String SEND_STATUS_FAILURE = "1";
	
	private static Logger logger = Logger.getLogger(TimedSendingTask.class);

	@Autowired
	private SearchDao searchDao;

	@Scheduled(cron = "0 0/2 * * * ?")
	public void execute() {

		String requestHost = PropertyUtil.getProperty("web.url");
		String weburl = requestHost + "/wechat/user/list";
		logger.info("post URL:" + weburl);

		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-type", "application/json");

		// 获取的部门id 1:徐汇专管员号 3:上海中软
		String sendParam = "{\"department_id\":\"3\",\"fetch_child\":\"1\",\"status\":\"1\"}";
		String result = HttpKit.post(weburl, sendParam, headers);
		UserListDto userDto = JSON.parseObject(result, UserListDto.class);
		if (userDto == null) {
			logger.error("解析部门成员详情结果发生错误");
			return;
		}

		if (!"0".equals(userDto.getErrcode())) {
			logger.error("获取部门成员详情失败，错误代码：" + userDto.getErrcode() + " 错误原因：" + userDto.getErrmsg());
			return;
		}

		if (!CollectionUtils.isEmpty(userDto.getUserlist())) {
			List<UserEntity> userList = userDto.getUserlist();
			HashMap<String, String> map = new HashMap<String, String>();
			for (UserEntity user : userList) {
				if (StrKit.notBlank(user.getMobile())) {
					map.put(user.getMobile(), user.getUserid());
				}
				logger.info("name:" + user.getName() + "||userid:" + user.getUserid());
			}

			List<Map<String, Object>> taskList = searchDao.getTaskList();
			if (CollectionUtils.isEmpty(taskList)) {
				logger.error("任务列表为空");
				return;
			}
			for (int i = 0; i < taskList.size(); i++) {
				Map<String, Object> taskMap = taskList.get(i);
				logger.info(taskMap);
				BigDecimal rwid = (BigDecimal) taskMap.get("rwid");
				String dxnr = (String) taskMap.get("dxnr");
				boolean allUserSendSuccess = true;
				List<Map<String, Object>> sendList = searchDao.getSendListByTaskid(rwid);
				for (int j = 0; j < sendList.size(); j++) {
					Map<String, Object> userMap = sendList.get(j);
					logger.info(userMap);
					String sjhm = (String)userMap.get("sjhm");
					String wxzhid = (String)userMap.get("wxzhid");
					//String jhzt = (String)userMap.get("jhzt");
					
					if(StrKit.notBlank(wxzhid)){
						logger.info("此用户已经发送过消息了");
					}else{
						if (map.containsKey(sjhm)) {
							wxzhid = map.get(sjhm);
							if (sendTextMessage(requestHost, wxzhid, dxnr, headers)) {
								updateWechatInfo(wxzhid, STATUS_ACTIVE, rwid, sjhm);
								logger.info("发送成功");
							}else{
								allUserSendSuccess = false;
								logger.error("发送失败");
							}
						}else{
							// 该手机号码暂未激活企业微信
							allUserSendSuccess = false;
							updateWechatInfo(wxzhid, STATUS_INACTIVE, rwid, sjhm);
						}
					}
				}
				
				if (allUserSendSuccess) {
					updateTaskStatus(SEND_STATUS_SUCCESS, "success", rwid);
					logger.info("All user send success");
				} else {
					updateTaskStatus(SEND_STATUS_FAILURE, "Include inactive users", rwid);
					logger.info("Include inactive users");
				}
			}
		}else{
			logger.error("部门成员列表为空");
		}
	}
	
	/**
	 * 更新微信账号信息
	 * 
	 * @param wxzhid
	 *            微信账号id
	 * @param jhzt
	 *            集合状态
	 * @param rwid
	 *            任务id
	 * @param sjhm
	 *            手机号码
	 * 
	 * @return 无
	 */
	private void updateWechatInfo(String wxzhid, String jhzt, BigDecimal rwid,
			String sjhm) {
		Map paramMap = new HashMap();
		paramMap.put("wxzhid", wxzhid);
		paramMap.put("jhzt", jhzt);
		paramMap.put("rwid", rwid);
		paramMap.put("sjhm", sjhm);
		searchDao.updateWechatInfo(paramMap);
	}
	
	/**
	 * 更新任务发送状态
	 * 
	 * @param fszt
	 *            发送状态
	 * @param sbyy
	 *            失败原因
	 * @param rwid
	 *            任务id
	 * 
	 * @return 无
	 */
	private void updateTaskStatus(String fszt, String sbyy, BigDecimal rwid) {
		Map paramMap = new HashMap();
		paramMap.put("fszt", fszt);
		paramMap.put("sbyy", sbyy);
		paramMap.put("rwid", rwid);
		searchDao.updateTaskStatus(paramMap);
	}
	
	/**
	 * 发送文本信息
	 * 
	 * @param requestHost
	 *            请求服务器
	 * @param userid
	 *            用户id
	 * @param content
	 *            信息内容
	 * @param headers
	 *            请求报文头部
	 * 
	 * @return 发送结果
	 */
	private boolean sendTextMessage(String requestHost, String userid,
			String content, HashMap<String, String> headers) {
		String weburl = requestHost + "/wechat/qyapi/sendTextMessage";
		String sendParam = "{\"touser\" : \"" + userid + "\",\"toparty\" : \"\",\"totag\" : \"\","
				+ "\"msgtype\" : \"text\",\"agentid\" : 4,\"text\" : {\"content\" : \"" + content + "\"},\"safe\":0}";
		System.out.println(sendParam);
		String resultJson = HttpKit.post(weburl, sendParam, headers);
		ResultDto userDto = JSON.parseObject(resultJson, ResultDto.class);
		return "0".equals(userDto.getErrcode());
	}
}
