package sharingcalender.calender.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import sharingcalender.calender.messageevent.MessageEvent;
import sharingcalender.calender.messageevent.MessageEventPayload;
import sharingcalender.calender.messageevent.MessageEventType.Topic;
import sharingcalender.calender.resolver.MessageEventResolver;


@Component
@RequiredArgsConstructor
public class CalendarScheduleConsumer {

    private final MessageEventResolver messageEventResolver;

    @KafkaListener(topics = {
        Topic.SCHEDULE
    },
    groupId = "schedule",
    concurrency = "3")
    public void scheduleListen(String message, Acknowledgment acknowledgment){
        MessageEvent<MessageEventPayload> messageEvent = MessageEvent.fromJson(message);

        messageEventResolver.handle(messageEvent);

        acknowledgment.acknowledge();
    }
}
