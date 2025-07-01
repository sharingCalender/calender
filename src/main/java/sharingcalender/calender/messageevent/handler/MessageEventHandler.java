package sharingcalender.calender.messageevent.handler;

import sharingcalender.calender.messageevent.MessageEvent;
import sharingcalender.calender.messageevent.MessageEventPayload;

public interface MessageEventHandler<T extends MessageEventPayload> {
    void handle(MessageEvent<T> messageEvent);
    boolean isSupports(MessageEvent<T> messageEvent);
}
