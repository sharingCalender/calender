package sharingcalender.calender.messageevent;

import lombok.Getter;
import sharingcalender.calender.util.serializer.DataSerializer;

@Getter
public class MessageEvent<T extends MessageEventPayload> {

    private MessageEventType type;
    private T payload;

    private MessageEvent() {}

    private MessageEvent(MessageEventType type, T payload) {
        this.type = type;
        this.payload = payload;
    }

    public static MessageEvent<MessageEventPayload> create(MessageEventType type,
        MessageEventPayload payload){

        return new MessageEvent<>(type,payload);

    }

    public String toJson(){
        return DataSerializer.serialize(this);
    }

    public static MessageEvent<MessageEventPayload> fromJson(String json){
        OutboxEventRaw outboxEventRaw = DataSerializer.deserialize(json,OutboxEventRaw.class);

        MessageEvent<MessageEventPayload> messageEvent = new MessageEvent<>();
        messageEvent.type = MessageEventType.from(outboxEventRaw.type);
        messageEvent.payload = DataSerializer.deserialize(outboxEventRaw.payload,
            messageEvent.getType()
                .getPayloadType());

        return messageEvent;
    }


    @Getter
    private static class OutboxEventRaw{
        private String type;
        private Object payload;
    }



}
