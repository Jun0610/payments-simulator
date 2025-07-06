package org.searializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pojo.Transaction;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class TransactionDeserializer implements Deserializer<Transaction> {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public Transaction deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, Transaction.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
