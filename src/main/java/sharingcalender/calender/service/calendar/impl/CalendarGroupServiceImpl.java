package sharingcalender.calender.service.calendar.impl;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Optional;
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
import sharingcalender.calender.exception.UnAuthorizedException;
import sharingcalender.calender.repository.CalendarGroupRepository;
import sharingcalender.calender.repository.CalendarRepository;
import sharingcalender.calender.repository.ChatMessageRepository;
import sharingcalender.calender.repository.ChatReadHistoryRepository;
import sharingcalender.calender.repository.ChatRoomRepository;
import sharingcalender.calender.repository.EventRepository;
import sharingcalender.calender.repository.UserCalendarRepository;
import sharingcalender.calender.repository.UserGroupRepository;
import sharingcalender.calender.repository.UserRepository;
import sharingcalender.calender.service.calendar.CalendarGroupService;

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
            new CalendarGroup(groupRegisterReq.groupName()));

        Calendar calendar = calendarRepository.save(new Calendar(calendarGroup));

        Optional<User> userEntity = userRepository.findByUsername(user.getUsername());

        if (userEntity.isEmpty()) {
            throw new UnAuthorizedException("User Is Not Valid");
        }

        userCalendarRepository.save(
            new UserCalendar(calendar, userEntity.get(), Authority.ADMIN));

        userGroupRepository.save(new UserGroup(userEntity.get(), calendarGroup));

        ChatRoom chatRoom = chatRoomRepository.save(new ChatRoom(calendarGroup));

        chatReadHistoryRepository.save(
            new ChatReadHistory(chatRoom, userEntity.get(), LocalDateTime.now()));
    }

    @Override
    public void deleteGroup(long calendarGroupId,String username) {

        Authority authorityForCalendar = userCalendarRepository.getAuthorityForCalendar(
            calendarGroupId, username);



        if (Authority.ADMIN == authorityForCalendar) {
            userCalendarRepository.deleteUserCalendarByCalendarGroupId(calendarGroupId);

            userGroupRepository.deleteByCalendarGroupId(calendarGroupId);

            eventRepository.deleteEventByCalendarGroupId(calendarGroupId);

            calendarRepository.deleteByCalendarGroupId(calendarGroupId);


            chatReadHistoryRepository.deleteAllByCalendarGroupId(calendarGroupId);

            chatMessageRepository.deleteAllByCalendarGroupId(calendarGroupId);

            chatRoomRepository.deleteChatRoomByCalendarGroupId(calendarGroupId);

            calendarGroupRepository.deleteByCalendarGroupId(calendarGroupId);


        } else {

            //TODO 채팅룸 기록을 지워줘야한다. 메시지는 지우지 않을 것이다.
            chatReadHistoryRepository.deleteByCalendarGroupIdAndUsername(calendarGroupId, username);

            userCalendarRepository.deleteUserCalendarByMember(calendarGroupId, username);

            userGroupRepository.deleteUserGroupByMember(calendarGroupId, username);
        }



    }




}
