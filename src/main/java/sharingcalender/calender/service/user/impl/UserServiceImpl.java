package sharingcalender.calender.service.user.impl;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sharingcalender.calender.dto.AuthenticatedUser;
import sharingcalender.calender.dto.calendar.request.CalendarGroupRegisterRequestDto;
import sharingcalender.calender.dto.user.request.OAuthUserIsExistRequestDto;
import sharingcalender.calender.dto.user.request.UserRegisterRequestDto;
import sharingcalender.calender.dto.user.response.UsernamePasswordResponseDto;
import sharingcalender.calender.entity.User;
import sharingcalender.calender.exception.AlreadyExistException;
import sharingcalender.calender.exception.ResourceNotFoundException;
import sharingcalender.calender.repository.UserRepository;
import sharingcalender.calender.service.calendar.CalendarGroupService;
import sharingcalender.calender.service.user.UserService;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CalendarGroupService calendarGroupService;
    private static final String MY_CALENDAR = "MyCalendar";


    @Override
    public void registerUser(UserRegisterRequestDto userRegisterRequestDto) {

        if (userRepository.existsByUsername(userRegisterRequestDto.username())) {
            throw new AlreadyExistException("Username Already Exists");
        }

        User user = User.create(
            userRegisterRequestDto.name(), userRegisterRequestDto.email(),
            LocalDateTime.now(),
            userRegisterRequestDto.mobile(), userRegisterRequestDto.username(),
            passwordEncoder.encode(userRegisterRequestDto.password()),
            userRegisterRequestDto.provider()
        );

        userRepository.save(user);

        calendarGroupService.registerGroup(new CalendarGroupRegisterRequestDto(MY_CALENDAR),
            userRegisterRequestDto.username());
    }

    @Transactional(readOnly = true)
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
    public boolean oauthUserIsExist(OAuthUserIsExistRequestDto oAuthUser) {
        String username = oAuthUser.email();

        if (userRepository.existsByUsername(username)) {
            return true;
        }

        User user = User.create(oAuthUser.name(), oAuthUser.email(), LocalDateTime.now(),
            oAuthUser.mobile(), username, oAuthUser.password(), oAuthUser.provider());

        userRepository.save(user);

        calendarGroupService.registerGroup(new CalendarGroupRegisterRequestDto(MY_CALENDAR),
            username);

        return false;
    }

}
