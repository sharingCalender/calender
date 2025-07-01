package sharingcalender.calender.repository.qdsl;

import java.util.List;
import sharingcalender.calender.dto.user.response.UserEmailSendingInfoResponseDto;

public interface UserGroupQueryDSLRepository {

    void deleteByCalendarGroupId(long calendarGroupId);

    void deleteUserGroupByMember(long calendarGroupId, String username);

    boolean userIsExist(long calendarGroupId, String username);

    List<UserEmailSendingInfoResponseDto> getUserListInGroup(long calendarGroupId);
}
