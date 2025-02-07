package sharingcalender.calender.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sharingcalender.calender.dto.AuthenticatedUser;
import sharingcalender.calender.dto.calendar.request.CalendarGroupDeleteRequestDto;
import sharingcalender.calender.dto.calendar.request.CalendarGroupRegisterRequestDto;
import sharingcalender.calender.exception.BadRequestException;
import sharingcalender.calender.service.calendar.CalendarGroupService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
public class CalendarController {

    private final CalendarGroupService calendarGroupService;

    @PostMapping("/group")
    public ResponseEntity<Void> registerGroup(
        @RequestBody CalendarGroupRegisterRequestDto groupRegisterReq, BindingResult bindingResult,
        @AuthenticationPrincipal AuthenticatedUser user) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Request Body Is Not Valid");
        }

        calendarGroupService.registerGroup(groupRegisterReq, user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/group")
    public ResponseEntity<Void> deleteGroup(
        @RequestBody CalendarGroupDeleteRequestDto calendarGroupDeleteReq,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Request Body Is Not Valid");
        }


        calendarGroupService.deleteGroup(calendarGroupDeleteReq.calendarGroupId());

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
