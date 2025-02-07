package sharingcalender.calender.dto.calendar.response;

import java.util.List;

public record CalendarLookUpResponseDto (
    long calendarId,
    List<EventInfoResponseDto> eventInfo
){}
