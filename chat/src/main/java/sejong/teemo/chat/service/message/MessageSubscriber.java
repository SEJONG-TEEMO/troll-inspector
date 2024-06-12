package sejong.teemo.chat.service.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import sejong.teemo.chat.domain.ChatMessage;

@Service
@RequiredArgsConstructor
public class MessageSubscriber implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageSubscriber.class);
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messageSendingOperations;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {

            String publishMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());
            ChatMessage roomMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
            messageSendingOperations.convertAndSend("sub/chat/room/" + roomMessage.getRoomId(), roomMessage);
            logger.info("Received message: {}", new String(message.getBody()));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}

