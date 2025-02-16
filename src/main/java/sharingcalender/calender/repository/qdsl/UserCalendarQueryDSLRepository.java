package sharingcalender.calender.repository.qdsl;

import sharingcalender.calender.entity.UserCalendar.Authority;

public interface UserCalendarQueryDSLRepository {

    void deleteUserCalendarByCalendarGroupId(long calendarGroupId);
    void saveWhenInvitationAccepted(long calendarGroupId, long userId);

    Authority getAuthorityForCalendar(long calendarGroupId, String username);

    void deleteUserCalendarByMember(long calendarGroupId, String username);

}
