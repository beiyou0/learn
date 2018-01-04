package org.learn.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by qianqian on 04/01/2018.
 */
public class ConsumerTest {
    private final static String TOPIC = "topic-javaclient";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";

    public static void main(String[] args) {
        Properties props = new Properties();
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "ConsumerTest-qian");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 50);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");  // can fetch data from the beginning
        props.put("group.id", UUID.randomUUID().toString());        // can fetch data from the beginning

        Consumer<Long, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(TOPIC));

        final int giveUp = 20;
        int noRecordsCount = 0;

        while (true) {
            ConsumerRecords<Long, String> consumerRecords = consumer.poll(1000);

            if (consumerRecords.count() == 0 ) {
                noRecordsCount++;
                if (noRecordsCount > giveUp)
                    break;
                else
                    continue;
            }

            for (ConsumerRecord<Long, String> record : consumerRecords) {
                System.out.printf("Consumer Record: (%d, %s, %d, %d)\n", record.key(), record.value(), record.partition(), record.offset());
            }

            consumer.commitAsync();
        }
        consumer.close();
        System.out.println("DONE.");
    }
}
