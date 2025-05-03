package sharingcalender.calender.repository.qdsl;

import java.time.LocalDateTime;
import java.util.List;
import sharingcalender.calender.dto.calendar.response.EventInfoResponseDto;

public interface EventQueryDSLRepository {

    void deleteEventByCalendarGroupId(long calendarGroupId);

    List<EventInfoResponseDto> getAllEventsInCalendarByCalendarGroupId(long calendarGroupId,
        LocalDateTime start, LocalDateTime end);


    List<EventInfoResponseDto> getAllEventsInCalendarByEventId(List<Long> eventIds);
}
