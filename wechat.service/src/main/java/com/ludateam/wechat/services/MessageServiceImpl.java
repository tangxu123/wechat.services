package com.ludateam.wechat.services;/*
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
 * Created by Him on 2017/8/17.
 */

import com.alibaba.fastjson.JSONObject;
import com.ludateam.wechat.api.MessageService;
import com.ludateam.wechat.entity.User;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.ludateam.wechat.kit.HttpKit;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service("messageService")
@Path("message")
@Produces({"application/json; charset=UTF-8", "text/xml; charset=UTF-8"})
public class MessageServiceImpl implements MessageService {
    @GET
    @Path("/sendTextMessage/")
    public User sendTextMessage(String data) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("test", "2");
        String json = JSONObject.toJSONString(map);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-type","application/json");
        HttpKit.post("http://127.0.0.1:8899/wechat/qyapi/sendTextMessage", json,headers);
        return null;
    }
}
