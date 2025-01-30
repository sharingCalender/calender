package sharingcalender.calender.repository.qdsl.impl;




import static sharingcalender.calender.entity.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sharingcalender.calender.dto.user.response.UsernamePasswordResponseDto;
import sharingcalender.calender.exception.UserNotFoundException;
import sharingcalender.calender.repository.qdsl.UserQueryDSLRepository;

@RequiredArgsConstructor
public class UserQueryDSLRepositoryImpl implements UserQueryDSLRepository {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public UsernamePasswordResponseDto findUserInfoByUsername(String username) {

        UsernamePasswordResponseDto usernamePasswordResponseDto = jpaQueryFactory
            .select(Projections.constructor(UsernamePasswordResponseDto.class,
                user.username,
                user.password))
            .from(user)
            .where(user.username.eq(username))
            .fetchFirst();

        return usernamePasswordResponseDto;

    }
}
