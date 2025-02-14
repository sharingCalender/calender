package sharingcalender.calender.repository.qdsl;

import java.util.List;
import sharingcalender.calender.dto.calendar.response.CalendarGroupInfoDto;

public interface CalendarGroupQueryDSLRepository {

    List<CalendarGroupInfoDto> getGroupInfoList(String username);
}
