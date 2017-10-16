package com.ludateam.wechat.api;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

/**
 * Created by lenovo on 2017/10/12.
 */
@Service("wechatPayService")
@Path("wechatpay")
@Produces({"application/json; charset=UTF-8", "text/xml; charset=UTF-8"})
public interface WechatPayService {
    @POST
    @Path("/account/{username}")
    String getAccountByUserName(@PathParam("username") String username);

    @POST
    @Path("/account")
    int updateAccountLastLogin(@QueryParam("id") int id);

    @POST
    @Path("/account/zsd/{username}")
    String getZsd(@PathParam("username") String username);

    @POST
    @Path("/skorder/{Jksbh}")
    String findSKorder(@PathParam("Jksbh") String Jksbh);

    @POST
    @Path("/skorder")
    String findSKorderByOrderId(@QueryParam("orderid") String orderid);

    @POST
    @Path("/skorder/result")
    String findSKorderByOrderIdAndResult(@QueryParam("orderid") String orderid);

    @POST
    @Path("/skorder/add")
    int addSkOrder(@Context HttpServletRequest request);

    @POST
    @Path("/query")
    String querySql(@Context HttpServletRequest request);

    @POST
    @Path("/skorder/fail")
    String findSKorderByOrderIdfail(@QueryParam("orderid") String orderid);

    @POST
    @Path("/skorder/update")
    int updateSKOrderZf(@Context HttpServletRequest request);

    @POST
    @Path("/skorder/findbycon")
    String findSKorderByCon(@Context HttpServletRequest request);

    @POST
    @Path("/skdetails/add")
    public int insertSKDetails(@Context HttpServletRequest request);
}
