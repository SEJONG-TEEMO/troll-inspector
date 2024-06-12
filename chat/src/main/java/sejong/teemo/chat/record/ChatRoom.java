package sejong.teemo.chat.record;

import java.util.UUID;

public record ChatRoom(String roomId, String name) {
    public static ChatRoom of(String name) {
        return new ChatRoom(name, UUID.randomUUID().toString());
    }
}
