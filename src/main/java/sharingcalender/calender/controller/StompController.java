package sharingcalender.calender.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import sharingcalender.calender.dto.chat.ChatMessageDto;
import sharingcalender.calender.exception.ResourceNotFoundException;
import sharingcalender.calender.service.chat.ChatMessageService;
import sharingcalender.calender.service.chat.RedisPubSubService;

@Controller
@RequiredArgsConstructor
public class StompController {

    private final RedisPubSubService pubSubService;
    private final ObjectMapper objectMapper;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/{chatRoomId}")
    public void sendMessage(@DestinationVariable long chatRoomId,@Payload ChatMessageDto chatMessageDto)
        throws JsonProcessingException {

        if (chatMessageDto.message().equals("error")) {
            throw new ResourceNotFoundException("ResourceNotFoundException");
        }

        String message = objectMapper.writeValueAsString(chatMessageDto);

        System.out.println("publish message: " + message);

        pubSubService.publish("chat", message);

        chatMessageService.saveMessage(chatMessageDto);

    }



}
