package sharingcalender.calender.repository.qdsl.impl;

import static sharingcalender.calender.entity.QCalendarGroup.calendarGroup;
import static sharingcalender.calender.entity.QChatRoom.chatRoom;
import static sharingcalender.calender.entity.QUserGroup.userGroup;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sharingcalender.calender.dto.chat.response.ChatRoomInfoResponseDto;
import sharingcalender.calender.repository.qdsl.ChatRoomQueryDSLRepository;


@RequiredArgsConstructor
public class ChatRoomQueryDSLRepositoryImpl implements ChatRoomQueryDSLRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public void deleteChatRoomByCalendarGroupId(long calendarGroupId) {

        jpaQueryFactory
            .delete(chatRoom)
            .where(chatRoom.calendarGroup.calendarGroupId.eq(calendarGroupId))
            .execute();
    }

    public ChatRoomInfoResponseDto findChatRoomInfoByCalendarGroupIdAndUsername(
        long calendarGroupId, String username) {

        return jpaQueryFactory
            .select(

                Projections.constructor(ChatRoomInfoResponseDto.class,
                    chatRoom.chatRoomId,
                    calendarGroup.groupName,
                    userGroup.user.name
                )
            )
            .from(calendarGroup)

            .join(chatRoom)
            .on(chatRoom.calendarGroup.calendarGroupId.eq(calendarGroup.calendarGroupId))

            .join(userGroup)
            .on(userGroup.calendarGroup.calendarGroupId.eq(calendarGroup.calendarGroupId))

            .where(calendarGroup.calendarGroupId.eq(calendarGroupId)
                .and(userGroup.user.username.eq(username)))


            .fetchFirst();
    }


}
