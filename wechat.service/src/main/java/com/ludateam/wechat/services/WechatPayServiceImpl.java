package com.ludateam.wechat.services;

import com.alibaba.fastjson.JSON;
import com.ludateam.wechat.dao.SearchDao;
import com.ludateam.wechat.kit.HttpKit;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/10/12.
 */
@Service("wechatPayService")
@Path("wechatpay")
@Produces({"application/json; charset=UTF-8", "text/xml; charset=UTF-8"})
public class WechatPayServiceImpl implements com.ludateam.wechat.api.WechatPayService {
    private static Logger logger = Logger.getLogger(WechatPayServiceImpl.class);

    @Autowired
    private SearchDao searchDao;


    @Override
    @POST
    @Path("/account/{username}")
    public String getAccountByUserName(@PathParam("username") String username) {
        Map map = searchDao.getAccountByUserName(username);
        String jsonstring = JSON.toJSONString(map);
        return jsonstring;
    }

    @Override
    @POST
    @Path("/account")
    public int updateAccountLastLogin(@QueryParam("id") int id) {
        return searchDao.updateAccountLastLogin(id);
    }

    @Override
    @POST
    @Path("/account/zsd/{username}")
    public String getZsd(@PathParam("username") String username) {
        Map map = searchDao.getZsd(username);
        String jsonstring = "{}";
        if (null != map) {
            jsonstring = JSON.toJSONString(map);
        }

        return jsonstring;
    }

    @Override
    @POST
    @Path("/skorder/{jksbh}")
    public String findSKorder(@PathParam("jksbh") String jksbh) {
        Map map = searchDao.findSKorder(jksbh);
        String jsonstring = "{}";
        if (null != map) {
            jsonstring = JSON.toJSONString(map);
        }

        return jsonstring;
    }

    @Override
    @POST
    @Path("/skorder")
    public String findSKorderByOrderId(@QueryParam("orderid") String orderid) {
        Map map = searchDao.findSKorderByOrderId(orderid);
        String jsonstring = "{}";
        if (null != map) {
            jsonstring = JSON.toJSONString(map);
        }

        return jsonstring;
    }

    @Override
    @POST
    @Path("/skorder/result")
    public String findSKorderByOrderIdAndResult(@QueryParam("orderid") String orderid) {
        Map map = searchDao.findSKorderByOrderIdAndResult(orderid);
        String jsonstring = "{}";
        if (null != map) {
            jsonstring = JSON.toJSONString(map);
        }

        return jsonstring;
    }

    @Override
    @POST
    @Path("/skorder/add")
    public int addSkOrder(@Context HttpServletRequest request) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-type", "application/json");

        String send_param = HttpKit.readData(request);

        Map skordeMap = (Map) JSON.parse(send_param);

        int map = searchDao.addskorder(skordeMap);

        return 1;
    }

    @Override
    @POST
    @Path("/query")
    public String querySql(@Context HttpServletRequest request) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-type", "application/json");

        String send_param = HttpKit.readData(request);

        Map sqlmap = (Map) JSON.parse(send_param);

        List<LinkedHashMap<String, Object>> list = searchDao.querySQL(sqlmap.get("sql").toString());

        return JSON.toJSONString(list);
    }

    @Override
    @POST
    @Path("/skorder/fail")
    public String findSKorderByOrderIdfail(@QueryParam("orderid") String orderid) {
        Map map = searchDao.findSKorderByOrderIdfail(orderid);
        String jsonstring = "{}";
        if (null != map) {
            jsonstring = JSON.toJSONString(map);
        }

        return jsonstring;
    }

    @Override
    @POST
    @Path("/skorder/update")
    public int updateSKOrderZf(@Context HttpServletRequest request) {
        String send_param = HttpKit.readData(request);
        Map sqlmap = (Map) JSON.parse(send_param);
        return searchDao.updateSKOrderZf(sqlmap);
    }
    
    @Override
    @POST
    @Path("/update")
    public int updateSQL(@Context HttpServletRequest request) {
        String send_param = HttpKit.readData(request);
        Map sqlmap = (Map) JSON.parse(send_param);
        return searchDao.updateSQL(sqlmap);
    }
    @Override
    @POST
    @Path("/skorder/findbycon")
    public String findSKorderByCon(@Context HttpServletRequest request) {
        String send_param = HttpKit.readData(request);
        Map sqlmap = (Map) JSON.parse(send_param);
        Map map = searchDao.findSKorderByCon(sqlmap);
        String jsonstring = "{}";
        if (null != map) {
            jsonstring = JSON.toJSONString(map);
        }

        return jsonstring;
    }

    @Override
    @POST
    @Path("/skdetails/add")
    public int insertSKDetails(@Context HttpServletRequest request) {
        String send_param = HttpKit.readData(request);
        Map sqlmap = (Map) JSON.parse(send_param);
        return searchDao.insertSKDetails(sqlmap);
    }
}
