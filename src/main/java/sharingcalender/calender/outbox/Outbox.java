package sharingcalender.calender.outbox;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sharingcalender.calender.messageevent.MessageEventType;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Outbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outboxId;
    @Enumerated(EnumType.STRING)
    private MessageEventType messageEventType;
    private String payload;
    private LocalDateTime createdAt;

    private Outbox(MessageEventType messageEventType,String payload, LocalDateTime createdAt) {
        this.messageEventType = messageEventType;
        this.payload = payload;
        this.createdAt = createdAt;
    }

    public static Outbox create(MessageEventType messageEventType,String payload,LocalDateTime createdAt){
        return new Outbox(messageEventType,payload,createdAt);
    }
}
