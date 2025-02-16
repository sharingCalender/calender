package sharingcalender.calender.repository.qdsl.impl;

import static sharingcalender.calender.entity.QCalendar.calendar;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sharingcalender.calender.repository.qdsl.CalendarQueryDSLRepository;

@RequiredArgsConstructor
public class CalendarQueryDSLRepositoryImpl implements CalendarQueryDSLRepository {

    private final JPAQueryFactory jpaQueryFactory;


    public void deleteByCalendarGroupId(long calendarGroupId) {
        jpaQueryFactory
            .delete(calendar)
            .where(calendar.calendarGroup.calendarGroupId.eq(calendarGroupId))
            .execute();
    }
}
