package sharingcalender.calender.repository.qdsl.impl;


import static sharingcalender.calender.entity.QCalendar.calendar;
import static sharingcalender.calender.entity.QEvent.event;
import static sharingcalender.calender.entity.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import sharingcalender.calender.dto.calendar.request.EventModifyRequestDto;
import sharingcalender.calender.dto.calendar.response.EventInfoResponseDto;
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

    public List<EventInfoResponseDto> getAllEventsInCalendarByCalendarGroupId(long calendarGroupId,
        String username, LocalDateTime start, LocalDateTime end) {

        return jpaQueryFactory
            .select(Projections.constructor(EventInfoResponseDto.class,
                event.eventId,
                event.calendar.calendarId,
                event.user.name,
                event.title,
                event.startDate,
                event.endDate,
                event.backgroundColor,
                event.borderColor,
                event.description,
                event.writer
            ))
            .from(event)
            .where
                (
                    event.calendar.calendarId.eq
                        (
                        JPAExpressions
                            .select(calendar.calendarId)
                            .from(calendar)
                            .where(calendar.calendarGroup.calendarGroupId.eq(calendarGroupId))
                        )
                        .and(event.startDate.lt(end))
                        .and(event.endDate.goe(start))
                )
            .fetch();
    }

//    public void modifyEvent(EventModifyRequestDto eventModifyReq) {
//        jpaQueryFactory
//            .update(event)
//            .set(event.title, eventModifyReq.title())
//            .set(event.startDate, eventModifyReq.startDate())
//            .set(event.endDate, eventModifyReq.endDate())
//            .set(event.description, eventModifyReq.description())
//            .execute();
//    }

}
