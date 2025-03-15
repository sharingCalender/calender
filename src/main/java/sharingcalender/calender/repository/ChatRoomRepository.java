package sharingcalender.calender.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import sharingcalender.calender.entity.ChatRoom;
import sharingcalender.calender.repository.qdsl.ChatRoomQueryDSLRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>,
    ChatRoomQueryDSLRepository {


    Optional<ChatRoom> findByCalendarGroup_CalendarGroupId(long calendarGroupId);
}
