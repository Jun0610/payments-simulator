package org.searializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.pojo.TransactionStats;
import org.pojo.User;

public class UserSerializer implements Serializer<User> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, User user) {
        try {
            return objectMapper.writeValueAsBytes(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
