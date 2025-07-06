package org;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.pojo.Transaction;
import org.pojo.User;
import org.producers.TransactionStatsProducer;
import org.producers.UserProducer;
import org.searializers.TransactionDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionProcessor {

    private static final Logger logger = LoggerFactory.getLogger(TransactionProcessor.class.getName());

    public static void main(String[] args) {

        Properties consumerProps = new Properties();
        consumerProps.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProps.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, TransactionDeserializer.class.getName());
        consumerProps.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "transaction-processors");
        consumerProps.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        new Thread(new TransactionStatsProducer()).start();
        new Thread(new UserProducer()).start();
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();


        try (KafkaConsumer<String, Transaction> consumer = new KafkaConsumer<>(consumerProps)) {
            consumer.subscribe(List.of("transactions"));
            while(true) {
                ConsumerRecords<String, Transaction> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, Transaction> record : records) {
                    executorService.submit(new ProcessTransactionTask(record.value()));
                }
            }
        }





    }

}
