package com.ludateam.wechat.api;
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

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

@Service("ludaService")
@Produces({"application/json; charset=UTF-8", "text/xml; charset=UTF-8"})
@Path("service")
public interface CallService {
    @POST
    @Path("/callService")
    String callService(@QueryParam("subUrl") String subUrl, @QueryParam("target") String target, @Context HttpServletRequest request);
    
	@POST
	@Path("/getBindingList")
	String getBindingList(@QueryParam("userid") String userid);

	@POST
	@Path("/setDefault")
	String setDefaultCompany(@QueryParam("userid") String userid, @QueryParam("djxh") String djxh);
	
	@POST
	@Path("/getVipSqid")
	String getVipSqid(@QueryParam("userid") String userid);
	
	@POST
	@Path("/getSmbsSqid")
	String getSmbsSqid(@QueryParam("userid") String userid);

	@GET
	@Path("/meeting/meetings")
	String meetings(@QueryParam("userid") String userid,@QueryParam("rysx") String rysx);

	@GET
	@Path("/meeting/designatedPersons")
	String designatedPersons(@QueryParam("meetingNumber") String meetingNumber, @QueryParam("userid") String userid);

	@GET
	@Path("/meeting/persons")
	String meetingPersons(@QueryParam("persons") String persons);

	@GET
	@Path("/meeting/desc")
	String meetingDesc(@QueryParam("meetingNumber") String meetingNumber);

	@GET
	@Path("/meeting/rysx")
	String rysx(@QueryParam("userid") String userid);
	
}
