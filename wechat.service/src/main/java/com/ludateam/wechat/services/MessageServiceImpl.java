package com.ludateam.wechat.services;

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
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ludateam.wechat.api.CallService;
import com.ludateam.wechat.dao.SearchDao;
import com.ludateam.wechat.dto.BindingResult;
import com.ludateam.wechat.dto.RecordWechatDto;
import com.ludateam.wechat.entity.BindingEntity;
import com.ludateam.wechat.kit.HttpKit;
import com.ludateam.wechat.utils.PropertyUtil;


@Service("messageService")
@Path("message")
@Produces({"application/json; charset=UTF-8", "text/xml; charset=UTF-8"})
public class MessageServiceImpl implements com.ludateam.wechat.api.MessageService   {

    private static Logger logger = Logger.getLogger(MessageServiceImpl.class);

    @Autowired
    private SearchDao searchDao;
    
    @Autowired
    private CallService callService;
    
    @Override
    @POST
    @Path("/sendTextMessage")
    public String sendTextMessage(@Context HttpServletRequest request) {

        String send_param = HttpKit.readData(request);
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-type", "application/json");

        String weburl = PropertyUtil.getProperty("web.url") + "/wechat/qyapi/sendTextMessage";
        logger.info("post " + send_param + " to " + weburl);

        String result = HttpKit.post(weburl, send_param, headers);
        return result;
    }

	@Override
    @POST
	@Path("/receiveMessage")
	public String receiveMessage(@QueryParam("msgJson") String msgJson) {
		logger.info("post receive message：" + msgJson);
		try {
			Map msgMap = (Map) JSON.parse(msgJson);
			String userid = (String) msgMap.get("fromUserName");
			String resultJson = callService.getBindingList(userid);
			BindingResult bindResult = JSON.parseObject(resultJson, BindingResult.class);
			BigDecimal djxh = null;
			if ("0".equals(bindResult.getErrcode())) {
				List<BindingEntity> bindingList = bindResult.getBindingList();
				for (int i = 0; i < bindingList.size(); i++) {
					BindingEntity bindingEntity = bindingList.get(i);
					String strDjxh = bindingEntity.getDjxh();
					if ("Y".equals(bindingEntity.getIsUse())) {
						djxh = new BigDecimal(strDjxh);
						break;
					}
				}
			}
			msgMap.put("djxh", djxh);
			int count = searchDao.saveReseiceMsg(msgMap);
			logger.info("save message count：" + count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "{\"errcode\":\"0\",\"errmsg\":\"ok\"}";
	}

	@Override
	public String saveSystemMessage(String msgJson) {
		
		RecordWechatDto resultDto = JSON.parseObject(msgJson, RecordWechatDto.class);
		String resultJson = callService.getBindingList(resultDto.getWxzhid());
		BindingResult bindResult = JSON.parseObject(resultJson, BindingResult.class);
		String strDjxh = "";
		if ("0".equals(bindResult.getErrcode())) {
			List<BindingEntity> bindingList = bindResult.getBindingList();
			for (int i = 0; i < bindingList.size(); i++) {
				BindingEntity bindingEntity = bindingList.get(i);
				if ("Y".equals(bindingEntity.getIsUse())) {
					strDjxh = bindingEntity.getDjxh();
					break;
				}
			}
		}
		resultDto.setDjxh(strDjxh);
		searchDao.saveWechatSendRecord(resultDto);
		return "{\"errcode\":\"0\",\"errmsg\":\"ok\"}";
	}
}
