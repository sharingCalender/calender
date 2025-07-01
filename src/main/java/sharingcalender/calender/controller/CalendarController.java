package sharingcalender.calender.controller;


import jakarta.validation.Valid;
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
import sharingcalender.calender.dto.calendar.request.GroupInvitationAcceptRequestDto;
import sharingcalender.calender.dto.calendar.request.GroupInvitationDelRequestDto;
import sharingcalender.calender.dto.calendar.request.GroupInvitationSaveRequestDto;
import sharingcalender.calender.dto.calendar.response.CalendarGroupListResponseDto;
import sharingcalender.calender.dto.calendar.response.CalendarLookUpResponseDto;
import sharingcalender.calender.dto.calendar.response.EventInfoResponseDto;
import sharingcalender.calender.dto.calendar.response.EventRegisterResponseDto;
import sharingcalender.calender.dto.calendar.response.GroupInvitationInfoListResponse;
import sharingcalender.calender.exception.BadRequestException;
import sharingcalender.calender.service.calendar.CalendarGroupService;
import sharingcalender.calender.service.calendar.EventService;
import sharingcalender.calender.service.calendar.GroupInvitationService;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
public class CalendarController {

    private final CalendarGroupService calendarGroupService;
    private final GroupInvitationService groupInvitationService;
    private final EventService eventService;

    @GetMapping("/group")
    public ResponseEntity<CalendarGroupListResponseDto> getGroupInfoList(
        @AuthenticationPrincipal AuthenticatedUser user) {
        CalendarGroupListResponseDto groupInfoList = calendarGroupService.getGroupInfoList(
            user.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(groupInfoList);
    }

    @PostMapping("/group")
    public ResponseEntity<Void> registerGroup(
        @RequestBody @Valid CalendarGroupRegisterRequestDto groupRegisterReq, BindingResult bindingResult,
        @AuthenticationPrincipal AuthenticatedUser user) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Request Body Is Not Valid");
        }

        calendarGroupService.registerGroup(groupRegisterReq, user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/group")
    public ResponseEntity<Void> deleteGroup(
        @RequestBody @Valid CalendarGroupDeleteRequestDto calendarGroupDeleteReq,
        BindingResult bindingResult, @AuthenticationPrincipal AuthenticatedUser user) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Request Body Is Not Valid");
        }

        calendarGroupService.deleteGroup(calendarGroupDeleteReq.calendarGroupId(),
            user.getUsername());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/event")
    public ResponseEntity<CalendarLookUpResponseDto> getAllEventsInCalendar(
        @RequestParam("calendarGroupId") long calendarGroupId, @RequestParam("start") String start,
        @RequestParam("end") String end, @AuthenticationPrincipal AuthenticatedUser user) {

        if (calendarGroupId < 0) {
            throw new BadRequestException("Request Body Is Not Valid");
        }

        List<EventInfoResponseDto> allEventsInCalendar = eventService.getEventsInCalendar(
            calendarGroupId, user.getUsername(), start, end);

        return ResponseEntity.status(HttpStatus.OK).body(
            new CalendarLookUpResponseDto(allEventsInCalendar));
    }

    @PostMapping("/event")
    public ResponseEntity<EventRegisterResponseDto> registerEvent(
        @RequestBody @Valid EventRegisterRequestDto eventRegisterReq,
        BindingResult bindingResult, @AuthenticationPrincipal AuthenticatedUser user) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Request Body Is Not Valid");
        }

        long eventId = eventService.registerEvent(eventRegisterReq, user.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new EventRegisterResponseDto(eventId));
    }

    @PatchMapping("/event")
    public ResponseEntity<Void> modifyEvent(@RequestBody @Valid EventModifyRequestDto eventModifyReq,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Request Body Is Not Valid");
        }

        eventService.modifyEvent(eventModifyReq);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/event/color")
    public ResponseEntity<Void> changeEventColor(
        @RequestBody @Valid EventChangeColorRequestDto eventChangeColorReq, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Request Body Is Not Valid");
        }

        eventService.changeEventColor(eventChangeColorReq);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/event")
    public ResponseEntity<Void> deleteEvent(@RequestBody @Valid EventDeleteRequestDto eventDeleteReq,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("Request Body Is Not Valid");
        }

        eventService.deleteEvent(eventDeleteReq);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/group/invitation")
    public ResponseEntity<GroupInvitationInfoListResponse> getInvitationList(
        @AuthenticationPrincipal AuthenticatedUser user) {
        GroupInvitationInfoListResponse invitationList = groupInvitationService.getInvitationList(
            user.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(invitationList);
    }

    @PostMapping("/group/invitation")
    public ResponseEntity<Void> saveGroupInvitation(
        @RequestBody @Valid GroupInvitationSaveRequestDto groupInvitationSaveReq,
        BindingResult bindingResult, @AuthenticationPrincipal AuthenticatedUser user) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("요청이 잘못되었습니다.");
        }

        groupInvitationService.saveGroupInvitation(groupInvitationSaveReq.username(),
            user.getUsername(), groupInvitationSaveReq.calendarGroupId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/group/invitation/accept")
    public ResponseEntity<Void> saveWhenInvitationAccepted(
        @RequestBody @Valid GroupInvitationAcceptRequestDto groupInvitationAcceptReq, BindingResult bindingResult,
        @AuthenticationPrincipal AuthenticatedUser user) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("요청이 잘못되었습니다.");
        }
        groupInvitationService.saveWhenInvitationAccepted(
            groupInvitationAcceptReq.calendarGroupId(),
            groupInvitationAcceptReq.groupInvitationId(), user.getUsername());

        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @DeleteMapping("/group/invitation")
    public ResponseEntity<Void> deleteGroupInvitation(
        @RequestBody @Valid GroupInvitationDelRequestDto groupInvitationDelReq,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("요청이 잘못되었습니다.");
        }

        groupInvitationService.deleteInvitation(groupInvitationDelReq.groupInvitationId());

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
