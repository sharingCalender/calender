package sharingcalender.calender.repository.qdsl.impl;


import static sharingcalender.calender.entity.QCalendarGroup.calendarGroup;
import static sharingcalender.calender.entity.QGroupInvitation.groupInvitation;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import sharingcalender.calender.dto.calendar.response.GroupInvitationInfo;
import sharingcalender.calender.repository.qdsl.GroupInvitationQueryDSLRepository;

@RequiredArgsConstructor
public class GroupInvitationQueryDSLRepositoryImpl implements GroupInvitationQueryDSLRepository {

    private final JPAQueryFactory jpaQueryFactory;


    public List<GroupInvitationInfo> getGroupInvitationList(String username) {

        return jpaQueryFactory
            .select(Projections.constructor(GroupInvitationInfo.class,
                calendarGroup.calendarGroupId,
                groupInvitation.usernameFrom,
                calendarGroup.groupName,
                groupInvitation.groupInvitationId
                ))
            .from(groupInvitation)
            .leftJoin(calendarGroup).on(groupInvitation.calendarGroup.calendarGroupId.eq(calendarGroup.calendarGroupId))
            .where(
                groupInvitation.user.username.eq(username)
            )
            .fetch();

    }




}
