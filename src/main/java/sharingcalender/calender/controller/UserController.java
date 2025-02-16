package sharingcalender.calender.controller;

import jakarta.validation.Valid;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sharingcalender.calender.dto.AuthenticatedUser;
import sharingcalender.calender.dto.calendar.request.CalendarGroupRegisterRequestDto;
import sharingcalender.calender.dto.user.request.OAuthUserIsExistRequestDto;
import sharingcalender.calender.dto.user.request.UserRegisterRequestDto;
import sharingcalender.calender.dto.user.response.UsernamePasswordResponseDto;
import sharingcalender.calender.exception.BadRequestException;
import sharingcalender.calender.service.calendar.CalendarGroupService;
import sharingcalender.calender.service.user.UserService;


@Controller
@RequestMapping("/api/calendar/user")
@RequiredArgsConstructor
public class UserController {

    private static final String MY_CALENDAR = "MyCalendar";
    private final UserService userService;
    private final CalendarGroupService calendarGroupService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerOriginUser(
        @RequestBody @Valid UserRegisterRequestDto userRegisterRequestDto,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Request Body Is Not Valid");
        }

        userService.registerUser(userRegisterRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<UsernamePasswordResponseDto> getUserOriginInfo(
        @PathVariable("username") String username) {

        if (Objects.isNull(username)) {
            throw new BadRequestException("Bad Request");
        }

        UsernamePasswordResponseDto usernameAndPassword = userService.getUsernameAndPassword(
            username);

        return ResponseEntity.status(HttpStatus.OK).body(usernameAndPassword);

    }

    @PostMapping("/oauth/isExist")
    public ResponseEntity<Void> oauthUserIsExist(
        @RequestBody OAuthUserIsExistRequestDto oAuthUserIsExistRequestDto,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Request Body Is Not Valid");
        }

        boolean isExist = userService.oauthUserIsExist(oAuthUserIsExistRequestDto);



        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
