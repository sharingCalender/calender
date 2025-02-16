package sharingcalender.calender.repository.qdsl.impl;


import static sharingcalender.calender.entity.QUserGroup.userGroup;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import sharingcalender.calender.repository.qdsl.UserGroupQueryDSLRepository;

@RequiredArgsConstructor
public class UserGroupQueryDSLRepositoryImpl implements UserGroupQueryDSLRepository {

    private final JPAQueryFactory jpaQueryFactory;


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
