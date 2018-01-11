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
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ludateam.wechat.dao.SearchDao;
import com.ludateam.wechat.dto.MqJsonDto;
import com.ludateam.wechat.dto.SendStatusDto;

@Service
public class MessageSenderHandler extends CommonServiceHandler {

	private static Logger logger = Logger.getLogger(MessageSenderHandler.class);

	@Resource(name = "xhAmqpTemplate")
	protected AmqpTemplate amqpTemplate;
	
    @Autowired
    protected SearchDao searchDao;
    
	/**
	 * 发送短信消息
	 * 
	 * @param message
	 *            消息内容
	 * 
	 * */
	public void sendSmsMessage(String message) {
		logger.info("sms--message--add--queue--start----");
		amqpTemplate.convertAndSend("MSG_SEND_EX", "FSFS=1", message);
		logger.info("sms--message--add--queue--finish----");
		try {
			MqJsonDto mqJsonDto = JSON.parseObject(message, MqJsonDto.class);
			List<String> sjhmList = splitSendList(mqJsonDto.getSjhm());
			SendStatusDto sendParam = new SendStatusDto();
			sendParam.setRwid(mqJsonDto.getRwid());
			sendParam.setFssx(DXFSZT_ADD_QUEUE);
			sendParam.setSjhmList(sjhmList);
			int count = searchDao.updateSmsStatus(sendParam);
			logger.info("sms--message--add--queue--result----" + count);
		} catch (Exception e) {
			logger.info("sms--message--add--queue--error--happened--");
			e.printStackTrace();
		}
	}

	/**
	 * 发送微信消息
	 * 
	 * @param message
	 *            消息内容
	 * 
	 * */
	public void sendWechatMessage(String message) {
		
		logger.info("wechat--message--add--queue--start----");
		amqpTemplate.convertAndSend("MSG_SEND_EX", "FSFS=2", message);
		logger.info("wechat--message--add--queue--finish----");

		try {
			MqJsonDto mqJsonDto = JSON.parseObject(message, MqJsonDto.class);
			List<String> wxzhidList = splitSendList(mqJsonDto.getWxzh());
			SendStatusDto sendParam = new SendStatusDto();
			sendParam.setRwid(mqJsonDto.getRwid());
			sendParam.setFssx(DXFSZT_ADD_QUEUE);
			sendParam.setWxzhidList(wxzhidList);
			int count = searchDao.updateWechatSendStatus(sendParam);
			logger.info("wechat--message--add--queue--result----" + count);
		} catch (Exception e) {
			logger.info("wechat--message--add--queue--error--happened--");
			e.printStackTrace();
		}
	}

}
