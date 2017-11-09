package com.ludateam.wechat.amqp;/*
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

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ludateam.wechat.dto.MqJsonDto;
import com.ludateam.wechat.kit.HttpKit;
import com.ludateam.wechat.utils.PropertyUtil;

@Service
public class MessageSenderHandler {

	private static Logger logger = Logger.getLogger(MessageSenderHandler.class);

	@Resource(name = "xhAmqpTemplate")
	protected AmqpTemplate amqpTemplate;

	/**
	 * 发送短信消息
	 * 
	 * @param message
	 *            消息内容
	 * 
	 * */
	public void sendSmsMessage(String message) {
		
		amqpTemplate.convertAndSend("MSG_SEND_EX", "FSFS=1", message);
		
		MqJsonDto mqJsonDto = JSON.parseObject(message, MqJsonDto.class);
		String sendParam = "{\"status\":\"3\",\"msgId\":\"\",\"rwid\",\""
				+ mqJsonDto.getRwid() + "\",\"sjh\":\"" + mqJsonDto.getSjhm()
				+ "\"}";
		logger.info("sms--message--add--queue--param----" + sendParam);
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-type", "application/json");
        String weburl = PropertyUtil.getProperty("nmhsjpt.url") + "/sendMsgToSms";
		String result = HttpKit.post(weburl, sendParam, headers);
		logger.info("sms--message--add--queue--result----" + result);
		logger.info("sms--message--add--queue--finish----");
	}

	/**
	 * 发送微信消息
	 * 
	 * @param message
	 *            消息内容
	 * 
	 * */
	public void sendWechatMessage(String message) {
		
		amqpTemplate.convertAndSend("MSG_SEND_EX", "FSFS=2", message);

		MqJsonDto mqJsonDto = JSON.parseObject(message, MqJsonDto.class);
		String sendParam = "{\"rwid\",\"" + mqJsonDto.getRwid()
				+ "\",\"wxzh\":\"" + mqJsonDto.getWxzh() + "\"}";
		logger.info("wechat--message--add--queue--param----" + sendParam);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-type", "application/json");
		String weburl = PropertyUtil.getProperty("nmhsjpt.url") + "/sendWechatToMq";
		String result = HttpKit.post(weburl, sendParam, headers);
		logger.info("wechat--message--add--queue--result----" + result);
		logger.info("wechat--message--add--queue--finish----");
	}
}
