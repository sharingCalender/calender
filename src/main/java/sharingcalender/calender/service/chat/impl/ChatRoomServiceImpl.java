package sharingcalender.calender.service.chat.impl;

import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sharingcalender.calender.dto.chat.response.ChatRoomInfoResponseDto;
import sharingcalender.calender.entity.ChatRoom;
import sharingcalender.calender.exception.BadRequestException;
import sharingcalender.calender.exception.ResourceNotFoundException;
import sharingcalender.calender.repository.ChatRoomRepository;
import sharingcalender.calender.service.chat.ChatRoomService;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public long getChatRoomId(long calendarGroupId) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findByCalendarGroup_CalendarGroupId(
            calendarGroupId);

        if (chatRoom.isEmpty()) {
            throw new BadRequestException("calendarGroupId is wrong");
        }

        return chatRoom.get().getChatRoomId();
    }

    public ChatRoomInfoResponseDto findChatRoomInfoByCalendarGroupIdAndUsername(
        long calendarGroupId, String username) {

        ChatRoomInfoResponseDto chatRoomInfo = chatRoomRepository.findChatRoomInfoByCalendarGroupIdAndUsername(
            calendarGroupId, username);

        if (Objects.isNull(chatRoomInfo)) {
            throw new ResourceNotFoundException("can not find chat Room");
        }

        return chatRoomInfo;
    }
}
