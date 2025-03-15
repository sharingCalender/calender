package sharingcalender.calender.repository.qdsl.impl;


import static sharingcalender.calender.entity.QChatReadHistory.chatReadHistory;
import static sharingcalender.calender.entity.QChatRoom.chatRoom;


import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import sharingcalender.calender.repository.qdsl.ChatReadHistoryQueryDSLRepository;


@RequiredArgsConstructor
public class ChatReadHistoryQueryDSLRepositoryImpl implements ChatReadHistoryQueryDSLRepository {

    private final JPAQueryFactory jpaQueryFactory;


    public void leaveChatRoom(long chatRoomId, String username, LocalDateTime localDateTime) {

        jpaQueryFactory
            .update(chatReadHistory)
            .set(chatReadHistory.lastDate, localDateTime)
            .where(
                chatReadHistory.chatRoom.chatRoomId.eq(chatRoomId)
                    .and(chatReadHistory.user.username.eq(username))
            )
            .execute();
    }

    public void deleteByCalendarGroupIdAndUsername(long calendarGroupId, String username) {

        jpaQueryFactory
            .delete(chatReadHistory)
            .where(

                chatReadHistory.chatRoom.chatRoomId.eq(
                    JPAExpressions
                        .select(chatRoom.chatRoomId)
                        .from(chatRoom)
                        .where(chatRoom.calendarGroup.calendarGroupId.eq(calendarGroupId))
                )
                    .and(chatReadHistory.user.username.eq(username))

            )
            .execute();
    }

    public void deleteAllByCalendarGroupId(long calendarGroupId) {
        jpaQueryFactory
            .delete(chatReadHistory)
            .where(
                chatReadHistory.chatRoom.chatRoomId.eq(
                    JPAExpressions
                        .select(chatRoom.chatRoomId)
                        .where(chatRoom.calendarGroup.calendarGroupId.eq(calendarGroupId))
                )
            )
            .execute();
    }
}
