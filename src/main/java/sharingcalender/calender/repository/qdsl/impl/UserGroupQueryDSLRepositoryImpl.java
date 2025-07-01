package sharingcalender.calender.repository.qdsl.impl;


import static sharingcalender.calender.entity.QUser.user;
import static sharingcalender.calender.entity.QUserGroup.userGroup;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import sharingcalender.calender.dto.user.response.UserEmailSendingInfoResponseDto;
import sharingcalender.calender.repository.qdsl.UserGroupQueryDSLRepository;

@RequiredArgsConstructor
public class UserGroupQueryDSLRepositoryImpl implements UserGroupQueryDSLRepository {

    private final JPAQueryFactory jpaQueryFactory;


    //TODO Join 으로 바꿔보자
    public List<UserEmailSendingInfoResponseDto> getUserListInGroup(long calendarGroupId){
        return jpaQueryFactory
            .select(
                Projections.constructor(UserEmailSendingInfoResponseDto.class, user.name,
                    user.email)
            )
            .from(userGroup)
            .join(userGroup.user)
            .where(userGroup.calendarGroup.calendarGroupId.eq(calendarGroupId))
            .fetch();
    }

    public void deleteByCalendarGroupId(long calendarGroupId) {

        jpaQueryFactory.delete(userGroup)
            .where(userGroup.calendarGroup.calendarGroupId.eq(calendarGroupId))
            .execute();
    }

    public void deleteUserGroupByMember(long calendarGroupId, String username) {

        jpaQueryFactory
            .delete(userGroup)
            .where(
                userGroup.calendarGroup.calendarGroupId.eq(calendarGroupId)
                    .and(userGroup.user.username.eq(username))
            )
            .execute();
    }

    public boolean userIsExist(long calendarGroupId, String username) {
        Long userGroupId = jpaQueryFactory
            .select(userGroup.userGroupId)
            .from(userGroup)
            .where(
                userGroup.calendarGroup.calendarGroupId.eq(calendarGroupId)
                    .and(userGroup.user.username.eq(username))
            ).fetchFirst();

        return Objects.nonNull(userGroupId);

    }
}
