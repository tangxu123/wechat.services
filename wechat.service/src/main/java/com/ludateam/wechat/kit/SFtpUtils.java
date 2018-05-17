package com.ludateam.wechat.kit;
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
 * Created by Him on 2018/5/16.
 */

import com.alibaba.fastjson.JSON;
import com.jcraft.jsch.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Properties;


public class SFtpUtils {
    private static Logger logger = Logger.getLogger(SFtpUtils.class);

    /**
     * download file from sftp server
     *
     * @param ftpHost
     * @param ftpUserName
     * @param ftpPassword
     * @param ftpPort
     * @param ftpPath
     * @param localPath
     * @param fileName
     * @return
     * @throws JSchException
     */
    public static String downloadSftpFile(String ftpHost, String ftpUserName,
                                          String ftpPassword, int ftpPort, String ftpPath, String localPath,
                                          String fileName) throws JSchException {
        Session session = null;
        Channel channel = null;
        ChannelSftp chSftp = null;
        HashMap<String, String> resultMap = new HashMap<String, String>();
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(ftpUserName, ftpHost, ftpPort);
            session.setPassword(ftpPassword);
            session.setTimeout(100000);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            chSftp = (ChannelSftp) channel;

            String ftpFilePath = ftpPath + "/" + fileName;
            String localFilePath = localPath + File.separatorChar + fileName;


            logger.info("get " + ftpFilePath + " from " + localFilePath);
            chSftp.get(ftpFilePath, localFilePath);

            resultMap.put("errcode", "0");
            resultMap.put("errmsg", "ok");
            resultMap.put("localFilePath", localFilePath);
        } catch (Exception e) {
            resultMap.put("errcode", "90001");
            resultMap.put("errmsg", e.getMessage());
            resultMap.put("localFilePath", null);
            logger.info("downloadSftpFile : download error.");
            e.printStackTrace();
        } finally {
            if (chSftp != null) {
                chSftp.quit();
            }
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
        return JSON.toJSONString(resultMap);
    }
}
