package org.producers;

import org.DBConnection;
import org.TransactionCount;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.jdbi.v3.core.Jdbi;
import org.pojo.TransactionStats;
import org.pojo.User;
import org.searializers.TransactionStatsSerializer;
import org.searializers.UserSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

public class UserProducer implements Runnable {

    private static Logger log = LoggerFactory.getLogger(UserProducer.class.getName());

    @Override
    public void run() {
        Properties producerProps = new Properties();
        producerProps.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        producerProps.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, UserSerializer.class.getName());
        try (KafkaProducer<String, User> producer = new KafkaProducer<>(producerProps)) {
            Jdbi jdbi = DBConnection.getJdbi();
            while (true) {
                try {
                    List<User> users = jdbi.withHandle(
                            handle -> handle.createQuery("SELECT userName, balance FROM users JOIN moneyBalance ON users.userId=moneyBalance.userId")
                            .map((rs, ctx) -> new User(rs.getString("userName"), rs.getInt("balance")))
                            .list());
                    for (User user : users) {
                        producer.send(new ProducerRecord<>("users", user));
                    }
                    log.debug("sent users to broker");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
