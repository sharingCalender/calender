package sharingcalender.calender.dto.chat;

public record ChatMessageDto(
    long chatRoomId,
    String message,
    String senderEmail,
    String name
) {}
