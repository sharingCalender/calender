package sharingcalender.calender.service.calendar.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
import sharingcalender.calender.exception.ResourceNotFoundException;
import sharingcalender.calender.repository.CalendarGroupRepository;
import sharingcalender.calender.repository.ChatReadHistoryRepository;
import sharingcalender.calender.repository.ChatRoomRepository;
import sharingcalender.calender.repository.GroupInvitationRepository;
import sharingcalender.calender.repository.UserCalendarRepository;
import sharingcalender.calender.repository.UserGroupRepository;
import sharingcalender.calender.repository.UserRepository;
import sharingcalender.calender.service.calendar.GroupInvitationService;

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

    public GroupInvitationInfoListResponse getInvitationList(String username) {

        List<GroupInvitationInfo> groupInvitationList = groupInvitationRepository.getGroupInvitationList(
            username);

        return new GroupInvitationInfoListResponse(groupInvitationList);
    }

    public void saveGroupInvitation(String username, String usernameFrom, long calendarGroupId) {
        Optional<User> userTo = userRepository.findByUsername(username);

        if (userTo.isEmpty()) {
            throw new ResourceNotFoundException("초대하려는 회원아이디를 찿을 수 없습니다.");
        }


        User user = userTo.get();

        if (user.getUsername().equals(usernameFrom)) {
            throw new BadRequestException("본인은 초대할 수 없습니다.");
        }

        if (userGroupRepository.userIsExist(calendarGroupId, username)) {
            throw new AlreadyExistException("이미 그룹에 존재하는 유저입니다.");
        }



        Optional<CalendarGroup> calendarGroup = calendarGroupRepository.findById(calendarGroupId);

        if (calendarGroup.isEmpty()) {
            throw new ResourceNotFoundException("해당 그룹을 찿을 수 없습니다.");
        }

        groupInvitationRepository.save(
            new GroupInvitation(user, calendarGroup.get(), usernameFrom));


    }

    public void saveWhenInvitationAccepted(long calendarGroupId,long groupInvitationId ,String username) {
        Optional<User> user = userRepository.findByUsername(username);

        Optional<CalendarGroup> calendarGroup = calendarGroupRepository.findById(calendarGroupId);

        if (calendarGroup.isEmpty()) {
            throw new ResourceNotFoundException("그룹 참여에 실패했습니다.");
        }

        userGroupRepository.save(new UserGroup(user.get(),calendarGroup.get()));

        userCalendarRepository.saveWhenInvitationAccepted(calendarGroupId, user.get().getUserId());

        groupInvitationRepository.deleteById(groupInvitationId);

        Optional<ChatRoom> chatRoom = chatRoomRepository.findByCalendarGroup_CalendarGroupId(
            calendarGroupId);

        if (chatRoom.isEmpty()) {
            throw new ResourceNotFoundException("그룹 참여에 실패했습니다.");
        }

        chatReadHistoryRepository.save(
            new ChatReadHistory(chatRoom.get(), user.get(), LocalDateTime.now()));

    }




    public void deleteInvitation(long groupInvitationId) {

        groupInvitationRepository.deleteById(groupInvitationId);
    }




}
