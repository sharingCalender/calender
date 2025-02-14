package sharingcalender.calender.repository.qdsl.impl;


import static sharingcalender.calender.entity.QCalendar.calendar;
import static sharingcalender.calender.entity.QCalendarGroup.calendarGroup;
import static sharingcalender.calender.entity.QUser.user;
import static sharingcalender.calender.entity.QUserGroup.userGroup;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import sharingcalender.calender.dto.calendar.response.CalendarGroupInfoDto;
import sharingcalender.calender.repository.qdsl.CalendarGroupQueryDSLRepository;


@RequiredArgsConstructor
public class CalendarGroupQueryDSLRepositoryImpl implements CalendarGroupQueryDSLRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<CalendarGroupInfoDto> getGroupInfoList(String username) {
        return jpaQueryFactory
            .select(Projections.constructor(CalendarGroupInfoDto.class,
                user.name,
                calendarGroup.groupName,
                calendarGroup.calendarGroupId,
                calendar.calendarId
            ))
            .from(user)

            .leftJoin(userGroup)
            .on(user.userId.eq(userGroup.user.userId))

            .leftJoin(calendarGroup)
            .on(userGroup.calendarGroup.calendarGroupId.eq(calendarGroup.calendarGroupId))

            .leftJoin(calendar)
            .on(calendar.calendarGroup.calendarGroupId.eq(calendarGroup.calendarGroupId))

            .where(user.username.eq(username))
            .fetch();

    }
}
