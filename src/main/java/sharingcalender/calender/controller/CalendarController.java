package sharingcalender.calender.controller;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sharingcalender.calender.dto.AuthenticatedUser;
import sharingcalender.calender.dto.calendar.request.CalendarGroupDeleteRequestDto;
import sharingcalender.calender.dto.calendar.request.CalendarGroupRegisterRequestDto;
import sharingcalender.calender.dto.calendar.request.EventChangeColorRequestDto;
import sharingcalender.calender.dto.calendar.request.EventDeleteRequestDto;
import sharingcalender.calender.dto.calendar.request.EventModifyRequestDto;
import sharingcalender.calender.dto.calendar.request.EventRegisterRequestDto;
import sharingcalender.calender.dto.calendar.response.CalendarLookUpResponseDto;
import sharingcalender.calender.dto.calendar.response.EventInfoResponseDto;
import sharingcalender.calender.dto.calendar.response.EventRegisterResponseDto;
import sharingcalender.calender.exception.BadRequestException;
import sharingcalender.calender.service.calendar.CalendarGroupService;
import sharingcalender.calender.service.calendar.EventService;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
public class CalendarController {

    private final CalendarGroupService calendarGroupService;
    private final EventService eventService;

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

    @GetMapping("/event")
    public ResponseEntity<CalendarLookUpResponseDto> getAllEventsInCalendar(
        @RequestParam("calendarGroupId") long calendarGroupId, @RequestParam("start") String start,
        @RequestParam("end") String end, @AuthenticationPrincipal AuthenticatedUser user) {

        if (calendarGroupId < 0) {
            throw new BadRequestException("Request Body Is Not Valid");
        }

        List<EventInfoResponseDto> allEventsInCalendar = eventService.getAllEventsInCalendar(
            calendarGroupId, user.username(), start, end);

        return ResponseEntity.status(HttpStatus.OK).body(
            new CalendarLookUpResponseDto(allEventsInCalendar.get(0).calendarId(),
                allEventsInCalendar));
    }

    @PostMapping("/event")
    public ResponseEntity<EventRegisterResponseDto> registerEvent(@RequestBody EventRegisterRequestDto eventRegisterReq,
        BindingResult bindingResult, @AuthenticationPrincipal AuthenticatedUser user) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Request Body Is Not Valid");
        }

        long eventId = eventService.registerEvent(eventRegisterReq, user.username());

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new EventRegisterResponseDto(eventId));
    }

    @PatchMapping("/event")
    public ResponseEntity<Void> modifyEvent(@RequestBody EventModifyRequestDto eventModifyReq,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Request Body Is Not Valid");
        }

        eventService.modifyEvent(eventModifyReq);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/event/color")
    public ResponseEntity<Void> changeEventColor(
        @RequestBody EventChangeColorRequestDto eventChangeColorReq, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Request Body Is Not Valid");
        }

        eventService.changeEventColor(eventChangeColorReq);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/event")
    public ResponseEntity<Void> deleteEvent(@RequestBody EventDeleteRequestDto eventDeleteReq,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Request Body Is Not Valid");
        }

        eventService.deleteEvent(eventDeleteReq);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
