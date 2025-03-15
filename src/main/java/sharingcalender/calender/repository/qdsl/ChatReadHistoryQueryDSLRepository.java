package sharingcalender.calender.repository.qdsl;

import java.time.LocalDateTime;

public interface ChatReadHistoryQueryDSLRepository {



    void leaveChatRoom(long chatRoomId, String username, LocalDateTime localDateTime);

    void deleteAllByCalendarGroupId(long calendarGroupId);

    void deleteByCalendarGroupIdAndUsername(long calendarGroupId, String username);
}
