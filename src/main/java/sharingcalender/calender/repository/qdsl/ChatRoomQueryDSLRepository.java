package sharingcalender.calender.repository.qdsl;

import sharingcalender.calender.dto.chat.response.ChatRoomInfoResponseDto;

public interface ChatRoomQueryDSLRepository {

    void deleteChatRoomByCalendarGroupId(long calendarGroupId);

    ChatRoomInfoResponseDto findChatRoomInfoByCalendarGroupIdAndUsername(
        long calendarGroupId, String username);
}
