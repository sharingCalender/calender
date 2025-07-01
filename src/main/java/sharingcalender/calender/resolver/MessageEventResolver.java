package sharingcalender.calender.resolver;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sharingcalender.calender.exception.MessageEventHandlerNotFoundException;
import sharingcalender.calender.messageevent.MessageEvent;
import sharingcalender.calender.messageevent.MessageEventPayload;
import sharingcalender.calender.messageevent.handler.MessageEventHandler;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageEventResolver {

    private final List<MessageEventHandler> messageEventHandlers;

    public void handle(MessageEvent<MessageEventPayload> messageEvent) {
        MessageEventHandler handler = findHandler(messageEvent);

        if (Objects.nonNull(handler)) {
            handler.handle(messageEvent);

        } else {
            log.error(
                "[MessageEventResolver.handle] Can Not Find Handler, Fail mailing, EventType = {}",
                messageEvent.getType().toString());

            throw new MessageEventHandlerNotFoundException("Can Not Find Handler, Fail mailing");
        }

    }

    public MessageEventHandler findHandler(MessageEvent<MessageEventPayload> messageEvent) {
        return messageEventHandlers.stream()
            .filter(handler -> handler.isSupports(messageEvent))
            .findAny()
            .orElse(null);

    }
}
