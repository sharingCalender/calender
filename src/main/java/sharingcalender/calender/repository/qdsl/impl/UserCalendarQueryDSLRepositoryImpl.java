package sharingcalender.calender.repository.qdsl.impl;

import static sharingcalender.calender.entity.QCalendar.calendar;
import static sharingcalender.calender.entity.QUserCalendar.userCalendar;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sharingcalender.calender.repository.qdsl.UserCalendarQueryDSLRepository;

@RequiredArgsConstructor
public class UserCalendarQueryDSLRepositoryImpl implements UserCalendarQueryDSLRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void deleteUserCalendarByCalendarGroupId(long calendarGroupId) {

        jpaQueryFactory.delete(userCalendar)
            .where(userCalendar.calendar.calendarId.eq(
                JPAExpressions.select(calendar.calendarId)
                    .from(calendar)
                    .where(calendar.calendarGroup.calendarGroupId.eq(calendarGroupId))
            ));
    }
}
