package sejong.teemo.chat.controller;

import org.springframework.data.redis.listener.ChannelTopic;
import sejong.teemo.chat.domain.ChatMessage;
import sejong.teemo.chat.service.message.MessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessagePublisher messagePublisher;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
//        messagePublisher.publish(chatMessage.getContent());
        messagePublisher.publish(new ChannelTopic(""), "");
        return chatMessage;
    }
}

