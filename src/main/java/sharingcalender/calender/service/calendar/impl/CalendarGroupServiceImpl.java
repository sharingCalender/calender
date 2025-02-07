package sharingcalender.calender.service.calendar.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sharingcalender.calender.dto.AuthenticatedUser;
import sharingcalender.calender.dto.calendar.request.CalendarGroupDeleteRequestDto;
import sharingcalender.calender.dto.calendar.request.CalendarGroupRegisterRequestDto;
import sharingcalender.calender.entity.Calendar;
import sharingcalender.calender.entity.CalendarGroup;
import sharingcalender.calender.entity.User;
import sharingcalender.calender.entity.UserCalendar;
import sharingcalender.calender.entity.UserCalendar.Authority;
import sharingcalender.calender.entity.UserGroup;
import sharingcalender.calender.exception.UnAuthorizedException;
import sharingcalender.calender.repository.CalendarGroupRepository;
import sharingcalender.calender.repository.CalendarRepository;
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

    public void registerGroup(CalendarGroupRegisterRequestDto groupRegisterReq, AuthenticatedUser user) {


        CalendarGroup calendarGroup = calendarGroupRepository.save(
            new CalendarGroup(groupRegisterReq.groupName()));

        Calendar calendar = calendarRepository.save(new Calendar(calendarGroup));

        Optional<User> userEntity = userRepository.findByUsername(user.username());

        if (userEntity.isEmpty()) {
            throw new UnAuthorizedException("User Is Not Valid");
        }

        userCalendarRepository.save(
            new UserCalendar(calendar, userEntity.get(), Authority.ADMIN));

        userGroupRepository.save(new UserGroup(userEntity.get(), calendarGroup));

    }

    public void deleteGroup(long calendarGroupId) {

        userCalendarRepository.deleteUserCalendarByCalendarGroupId(calendarGroupId);

        userGroupRepository.deleteByCalendarGroupId(calendarGroupId);

        eventRepository.deleteEventByCalendarGroupId(calendarGroupId);

        // 마지막에 calendarGroup 삭제
        calendarGroupRepository.deleteById(calendarGroupId);

    }


}
