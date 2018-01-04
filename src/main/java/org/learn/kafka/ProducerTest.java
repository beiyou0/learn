package org.learn.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Created by qianqian on 04/01/2018.
 */
public class ProducerTest {
    private final static String TOPIC = "topic-javaclient";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "ProducerTest-qian");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<Long, String> producer = new KafkaProducer<>(props);
        long time = System.currentTimeMillis();
        int sendMsgCount = 50;
        try {
            for (long index = time; index < time + sendMsgCount; index++) {
                String json = "{\"version\": \"1.1\", " +
                        "\"host\": \"localhost\", " +
                        "\"short_message\": \"kafka topic consumer\", " +
                        "\"full_message\": \"Graylog GELF Kafka to consume events from topic - topic-javaclient " +  index + "\", " +
                        "\"_userId\": \"qianqian\"}";
//                ProducerRecord<Long, String> record = new ProducerRecord<>(TOPIC, index, "Hello Mom " + index);
                ProducerRecord<Long, String> record = new ProducerRecord<>(TOPIC, index, json);
                RecordMetadata metadata = producer.send(record).get();

                long elapsedTime = System.currentTimeMillis() - time;
                System.out.printf("Send record(key=%s, value=%s) metadata(partition=%d, offset=%d) time=%d\n", record.key(),
                        record.value(), metadata.partition(), metadata.offset(), elapsedTime);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            producer.flush();
            producer.close();
        }
    }
}
