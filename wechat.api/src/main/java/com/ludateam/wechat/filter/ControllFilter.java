package com.ludateam.wechat.filter;
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

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;

public class ControllFilter implements ContainerResponseFilter {

    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        if (containerRequestContext.getMethod().equals("OPTIONS")) {
            containerResponseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
            containerResponseContext.getHeaders().add("Access-Control-Allow-Headers", "Content-Type,x-requested-with,Authorization,Access-Control-Allow-Origin");
            containerResponseContext.getHeaders().add("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
            containerResponseContext.getHeaders().add("Access-Control-Max-Age" ,"360");
        }
    }
}