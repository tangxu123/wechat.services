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
import org.springframework.util.CollectionUtils;

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
		
		// 微信企业对照关系列表
		List<Map<String, String>> dzgxList = searchDao.findWxqyDzbByWxzhid(wxzhid);
		if (CollectionUtils.isEmpty(dzgxList)) {
			// 源表对应的记录为空
			result.setErrcode("99");
			result.setErrmsg("源表对应的记录为空！");
			return JSON.toJSONString(result);
		}
		
		// 当前微信绑定关系（这个微信目前代表哪家企业）
		List<Map<String, String>> bdgxList = searchDao.findWxBdgxByWxzhid(wxzhid);
		int bdgxSize = CollectionUtils.isEmpty(bdgxList) ? 0 : bdgxList.size();
		int dzgxSize = dzgxList.size();
		// 对照表登记序号
		String dzbDjxh = dzgxList.get(0).get("djxh");
		// 绑定关系id
		String bdgxid = null;
		// 绑定登记序号
		String bddjxh = null;
		if (bdgxSize > 0) {
			bdgxid = bdgxList.get(0).get("gxid");
			bddjxh = bdgxList.get(0).get("djxh");
		}

		// 微信企业对照关系为1条（这个微信号的主人只在一家企业任职）
		if (dzgxSize == 1) {
			// 第一条数据默认选中
			dzgxList.get(0).put("isUse", "Y");
			if (bdgxSize == 0) {
				// 绑定关系记录为空、则新增一条绑定关系
				insertWxBdgx(wxzhid, dzbDjxh);
			} else if (bdgxSize == 1) {
				// 如果两条数据相等不做处理，否则 禁用旧绑定关系，并新增一条绑定关系
				if (!dzbDjxh.equals(bddjxh)) {
					// 目标表失效
					searchDao.setWxDbgxUnableByGxid(bdgxid);
					// 源表数据插入目标表
					insertWxBdgx(wxzhid, dzbDjxh);
				}
			} else {
				// 目标表存在多条对应的数据
				result.setErrcode("100");
				result.setErrmsg("数据异常，一个微信账号只能设定一条最新绑定关系");
			}
		} else {
			// 源表查询的数据为多条
			if (bdgxSize == 0) {
				// 取源表第一条数据插入目标表 并打上标记
				insertWxBdgx(wxzhid, dzbDjxh);
				// 第一条数据默认选中
				dzgxList.get(0).put("isUse", "Y");
			} else if (bdgxSize == 1) {
				// 目标表中存在一条数据
				// 如果目标表中的数据存在于源表的多条数据中 则不需要做处理
				// 否则 需要目标表中的数据失效 然后插入第一条数据
				if (!checkData(bddjxh, dzgxList)) {
					// 目标表失效
					searchDao.setWxDbgxUnableByGxid(bdgxid);
					// 源表数据插入目标表
					insertWxBdgx(wxzhid, dzbDjxh);
					// 第一条数据默认选中
					dzgxList.get(0).put("isUse", "Y");
				}
			} else {
				// 目标表存在多条对应的数据
				result.setErrcode("100");
				result.setErrmsg("数据异常，一个微信账号只能设定一条最新绑定关系");
			}
		}
		
		// 给第一条数据打上使用标记
		result.setBindingList(dzgxList);
		return JSON.toJSONString(result);
	}

	@Override
	public String setDefaultCompany(String userid, String djxh) {
		ResponseResult result = new ResponseResult();
		if (StringUtils.isBlank(userid) || StringUtils.isBlank(djxh)) {
			result.setErrcode("101");
			result.setErrmsg("参数不能为空！");
			return JSON.toJSONString(result);
		}
		List<Map<String, String>> queryList = searchDao.findWxBdgxByWxzhid(userid);
		int querySize = queryList.size();
		if (querySize == 0) {
			// 目标表为空 做插入操作
			insertWxBdgx(userid, djxh);
		} else if (querySize == 1) {
			// 目标表存在一条记录 进行比较 不相等则更新插入操作
			String gxid = queryList.get(0).get("gxid");
			String currentDjxh = queryList.get(0).get("djxh");
			if (!currentDjxh.equals(djxh)) {
				// 存在的数据失效
				searchDao.setWxDbgxUnableByGxid(gxid);
				// 源表数据插入目标表
				insertWxBdgx(userid, djxh);
			}
		} else {
			result.setErrcode("100");
			result.setErrmsg("数据异常，一个微信账号只能设定一条最新绑定关系");
		}
		return JSON.toJSONString(result);
	}

	/**
	 * 插入微信绑定关系
	 * 
	 * @param wxzhid 微信账号id
	 * @param djxh 登记序号
	 * @return 插入条数
	 */
	public int insertWxBdgx(String wxzhid, String djxh) {
		Map<String, String> insertParams = new HashMap<String, String>();
		insertParams.put("wxzhid", wxzhid);
		insertParams.put("djxh", djxh);
		return searchDao.insertWxBdgx(insertParams);
	}

	/**
	 * 检查数据是否存在
	 * 
	 * @param djxh 登记序号
	 * @param list 对照关系列表
	 * @return
	 */
	public boolean checkData(String djxh, List<Map<String, String>> list) {
		for (int i = 0; i < list.size(); i++) {
			if (djxh.equals(list.get(i).get("djxh"))) {
				list.get(i).put("isUse", "Y");
				return true;
			}
		}
		return false;
	}
}
