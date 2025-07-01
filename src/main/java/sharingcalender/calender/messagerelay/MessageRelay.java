package sharingcalender.calender.messagerelay;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import sharingcalender.calender.outbox.Outbox;
import sharingcalender.calender.outbox.OutboxEvent;
import sharingcalender.calender.outbox.OutboxRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageRelay {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String,String> messageRelayKafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void createOutbox(OutboxEvent outboxEvent){
        log.info("[MessageRelay.createOutbox] outboxEvent = {}" , outboxEvent);
        outboxRepository.save(outboxEvent.getOutbox());
    }

    @Async("messageRelayPublishEventExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishMessageEvent(OutboxEvent outboxEvent){
        publishMessageEvent(outboxEvent.getOutbox());
    }

    public void publishMessageEvent(Outbox outbox){
        try {
            messageRelayKafkaTemplate.send(
                    outbox.getMessageEventType().getTopic(),
                    outbox.getPayload())
                .get(10, TimeUnit.SECONDS);
            outboxRepository.delete(outbox);
        } catch (Exception e) {
            log.error("[MessageRelay.publishMessageEvent] outbox = ", outbox);
        }
    }

    @Scheduled(
        fixedDelay = 60,
        initialDelay = 10,
        timeUnit = TimeUnit.SECONDS,
        scheduler = "messageRelayPublishPendingEventExecutor"
    )
    public void publishPendingMessageEvent(){
        List<Outbox> outboxes = outboxRepository.findOutboxesByCreatedAtLessThanEqualOrderByCreatedAtAsc(
            LocalDateTime.now().minusSeconds(10),
            Pageable.ofSize(50)
        );

        outboxes.stream().forEach(messageEvent -> publishMessageEvent(messageEvent));

    }


}
