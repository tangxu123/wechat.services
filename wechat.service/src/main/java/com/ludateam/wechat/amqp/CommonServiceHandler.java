package com.ludateam.wechat.amqp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.ludateam.wechat.dto.MqJsonDto;
import com.ludateam.wechat.dto.SendMsgResultDto;
import com.ludateam.wechat.entity.QiYeTextMsg;
import com.ludateam.wechat.entity.Text;
import com.ludateam.wechat.kit.HttpKit;
import com.ludateam.wechat.utils.PropertyUtil;

public class CommonServiceHandler {

	private static Logger logger = Logger.getLogger(CommonServiceHandler.class);
	/** 短信发送状态: 等待发送 */
	public static final String DXFSZT_WAIT = "0";
	/** 短信发送状态: 发送成功 */
	public static final String DXFSZT_SUCCESS = "1";
	/** 短信发送状态 :发送失败 */
	public static final String DXFSZT_FAILURE = "2";
	/** 短信发送状态: 发送至队列 */
	public static final String DXFSZT_ADD_QUEUE = "3";
	/** 短信发送状态:已发送至云MAS */
	public static final String DXFSZT_YMAS_SUCCESS = "4";
	/** 短信发送状态: 发送云MAS失败 */
	public static final String DXFSZT_YMAS_FAILURE = "5";

	/**
	 * 拆分发送对象成集合
	 * 
	 * @param sendUserList
	 *            发送对象
	 * 
	 * */
	public List<String> splitSendList(String sendUserList) {
		List<String> sendList = new ArrayList<String>();
		String[] array = sendUserList.split(",");
		for (int i = 0; i < array.length; i++) {
			sendList.add(array[i]);
		}
		return sendList;
	}

	/**
	 * 发送文本信息
	 * 
	 * @param mqJsonDto
	 *            队列消息
	 * 
	 * @return 发送结果
	 */
	public SendMsgResultDto sendTextMessage(MqJsonDto mqJsonDto) {
		
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-type", "application/json");

		int qyhid = mqJsonDto.getQyhid();
		String weburl = PropertyUtil.getProperty(String.valueOf(qyhid)) + "/wechat/qyapi/sendTextMessage";
		String userid = mqJsonDto.getWxzh();
		String content = mqJsonDto.getDxnr();
		String agentid = String.valueOf(mqJsonDto.getWxyyid());

		QiYeTextMsg textMsg = new QiYeTextMsg();
		textMsg.setTouser(userid.replace(",", "|"));
		textMsg.setToparty("");
		textMsg.setTotag("");
		textMsg.setMsgtype("text");
		textMsg.setAgentid(agentid);
		textMsg.setText(new Text(content));
		textMsg.setSafe("0");

		SendMsgResultDto resultDto = null;
		try {
			String sendParam = JSON.toJSONString(textMsg);
			String resultJson = HttpKit.post(weburl, sendParam, headers);
			logger.info("send param:" + sendParam);
			logger.info("send result:" + resultJson);
			resultDto = JSON.parseObject(resultJson, SendMsgResultDto.class);
		} catch (Exception e) {
			logger.info("send--text--message--exception--happened");
			e.printStackTrace();
		}
		return resultDto;
	}
}
