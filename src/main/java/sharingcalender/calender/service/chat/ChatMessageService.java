package sharingcalender.calender.service.chat;


import java.util.List;
import java.util.Map;
import sharingcalender.calender.dto.chat.ChatMessageDto;
import sharingcalender.calender.dto.chat.response.ChatMessageResponseDto;


public interface ChatMessageService {

    Map<String,Object> getRecentMessages(long chatRoomId, String username);

    void saveMessage(ChatMessageDto chatMessageDto);

    List<ChatMessageResponseDto> getMessageListWhenScrollUp(long chatRoomId,
        long chatMessageId);

    List<ChatMessageResponseDto> getMessageListWhenScrollDown(long chatRoomId,
        long chatMessageId);

    List<ChatMessageResponseDto> getAllUnReadMessageListWhenNewMessageInput(long chatRoomId,
        long chatMessageId);
}
