package sharingcalender.calender.repository.qdsl.impl;


import static sharingcalender.calender.entity.QUserGroup.userGroup;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sharingcalender.calender.repository.qdsl.UserGroupQueryDSLRepository;

@RequiredArgsConstructor
public class UserGroupQueryDSLRepositoryImpl implements UserGroupQueryDSLRepository {

    private final JPAQueryFactory jpaQueryFactory;


    public void deleteByCalendarGroupId(long calendarGroupId) {

        jpaQueryFactory.delete(userGroup)
            .where(userGroup.userGroupId.eq(calendarGroupId))
            .execute();
    }
}
