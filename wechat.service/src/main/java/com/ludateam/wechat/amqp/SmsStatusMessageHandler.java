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
 * Created by Him on 2017/11/8.
 */

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.ludateam.wechat.dao.SearchDao;
import com.ludateam.wechat.dto.SendStatusDto;
import com.ludateam.wechat.entity.SmsStatus;
import com.rabbitmq.client.Channel;

/**
 * @author Him
 */
@Component
public class SmsStatusMessageHandler extends CommonServiceHandler implements
		ChannelAwareMessageListener {
	
	@Autowired
	protected SearchDao searchDao;

	private static Logger logger = Logger.getLogger(SmsStatusMessageHandler.class);

    @Override
    @RabbitListener(queues = "Q_SMS_STATUS")
    public void onMessage(Message message, Channel channel) throws Exception {
        String jsonData = new String(message.getBody());
        logger.info("======================= get original  json string from mq : " + jsonData);
        try {
            jsonData = StringEscapeUtils.unescapeJava(jsonData);
            jsonData = jsonData.substring(1, jsonData.length() - 1);
            SmsStatus smsStatus = JSON.parseObject(jsonData, SmsStatus.class, Feature.AllowSingleQuotes);
			List<String> sjhmList = splitSendList(smsStatus.getMobile());
			SendStatusDto dto = new SendStatusDto();
			dto.setRwid("");
			dto.setMsgGroup(smsStatus.getMsgGroup());
			dto.setSjhmList(sjhmList);
			if ("DELIVRD".equals(smsStatus.getErrorCode())) {
				dto.setFssx(DXFSZT_SUCCESS);
			} else {
				dto.setFssx(DXFSZT_FAILURE);
			}

			int count = searchDao.updateSmsSendStatus(dto);
			logger.info("sms--send--status : " + count);
		} catch (Exception ex) {
			logger.error("conver json to object error : ", ex);
		}
    }
}
