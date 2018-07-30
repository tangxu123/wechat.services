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
import org.springframework.web.bind.annotation.PathVariable;

import javax.ws.rs.*;

@Service("calendarService")
@Produces({"application/json; charset=UTF-8", "text/xml; charset=UTF-8"})
@Path("calendar")
public interface CalendarService {
	@GET
	@Path("/meeting/meetings")
	String meetings(@QueryParam("userid") String userid,@QueryParam("rysx") String rysx,@QueryParam("jgdm") String jgdm);

	@GET
	@Path("/meeting/designatedPersons")
	String designatedPersons(@QueryParam("meetingNumber") String meetingNumber, @QueryParam("userid") String userid);

	@GET
	@Path("/meeting/persons")
	String meetingPersons(@QueryParam("persons") String persons,@QueryParam("lrrydm") String lrrydm);

	@GET
	@Path("/meeting/desc")
	String meetingDesc(@QueryParam("meetingNumber") String meetingNumber, @QueryParam("userid") String userid);

	@GET
	@Path("/meeting/rysx")
	String rysx(@QueryParam("userid") String userid);

	@GET
	@Path("/meeting/ownerMeetings")
	String ownerMeetings(@QueryParam("userid") String userid,@QueryParam("rysx") String rysx);
}
