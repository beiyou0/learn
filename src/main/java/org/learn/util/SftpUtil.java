package org.learn.util;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class SftpUtil {
    private static Logger logger = LoggerFactory.getLogger(SftpUtil.class);
    private Session session;

    public SftpUtil(String host, String user, String password) {
        try {
            session = getSession(host, user, password);
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    public Session getSession(String host, String user, String password) throws JSchException {
        Session session = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, 22);
            session.setPassword(password);
            //设置第一次登陆的时候提示，可选值:(ask | yes | no)
            session.setConfig("StrictHostKeyChecking", "no");
            //30秒连接超时
            session.connect(30000);
        } catch (JSchException e) {
            e.printStackTrace();
            System.out.println("SFTPUitl get connection error");
            throw e;
        }
        return session;
    }

    public void closeSession() {
        if(null != session)
            session.disconnect();
    }

    public String execCMDByJSch(String cmd) throws IOException, JSchException {
        String result = "";
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(cmd);
        channelExec.setErrStream(System.err);
        channelExec.connect();

        InputStream in = channelExec.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));

        String buf;
        while ((buf = reader.readLine()) != null){
            System.out.println(buf);
            result += buf + "\r\n";
        }
        reader.close();
        channelExec.disconnect();

        return result;
    }

    public void downloadFile(String file, String dstdir) throws JSchException, SftpException {
        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();
        channelSftp.get(file, dstdir);
        System.out.println(">>>>>>>>>>  File:" + file + " has downloaded success!");
        channelSftp.disconnect();
    }

    public static void main(String[] args) throws IOException, JSchException, SftpException {
        String host = "83.249.49.3";
        String user = "wasup";
        String password = "Wasup12$%";
        String file = "/washome/IBM/WebSphere/";
        String zipfile = "log_" + String.valueOf(System.currentTimeMillis()) + ".zip";
        String cmd = "zip -r " + zipfile + file;
        String dsrdir = ".";

        SftpUtil sftp = new SftpUtil(host, user, password);
        String result = sftp.execCMDByJSch(cmd);
        System.out.println(result);
        sftp.downloadFile(zipfile, dsrdir);
    }
}
