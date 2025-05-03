package sharingcalender.calender.service.calendar.impl;


import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sharingcalender.calender.dto.AuthenticatedUser;
import sharingcalender.calender.dto.calendar.request.CalendarGroupRegisterRequestDto;
import sharingcalender.calender.dto.calendar.response.CalendarGroupListResponseDto;
import sharingcalender.calender.entity.Calendar;
import sharingcalender.calender.entity.CalendarGroup;
import sharingcalender.calender.entity.ChatReadHistory;
import sharingcalender.calender.entity.ChatRoom;
import sharingcalender.calender.entity.User;
import sharingcalender.calender.entity.UserCalendar;
import sharingcalender.calender.entity.UserCalendar.Authority;
import sharingcalender.calender.entity.UserGroup;
import sharingcalender.calender.repository.CalendarGroupRepository;
import sharingcalender.calender.repository.CalendarRepository;
import sharingcalender.calender.repository.ChatMessageRepository;
import sharingcalender.calender.repository.ChatReadHistoryRepository;
import sharingcalender.calender.repository.ChatRoomRepository;
import sharingcalender.calender.repository.EventRepository;
import sharingcalender.calender.repository.UserCalendarRepository;
import sharingcalender.calender.repository.UserGroupRepository;
import sharingcalender.calender.repository.UserRepository;
import sharingcalender.calender.service.ResourceValidator;
import sharingcalender.calender.service.calendar.CalendarGroupService;

//refactoring
@Service
@RequiredArgsConstructor
@Transactional
public class CalendarGroupServiceImpl implements CalendarGroupService {



    private final CalendarGroupRepository calendarGroupRepository;
    private final CalendarRepository calendarRepository;
    private final UserCalendarRepository userCalendarRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatReadHistoryRepository chatReadHistoryRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ResourceValidator resourceValidator;

    @Transactional(readOnly = true)
    @Override
    public CalendarGroupListResponseDto getGroupInfoList(String username) {

        CalendarGroupListResponseDto calendarGroupListResponseDto = new CalendarGroupListResponseDto(
            calendarGroupRepository.getGroupInfoList(username));

        return calendarGroupListResponseDto;

    }

    @Override
    public void registerGroup(CalendarGroupRegisterRequestDto groupRegisterReq, AuthenticatedUser user) {

        CalendarGroup calendarGroup = calendarGroupRepository.save(
            CalendarGroup.create(groupRegisterReq.groupName()));

        Calendar calendar = createCalendarToGroup(calendarGroup);

        User userEntity = resourceValidator.validateUser(user.getUsername());

        registerUserToGroup(calendar, userEntity, calendarGroup);

        createChatRoomToGroup(calendarGroup, userEntity);
    }

    private Calendar createCalendarToGroup(CalendarGroup calendarGroup) {
        Calendar calendar = calendarRepository.save(Calendar.create(calendarGroup));
        return calendar;
    }

    private void createChatRoomToGroup(CalendarGroup calendarGroup, User userEntity) {
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create(calendarGroup));

        chatReadHistoryRepository.save(
            ChatReadHistory.create(chatRoom, userEntity, LocalDateTime.now()));
    }

    private void registerUserToGroup(Calendar calendar, User userEntity, CalendarGroup calendarGroup) {
        userCalendarRepository.save(UserCalendar.create(calendar, userEntity, Authority.ADMIN));

        userGroupRepository.save(UserGroup.create(userEntity, calendarGroup));
    }

    @Override
    public void deleteGroup(long calendarGroupId,String username) {

        Authority authorityForCalendar = userCalendarRepository.getAuthorityForCalendar(
            calendarGroupId, username);

        if (Authority.ADMIN == authorityForCalendar) {
            deleteGroupByAdmin(calendarGroupId);
        } else {
            //TODO 채팅룸 기록을 지워줘야한다. 메시지는 지우지 않을 것이다.
            deleteGroupByMember(calendarGroupId, username);
        }
    }

    private void deleteGroupByMember(long calendarGroupId, String username) {
        deleteUserFromChatRoom(calendarGroupId, username);

        deleteUserFromGroup(calendarGroupId, username);
    }

    private void deleteUserFromGroup(long calendarGroupId, String username) {
        userCalendarRepository.deleteUserCalendarByMember(calendarGroupId, username);

        userGroupRepository.deleteUserGroupByMember(calendarGroupId, username);
    }

    private void deleteUserFromChatRoom(long calendarGroupId, String username) {
        chatReadHistoryRepository.deleteByCalendarGroupIdAndUsername(calendarGroupId, username);
    }

    private void deleteGroupByAdmin(long calendarGroupId) {
        deleteAllUserFromGroup(calendarGroupId);

        deleteCalendarAndEventsFromGroup(calendarGroupId);

        deleteChatRoomFromGroup(calendarGroupId);

        deleteGroup(calendarGroupId);
    }

    private void deleteGroup(long calendarGroupId) {
        calendarGroupRepository.deleteByCalendarGroupId(calendarGroupId);
    }

    private void deleteChatRoomFromGroup(long calendarGroupId) {
        chatReadHistoryRepository.deleteAllByCalendarGroupId(calendarGroupId);

        chatMessageRepository.deleteAllByCalendarGroupId(calendarGroupId);

        chatRoomRepository.deleteChatRoomByCalendarGroupId(calendarGroupId);
    }

    private void deleteCalendarAndEventsFromGroup(long calendarGroupId) {
        eventRepository.deleteEventByCalendarGroupId(calendarGroupId);

        calendarRepository.deleteByCalendarGroupId(calendarGroupId);
    }

    private void deleteAllUserFromGroup(long calendarGroupId) {
        userCalendarRepository.deleteUserCalendarByCalendarGroupId(calendarGroupId);

        userGroupRepository.deleteByCalendarGroupId(calendarGroupId);
    }


}
