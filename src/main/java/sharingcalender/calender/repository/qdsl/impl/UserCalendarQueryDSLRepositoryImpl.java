package sharingcalender.calender.repository.qdsl.impl;

import static sharingcalender.calender.entity.QCalendar.calendar;
import static sharingcalender.calender.entity.QUser.user;
import static sharingcalender.calender.entity.QUserCalendar.userCalendar;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sharingcalender.calender.entity.QCalendar;
import sharingcalender.calender.entity.QUser;
import sharingcalender.calender.entity.UserCalendar.Authority;
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
            ))
            .execute();
    }


    public void saveWhenInvitationAccepted(long calendarGroupId, long userId) {
        jpaQueryFactory.insert(userCalendar)
            .columns(userCalendar.calendar.calendarId,userCalendar.user.userId,userCalendar.authority)
            .values(
                JPAExpressions
                    .select(calendar.calendarId)
                    .from(calendar)
                    .where(calendar.calendarGroup.calendarGroupId.eq(calendarGroupId)),
                userId,
                Authority.MEMBER.name()
            ).execute();
    }

    public Authority getAuthorityForCalendar(long calendarGroupId,String username) {
        return jpaQueryFactory
            .select(userCalendar.authority)
            .from(userCalendar)
            .where(
                userCalendar.user.userId.eq(
                        JPAExpressions
                            .select(user.userId)
                            .from(user)
                            .where(user.username.eq(username))
                    )
                    .and(userCalendar.calendar.calendarGroup.calendarGroupId.eq(calendarGroupId))
            )
            .fetchFirst();
    }

    public void deleteUserCalendarByMember(long calendarGroupId, String username) {
        jpaQueryFactory
            .delete(userCalendar)
            .where(
                userCalendar.calendar.calendarGroup.calendarGroupId.eq(calendarGroupId)
                    .and(userCalendar.user.username.eq(username))

            )
            .execute();
    }
}
