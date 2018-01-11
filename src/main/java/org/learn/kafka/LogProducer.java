package org.learn.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qianqian on 10/01/2018.
 */
public class LogProducer implements Runnable{
    private String envTag = "Test";
    private String hostname = "qianqian-mac";
    private String logFolder = "/Users/qianqian/work/log/forTest";
    private String bootstrap_servers = "localhost:9092,localhost:9093,localhost:9094";
    private String topic = "forTest";
    private KafkaProducer<Long, String> producer;
    private ArrayList<FileMonitor> fmList;
    private long readPeriod;
    private String dateStrFlag; // time flag for junction between 2 days


    public LogProducer(String bootstrap_servers, String topic, String envTag, String logFolder, long readPeriod, ArrayList<FileMonitor> fmList) {
        this.bootstrap_servers = bootstrap_servers;
        this.topic = topic;
        this.envTag = envTag;
        this.logFolder = logFolder;
        this.readPeriod = readPeriod;
        this.fmList = fmList;
        this.dateStrFlag = new SimpleDateFormat("yyyyMMdd").format(new Date());
        try {
            this.hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private String getMatcher(String regex, String source) {
        String result = "";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(source);
        while (m.find()) {
            result = m.group();
        }
        return result;
    }

    public long getReadPeriod() {
        return readPeriod;
    }

    @Override
    public void run() {
        try {
            // create kafka producer instance
            Properties props = new Properties();
            props.put(ProducerConfig.CLIENT_ID_CONFIG, "ProducerTest-qian");
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap_servers);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            producer = new KafkaProducer<>(props);

            // loop to-be-monitored logs, read new lines and send to corresponding kafka topic
            Date today = new Date();
            String todayStr = new SimpleDateFormat("yyyyMMdd").format(today);

            if(fmList != null) {
                for (FileMonitor fm : fmList) {
                    long lastTimeFileSize = fm.getLastTimeFileSize();
                    File f = new File(logFolder + "/" + fm.getName() + "." + dateStrFlag);
                    if (f.exists() && f.isFile()) {
                        RandomAccessFile randomFile = new RandomAccessFile(f, "r");
                        randomFile.seek(lastTimeFileSize);
                        String line;
                        while ((line = randomFile.readLine()) != null) {
                            Long timeIndex = System.currentTimeMillis();
                            String jsonGELF = "{\"version\": \"0.0\", " +
                                    "\"host\": \"" + envTag + "\", " +
                                    "\"message\": \"" + line + "\", " +
                                    "\"_hostname\": \"" + hostname + "\", " +
                                    "\"_type\": \"" + line.split(",")[1].replaceAll("[\\[|\\]]", "") + "\", " +
                                    "\"_sales_org\": \"" + getMatcher("SalesOrg: [0-9]*", line).replace("SalesOrg:", "").trim() + "\", " +
                                    "\"_order_id\": \"" + getMatcher("Order: [0-9]*", line).replace("Order:", "").trim() + "\", " +
                                    "\"_file\": \"" + f.getName() + "\"}";
                            System.out.println(jsonGELF);
                            ProducerRecord<Long, String> record = new ProducerRecord<>(topic, timeIndex, jsonGELF);
                            RecordMetadata metadata = producer.send(record).get();

                            long elapsedTime = System.currentTimeMillis() - timeIndex;
                            System.out.printf("Send record(key=%s, value=%s) metadata(partition=%d, offset=%d) time=%d\n", record.key(),
                                    record.value(), metadata.partition(), metadata.offset(), elapsedTime);
                        }
                        if (todayStr.equals(dateStrFlag)) {  // in one day
                            lastTimeFileSize = randomFile.length();
                            fm.setLastTimeFileSize(lastTimeFileSize);
                        }
                        else {
                            lastTimeFileSize = 0;
                            fm.setLastTimeFileSize(lastTimeFileSize);
                            dateStrFlag = todayStr;
                        }
                        System.out.println("******** File Size now: " + lastTimeFileSize);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }  catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            producer.flush();
            producer.close();
        }
    }

    public static void main(String[] args) {
        try {
            Properties prop = new Properties();
            FileInputStream in = new FileInputStream("src/main/resources/config.properties");
            prop.load(in);
            in.close();
            String kafka_server = prop.getProperty("kafka_server");
            String env = prop.getProperty("esb_env");

            ArrayList<LogTopic> logTopicList = LogTopic.logTopicXMLParser("src/main/resources/logtopic.xml");

            ArrayList<LogProducer> logProducerList = new ArrayList<>();
            for (LogTopic ltTmp : logTopicList) {
                LogProducer lp = new LogProducer(kafka_server, ltTmp.getTopicName(), env,
                        ltTmp.getLogFolder(), ltTmp.getReadPeriod(), ltTmp.getFileMonitorList());
                logProducerList.add(lp);
            }

            ScheduledExecutorService exec = Executors.newScheduledThreadPool(4);
            for (LogProducer lpTmp: logProducerList) {
                exec.scheduleWithFixedDelay(lpTmp, 0, lpTmp.getReadPeriod(), TimeUnit.SECONDS);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
