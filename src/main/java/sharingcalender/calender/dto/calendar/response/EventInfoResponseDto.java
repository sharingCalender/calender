package sharingcalender.calender.dto.calendar.response;

import java.time.LocalDateTime;

public record EventInfoResponseDto(

    long eventId,
    long calendarId,
    String name,
    String title,
    LocalDateTime startDate,
    LocalDateTime endDate,
    String backgroundColor,
    String borderColor,
    String description,
    String writer

){}
