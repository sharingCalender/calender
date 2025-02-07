package sharingcalender.calender.service.calendar;

import java.util.List;
import sharingcalender.calender.dto.calendar.request.EventDeleteRequestDto;
import sharingcalender.calender.dto.calendar.request.EventModifyRequestDto;
import sharingcalender.calender.dto.calendar.request.EventRegisterRequestDto;
import sharingcalender.calender.dto.calendar.response.EventInfoResponseDto;

public interface EventService {

    List<EventInfoResponseDto> getAllEventsInCalendar(long calendarGroupId, String username);

    void registerEvent(EventRegisterRequestDto eventRegisterReq, String username);

    void modifyEvent(EventModifyRequestDto eventModifyReq);

    void deleteEvent(EventDeleteRequestDto eventDeleteReq);
}
