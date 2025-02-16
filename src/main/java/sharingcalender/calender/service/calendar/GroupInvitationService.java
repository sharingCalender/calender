package sharingcalender.calender.service.calendar;

import sharingcalender.calender.dto.calendar.response.GroupInvitationInfoListResponse;

public interface GroupInvitationService {

    GroupInvitationInfoListResponse getInvitationList(String username);

    void saveGroupInvitation(String username, String usernameFrom, long calendarGroupId);

    void saveWhenInvitationAccepted(long calendarGroupId, long groupInvitationId, String username);

    void deleteInvitation(long groupInvitationId);
}
