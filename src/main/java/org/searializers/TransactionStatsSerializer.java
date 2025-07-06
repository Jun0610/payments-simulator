package org.searializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pojo.TransactionStats;
import org.apache.kafka.common.serialization.Serializer;

public class TransactionStatsSerializer implements Serializer<TransactionStats> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, TransactionStats stats) {
        try {
            return objectMapper.writeValueAsBytes(stats);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
