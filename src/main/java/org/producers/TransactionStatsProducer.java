package org.producers;

import org.TransactionCount;
import org.pojo.TransactionStats;
import org.searializers.TransactionStatsSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class TransactionStatsProducer implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(TransactionStatsProducer.class.getName());

    @Override
    public void run() {
        Properties producerProps = new Properties();
        producerProps.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        producerProps.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, TransactionStatsSerializer.class.getName());
        try (KafkaProducer<String, TransactionStats> producer = new KafkaProducer<>(producerProps)){
            while (true) {
                try {
                    long curCount = TransactionCount.getCount();
                    Thread.sleep(1000);
                    long newCount = TransactionCount.getCount();
                    TransactionStats.getInstance().setTransactionsPerSecond(newCount - curCount);
                    ProducerRecord<String, TransactionStats> record = new ProducerRecord<>("transaction-stats", TransactionStats.getInstance());
                    log.info("transactions processed in last second: {}", newCount - curCount);
                    producer.send(record);
                    TransactionCount.resetCount();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }



}
