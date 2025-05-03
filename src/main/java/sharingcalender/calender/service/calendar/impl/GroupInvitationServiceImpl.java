package sharingcalender.calender.service.calendar.impl;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sharingcalender.calender.dto.calendar.response.GroupInvitationInfo;
import sharingcalender.calender.dto.calendar.response.GroupInvitationInfoListResponse;
import sharingcalender.calender.entity.CalendarGroup;
import sharingcalender.calender.entity.ChatReadHistory;
import sharingcalender.calender.entity.ChatRoom;
import sharingcalender.calender.entity.GroupInvitation;
import sharingcalender.calender.entity.User;
import sharingcalender.calender.entity.UserGroup;
import sharingcalender.calender.exception.AlreadyExistException;
import sharingcalender.calender.exception.BadRequestException;
import sharingcalender.calender.repository.CalendarGroupRepository;
import sharingcalender.calender.repository.ChatReadHistoryRepository;
import sharingcalender.calender.repository.ChatRoomRepository;
import sharingcalender.calender.repository.GroupInvitationRepository;
import sharingcalender.calender.repository.UserCalendarRepository;
import sharingcalender.calender.repository.UserGroupRepository;
import sharingcalender.calender.repository.UserRepository;
import sharingcalender.calender.service.ResourceValidator;
import sharingcalender.calender.service.calendar.GroupInvitationService;

// refactoring
@Service
@RequiredArgsConstructor
@Transactional
public class GroupInvitationServiceImpl implements GroupInvitationService {

    private final UserRepository userRepository;
    private final CalendarGroupRepository calendarGroupRepository;
    private final GroupInvitationRepository groupInvitationRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserCalendarRepository userCalendarRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatReadHistoryRepository chatReadHistoryRepository;
    private final ResourceValidator resourceValidator;

    @Transactional(readOnly = true)
    public GroupInvitationInfoListResponse getInvitationList(String username) {

        List<GroupInvitationInfo> groupInvitationList = groupInvitationRepository.getGroupInvitationList(
            username);

        return new GroupInvitationInfoListResponse(groupInvitationList);
    }

    public void saveGroupInvitation(String username, String usernameFrom, long calendarGroupId) {
        User user = validateMemberWhenInvited(username, usernameFrom, calendarGroupId);

        CalendarGroup calendarGroup = resourceValidator.validateCalendarGroup(calendarGroupId);

        groupInvitationRepository.save(GroupInvitation.create(user, calendarGroup, usernameFrom));

    }



    private User validateMemberWhenInvited(String username, String usernameFrom, long calendarGroupId) {
        User invitatedUser = resourceValidator.validateUser(username);

        if (invitatedUser.getUsername().equals(usernameFrom)) {
            throw new BadRequestException("본인은 초대할 수 없습니다.");
        }

        if (userGroupRepository.userIsExist(calendarGroupId, username)) {
            throw new AlreadyExistException("이미 그룹에 존재하는 유저입니다.");
        }
        return invitatedUser;
    }

    public void saveWhenInvitationAccepted(long calendarGroupId,long groupInvitationId ,String username) {
        User user = resourceValidator.validateUser(username);

        CalendarGroup calendarGroup = resourceValidator.validateCalendarGroup(calendarGroupId);

        ChatRoom chatRoom = resourceValidator.validateChatRoom(calendarGroupId);

        userGroupRepository.save(UserGroup.create(user, calendarGroup));

        userCalendarRepository.saveWhenInvitationAccepted(calendarGroupId, user.getUserId());

        groupInvitationRepository.deleteById(groupInvitationId);

        chatReadHistoryRepository.save(ChatReadHistory.create(chatRoom, user, LocalDateTime.now()));

    }




    public void deleteInvitation(long groupInvitationId) {

        groupInvitationRepository.deleteById(groupInvitationId);
    }







}
