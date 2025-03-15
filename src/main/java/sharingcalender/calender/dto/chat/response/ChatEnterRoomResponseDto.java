package sharingcalender.calender.dto.chat.response;

import java.util.List;

public record ChatEnterRoomResponseDto (
    String email,
    long roomId,
    List<ChatMessageResponseDto> messages,
    long unReadMessageId,
    String groupName,
    String name
){}
