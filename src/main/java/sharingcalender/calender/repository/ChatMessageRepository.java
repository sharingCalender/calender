package sharingcalender.calender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sharingcalender.calender.entity.ChatMessage;
import sharingcalender.calender.repository.qdsl.ChatMessageQueryDSLRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>,
    ChatMessageQueryDSLRepository {


}
