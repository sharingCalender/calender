package sharingcalender.calender.service.calendar;


import sharingcalender.calender.dto.AuthenticatedUser;
import sharingcalender.calender.dto.calendar.request.CalendarGroupRegisterRequestDto;

public interface CalendarGroupService {

    void registerGroup(CalendarGroupRegisterRequestDto groupRegisterReq, AuthenticatedUser user);

    void deleteGroup(long calendarGroupId);
}
