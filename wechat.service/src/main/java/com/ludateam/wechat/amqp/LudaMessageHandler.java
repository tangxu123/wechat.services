package com.ludateam.wechat.amqp;

/*
 * Copyright 2017 Luda Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Created by Him on 2017/11/2.
 */

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.ludateam.wechat.dto.MqJsonDto;
import com.ludateam.wechat.dto.SendMsgResultDto;
import com.ludateam.wechat.entity.QiYeTextMsg;
import com.ludateam.wechat.entity.SmsRequestParam;
import com.ludateam.wechat.entity.Text;
import com.ludateam.wechat.kit.HttpKit;
import com.ludateam.wechat.utils.PropertyUtil;
import com.rabbitmq.client.Channel;

/**
 * @author Him
 */
@Component
public class LudaMessageHandler implements ChannelAwareMessageListener {
    private static Logger logger = Logger.getLogger(LudaMessageHandler.class);


    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        System.out.println("myQueue1:" + message);
        System.out.println("myQueue1:" + new String(message.getBody()));
    }

    
	@RabbitListener(queues = "Q_SMS")
	public void onSmsMessage(Message message, Channel channel) throws Exception {
		
		logger.info("sms--message--send--yun--mars--start--");
		String sendParam = new String(message.getBody());
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-type", "application/json");
        String weburl = PropertyUtil.getProperty("web.url") + "/wechat/sms/send";
		String nmhurl = PropertyUtil.getProperty("nmhsjpt.url") + "/sendMsgToSms";
		String result = "";
		String status = "";
		String msgGroup = "";
		String rwid = "";
		String sjhm = "";
		try {
			result = HttpKit.post(weburl, sendParam, headers);
			Map resultMap = JSON.parseObject(result, Map.class);
			msgGroup = (String) resultMap.get("msgGroup");
			rwid = (String) resultMap.get("rwId");
			sjhm = (String) resultMap.get("sjhm");
		} catch (Exception e) {
			logger.info("sms--message--send--yun--mars--error--happened--");
		}

		sjhm = "'" + sjhm.replace(",", "','") + "'";
		if (msgGroup == null || "".equals(msgGroup)) {
			status = "5";
		} else {
			status = "4";
		}
		
		SmsRequestParam requestParam = new SmsRequestParam();
		requestParam.setRwid(rwid);
		requestParam.setMsgId(msgGroup);
		requestParam.setSjh(sjhm);
		requestParam.setStatus(status);
		try {
			String postJson = JSON.toJSONString(requestParam);
			result = HttpKit.post(nmhurl, postJson, headers);
		} catch (Exception e) {
			logger.info("sms--message--send--result--callback--error--happened--");
		}
		logger.info("sms--message--send--yun--mars--end--");
	}

	@RabbitListener(queues = "Q_WEIXIN")
	public void onWeixinMessage(Message message, Channel channel) throws Exception {
		
		logger.info("send--weixin--message--start--");
		
		String mqjson = new String(message.getBody());
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-type", "application/json");
		
		try {
			MqJsonDto mqJsonDto = JSON.parseObject(mqjson, MqJsonDto.class);
			String rwid = mqJsonDto.getRwid();
			String wxzh = mqJsonDto.getWxzh();
			String content = mqJsonDto.getDxnr();
			
			SendMsgResultDto resultDto = sendTextMessage(mqJsonDto, headers);
			String uswxzh = resultDto.getInvaliduser();
			if (uswxzh != null && !"".equals(uswxzh)) {
				uswxzh = "'" + uswxzh.replace(",", "','") + "'";
			}

			wxzh = "'" + wxzh.replace(",", "','") + "'";
			String nmhurl = PropertyUtil.getProperty("nmhsjpt.url") + "/sendMsgToWeChat";
			String sendParam = "{\"rwid\":\"" + rwid + "\",\"wxzh\":\"" + wxzh + "\",\"uswxzh\":\"" + uswxzh + "\"}";
			String result = HttpKit.post(nmhurl, sendParam, headers);
			logger.info("wechat--message--send--result--callback--");
		} catch (Exception e) {
			logger.info("send--text--message--exception--happened");
		}
		logger.info("send--weixin--message--end--");
	}
	
	/**
	 * 发送文本信息
	 * 
	 * @param mqJsonDto
	 *            队列消息
	 * @param headers
	 *            请求报文头部
	 * 
	 * @return 发送结果
	 */
	private SendMsgResultDto sendTextMessage(MqJsonDto mqJsonDto,
			HashMap<String, String> headers) {
		
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
			logger.info(resultJson);
			resultDto = JSON.parseObject(resultJson, SendMsgResultDto.class);
		} catch (Exception e) {
			logger.info("send--text--message--exception--happened");
		}
		return resultDto;
	}
}
