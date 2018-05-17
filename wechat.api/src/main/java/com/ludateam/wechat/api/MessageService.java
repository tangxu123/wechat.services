package com.ludateam.wechat.api;


import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

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
 * Created by Him on 2017/8/17.
 */
@Service("messageService")
@Path("message")
@Produces({"application/json; charset=UTF-8", "text/xml; charset=UTF-8"})
public interface MessageService {

    /**
     * 发送文本消息
     *
     * @param request 请求消息
     * @return 发送结果
     */
    @POST
    @Path("/sendTextMessage")
    String sendTextMessage(@Context HttpServletRequest request);


    /**
     * 发送文件或图片消息
     * @param request
     * @param target 内网 or 外网
     * @param type image or file
     * @return
     */
    @POST
    @Path("/sendMediaMessage")
    String sendMediaMessage(@Context HttpServletRequest request, @QueryParam("target") String target, @QueryParam("type") String type);

    /**
     * 接受消息（文本消息、图片消息等）
     *
     * @param msgJson 请求内容
     * @return 存储结果
     */
    @POST
    @Path("/receiveMessage")
    String receiveMessage(@QueryParam("msgJson") String msgJson);

    /**
     * 保存系统发送给应用的消息
     *
     * @param msgJson 请求内容
     * @return 存储结果
     */
    @POST
    @Path("/saveSystemMessage")
    String saveSystemMessage(@QueryParam("msgJson") String msgJson);
}
