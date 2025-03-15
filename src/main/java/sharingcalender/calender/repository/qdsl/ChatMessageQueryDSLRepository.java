package sharingcalender.calender.repository.qdsl;

import java.util.List;
import sharingcalender.calender.dto.chat.response.ChatMessageResponseDto;

public interface ChatMessageQueryDSLRepository {

    List<ChatMessageResponseDto> getBeforeHistory(long chatRoomId, String username);

    List<ChatMessageResponseDto> getAfterHistory(long chatRoomId, String username);

    List<ChatMessageResponseDto> getMessageListWhenScrollUp(long chatRoomId,
        long chatMessageId);

    List<ChatMessageResponseDto> getMessageListWhenScrollDown(long chatRoomId,
        long chatMessageId);

    List<ChatMessageResponseDto> getAllUnReadMessageListWhenNewMessageInput(long chatRoomId,
        long chatMessageId);

    void deleteAllByCalendarGroupId(long calendarGroupId);
}
