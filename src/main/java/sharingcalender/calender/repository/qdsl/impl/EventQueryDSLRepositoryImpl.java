package sharingcalender.calender.repository.qdsl.impl;


import static sharingcalender.calender.entity.QCalendar.calendar;
import static sharingcalender.calender.entity.QEvent.event;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sharingcalender.calender.repository.qdsl.EventQueryDSLRepository;


@RequiredArgsConstructor
public class EventQueryDSLRepositoryImpl implements EventQueryDSLRepository {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public void deleteEventByCalendarGroupId(long calendarGroupId) {
        jpaQueryFactory.delete(event)
            .where(event.calendar.calendarId.eq(
                JPAExpressions
                    .select(calendar.calendarId)
                    .from(calendar)
                    .where(calendar.calendarGroup.calendarGroupId.eq(calendarGroupId))
            ));
    }

}
