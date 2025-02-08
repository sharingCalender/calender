package sharingcalender.calender.dto.calendar.response;

import java.time.LocalDateTime;

public record EventInfoResponseDto(

    long id,
    long calendarId,
    String name,
    String title,
    LocalDateTime start,
    LocalDateTime end,
    String backgroundColor,
    String borderColor,
    String description,
    String writer

){}
