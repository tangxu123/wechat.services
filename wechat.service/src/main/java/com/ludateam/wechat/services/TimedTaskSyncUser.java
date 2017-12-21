package com.ludateam.wechat.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.ludateam.wechat.entity.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.ludateam.wechat.dao.SearchDao;
import com.ludateam.wechat.dto.SyncUserJobDto;
import com.ludateam.wechat.dto.SyncUserJobResultDto;
import com.ludateam.wechat.dto.UserListDto;
import com.ludateam.wechat.kit.HttpKit;
import com.ludateam.wechat.kit.StrKit;
import com.ludateam.wechat.utils.PropertyUtil;

@Component
public class TimedTaskSyncUser {

    private static Logger logger = Logger.getLogger(TimedTaskSyncUser.class);

    @Autowired
    private SearchDao searchDao;

    /**
     * 根据市局下放至徐汇分局的实名办税信息，采用增量更新的方式，定期同步企业微信通讯录，并把实名信息与企业绑定关系存入对照表中。
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void executeSyncNsUser() {

        searchDao.removeDuplicatePhoneNumber();
        List<TaxOfficerEntity> resultList = searchDao.getUserList();
        String sendParam = "{\"content\" : \"姓名,帐号,微信号,手机号,邮箱,所在部门,职位\n";
        for (int i = 0; i < resultList.size(); i++) {
            TaxOfficerEntity entity = resultList.get(i);
            String wxh = entity.getWxid();
            if (StrKit.notBlank(wxh) && !validateWeixinid(wxh.charAt(0))) {
                wxh = "";
            }
            sendParam += entity.getMobile() + "," + entity.getUserid() + ","
                    + wxh + "," + entity.getMobile() + "," + entity.getEmail()
                    + "," + entity.getDepartment() + ",\n";
        }
        sendParam += "\"}";

        logger.info("post sync user:" + sendParam);
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-type", "application/json");
        String requestHost = PropertyUtil.getProperty("1");
        String weburl = requestHost + "/wechat/replaceuser";

        try {
            String result = HttpKit.post(weburl, sendParam, headers);
            logger.info("sync user result:" + result);

            SyncUserJobDto jobDto = JSON.parseObject(result, SyncUserJobDto.class);
            if ("0".equals(jobDto.getErrcode())) {
                int count = searchDao.updateEnableRelation();
                logger.info("updateEnableRelation count:" + count);
                count = searchDao.updateDisableRelation();
                logger.info("updateDisableRelation count:" + count);
            }
            jobDto.setZxsl(resultList.size());
            jobDto.setWxqyhId("1");
            searchDao.saveSyncUserJob(jobDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取得异步任务的执行结果（9:30:00 开始--9:31:59 每隔5秒监听一次）
     */
    @Scheduled(cron = "0-59/5 0-1,30-31 * * * ?")
    public void executeJobResult() {

        List<SyncUserJobDto> jobidList = searchDao.getJobidList();
        if (!CollectionUtils.isEmpty(jobidList)) {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Content-type", "application/json");
            for (int i = 0; i < jobidList.size(); i++) {
                SyncUserJobDto jobDto = jobidList.get(i);
                String jobid = jobDto.getJobid();
                String requestHost = PropertyUtil.getProperty(jobDto.getWxqyhId());
                String weburl = requestHost + "/wechat/batch/result";
                String sendParam = "{\"jobid\":\"" + jobid + "\"}";
                String result = HttpKit.post(weburl, sendParam, headers);
                logger.info("get batch result:" + result);
                SyncUserJobResultDto jobResultDto = JSON.parseObject(result, SyncUserJobResultDto.class);
                jobResultDto.setJobid(jobid);
                searchDao.saveSyncUserJobResult(jobResultDto);
                if (!"0".equals(jobResultDto.getErrcode())) {
                    sendTextMessage(jobDto.getWxqyhId(), "4", "通讯录同步异常，错误码："
                            + jobResultDto.getErrcode() + "，错误内容："
                            + jobResultDto.getErrmsg());
                }
            }
        } else {
            logger.info("no data ");
        }
    }

    /**
     * 同步税务人员
     */
    @Scheduled(cron = "0 30 8 * * ?")
    public void executeSyncSwryUser() {

        List<TaxOfficerEntity> resultList = searchDao.getXhSwryList();
        String sendParam = "{\"content\" : \"姓名,帐号,微信号,手机号,邮箱,所在部门,职位\n";
        for (int i = 0; i < resultList.size(); i++) {
            TaxOfficerEntity entity = resultList.get(i);
            String wxh = "";
            sendParam += entity.getName() + "," + entity.getUserid() + ","
                    + wxh + "," + entity.getMobile() + "," + entity.getEmail()
                    + "," + entity.getDepartment() + "," + entity.getPosition() + "\n";
        }
        sendParam += "\"}";

        try {
            logger.info("post sync user:" + sendParam);
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Content-type", "application/json");
            String requestHost = PropertyUtil.getProperty("2");
            String weburl = requestHost + "/wechat/replaceuser";
            String result = HttpKit.post(weburl, sendParam, headers);
            logger.info("sync user result:" + result);

            SyncUserJobDto jobDto = JSON.parseObject(result, SyncUserJobDto.class);
            if ("0".equals(jobDto.getErrcode())) {
                jobDto.setZxsl(resultList.size());
                jobDto.setWxqyhId("2");
                searchDao.saveSyncUserJob(jobDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 回写徐汇专管员号、徐汇税务号通讯录成员的关注状态
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void executeFollowStatus() {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-type", "application/json");
        try {
            String xhzgyHost = PropertyUtil.getProperty("1");
            String xhzgyWeburl = xhzgyHost + "/wechat/user/simplelist";
            String xhzgyParam = makeRequestParam("7");
            String xhzgyResult = HttpKit.post(xhzgyWeburl, xhzgyParam, headers);
            UserListDto xhzgyUserDto = JSON.parseObject(xhzgyResult, UserListDto.class);
            if ("0".equals(xhzgyUserDto.getErrcode())) {
                List<UserEntity> userList = xhzgyUserDto.getUserlist();
                int count = searchDao.updateXhzgyCancelFollow();
                logger.info("XHZGY Follow Status Cancel--" + count);
                count = searchDao.updateXhzgyFollowStatus(getUseridList(userList));
                logger.info("XHZGY Follow Status Callback--" + count);
            }

            String xhswHost = PropertyUtil.getProperty("2");
            String xhswWeburl = xhswHost + "/wechat/user/simplelist";
            String xhswParam = makeRequestParam("1");
            String xhswResuslt = HttpKit.post(xhswWeburl, xhswParam, headers);
            UserListDto xhswUserDto = JSON.parseObject(xhswResuslt, UserListDto.class);
            if ("0".equals(xhswUserDto.getErrcode())) {
                List<UserEntity> userList = xhswUserDto.getUserlist();
                int count = searchDao.updateXhswCancelFollow();
                logger.info("XHSW Follow Status Cancel--" + count);
                count = searchDao.updateXhswFollowStatus(getUseridList(userList));
                logger.info("XHSW Follow Status Callback--" + count);
            }
        } catch (Exception e) {
            logger.info("execute Follow Status Callback Exception---" + e);
            e.printStackTrace();
        }
    }

    /**
     * 获取部门成员
     *
     * @param departmentId 获取的部门id
     * @param fetch_child  1/0：是否递归获取子部门下面的成员
     * @param status       0获取全部成员，1获取已关注成员列表，2获取禁用成员列表，4获取未关注成员列表
     * @return 获取部门成员参数
     */
    private String makeRequestParam(String departmentId) {
        return "{\"department_id\":\"" + departmentId + "\",\"fetch_child\":\"1\",\"status\":\"1\"}";
    }

    /**
     * 提取部门成员的成员id
     *
     * @param userList 成员列表
     * @return 成员id列表
     */
    private List<String> getUseridList(List<UserEntity> userList) {
        List<String> useridList = new ArrayList<String>();
        for (UserEntity userEntity : userList) {
            useridList.add(userEntity.getUserid());
        }
        return useridList;
    }

    /**
     * 验证微信号是否合法<br>
     * 微信号格式由字母、数字、”-“、”_“组成，长度为 3-20 字节，首字符必须是字母或”-“或”_“
     *
     * @param firstChar 首位字母
     * @return 验证结果
     */
    private static boolean validateWeixinid(char firstChar) {
        int ascii = (int) firstChar;
        if (ascii == 45 || ascii == 95) {
            return true;
        }

        if (ascii >= 65 && ascii <= 90) {
            return true;
        }

        if (ascii >= 97 && ascii <= 122) {
            return true;
        }
        return false;
    }

    /**
     * 发送文本信息
     *
     * @param qyhid   企业号ID
     * @param agentid 应用ID
     * @param content 发送内容
     * @return 无
     */
    private void sendTextMessage(String qyhid, String agentid, String content) {

        QiYeTextMsg textMsg = new QiYeTextMsg();
        textMsg.setTouser(searchDao.getUserForSendMonitorMsg(qyhid));
        textMsg.setToparty("");
        textMsg.setTotag("");
        textMsg.setMsgtype("text");
        textMsg.setAgentid(agentid);
        textMsg.setText(new Text(content));
        textMsg.setSafe("0");

        try {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Content-type", "application/json");
            String weburl = PropertyUtil.getProperty(String.valueOf(qyhid)) + "/wechat/qyapi/sendTextMessage";
            String sendParam = JSON.toJSONString(textMsg);
            HttpKit.post(weburl, sendParam, headers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据T_WX_NSRMD 建立 重点企业 标签
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void executeUserTag() {
        logger.info("***** executeUserTag *****");
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-type", "application/json");
        String host = PropertyUtil.getProperty("1");

        String create_url = host + "/wechat/tag/create";
        String get_url = host + "/wechat/tag/list";
        String create_param = "{\n" +
                "   \"tagname\": \"重点企业\"\n" +
                "}";

        String get_result = HttpKit.post(get_url, "", headers);
        TagListEntity tagListEntity = JSON.parseObject(get_result, TagListEntity.class);
        String tagid = null;
        if ("0".equals(tagListEntity.getErrcode())) {
            tagid = getTagID(tagListEntity.getTaglist(), "重点企业");
            if (tagid == null) {
                String create_result = HttpKit.post(create_url, create_param, headers);
                logger.info("***** executeUserTag *****" + create_result);
                TagCreatResultEntity tagCreatResultEntity = JSON.parseObject(create_result, TagCreatResultEntity.class);

                if ("0".equals(tagCreatResultEntity.getErrcode())) {
                    tagid = tagCreatResultEntity.getTagid();
                }
            }

            logger.info("***** tagid *****" + tagid);
            //删除
            //获取标签人员
            String getusers_url = host + "/wechat/tag/getusers";
            String getusers_result = HttpKit.post(getusers_url, "{\"tagid\":\"" + tagid + "\"}", headers);
            TagUsersEntity tagUsersEntity = JSON.parseObject(getusers_result, TagUsersEntity.class);

            TagDelUserRequestEntity tagDelUserRequestEntity = new TagDelUserRequestEntity();
            tagDelUserRequestEntity.setTagid(tagid);

            List<String> userids = new ArrayList<String>();
            for (TagUser user : tagUsersEntity.getUserlist()
                    ) {
                userids.add(user.getUserid());
            }
            String[] userarray = new String[userids.size()];
            tagDelUserRequestEntity.setUserlist(userids.toArray(userarray));

            String jstr = JSON.toJSONString(tagDelUserRequestEntity);

            logger.info(jstr);

            String delusers_url = host + "/wechat/tag/deltagusers";
            HttpKit.post(delusers_url, jstr, headers);

            //增加人员
            String addusers_url = host + "/wechat/tag/addusers";

            List<VIPUserEntity> vipUserEntityList = searchDao.getVIPUserList();

            TagDelUserRequestEntity addusersRequestEntity = new TagDelUserRequestEntity();
            addusersRequestEntity.setTagid(tagid);

            List<String> adduserids = new ArrayList<String>();
            for (VIPUserEntity vue : vipUserEntityList
                    ) {
                adduserids.add(vue.getWxzhid());
            }
            String[] adduserarray = new String[adduserids.size()];
            addusersRequestEntity.setUserlist(adduserids.toArray(adduserarray));


            String addusers_result = HttpKit.post(addusers_url, JSON.toJSONString(addusersRequestEntity), headers);

            logger.info("***** executeUserTag addusers_result *****" + addusers_result);
        }

    }

    private String getTagID(List<TagEntity> tagEntityList, String name) {
        String tagid = null;
        for (TagEntity tagEntity : tagEntityList) {
            if (tagEntity.getTagname().equals(name)) {
                tagid = tagEntity.getTagid();
                break;
            }
        }
        return tagid;
    }

    public static void main(String[] args) {

    }
}
