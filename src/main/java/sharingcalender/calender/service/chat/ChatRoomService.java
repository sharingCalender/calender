package sharingcalender.calender.service.chat;

import sharingcalender.calender.dto.chat.response.ChatRoomInfoResponseDto;

public interface ChatRoomService {

    long getChatRoomId(long calendarGroupId);

    ChatRoomInfoResponseDto findChatRoomInfoByCalendarGroupIdAndUsername(
        long calendarGroupId, String username);
}
