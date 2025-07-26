package sharingcalender.calender.service.calendar;


import sharingcalender.calender.dto.calendar.request.EventChangeColorRequestDto;
import sharingcalender.calender.dto.calendar.request.EventDeleteRequestDto;
import sharingcalender.calender.dto.calendar.request.EventModifyRequestDto;
import sharingcalender.calender.dto.calendar.request.EventRegisterRequestDto;
import sharingcalender.calender.dto.calendar.response.EventListResponseDto;

public interface EventService {

    EventListResponseDto getEventsInCalendar(long calendarGroupId, String username, String start,
        String end);

    long registerEvent(EventRegisterRequestDto eventRegisterReq, String username);

    void modifyEvent(EventModifyRequestDto eventModifyReq);

    void deleteEvent(EventDeleteRequestDto eventDeleteReq);

    void changeEventColor(EventChangeColorRequestDto eventChangeColorReq);
}
