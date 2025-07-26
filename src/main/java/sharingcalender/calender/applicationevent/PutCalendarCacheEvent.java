package sharingcalender.calender.applicationevent;

import java.time.LocalDateTime;
import sharingcalender.calender.dto.calendar.response.EventListResponseDto;

public record PutCalendarCacheEvent (
    long calendarGroupId,
    LocalDateTime start,
    EventListResponseDto eventList
) {

    public static PutCalendarCacheEvent create(long calendarGroupId, LocalDateTime start,
        EventListResponseDto eventList) {
        return new PutCalendarCacheEvent(calendarGroupId, start, eventList);
    }
}
