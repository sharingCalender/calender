package sharingcalender.calender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sharingcalender.calender.entity.ChatReadHistory;
import sharingcalender.calender.repository.qdsl.ChatReadHistoryQueryDSLRepository;

public interface ChatReadHistoryRepository extends JpaRepository<ChatReadHistory, Long>,
    ChatReadHistoryQueryDSLRepository {


}
