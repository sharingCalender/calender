package sharingcalender.calender.advice;

import java.io.IOException;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.ControllerAdvice;
import sharingcalender.calender.exception.ResourceNotFoundException;


@ControllerAdvice
@RequiredArgsConstructor
public class StompControllerAdvice {

    private static final String STOMP_ERROR_PATH = "/queue/errors/";

    private final SimpMessageSendingOperations messageTemplate;

    @MessageExceptionHandler(RuntimeException.class)
    public void handleRuntimeException(Message<?> message, RuntimeException e) {

        SimpMessageHeaderAccessor messageHeaderAccessor = SimpMessageHeaderAccessor.wrap(message);

        Principal user = messageHeaderAccessor.getUser();
        String subscribeDestination =
            STOMP_ERROR_PATH + messageHeaderAccessor.getFirstNativeHeader("chatRoomId");

        messageTemplate.convertAndSendToUser(user.getName(), subscribeDestination, "메시지 전송에 문제가 생겼습니다.");

    }

    @MessageExceptionHandler(IOException.class)
    public void handleIOException(Message<?> message, IOException e) {

        SimpMessageHeaderAccessor messageHeaderAccessor = SimpMessageHeaderAccessor.wrap(message);

        Principal user = messageHeaderAccessor.getUser();

        String subscribeDestination =
            STOMP_ERROR_PATH + messageHeaderAccessor.getFirstNativeHeader("chatRoomId");

        messageTemplate.convertAndSendToUser(user.getName(), subscribeDestination,
            "메시지 전송에 문제가 생겼습니다.");
    }

    @MessageExceptionHandler(ResourceNotFoundException.class)
    public void handleResourceNotFoundException(Message<?> message, ResourceNotFoundException e) {

        SimpMessageHeaderAccessor messageHeaderAccessor = SimpMessageHeaderAccessor.wrap(message);

        Principal user = messageHeaderAccessor.getUser();

        String subscribeDestination =
            STOMP_ERROR_PATH + messageHeaderAccessor.getFirstNativeHeader("chatRoomId");

        messageTemplate.convertAndSendToUser(user.getName(), subscribeDestination, e.getMessage());

    }
}
