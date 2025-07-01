package sharingcalender.calender.service.calendar.impl;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sharingcalender.calender.dto.user.response.UserEmailSendingInfoResponseDto;
import sharingcalender.calender.repository.UserGroupRepository;
import sharingcalender.calender.service.calendar.UserGroupService;

@Service
@RequiredArgsConstructor
@Transactional
public class UserGroupServiceImpl implements UserGroupService {

    private final UserGroupRepository userGroupRepository;

    @Transactional(readOnly = true)
    public List<UserEmailSendingInfoResponseDto> getUserListInGroup(long calendarGroupId){
        return userGroupRepository.getUserListInGroup(calendarGroupId);
    }

}
