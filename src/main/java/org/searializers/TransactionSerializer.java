package org.searializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pojo.Transaction;
import org.apache.kafka.common.serialization.Serializer;

public class TransactionSerializer implements Serializer<Transaction> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, Transaction transaction) {
        try {
            return objectMapper.writeValueAsBytes(transaction);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
