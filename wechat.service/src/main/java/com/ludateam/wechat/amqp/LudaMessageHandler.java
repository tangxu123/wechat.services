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
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.ludateam.wechat.dto.MqJsonDto;
import com.ludateam.wechat.dto.SendMsgResultDto;
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
        //channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    
	@RabbitListener(queues = "Q_SMS")
	public void onSmsMessage(Message message, Channel channel) throws Exception {
		logger.info("output sms message start");
		String sendParam = new String(message.getBody());
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-type", "application/json");
        String weburl = PropertyUtil.getProperty("web.url") + "/wechat/sms/send";
		String result = HttpKit.post(weburl, sendParam, headers);
		logger.info("sms--message--send--result----" + result);
		
		Map resultMap = JSON.parseObject(result, Map.class);
		String status = "";
		String msgGroup = (String) resultMap.get("msgGroup");
		String rwid = (String) resultMap.get("rwId");
		String sjhm = (String) resultMap.get("sjhm");
		
		if (msgGroup == null || "".equals(msgGroup)) {
			status = "5";
		} else {
			status = "4";
		}
		
		sendParam = "{\"status\":\"" + status + "\",\"msgId\":\"" + msgGroup
				+ "\",\"rwid\",\"" + rwid + "\",\"sjh\":\"" + sjhm + "\"}";
		String nmhurl = PropertyUtil.getProperty("nmhsjpt.url") + "/sendMsgToSms";
		result = HttpKit.post(nmhurl, sendParam, headers);
		logger.info("sms--message--send--result----callback---" + result);
		
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		logger.info("output sms message end");
	}

	@RabbitListener(queues = "Q_WEIXIN")
	public void onWeixinMessage(Message message, Channel channel)
			throws Exception {
		logger.info("output weixin message start");
		
		String mqjson = new String(message.getBody());
		MqJsonDto mqJsonDto = JSON.parseObject(mqjson, MqJsonDto.class);
		String rwid = mqJsonDto.getRwid();
		String wxzh = mqJsonDto.getWxzh();
		String content = mqJsonDto.getDxnr();
		 
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-type", "application/json");
		String weburl = PropertyUtil.getProperty("web.url") + "/wechat/qyapi/sendTextMessage";
		String nmhurl = PropertyUtil.getProperty("nmhsjpt.url") + "/sendMsgToSms";
		
		SendMsgResultDto resultDto = sendTextMessage(weburl, wxzh, content, headers);
		String uswxzh = resultDto.getInvaliduser();
		String sendParam = "{\"rwid\":\""+rwid+"\",\"wxzh\":\""+wxzh+"\",\"uswxzh\",\""+uswxzh+"\"}";
		String result = HttpKit.post(nmhurl, sendParam, headers);
		logger.info("sms--message--send--result--callback--" + result);
		
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		logger.info("output weixin message end");
	}
	
	/**
	 * 发送文本信息
	 * 
	 * @param weburl
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
	private SendMsgResultDto sendTextMessage(String weburl, String userid,
			String content, HashMap<String, String> headers) {
		String sendParam = "{\"touser\" : \""
				+ userid.replace(",", "|")
				+ "\",\"toparty\" : \"\",\"totag\" : \"\","
				+ "\"msgtype\" : \"text\",\"agentid\" : 4,\"text\" : {\"content\" : \""
				+ content + "\"},\"safe\":0}";
		logger.info(sendParam);
		String resultJson = HttpKit.post(weburl, sendParam, headers);
		logger.info(resultJson);
		SendMsgResultDto resultDto = JSON.parseObject(resultJson,
				SendMsgResultDto.class);
		return resultDto;
	}
}
