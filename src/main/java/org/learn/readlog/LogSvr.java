package org.learn.readlog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by qianqian on 05/01/2018.
 */
public class LogSvr {
    private SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * 将信息记录到日志文件
     * @param logFile 日志文件
     * @param mesInfo 信息
     * @throws IOException
     */
    public void logMsg(File logFile, String mesInfo) throws IOException {
        if(logFile == null) {
            throw new IllegalStateException("logFile can not be null!");
        }
        Writer txtWriter = new FileWriter(logFile,true);
//        txtWriter.write(dateFormat.format(new Date()) +"\t"+mesInfo+"\n");
//        txtWriter.write(dateFormat.format(new Date()) + ",[INFO],<Stored Procedure processing complete - EventId: 111111  BOEventId: 111111  Order: 111111  SalesOrg: 111111\n");  //2017-11-30 00:59:04.775
        txtWriter.write(dateFormat.format(new Date()) + ",[INFO],<Stored Procedure processing complete - EventId: 22222  BOEventId: 22222  Order: 22222  SalesOrg: 22222\n");  //2017-11-30 00:59:04.775
//        txtWriter.write(dateFormat.format(new Date()) + ",[INFO],Processing finished,Request Id,1235436346915045608\n");  //2017-11-30 00:59:04.775
        txtWriter.flush();
    }

    public static void main(String[] args) throws Exception{

        final LogSvr logSvr = new LogSvr();
//        final File tmpLogFile = new File("src/main/resources/aep/aep_so_chl1.log.20180103");
        final File tmpLogFile = new File("src/main/resources/aep/aep_so_chl2.log.20180103");
//        final File tmpLogFile = new File("src/main/resources/idm/idm_request.log.20180103");
        if(!tmpLogFile.exists()) {
            tmpLogFile.createNewFile();
        }
        //启动一个线程每5秒钟向日志文件写一次数据
        ScheduledExecutorService exec =
                Executors.newScheduledThreadPool(1);
        exec.scheduleWithFixedDelay(new Runnable(){
            public void run() {
                try {
                    logSvr.logMsg(tmpLogFile, " aep log test !");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, 15, TimeUnit.SECONDS);
    }
}
