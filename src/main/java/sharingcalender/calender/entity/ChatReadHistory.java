package sharingcalender.calender.entity;


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
public class ChatReadHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatReadHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime lastDate;

    public ChatReadHistory(ChatRoom chatRoom, User user, LocalDateTime lastDate) {
        this.chatRoom = chatRoom;
        this.user = user;
        this.lastDate = lastDate;
    }
}
