package com.ludateam.wechat.services;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import com.ludateam.wechat.dao.calendar.MeetingDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.ludateam.wechat.dao.SearchDao;
import com.ludateam.wechat.dto.BindingResult;
import com.ludateam.wechat.dto.ResultDto;
import com.ludateam.wechat.dto.VipSqidDto;
import com.ludateam.wechat.entity.BindingEntity;
import com.ludateam.wechat.kit.HttpKit;
import com.ludateam.wechat.utils.DesUtils;
import com.ludateam.wechat.utils.PropertyUtil;
import sun.misc.BASE64Decoder;

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
	//正局
	final static String ZJ = "0";
	//副局
	final static String FJ = "1";
	//正所
	final static String ZS = "2";
	//副所
	final static String FS = "3";
	//普通
	final static String PT = "4";
	@Autowired
	private SearchDao searchDao;
	@Autowired
	private MeetingDao meetingDao;


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

		BindingResult result = new BindingResult();

		//判断参数是否为空
		if(StringUtils.isBlank(wxzhid)){
			result.setErrcode("99");
			result.setErrmsg("用户ID不能为空！");
			return JSON.toJSONString(result);
		}

		// 微信企业对照关系列表
		List<BindingEntity> bindingList = searchDao.findWxqyDzbByWxzhid(wxzhid);
		if (CollectionUtils.isEmpty(bindingList)) {
			// 源表对应的记录为空
			result.setErrcode("99");
			result.setErrmsg("该用户暂未绑定企业！");
			return JSON.toJSONString(result);
		}

		int dzgxSize = bindingList.size();
		// 对照表登记序号
		String dzbDjxh = bindingList.get(0).getDjxh();
		// 当前微信绑定关系（这个微信目前代表哪家企业）
		List<BindingEntity> bdgxList = searchDao.findWxBdgxByWxzhid(wxzhid);

		// 微信企业对照关系为1条（这个微信号的主人只在一家企业任职）
		if (dzgxSize == 1) {
			// 第一条数据默认选中
			bindingList.get(0).setIsUse("Y");
			if (CollectionUtils.isEmpty(bdgxList)) {
				// 绑定关系记录为空、则新增一条绑定关系
				insertWxBdgx(wxzhid, dzbDjxh);
			} else if (bdgxList.size() == 1) {
				BindingEntity bindingEntity = bdgxList.get(0);
				// 绑定关系id
				String bdgxid = bindingEntity.getGxid();
				// 绑定登记序号
				String bddjxh = bindingEntity.getDjxh();

				// 如果两条数据相等不做处理，否则 禁用旧绑定关系，并新增一条绑定关系
				if (!dzbDjxh.equals(bddjxh)) {
					// 目标表失效
					searchDao.setWxDbgxUnableByGxid(bdgxid);
					// 源表数据插入目标表
					insertWxBdgx(wxzhid, dzbDjxh);
				}
			} else {
				reSettingWxBdgx(bdgxList, wxzhid, dzbDjxh);
			}
		} else {
			// 源表查询的数据为多条
			if (CollectionUtils.isEmpty(bdgxList)) {
				// 取源表第一条数据插入目标表 并打上标记
				insertWxBdgx(wxzhid, dzbDjxh);
				// 第一条数据默认选中
				bindingList.get(0).setIsUse("Y");
			} else if (bdgxList.size() == 1) {
				BindingEntity bindingEntity = bdgxList.get(0);
				// 绑定关系id
				String bdgxid = bindingEntity.getGxid();
				// 绑定登记序号
				String bddjxh = bindingEntity.getDjxh();

				if (!checkData(bddjxh, bindingList)) {
					// 目标表失效
					searchDao.setWxDbgxUnableByGxid(bdgxid);
					// 源表数据插入目标表
					insertWxBdgx(wxzhid, dzbDjxh);
					// 第一条数据默认选中
					bindingList.get(0).setIsUse("Y");
				}
			} else {
				reSettingWxBdgx(bdgxList, wxzhid, dzbDjxh);
			}
		}

		result.setBindingList(bindingList);
		return JSON.toJSONString(result);
	}

	@Override
	public String setDefaultCompany(String userid, String djxh) {
		ResultDto result = new ResultDto();
		if (StringUtils.isBlank(userid) || StringUtils.isBlank(djxh)) {
			result.setErrcode("99");
			result.setErrmsg("用户ID不能为空！");
			return JSON.toJSONString(result);
		}
		
		List<BindingEntity> bdgxList = searchDao.findWxBdgxByWxzhid(userid);
		if (CollectionUtils.isEmpty(bdgxList)) {
			insertWxBdgx(userid, djxh);
		} else if (bdgxList.size() == 1) {
			BindingEntity bindingEntity = bdgxList.get(0);
			String gxid = bindingEntity.getGxid();
			String currentDjxh = bindingEntity.getDjxh();
			if (!currentDjxh.equals(djxh)) {
				searchDao.setWxDbgxUnableByGxid(gxid);
				insertWxBdgx(userid, djxh);
			}
		} else {
			reSettingWxBdgx(bdgxList, userid, djxh);
		}
		
		result.setErrcode("0");
		result.setErrmsg("设置成功！");
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
	 * 重置微信绑定关系
	 * 
	 * @param bdgxList
	 *            绑定关系列表
	 * @param wxzhid
	 *            微信账号id
	 * @param djxh
	 *            登记序号
	 * @return 无
	 */
	public void reSettingWxBdgx(List<BindingEntity> bdgxList, String wxzhid, String djxh) {
		for (BindingEntity bindingEntity : bdgxList) {
			searchDao.setWxDbgxUnableByGxid(bindingEntity.getGxid());
		}
		insertWxBdgx(wxzhid, djxh);
	}
	
	/**
	 * 检查数据是否存在
	 * 
	 * @param djxh 登记序号
	 * @param list 对照关系列表
	 * @return
	 */
	public boolean checkData(String djxh, List<BindingEntity> list) {
		for (int i = 0; i < list.size(); i++) {
			if (djxh.equals(list.get(i).getDjxh())) {
				list.get(i).setIsUse("Y");
				return true;
			}
		}
		return false;
	}

	@Override
	public String getVipSqid(String userid) {
		
		VipSqidDto result = new VipSqidDto();
		if (StringUtils.isBlank(userid)) {
			result.setErrcode("99");
			result.setErrmsg("用户ID不能为空！");
			return JSON.toJSONString(result);
		}

		List<BindingEntity> bdgxList = searchDao.findWxBdgxByWxzhid(userid);
		if (CollectionUtils.isEmpty(bdgxList)) {
			result.setErrcode("99");
			result.setErrmsg("请先绑定当前用户的身份！");
			return JSON.toJSONString(result);
		}
		BindingEntity bindingEntity = bdgxList.get(0);
		int count = searchDao.getVipCount(userid, bindingEntity.getDjxh());
		if (count == 0) {
			result.setErrcode("99");
			result.setErrmsg("当前企业身份暂不支持二维码取号");
			return JSON.toJSONString(result);
		}
		
		List<String> sqidList = searchDao.getVipSqid(userid, bindingEntity.getDjxh());
		if(CollectionUtils.isEmpty(sqidList)){
			result.setErrcode("99");
			result.setErrmsg("您绑定的企业身份已经失效，请重新进行绑定！");
			return JSON.toJSONString(result);
		}
		
		result.setErrcode("0");
		result.setErrmsg("success");
		result.setSqid(sqidList.get(0));
		return JSON.toJSONString(result);
	}

	@Override
	public String getSmbsSqid(String userid) {
		
		VipSqidDto sqidResult = new VipSqidDto();
		if (StringUtils.isBlank(userid)) {
			sqidResult.setErrcode("99");
			sqidResult.setErrmsg("用户ID不能为空！");
			return JSON.toJSONString(sqidResult);
		}
		
		String bindingListStr = getBindingList(userid);
		BindingResult bindingResult = JSON.parseObject(bindingListStr, BindingResult.class);
		List<BindingEntity> bindingList = bindingResult.getBindingList();
		String bingingDjxh = "";
		for (BindingEntity bindingEntity : bindingList) {
			if ("Y".equals(bindingEntity.getIsUse())) {
				bingingDjxh = bindingEntity.getDjxh();
				break;
			}
		}		List<String> sqidList = searchDao.getSmbsSqid(userid, bingingDjxh);
		sqidResult.setErrcode("0");
		sqidResult.setErrmsg("success");
		sqidResult.setSqid(sqidList.get(0));
		return JSON.toJSONString(sqidResult);
	}

	@Override
	public String meetings(String userid,String rysx, String jgdm) {
		List result = new ArrayList();
		Map m = new HashMap();
		m.put("userid",userid);
		m.put("jgdm",jgdm);
		result = meetingDao.getMeetingData(m);

//		if(ZJ.equals(rysx) || FJ.equals(rysx) ){
//			result = meetingDao.getMeetingDataJu(userid);
//		}else if(ZS.equals(rysx) || FS.equals(rysx) ){
//			result = meetingDao.getMeetingDataSuo(userid);
//		}else if(PT.equals(rysx)){
//			result = meetingDao.getMeetingDataPt(userid);
//		}

		if(null == result){
			return "";
		}else{
			return JSON.toJSONString(result);

		}
	}

	@Override
	public String designatedPersons(String meetingNumber, String userid) {
		System.out.println(meetingNumber);
		System.out.println(userid);
		Map m = new HashMap();
		m.put("meetingNumber",meetingNumber);
		m.put("userid",userid);
		List result = meetingDao.getDesignatedPersons(m);
		return JSON.toJSONString(result);
	}

	@Override
	public String meetingPersons(String persons,String lrrydm) {
		try {
			persons = URLDecoder.decode(persons,"UTF-8");
			System.out.println("111111111111111111="+persons);
			BASE64Decoder decoder = new BASE64Decoder();
			String a = new String(decoder.decodeBuffer(persons),"UTF-8");
//            String a = new String (Base64.getDecoder().decode(persons),"UTF-8");

			System.out.println("22222222222222222222="+a);

			String []strings = a.split("#");
			System.out.println(strings.length);
			for(String s : strings){
				String []swryData = s.split(",");
				String swrymc = swryData[0];
				String swrydm = swryData[1];
				String swjgdm = swryData[2];
				String hybh = swryData[3];

				Map m = new HashMap();
				m.put("swrymc",swrymc);
				m.put("swrydm",swrydm);
				m.put("swjgdm",swjgdm);
				m.put("hybh",hybh);
				m.put("lrrydm",lrrydm);

				meetingDao.saveMeetingPersons(m);

			}
		}catch(Exception e){
			e.printStackTrace();
			return "F";
		}
		return "S";
	}

	@Override
	public String meetingDesc(String meetingNumber, String userid) {
		List result = meetingDao.getMeetingDesc(meetingNumber);
		System.out.println("resultresultresultresultresultresult="+result);




		if(null != result || !"".equals(result)){
			Map m = new HashMap();
			m.put("meetingNumber",meetingNumber);
			m.put("userid",userid);
			meetingDao.updateCkbj(m);


			Map mm = new HashMap();
			mm.put("meetingNumber", meetingNumber);
			mm.put("userid", userid);

			List l = meetingDao.getBbmcxry(mm);
			if(null != l){
				String temp = "";
				for(int i=0;i<l.size();i++){
					Map map = (Map)l.get(i);
					String zpzj = (String)map.get("zpzj");
					String sx = (String)map.get("sx");
					if("0".equals(sx)){
						((Map)result.get(0)).put("zpzj",zpzj);
					}else{
						temp = temp+zpzj+"、";
					}
				}
				if(!"".equals(temp)){
					temp = temp.substring(0,temp.length()-1);
					((Map)result.get(0)).put("zpbr",temp);
				}

			}
		}

		return JSON.toJSONString(result);
	}

	@Override
	public String rysx(String userid) {
		List result = meetingDao.getRysx(userid);
		return JSON.toJSONString(result);
	}

	@Override
	public String ownerMeetings(String userid, String rysx) {
		List result = meetingDao.getOwnerMeetings(userid);

		if(null == result){
			return "";
		}else{
			return JSON.toJSONString(result);

		}
	}
}
