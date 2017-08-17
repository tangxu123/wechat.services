package com.ludateam.wechat.services;


import com.ludateam.wechat.entity.User;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;

/**
 * @author Him
 * http://127.0.1.1:9009/user/getUserByPhone/18888888888
 */

@Service("userService")
@Path("user")
@Produces({"application/json; charset=UTF-8", "text/xml; charset=UTF-8"})
public class UserServiceImpl implements com.ludateam.wechat.api.UserService {

    @POST
    @Path("/getUserByPhone/{phone}/")
    public User getUserByPhone(@PathParam("phone") String phone) {
        try {
            //测试超时
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        User user = new User(11L, "ByPhone", phone);
        return user;
    }

    @GET
    @Path("/getUserByName/{name}/")
    public User getUserByName(@PathParam("name") String name) {
        User user = new User(11L, name, "18888888888");
        return user;
    }
}
