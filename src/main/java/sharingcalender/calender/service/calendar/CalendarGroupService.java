package sharingcalender.calender.service.calendar;


import sharingcalender.calender.dto.AuthenticatedUser;
import sharingcalender.calender.dto.calendar.request.CalendarGroupRegisterRequestDto;
import sharingcalender.calender.dto.calendar.response.CalendarGroupListResponseDto;

public interface CalendarGroupService {

    void registerGroup(CalendarGroupRegisterRequestDto groupRegisterReq, String username);

    void deleteGroup(long calendarGroupId,String username);

    CalendarGroupListResponseDto getGroupInfoList(String username);
}
