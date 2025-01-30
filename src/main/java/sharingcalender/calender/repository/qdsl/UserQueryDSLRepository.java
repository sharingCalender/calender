package sharingcalender.calender.repository.qdsl;

import sharingcalender.calender.dto.user.response.UsernamePasswordResponseDto;

public interface UserQueryDSLRepository {

    UsernamePasswordResponseDto findUserInfoByUsername(String username);

}
