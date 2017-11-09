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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.ludateam.wechat.entity.SmsStatus;
import com.ludateam.wechat.kit.HttpKit;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SmsStatusMessageHandler implements ChannelAwareMessageListener {
    private static Logger logger = Logger.getLogger(SmsStatusMessageHandler.class);

    @Override
    @RabbitListener(queues = "Q_SMS_STATUS")
    public void onMessage(Message message, Channel channel) throws Exception {
        String jsonData = new String(message.getBody());
        logger.info("======================= get original  json string from mq : " + jsonData);
        SmsStatus smsStatus = new SmsStatus();
        try {
            jsonData = StringEscapeUtils.unescapeJava(jsonData);
            jsonData = jsonData.substring(1, jsonData.length() - 1);
            smsStatus = JSON.parseObject(jsonData, SmsStatus.class, Feature.AllowSingleQuotes);
        } catch (Exception ex) {
            logger.error("conver json to object error : ", ex);
        }

        logger.info("======================= get smsStatus  : " + smsStatus.getMobile());

        Map<String, String> param = new HashMap<String, String>();

        //status ,msgId,rwid,sjh
        if ("DELIVRD".equals(smsStatus.getErrorCode())) {
            param.put("status", "1");
        } else {
            param.put("status", "2");
        }
        param.put("rwid", "");
        param.put("sjh", smsStatus.getMobile());
        param.put("msgId", smsStatus.getMsgGroup());

        try {
            HttpKit.get("http://172.16.200.253:8888/sendMsgToSms", param);
        } catch (Exception ex) {
            logger.error("call sms status url error : ", ex);
        }
        //channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
