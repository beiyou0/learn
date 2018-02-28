package org.learn.kafka;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by qianqian on 10/01/2018.
 */
public class LogTopic {
    String topicId = "topic-1";
    String topicName = "test";
    String logFolder = "/home/mqmswg/work/log/test.aep";
    long readPeriod = 30;
    ArrayList<FileMonitor> fileMonitorList;


    public LogTopic() {

    }

    public LogTopic(String topicId, String topicName, String logFolder, long readPeriod, ArrayList<FileMonitor> fileMonitorList) {
        this.topicId = topicId;
        this.topicName = topicName;
        this.logFolder = logFolder;
        this.readPeriod = readPeriod;
        this.fileMonitorList = fileMonitorList;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setLogFolder(String logFolder) {
        this.logFolder = logFolder;
    }

    public void setReadPeriod(long readPeriod) {
        this.readPeriod = readPeriod;
    }

    public void setFileMonitorList(ArrayList<FileMonitor> fileMonitorList) {
        this.fileMonitorList = fileMonitorList;
    }

    public String getTopicId() {
        return topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getLogFolder() {
        return logFolder;
    }

    public long getReadPeriod() {
        return readPeriod;
    }

    public ArrayList<FileMonitor> getFileMonitorList() {
        return fileMonitorList;
    }

    public static ArrayList<LogTopic> logTopicXMLParser(String filepath) {
        SAXReader sax = new SAXReader();
        ArrayList<LogTopic> logTopicList = new ArrayList<>();
        Document doc = null;
        try {
            doc = sax.read(filepath);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element topics = doc.getRootElement();
        Iterator it = topics.elementIterator();
        while (it.hasNext()) {
            LogTopic logTopic = new LogTopic();
            Element topic = (Element) it.next();
            List<Attribute> topicAttrs = topic.attributes();
            for (Attribute attr : topicAttrs) {
                if (attr.getName().equals("id"))
                    logTopic.setTopicId(attr.getValue());
                if (attr.getName().equals("name"))
                    logTopic.setTopicName(attr.getValue());
            }

            Iterator itTopic = topic.elementIterator();
            while (itTopic.hasNext()) {
                Element logfolder = (Element) itTopic.next();
                List<Attribute> logAttrs = logfolder.attributes();
                for (Attribute attr : logAttrs) {
                    if (attr.getName().equals("path"))
                        logTopic.setLogFolder(attr.getValue());
                    if (attr.getName().equals("readperiod"))
                        logTopic.setReadPeriod(Long.parseLong(attr.getValue()));
                }

                ArrayList<FileMonitor> fmList = new ArrayList<>();
                Iterator itLog = logfolder.elementIterator();
                while (itLog.hasNext()) {
                    Element log = (Element) itLog.next();
                    FileMonitor fm = new FileMonitor(log.getTextTrim(), 0);
                    fmList.add(fm);
                }
                logTopic.setFileMonitorList(fmList);
            }
            logTopicList.add(logTopic);
        }
        return logTopicList;
    }
}
