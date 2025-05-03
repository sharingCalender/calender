package sharingcalender.calender.service.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import sharingcalender.calender.dto.chat.ChatMessageDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisPubSubService implements MessageListener {

    private final StringRedisTemplate redisReadTemplate;
    private final SimpMessageSendingOperations messageTemplate;
    private final ObjectMapper objectMapper;

    public void publish(String channel, String message) {
        redisReadTemplate.convertAndSend(channel, message);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String payload = new String(message.getBody());

        try {
            ChatMessageDto chatMessageDto = objectMapper.readValue(payload, ChatMessageDto.class);
            messageTemplate.convertAndSend("/topic/" + chatMessageDto.chatRoomId(), chatMessageDto);

        } catch (JsonProcessingException e) {
            log.error("Parsing Exception When Publishing from Redis Broker :  ", e);

            throw new RuntimeException(e);
        }

    }
}
