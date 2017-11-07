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

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * @author Him
 */
@Component
public class LudaMessageHandler implements ChannelAwareMessageListener {


    @RabbitListener(queues = "myQueue")
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        System.out.println("myQueue1:" + message);
        System.out.println("myQueue1:" + new String(message.getBody()));

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
