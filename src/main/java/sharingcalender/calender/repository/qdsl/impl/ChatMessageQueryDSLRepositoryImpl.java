package sharingcalender.calender.repository.qdsl.impl;

import static sharingcalender.calender.entity.QChatMessage.chatMessage;
import static sharingcalender.calender.entity.QChatReadHistory.chatReadHistory;
import static sharingcalender.calender.entity.QChatRoom.chatRoom;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import sharingcalender.calender.dto.chat.response.ChatMessageResponseDto;
import sharingcalender.calender.repository.qdsl.ChatMessageQueryDSLRepository;


@RequiredArgsConstructor
public class ChatMessageQueryDSLRepositoryImpl implements ChatMessageQueryDSLRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<ChatMessageResponseDto> getMessageListWhenScrollDown(long chatRoomId,
        long chatMessageId) {

        return jpaQueryFactory
            .select(
                Projections.constructor(ChatMessageResponseDto.class,
                    chatMessage.chatRoom.chatRoomId,
                    chatMessage.content,
                    chatMessage.user.username,
                    chatMessage.chatMessageId,
                    chatMessage.user.name

                    )
            )
            .from(chatMessage)
            .where(chatMessage.chatMessageId.gt(chatMessageId)
                    .and(chatMessage.chatRoom.chatRoomId.eq(chatRoomId))
            )
//            .orderBy(chatMessage.messageDate.asc())
            .limit(50)
            .fetch();

    }

    public List<ChatMessageResponseDto> getAllUnReadMessageListWhenNewMessageInput(long chatRoomId,
        long chatMessageId) {

        return jpaQueryFactory
            .select(
                Projections.constructor(ChatMessageResponseDto.class,
                    chatMessage.chatRoom.chatRoomId,
                    chatMessage.content,
                    chatMessage.user.username,
                    chatMessage.chatMessageId,
                    chatMessage.user.name

                )
            )
            .from(chatMessage)
            .where(chatMessage.chatMessageId.gt(chatMessageId)
                .and(chatMessage.chatRoom.chatRoomId.eq(chatRoomId))
            )
            .fetch();

    }

    public List<ChatMessageResponseDto> getMessageListWhenScrollUp(long chatRoomId,
        long chatMessageId) {

        return jpaQueryFactory
            .select(
                Projections.constructor(ChatMessageResponseDto.class,
                    chatMessage.chatRoom.chatRoomId,
                    chatMessage.content,
                    chatMessage.user.username,
                    chatMessage.chatMessageId,
                    chatMessage.user.name
                )
            )
            .from(chatMessage)
            .where(chatMessage.messageDate.lt(
                JPAExpressions
                    .select(chatMessage.messageDate)
                    .from(chatMessage)
                    .where(chatMessage.chatMessageId.eq(chatMessageId))
                )
                    .and(chatMessage.chatRoom.chatRoomId.eq(chatRoomId))
            )
            .orderBy(chatMessage.chatMessageId.desc())
            .limit(50)
            .fetch();

//        Collections.sort(messageList, Collections.reverseOrder());



    }

    public List<ChatMessageResponseDto> getBeforeHistory(long chatRoomId,String username) {
        List<ChatMessageResponseDto> beforeHistory = jpaQueryFactory
            .select(
                Projections.constructor(ChatMessageResponseDto.class,
                    chatMessage.chatRoom.chatRoomId,
                    chatMessage.content,
                    chatMessage.user.username,
                    chatMessage.chatMessageId,
                    chatMessage.user.name
                )
            )
            .from(chatMessage)
            .where(
                chatMessage.messageDate.lt(
                        JPAExpressions
                            .select(chatReadHistory.lastDate)
                            .from(chatReadHistory)
                            .where(
                                chatReadHistory.chatRoom.chatRoomId.eq(chatRoomId)
                                    .and(chatReadHistory.user.username.eq(username))
                            )
                    )
                    .and(chatMessage.chatRoom.chatRoomId.eq(chatRoomId))
            )
            .orderBy(chatMessage.chatMessageId.desc())
            .limit(20)
            .fetch();

        Collections.reverse(beforeHistory);

        return beforeHistory;


    }

    public List<ChatMessageResponseDto> getAfterHistory(long chatRoomId,String username) {
        return jpaQueryFactory
            .select(
                Projections.constructor(ChatMessageResponseDto.class,
                    chatMessage.chatRoom.chatRoomId,
                    chatMessage.content,
                    chatMessage.user.username,
                    chatMessage.chatMessageId,
                    chatMessage.user.name

                )
            )
            .from(chatMessage)
            .where(
                chatMessage.messageDate.goe(
                    JPAExpressions
                        .select(chatReadHistory.lastDate)
                        .from(chatReadHistory)
                        .where(
                            chatReadHistory.chatRoom.chatRoomId.eq(chatRoomId)
                                .and(chatReadHistory.user.username.eq(username))
                        )
                )
                .and(chatMessage.chatRoom.chatRoomId.eq(chatRoomId))
            )
            .orderBy(chatMessage.chatMessageId.asc())
            .fetch();
    }

    public void deleteAllByCalendarGroupId(long calendarGroupId) {
        jpaQueryFactory
            .delete(chatMessage)
            .where(
                chatMessage.chatRoom.chatRoomId.eq(
                    JPAExpressions
                        .select(chatRoom.chatRoomId)
                        .where(chatRoom.calendarGroup.calendarGroupId.eq(calendarGroupId))
                )
            )
            .execute();
    }
}
