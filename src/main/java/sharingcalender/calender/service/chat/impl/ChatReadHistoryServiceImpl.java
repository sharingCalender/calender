package sharingcalender.calender.service.chat.impl;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sharingcalender.calender.repository.ChatReadHistoryRepository;
import sharingcalender.calender.service.chat.ChatReadHistoryService;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatReadHistoryServiceImpl implements ChatReadHistoryService {

    private final ChatReadHistoryRepository chatReadHistoryRepository;


    public void leaveChatRoom(long chatRoomId, String username) {
        chatReadHistoryRepository.leaveChatRoom(chatRoomId, username, LocalDateTime.now());
    }
}
