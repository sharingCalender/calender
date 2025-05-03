package sharingcalender.calender.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 800)
    private String content;

    private LocalDateTime messageDate;

    private ChatMessage(ChatRoom chatRoom, User user, String content, LocalDateTime messageDate) {
        this.chatRoom = chatRoom;
        this.user = user;
        this.content = content;
        this.messageDate = messageDate;
    }

    public static ChatMessage create(ChatRoom chatRoom, User user, String content,
        LocalDateTime messageDate) {
        return new ChatMessage(chatRoom, user, content, messageDate);
    }
}
