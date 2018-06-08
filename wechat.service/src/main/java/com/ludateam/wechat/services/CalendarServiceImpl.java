package com.ludateam.wechat.services;


import com.alibaba.fastjson.JSON;
import com.ludateam.wechat.dao.calendar.MeetingDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by autod on 2018/5/28.
 */
@Service("calendarService")
@Produces({ "application/json; charset=UTF-8", "text/xml; charset=UTF-8" })
@Path("calendar")
public class CalendarServiceImpl implements com.ludateam.wechat.api.CalendarService{
    private static Logger logger = Logger.getLogger(CalendarServiceImpl.class);
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
    private MeetingDao meetingDao;


    @Override
    public String meetings(String userid,String rysx) {
        List result = new ArrayList();
        if(ZJ.equals(rysx) || FJ.equals(rysx) ){
            result = meetingDao.getMeetingDataJu(userid);
        }else if(ZS.equals(rysx) || FS.equals(rysx) ){
            result = meetingDao.getMeetingDataSuo(userid);
        }else if(PT.equals(rysx)){
            result = meetingDao.getMeetingDataPt(userid);
        }

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
    public String meetingPersons(String persons) {
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

                meetingDao.saveMeetingPersons(m);

            }
        }catch(Exception e){
            e.printStackTrace();
            return "F";
        }
        return "S";
    }

    @Override
    public String meetingDesc(String meetingNumber) {
        List result = meetingDao.getMeetingDesc(meetingNumber);
        return JSON.toJSONString(result);
    }

    @Override
    public String rysx(String userid) {
        List result = meetingDao.getRysx(userid);
        return JSON.toJSONString(result);
    }


}
