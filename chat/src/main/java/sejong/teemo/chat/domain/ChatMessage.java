package sejong.teemo.chat.domain;

import lombok.Getter;

@Getter
public class ChatMessage {

    private MessageType type;
    private String roomId;
    private String content;
    private String sender;
    private long timestamp;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}
