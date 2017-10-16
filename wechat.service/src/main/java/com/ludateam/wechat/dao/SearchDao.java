package com.ludateam.wechat.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface SearchDao {
    /**
     * 保存企业微信接受的消息
     */
    int saveReseiceMsg(Map msgMap);

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
}
