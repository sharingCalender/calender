package sharingcalender.calender.outbox;

import lombok.Getter;

@Getter
public class OutboxEvent {

    private Outbox outbox;

    private OutboxEvent(Outbox outbox) {
        this.outbox = outbox;
    }

    public static OutboxEvent create(Outbox outbox){
        return new OutboxEvent(outbox);
    }
}
