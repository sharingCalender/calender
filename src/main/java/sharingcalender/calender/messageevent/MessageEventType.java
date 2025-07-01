package sharingcalender.calender.messageevent;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import sharingcalender.calender.messageevent.payload.ScheduleCreatedEventPayload;

@Getter
@Slf4j
public enum MessageEventType {

    SCHEDULE_CREATED(ScheduleCreatedEventPayload.class,Topic.SCHEDULE);

    private final Class<? extends MessageEventPayload> payloadType;
    private final String topic;

    MessageEventType(Class<? extends MessageEventPayload> payloadType, String topic) {
        this.payloadType = payloadType;
        this.topic = topic;
    }

    public static MessageEventType from(String type){
        try{
            return valueOf(type);
        } catch (Exception e){
            log.error("[OutboxEventType.from] type = ", type, e);

            return null;
        }
    }

    public static class Topic{
        public static final String SCHEDULE = "schedule";
    }
}
