package sharingcalender.calender.service.user;


import sharingcalender.calender.dto.user.request.OAuthUserIsExistRequestDto;
import sharingcalender.calender.dto.user.request.UserRegisterRequestDto;
import sharingcalender.calender.dto.user.response.UsernamePasswordResponseDto;

public interface UserService {

    void registerUser(UserRegisterRequestDto userRegisterRequestDto);

    UsernamePasswordResponseDto getUsernameAndPassword(String username);

    boolean oauthUserIsExist(OAuthUserIsExistRequestDto oAuthUser);

}
