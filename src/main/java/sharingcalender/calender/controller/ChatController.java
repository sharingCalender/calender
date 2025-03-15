package sharingcalender.calender.controller;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sharingcalender.calender.dto.AuthenticatedUser;
import sharingcalender.calender.dto.chat.request.ChatLeaveRoomRequestDto;
import sharingcalender.calender.dto.chat.response.ChatEnterRoomResponseDto;
import sharingcalender.calender.dto.chat.response.ChatMessageResponseDto;
import sharingcalender.calender.dto.chat.response.ChatRoomInfoResponseDto;
import sharingcalender.calender.dto.chat.response.ChatScrollResponseDto;
import sharingcalender.calender.exception.BadRequestException;
import sharingcalender.calender.service.chat.ChatMessageService;
import sharingcalender.calender.service.chat.ChatReadHistoryService;
import sharingcalender.calender.service.chat.ChatRoomService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/calendar/chat")
public class ChatController {

    private final ChatReadHistoryService chatReadHistoryService;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    @GetMapping("/enter")
    public ResponseEntity<ChatEnterRoomResponseDto> enterChatRoom(
        @RequestParam("calendarGroupId") long calendarGroupId,
        @AuthenticationPrincipal AuthenticatedUser user) {

        if (calendarGroupId <= 0) {
            throw new BadRequestException("calendarGroupId is wrong");
        }

        ChatRoomInfoResponseDto chatRoomInfo = chatRoomService.findChatRoomInfoByCalendarGroupIdAndUsername(
            calendarGroupId, user.getUsername());

        List<ChatMessageResponseDto> messages;

        Map<String, Object> recentMessages = chatMessageService.getRecentMessages(chatRoomInfo.chatRoomId(),
            user.getUsername());
        messages = (List<ChatMessageResponseDto>) recentMessages.get("messages");
        long unReadMessageId = (long) recentMessages.get("unReadMessageId");

        ChatEnterRoomResponseDto chatEnterRoomResponseDto = new ChatEnterRoomResponseDto(
            user.getUsername(), chatRoomInfo.chatRoomId(), messages, unReadMessageId,
            chatRoomInfo.groupName(), chatRoomInfo.name());

        return ResponseEntity.status(HttpStatus.OK).body(chatEnterRoomResponseDto);

    }


    @GetMapping("/scrollUp")
    public ResponseEntity<ChatScrollResponseDto> getMessagesWhenScrollUp(
        @RequestParam("chatRoomId") long chatRoomId,
        @RequestParam("chatMessageId") long chatMessageId) {

        if (chatRoomId <= 0) {
            throw new BadRequestException("chatRoomId is wrong");
        }
        if (chatMessageId <= 0) {
            throw new BadRequestException("chatMessageId is wrong");
        }

        List<ChatMessageResponseDto> messageListWhenScrollUp = chatMessageService.getMessageListWhenScrollUp(
            chatRoomId, chatMessageId);

        return ResponseEntity.status(HttpStatus.OK)
            .body(new ChatScrollResponseDto(messageListWhenScrollUp));

    }

    @GetMapping("/scrollDown")
    public ResponseEntity<ChatScrollResponseDto> getMessagesWhenScrollDown(
        @RequestParam("chatRoomId") long chatRoomId,
        @RequestParam("chatMessageId") long chatMessageId) {

        if (chatRoomId <= 0) {
            throw new BadRequestException("chatRoomId is wrong");
        }
        if (chatMessageId <= 0) {
            throw new BadRequestException("chatMessageId is wrong");
        }

        List<ChatMessageResponseDto> messageListWhenScrollDown = chatMessageService.getMessageListWhenScrollDown(
            chatRoomId, chatMessageId);

        return ResponseEntity.status(HttpStatus.OK)
            .body(new ChatScrollResponseDto(messageListWhenScrollDown));

    }

    @GetMapping("/unReadMessages")
    public ResponseEntity<ChatScrollResponseDto> getAllUnReadMessagesWhenNewMessageInput(
        @RequestParam("chatRoomId") long chatRoomId,
        @RequestParam("chatMessageId") long chatMessageId) {

        if (chatRoomId <= 0) {
            throw new BadRequestException("chatRoomId is wrong");
        }
        if (chatMessageId <= 0) {
            throw new BadRequestException("chatMessageId is wrong");
        }

        List<ChatMessageResponseDto> messageListWhenScrollDown = chatMessageService.getAllUnReadMessageListWhenNewMessageInput(
            chatRoomId, chatMessageId);

        return ResponseEntity.status(HttpStatus.OK)
            .body(new ChatScrollResponseDto(messageListWhenScrollDown));

    }

    @PostMapping("/leave")
    public ResponseEntity<Void> leaveChatRoom(
        @RequestBody @Valid ChatLeaveRoomRequestDto chatLeaveRoomRequestDto,
        BindingResult bindingResult, @AuthenticationPrincipal AuthenticatedUser user) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("leaving room request is wrong");
        }

        chatReadHistoryService.leaveChatRoom(chatLeaveRoomRequestDto.chatRoomId(),
            user.getUsername());

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
