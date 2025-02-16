package sharingcalender.calender.repository.qdsl;

import java.util.List;
import sharingcalender.calender.dto.calendar.response.GroupInvitationInfo;

public interface GroupInvitationQueryDSLRepository {

    List<GroupInvitationInfo> getGroupInvitationList(String username);
}
