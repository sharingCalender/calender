package sharingcalender.calender.outbox;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import sharingcalender.calender.messageevent.MessageEvent;
import sharingcalender.calender.messageevent.MessageEventPayload;
import sharingcalender.calender.messageevent.MessageEventType;

@RequiredArgsConstructor
@Component
public class OutboxEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(MessageEventType messageEventType, MessageEventPayload payload){
        Outbox outbox = Outbox.create(
            messageEventType,
            MessageEvent.create(messageEventType, payload).toJson(),
            LocalDateTime.now()
        );

        applicationEventPublisher.publishEvent(OutboxEvent.create(outbox));
    }
}
