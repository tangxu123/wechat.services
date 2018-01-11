package com.ludateam.wechat.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ludateam.wechat.dto.SendStatusDto;
import com.ludateam.wechat.dto.SyncUserJobDto;
import com.ludateam.wechat.dto.SyncUserJobResultDto;
import com.ludateam.wechat.entity.BindingEntity;
import com.ludateam.wechat.entity.FsmdEntity;
import com.ludateam.wechat.entity.FsrwEntity;
import com.ludateam.wechat.entity.SssxTzsEntity;
import com.ludateam.wechat.entity.TaskEntity;
import com.ludateam.wechat.entity.TaxOfficerEntity;
import com.ludateam.wechat.entity.VIPUserEntity;

public interface SearchDao {

    /**
     * 保存企业微信接收的消息
     */
    int saveReseiceMsg(Map msgMap);

    /**
     * 取得发送任务列表
     */
    List<TaskEntity> getTaskList(TaskEntity entity);

    /**
     * 取得专管员号通讯录列表(同步企业微信通讯录)
     */
    List<TaxOfficerEntity> getUserList();

    /**
     * 同步未停用实名办税员与企业的对照关系
     */
    int updateEnableRelation();

    /**
     * 同步未停用实名办税员与企业的对照关系
     */
    int updateDisableRelation();

    /**
     * 保存同步用户任务信息
     */
    int saveSyncUserJob(SyncUserJobDto jobDto);

    /**
     * 保存同步用户任务的结果信息
     */
    int saveSyncUserJobResult(SyncUserJobResultDto jobResultDto);

    /**
     * 取得jobid列表
     */
    List<SyncUserJobDto> getJobidList();

    /**
     * 取得发送任务列表
     */
    List<SssxTzsEntity> getTzsList();

    /**
     * 保存发送任务
     */
    int saveFsrw(FsrwEntity entity);

    /**
     * 保存发送名单
     */
    int saveFsmd(FsmdEntity entity);

    /**
     * 更新通知书状态
     */
    int updateTzsStatus(String wsh);

    /**
     * 已申报数据统计
     */
    void getHxzgYsbtj();

    /**
     * 催报催缴临时数据生成
     */
    void getHxzgCbcj(int tjnd);

    /**
     * 取得未申报信息列表
     */
    List<SssxTzsEntity> getWsbList();

    /**
     * 取得徐汇税务人员列表
     */
    List<TaxOfficerEntity> getXhSwryList();

    Map getAccountByUserName(String accountUserName);

    int updateAccountLastLogin(int id);

    Map getZsd(String accountUserName);

    Map findSKorder(String jksbh);

    Map findSKorderByOrderId(String orderid);

    Map findSKorderByOrderIdAndResult(String orderid);

    Map findSKorderByOrderIdfail(String orderid);

    int addskorder(Map skorderMap);

    List<LinkedHashMap<String, Object>> querySQL(String sqlContent);

    int updateSKOrderZf(Map paramMap);

    Map findSKorderByCon(Map paramMap);

    int insertSKDetails(Map paramMap);

    int updateSQL(Map paramMap);

    /**
     * 根据微信账号id查询微信企业对照关系列表
     */
    List<BindingEntity> findWxqyDzbByWxzhid(String wxzhid);

    /**
     * 根据微信账号id查询当前微信绑定关系
     */
    BindingEntity findWxBdgxByWxzhid(String wxzhid);

    /**
     * 新增微信绑定关系
     */
    int insertWxBdgx(Map<String, String> params);

    /**
     * 根据关系id禁用旧绑定关系
     */
    int setWxDbgxUnableByGxid(String gxid);

    /**
     * 根据微信账号id更新徐汇专管员号的关注状态
     */
    int updateXhzgyFollowStatus(List<String> wxzhidList);

    /**
     * 根据微信账号id更新徐汇税务号的关注状态
     */
    int updateXhswFollowStatus(List<String> swryidList);

    /**
     * 更新徐汇专管员号的关注状态为取消关注
     */
    int updateXhzgyCancelFollow();

    /**
     * 更新徐汇税务号的关注状态为取消关注
     */
    int updateXhswCancelFollow();

    /**
     * 取得绿色通道用户的授权ID
     */
    List<String> getVipSqid(String wxzhid, String djxh);

    /**
     * 移除通讯录中重复的手机号码
     */
    int removeDuplicatePhoneNumber();

	/**
	 * 取得绿色通道用户的数量（判断是否是绿色通道用户）
	 */
	int getVipCount(String wxzhid, String djxh);

    /**
     * 取得用来推送监控消息的用户
     */
    String getUserForSendMonitorMsg(String qyhid);

    /**
     * 取得重点企业的用户id
     */
    List<VIPUserEntity> getVIPUserList();

	/**
	 * 更新短信状态
	 */
	int updateSmsStatus(SendStatusDto dto);

	/**
	 * 更新短信发送状态
	 */
	int updateSmsSendStatus(SendStatusDto dto);

	/**
	 * 更新微信发送状态
	 */
	int updateWechatSendStatus(SendStatusDto dto);

}
