package sharingcalender.calender.service.user.impl;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sharingcalender.calender.dto.user.request.OAuthUserIsExistRequestDto;
import sharingcalender.calender.dto.user.request.UserRegisterRequestDto;
import sharingcalender.calender.dto.user.response.UsernamePasswordResponseDto;
import sharingcalender.calender.entity.User;
import sharingcalender.calender.exception.AlreadyExistException;
import sharingcalender.calender.exception.ResourceNotFoundException;
import sharingcalender.calender.repository.UserRepository;
import sharingcalender.calender.service.user.UserService;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(UserRegisterRequestDto userRegisterRequestDto) {

        if (userRepository.existsByUsername(userRegisterRequestDto.username())) {
            throw new AlreadyExistException("Username Already Exists");
        }

        User user = new User(userRegisterRequestDto.name(), userRegisterRequestDto.email(),
            LocalDateTime.now(),
            userRegisterRequestDto.mobile(), userRegisterRequestDto.username(),
            passwordEncoder.encode(userRegisterRequestDto.password()),
            userRegisterRequestDto.provider());

        userRepository.save(user);
    }

    @Override
    public UsernamePasswordResponseDto getUsernameAndPassword(String username) {

        UsernamePasswordResponseDto usernamePasswordResponseDto = userRepository.findUserInfoByUsername(
            username);

        if (usernamePasswordResponseDto == null) {
            throw new ResourceNotFoundException("User who has this username is Not Found ");
        }

        return usernamePasswordResponseDto;
    }

    @Override
    public void oauthUserIsExist(OAuthUserIsExistRequestDto oAuthUser) {
        String username = oAuthUser.id() + "-" + oAuthUser.provider();

        if (userRepository.existsByUsername(username)) {
            return;
        }

        User user = new User(oAuthUser.name(), oAuthUser.email(), LocalDateTime.now(),
            oAuthUser.mobile(), username, oAuthUser.password(), oAuthUser.provider());

        userRepository.save(user);

    }

}
