package com.ludateam.wechat.services;

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

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.ludateam.wechat.dao.SearchDao;
import com.ludateam.wechat.dto.ResponseResult;
import com.ludateam.wechat.kit.HttpKit;
import com.ludateam.wechat.utils.DesUtils;
import com.ludateam.wechat.utils.PropertyUtil;

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
@Produces({ "application/json; charset=UTF-8", "text/xml; charset=UTF-8" })
@Path("service")
public class CallServiceImpl implements com.ludateam.wechat.api.CallService {
	private static Logger logger = Logger.getLogger(CallServiceImpl.class);

	@Autowired
	private SearchDao searchDao;

	@Override
	@POST
	@Path("/callService")
	public String callService(@QueryParam("subUrl") String subUrl,
			@QueryParam("target") String target,
			@Context HttpServletRequest request) {
		String send_param = HttpKit.readData(request);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-type", "application/json");

		logger.info("get target: " + target);

		if (target == null) {
			return "{\"errcode\":9999,\"errmsg\":\"target is null\"}";
		}

		String weburl = PropertyUtil.getProperty(target) + subUrl;
		logger.info("post " + send_param + " to " + weburl);
		String result = HttpKit.post(weburl, send_param, headers);
		return result;
	}

	public static void main(String args[]) {
		try {
			DesUtils des = new DesUtils("!@#asd123");// 自定义密钥
			System.out.println("解密后的字符：" + des.decrypt("10f17868b4efe944"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getBindingList(String wxzhid) {
		
		
		ResponseResult result = new ResponseResult();
		
		//判断参数是否为空
		if(StringUtils.isBlank(wxzhid)){
			result.setErrcode("101");
			result.setErrmsg("参数不能为空！");
			return JSON.toJSONString(result);
		}
		
		List<Map<String, String>> query1 = searchDao
				.findWxqyDzbByWxzhid(wxzhid);

		int query1Size = query1.size();

		if (query1Size >= 1) {

			// 给第一条数据打上使用标记
			
			result.setBindingList(query1);

			String djxh1 = String.valueOf(query1.get(0).get("DJXH"));
			List<Map<String, String>> query2 = searchDao
					.findWxBdgxByWxzhid(wxzhid);
			int query2Size = query2.size();

			String gxid2 = null;
			String djxh2 = null;
			if (query2.size() > 0) {
				gxid2 = query2.get(0).get("GXID");
				djxh2 = String.valueOf(query2.get(0).get("DJXH"));
			}

			if (query1Size == 1) {
				//一条数据默认选中
				query1.get(0).put("isUse", "Y");
				// 源表查询的数据为1条
				if (query2Size == 0) {
					// 目标表对应的记录为空
					insertWxBdgx(wxzhid, djxh1);
				} else if (query2Size == 1) {
					// 目标表有一条对应的数据
					// 如果两条数据相等不做处理，否则 目标表数据失效，并插入源表的数据
					if (!djxh2.equals(djxh1)) {
						// 目标表失效
						searchDao.setWxDbgsUnableByGxid(gxid2);
						// 源表数据插入目标表
						insertWxBdgx(wxzhid, djxh1);
					}
				} else {
					// 目标表存在多条对应的数据
					result.setErrcode("100");
					result.setErrmsg("目标表对应的数据存在多条！");
				}
			} else {
				// 源表查询的数据为多条
				if (query2Size == 0) {
					// 取源表第一条数据插入目标表 并打上标记
					insertWxBdgx(wxzhid, djxh1);
				} else if (query2Size == 1) {
					// 目标表中存在一条数据
					// 如果目标表中的数据存在于源表的多条数据中 则不需要做处理
					// 否则 需要目标表中的数据失效 然后插入第一条数据
					if (!checkData(djxh2, query1)) {
						// 目标表失效
						searchDao.setWxDbgsUnableByGxid(gxid2);
						// 源表数据插入目标表
						insertWxBdgx(wxzhid, djxh1);
					}
				} else {
					// 目标表存在多条对应的数据
					result.setErrcode("100");
					result.setErrmsg("目标表对应的数据存在多条！");
				}
			}
		} else {
			// 源表对应的记录为空
			result.setErrcode("99");
			result.setErrmsg("源表对应的记录为空！");
		}
		return JSON.toJSONString(result);
	}

	@Override
	public String setDefaultCompany(String userid, String djxh) {
		ResponseResult result = new ResponseResult();
		if(StringUtils.isBlank(userid)||StringUtils.isBlank(djxh)){
			result.setErrcode("101");
			result.setErrmsg("参数不能为空！");
			return JSON.toJSONString(result);
		}
		List<Map<String,String>> queryList = searchDao.findWxBdgxByWxzhid(userid);
		int querySize = queryList.size();
		if(querySize==0){
			//目标表为空 做插入操作
			insertWxBdgx(userid, djxh);
		}else if(querySize==1){
			//目标表存在一条记录 进行比较  不相等则更新插入操作
			String gxid2 = queryList.get(0).get("GXID");
			String djxh2 = queryList.get(0).get("DJXH");
			if(!djxh2.equals(djxh)){
				//存在的数据失效
				searchDao.setWxDbgsUnableByGxid(gxid2);
				// 源表数据插入目标表
				insertWxBdgx(userid, djxh);
			}
		}else{
			result.setErrcode("100");
			result.setErrmsg("目标表对应的数据存在多条");
		}
		return JSON.toJSONString(result);
	}

	/**
	 * 插入数据
	 * 
	 * @param wxzhid
	 * @param djxh
	 * @return
	 */
	public int insertWxBdgx(String wxzhid, String djxh) {
		Map<String, String> insertParmas = new HashMap<String, String>();
		insertParmas.put("wxzhid", wxzhid);
		insertParmas.put("djxh", djxh);
		return searchDao.insertWxBdgx(insertParmas);
	}

	/**
	 * 检查数据是否存在
	 * 
	 * @param djxh
	 * @param list
	 * @return
	 */
	public boolean checkData(String djxh, List<Map<String, String>> list) {
		for (int i = 0; i < list.size(); i++) {
			if (djxh.equals(String.valueOf(list.get(i).get("DJXH")))) {
				list.get(i).put("isUse","Y");
				return true;
			}
		}
		return false;
	}
}
