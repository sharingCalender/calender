package sharingcalender.calender.dto.chat.response;

public record ChatMessageResponseDto (
    long chatRoomId,
    String message,
    String senderEmail,
    long messageId,
    String name
){}
