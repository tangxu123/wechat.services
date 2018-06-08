package com.ludateam.wechat.amqp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.ludateam.wechat.entity.*;
import com.ludateam.wechat.kit.MediaMessage;
import com.ludateam.wechat.kit.SFtpUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.ludateam.wechat.dto.MqJsonDto;
import com.ludateam.wechat.dto.SendMsgResultDto;
import com.ludateam.wechat.kit.HttpKit;
import com.ludateam.wechat.utils.PropertyUtil;

public class CommonServiceHandler {

	private static Logger logger = Logger.getLogger(CommonServiceHandler.class);
	/** 短信发送状态: 等待发送 */
	public static final String DXFSZT_WAIT = "0";
	/** 短信发送状态: 发送成功 */
	public static final String DXFSZT_SUCCESS = "1";
	/** 短信发送状态 :发送失败 */
	public static final String DXFSZT_FAILURE = "2";
	/** 短信发送状态: 发送至队列 */
	public static final String DXFSZT_ADD_QUEUE = "3";
	/** 短信发送状态:已发送至云MAS */
	public static final String DXFSZT_YMAS_SUCCESS = "4";
	/** 短信发送状态: 发送云MAS失败 */
	public static final String DXFSZT_YMAS_FAILURE = "5";

	/**
	 * 拆分发送对象成集合
	 * 
	 * @param sendUserList
	 *            发送对象
	 * 
	 * */
	public List<String> splitSendList(String sendUserList) {
		List<String> sendList = new ArrayList<String>();
		String[] array = sendUserList.split(",");
		for (int i = 0; i < array.length; i++) {
			sendList.add(array[i]);
		}
		return sendList;
	}

	/**
	 * 发送文本信息
	 * 
	 * @param mqJsonDto
	 *            队列消息
	 * 
	 * @return 发送结果
	 */
	public SendMsgResultDto sendTextMessage(MqJsonDto mqJsonDto) {
		
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-type", "application/json");

		int qyhid = mqJsonDto.getQyhid();
		String weburl = PropertyUtil.getProperty(String.valueOf(qyhid)) + "/wechat/qyapi/sendTextMessage";
		String userid = mqJsonDto.getWxzh();
		String content = mqJsonDto.getDxnr();
		String agentid = String.valueOf(mqJsonDto.getWxyyid());

		QiYeTextMsg textMsg = new QiYeTextMsg();
		textMsg.setTouser(userid.replace(",", "|"));
		textMsg.setToparty("");
		textMsg.setTotag("");
		textMsg.setMsgtype("text");
		textMsg.setAgentid(agentid);
		textMsg.setText(new Text(content));
		textMsg.setSafe("0");

		SendMsgResultDto resultDto = null;
		try {
			String sendParam = JSON.toJSONString(textMsg);
			String resultJson = HttpKit.post(weburl, sendParam, headers);
			logger.info("send param:" + sendParam);
			logger.info("send result:" + resultJson);
			resultDto = JSON.parseObject(resultJson, SendMsgResultDto.class);
		} catch (Exception e) {
			logger.info("send--text--message--exception--happened");
			e.printStackTrace();
		}
		return resultDto;
	}

	/**
	 * 发送多媒体信息
	 *
	 * @param mqJsonDto
	 *            队列消息
	 *
	 * @return 发送结果
	 */
	public SendMsgResultDto sendMediaMessage(MqJsonDto mqJsonDto) {
		SendMsgResultDto resultDto = null;

		//发送内容类型  1：文本 2：图片 3：文件 4：链接
		String fsnrlx  = mqJsonDto.getFsnrlx();

		int qyhid = mqJsonDto.getQyhid();
		//conf.properties
		if("2".equals(fsnrlx)  ){
			String weburl = PropertyUtil.getProperty(String.valueOf(qyhid)) + "/wechat/qyapi/sendImageMessage";
			resultDto = sendMediaMessageByFileAndImage(mqJsonDto,"image",String.valueOf(qyhid),weburl);
		}else if("3".equals(fsnrlx)){
			String weburl = PropertyUtil.getProperty(String.valueOf(qyhid)) + "/wechat/qyapi/sendFileMessage";
			resultDto = sendMediaMessageByFileAndImage(mqJsonDto,"file",String.valueOf(qyhid),weburl);
		} else if("4".equals(fsnrlx)){
			String weburl = PropertyUtil.getProperty(String.valueOf(qyhid)) + "/wechat/qyapi/sendNewsMessage";
			resultDto = sendMediaMessageByLink(mqJsonDto,"news",String.valueOf(qyhid),weburl);
		}

		return resultDto;
	}

	private SendMsgResultDto sendMediaMessageByFileAndImage(MqJsonDto mqJsonDto,String type,String target,String weburl){
		SendMsgResultDto resultDto = null;
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-type", "application/json");

		int qyhid = mqJsonDto.getQyhid();

		String userid = mqJsonDto.getWxzh();
		String content = mqJsonDto.getDxnr();
		//FTP 参数
		String host = PropertyUtil.getProperty("sftp.host");
		String port = PropertyUtil.getProperty("sftp.port");
		String username = PropertyUtil.getProperty("sftp.username");
		String password = PropertyUtil.getProperty("sftp.password");
		String localpath = PropertyUtil.getProperty("sftp.localpath");

		String delFilePath = "";
		try{
			Map content_map = (Map)JSON.parseObject(content,List.class).get(0);
			String filepath = (String)content_map.get("ftppath");
			String filename = (String)content_map.get("filename");

			String agentid = String.valueOf(mqJsonDto.getWxyyid());
			delFilePath = localpath + File.separatorChar + filename;
			String sftpJson = SFtpUtils.downloadSftpFile(host, username, password, Integer.valueOf(port), filepath, localpath, filename);
			logger.info("download file from sftp server , " + sftpJson);
			JSONObject jsonObject = JSONObject.parseObject(sftpJson);

			Map<String, Object> sftpMap = (Map<String, Object>) jsonObject;
			//从sftp server 下载文件异常
			if (!"0".equals(sftpMap.get("errcode"))) {
				logger.info("download file from sftp server , " + "--exception--happened");
			} else {
				String saveFilePath = HttpKit.send(type, (String)sftpMap.get("localFilePath"), target);

				FileMessage o = new FileMessage();
				o.setAgentid(agentid);
				o.setFilename(filename);
				o.setFtppath(filepath);
				o.setMsgtype(type);
				o.setPath(saveFilePath);
				o.setSafe("0");
				o.setTouser(userid.replace(",", "|"));

				o.setPath(saveFilePath);
				logger.info("post " + JSON.toJSONString(o) + " to " + weburl);
				String resultJson = HttpKit.post(weburl, JSON.toJSONString(o), headers);
				logger.info("send param:" + JSON.toJSONString(o));
				logger.info("send result:" + resultJson);
				resultDto = JSON.parseObject(resultJson, SendMsgResultDto.class);
			}
		}catch(Exception e){
			logger.info("send--file--message--exception--happened");
			logger.error("222222222222222222222222"+type);
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());
			e.printStackTrace();
		}finally{
			System.out.println("delFilePathdelFilePathdelFilePathdelFilePath="+delFilePath);
			SFtpUtils.deleteFile(delFilePath);
		}

		return resultDto;
	}
	public static void main(String[] args) {
		List l = new ArrayList();
		Map m = new HashMap();
		m.put("ftppath","/root/dwxpt/wx/upload/chat/20180529/44A30B1C633511E8A4B8005056902E0A.JPG");
		m.put("filename","a.jpg");
		l.add(m);
		String s = JSON.toJSON(l).toString();
		Map mm = (Map)JSON.parseObject(s,List.class).get(0);
		System.out.println(mm.get("ftppath"));
//		String s = "";
	}
	/**
	 * 发送新闻信息
	 *
	 * @param mqJsonDto
	 *            队列消息
	 *
	 * @return 发送结果
	 */
	private SendMsgResultDto sendMediaMessageByLink(MqJsonDto mqJsonDto,String type,String target,String weburl) {
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-type", "application/json");

		String userid = mqJsonDto.getWxzh();
		String content = mqJsonDto.getDxnr();
		String agentid = String.valueOf(mqJsonDto.getWxyyid());


		List l  = JSON.parseObject(content,List.class);
		List<Article> articles = new ArrayList<Article>();
		for(int i=0;i<l.size();i++){
			Map content_map = (Map)l.get(i);
			String title = (String)content_map.get("title");
			String description = (String)content_map.get("description");
			String url = (String)content_map.get("url");
			String picurl = (String)content_map.get("picurl");
			articles.add(new Article(title, description, url, picurl));
		}
		News news = new News(articles);
		QiYeNewsMsg newsMsg = new QiYeNewsMsg();
		newsMsg.setTouser(userid.replace(",", "|"));
		newsMsg.setToparty("");
		newsMsg.setTotag("");
		newsMsg.setMsgtype(type);
		newsMsg.setAgentid(agentid);
		newsMsg.setSafe("0");
		newsMsg.setNews(news);

		SendMsgResultDto resultDto = null;
		try {
			String resultJson = HttpKit.post(weburl, JSON.toJSONString(newsMsg), headers);
			logger.info("send param:" + JSON.toJSONString(newsMsg));
			logger.info("send result:" + resultJson);
			resultDto = JSON.parseObject(resultJson, SendMsgResultDto.class);
		} catch (Exception e) {
			logger.info("send--file--message--exception--happened");
			logger.error("2222222222222222222222222"+type);
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());
			e.printStackTrace();
		}
		return resultDto;

	}
		/**
         * 发送新闻信息
         *
         * @param mqJsonDto
         *            队列消息
         *
         * @return 发送结果
         */
	public SendMsgResultDto sendNewsMessage(MqJsonDto mqJsonDto) {


		
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-type", "application/json");

		int qyhid = mqJsonDto.getQyhid();
		String weburl = PropertyUtil.getProperty(String.valueOf(qyhid)) + "/wechat/qyapi/sendNewsMessage";
		String userid = mqJsonDto.getWxzh();
		String content = mqJsonDto.getDxnr();
		String agentid = String.valueOf(mqJsonDto.getWxyyid());
		String title = mqJsonDto.getTitle();
		String url = mqJsonDto.getUrl();
		String description = "";
		String[] lines = content.split("\\|");
		for (int i = 0; i < lines.length; i++) {
			description += lines[i] + "\n";
		}
		
		List<Article> articles = new ArrayList<Article>();
		articles.add(new Article(title, description, url, ""));
		News news = new News(articles);
		QiYeNewsMsg newsMsg = new QiYeNewsMsg();
		newsMsg.setTouser(userid.replace(",", "|"));
		newsMsg.setToparty("");
		newsMsg.setTotag("");
		newsMsg.setMsgtype("news");
		newsMsg.setAgentid(agentid);
		newsMsg.setSafe("0");
		newsMsg.setNews(news);
		
		SendMsgResultDto resultDto = null;
		try {
			String sendParam = JSON.toJSONString(newsMsg);
			String resultJson = HttpKit.post(weburl, sendParam, headers);
			logger.info("send param:" + sendParam);
			logger.info("send result:" + resultJson);
			resultDto = JSON.parseObject(resultJson, SendMsgResultDto.class);
		} catch (Exception e) {
			logger.info("send--text--message--exception--happened");
			e.printStackTrace();
		}
		return resultDto;
	}
}
