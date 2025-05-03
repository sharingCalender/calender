package sharingcalender.calender.service.chat.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sharingcalender.calender.dto.chat.ChatMessageDto;
import sharingcalender.calender.dto.chat.response.ChatMessageResponseDto;
import sharingcalender.calender.entity.ChatMessage;
import sharingcalender.calender.entity.ChatRoom;
import sharingcalender.calender.entity.User;
import sharingcalender.calender.exception.ResourceNotFoundException;
import sharingcalender.calender.repository.ChatMessageRepository;
import sharingcalender.calender.repository.ChatRoomRepository;
import sharingcalender.calender.repository.UserRepository;
import sharingcalender.calender.service.chat.ChatMessageService;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    public void saveMessage(ChatMessageDto chatMessageDto) {
        Optional<User> user = userRepository.findByUsername(chatMessageDto.senderEmail());

        if (user.isEmpty()) {
            throw new ResourceNotFoundException("can not find user");
        }

        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(chatMessageDto.chatRoomId());

        if (chatRoom.isEmpty()) {
            throw new ResourceNotFoundException("can not find chat room");
        }

        chatMessageRepository.save(
            ChatMessage.create(chatRoom.get(), user.get(), chatMessageDto.message(),
                LocalDateTime.now()));
    }

    public Map<String,Object> getRecentMessages(long chatRoomId, String username) {

        Map<String, Object> responseMap = new HashMap<>();

        List<ChatMessageResponseDto> afterHistory = chatMessageRepository.getAfterHistory(chatRoomId,
            username);

        List<ChatMessageResponseDto> beforeHistory = chatMessageRepository.getBeforeHistory(chatRoomId,
            username);

        if (afterHistory.isEmpty()) {
            responseMap.put("unReadMessageId", 0L);
        } else {
            responseMap.put("unReadMessageId", afterHistory.get(0).messageId());
        }

        beforeHistory.addAll(afterHistory);

        responseMap.put("messages", beforeHistory);
        return responseMap;
    }

    public List<ChatMessageResponseDto> getMessageListWhenScrollUp(long chatRoomId,
        long chatMessageId) {

        return chatMessageRepository.getMessageListWhenScrollUp(chatRoomId, chatMessageId);
    }

    public List<ChatMessageResponseDto> getMessageListWhenScrollDown(long chatRoomId,
        long chatMessageId) {

        return chatMessageRepository.getMessageListWhenScrollDown(chatRoomId, chatMessageId);
    }

    public List<ChatMessageResponseDto> getAllUnReadMessageListWhenNewMessageInput(long chatRoomId,
        long chatMessageId) {

        return chatMessageRepository.getAllUnReadMessageListWhenNewMessageInput(chatRoomId, chatMessageId);
    }




}
