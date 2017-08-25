package com.ludateam.wechat.services;

import com.ludateam.wechat.kit.HttpKit;
import com.ludateam.wechat.utils.PropertyUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.HashMap;

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
 * Created by Him on 2017/8/25.
 */
@Service("ludaService")
@Produces({"application/json; charset=UTF-8", "text/xml; charset=UTF-8"})
@Path("service")
public class CallServiceImpl implements com.ludateam.wechat.api.CallService {
    private static Logger logger = Logger.getLogger(CallServiceImpl.class);

    @POST
    @Path("/callService")
    public String callService(@QueryParam("subUrl") String subUrl, @Context HttpServletRequest request) {
        String send_param = HttpKit.readData(request);
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-type", "application/json");


        String weburl = PropertyUtil.getProperty("web.url") + subUrl;
        logger.info("post " + send_param + " to " + weburl);
        String result = HttpKit.post(weburl, send_param, headers);
        return result;
    }
}
