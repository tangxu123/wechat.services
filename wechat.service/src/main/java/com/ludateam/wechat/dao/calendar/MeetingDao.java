package com.ludateam.wechat.dao.calendar;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by autod on 2018/5/28.
 */
@Component
public interface MeetingDao {
    /**
     * 获取会议信息
     */
    List getMeetingData(Map m);
    /**
     * 获取会议信息 局
     */
    List getMeetingDataJu(String userid);

    /**
     * 获取会议信息 局
     */
    List getMeetingDataSuo(String userid);

    /**
     * 获取会议信息 局
     */
    List getMeetingDataPt(String userid);

    /**
     * 获取会议可指派参会人员信息
     */
    List getDesignatedPersons(Map m);
    /**
     * 保存参会人员信息
     */
    int saveMeetingPersons(Map map);
    /**
     * 获取会议描述信息
     */
    List getMeetingDesc(String meetingNumber);

    /**
     * 获取人员属性
     */
    List getRysx(String userid);

    /**
    * 获取自己的日历信息
    */
    List getOwnerMeetings(String userid);

    /**
     * 更新查看标记
     */
    int updateCkbj(Map map);
    /**
     * 本部门参会人员
     */
    List getBbmcxry(Map map);

}
