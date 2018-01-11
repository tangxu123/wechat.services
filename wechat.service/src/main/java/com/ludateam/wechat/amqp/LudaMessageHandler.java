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
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.ludateam.wechat.dao.SearchDao;
import com.ludateam.wechat.dto.MqJsonDto;
import com.ludateam.wechat.dto.SendMsgResultDto;
import com.ludateam.wechat.dto.SendStatusDto;
import com.ludateam.wechat.kit.HttpKit;
import com.ludateam.wechat.utils.PropertyUtil;
import com.rabbitmq.client.Channel;

/**
 * @author Him
 */
@Component
public class LudaMessageHandler extends CommonServiceHandler implements
		ChannelAwareMessageListener {
	
    @Autowired
    protected SearchDao searchDao;
    
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
		
		try {
			String result = HttpKit.post(weburl, sendParam, headers);
			Map resultMap = JSON.parseObject(result, Map.class);
			String msgGroup = (String) resultMap.get("msgGroup");
			String rwid = (String) resultMap.get("rwId");
			String sjhm = (String) resultMap.get("sjhm");
			List<String> sjhmList = splitSendList(sjhm);
			SendStatusDto dto = new SendStatusDto();
			dto.setRwid(rwid);
			dto.setMsgGroup(msgGroup);
			dto.setSjhmList(sjhmList);
			if (msgGroup == null || "".equals(msgGroup)) {
				dto.setFssx(DXFSZT_YMAS_FAILURE);
			} else {
				dto.setFssx(DXFSZT_YMAS_SUCCESS);
			}
			
			int count = searchDao.updateSmsStatus(dto);
			logger.info("sms--message--send--yun--mars--result----" + count);
			logger.info("sms--message--send--yun--mars--end--");
		} catch (Exception e) {
			logger.info("sms--message--send--yun--mars--error--happened--");
			e.printStackTrace();
		}
	}

	@RabbitListener(queues = "Q_WEIXIN")
	public void onWeixinMessage(Message message, Channel channel) throws Exception {
		
		logger.info("send--weixin--message--start--");
		try {
			String mqjson = new String(message.getBody());
			MqJsonDto mqJsonDto = JSON.parseObject(mqjson, MqJsonDto.class);
			String rwid = mqJsonDto.getRwid();
			String wxzh = mqJsonDto.getWxzh();
			SendMsgResultDto resultDto = sendTextMessage(mqJsonDto);
			List<String> wxzhidList = splitSendList(wxzh);
			SendStatusDto sendParam = new SendStatusDto();
			sendParam.setRwid(rwid);
			sendParam.setWxzhidList(wxzhidList);
			if ("0".equals(resultDto.getErrcode())) {
				sendParam.setFssx(DXFSZT_SUCCESS);
				int count = searchDao.updateWechatSendStatus(sendParam);
				logger.info("send--weixin--message--success--" + count);
				String invaliduser = resultDto.getInvaliduser();
				if (invaliduser != null && !"".equals(invaliduser)) {
					List<String> invaliduserList = splitSendList(invaliduser);
					sendParam.setWxzhidList(invaliduserList);
					sendParam.setFssx(DXFSZT_FAILURE);
					count = searchDao.updateWechatSendStatus(sendParam);
					logger.info("send--weixin--message--failure--" + count);
				}
			} else {
				sendParam.setFssx(DXFSZT_FAILURE);
				int count = searchDao.updateWechatSendStatus(sendParam);
				logger.info("send--weixin--message--failure--" + count);
			}
		} catch (Exception e) {
			logger.info("send--text--message--exception--happened");
			e.printStackTrace();
		}
		logger.info("send--weixin--message--end--");
	}
}
