package sharingcalender.calender.service.calendar;

import java.util.List;
import sharingcalender.calender.dto.user.response.UserEmailSendingInfoResponseDto;

public interface UserGroupService {

    List<UserEmailSendingInfoResponseDto> getUserListInGroup(long calendarGroupId);
}
