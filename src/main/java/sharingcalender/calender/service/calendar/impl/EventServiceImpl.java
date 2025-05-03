package sharingcalender.calender.service.calendar.impl;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sharingcalender.calender.dto.calendar.request.EventChangeColorRequestDto;
import sharingcalender.calender.dto.calendar.request.EventDeleteRequestDto;
import sharingcalender.calender.dto.calendar.request.EventModifyRequestDto;
import sharingcalender.calender.dto.calendar.request.EventRegisterRequestDto;
import sharingcalender.calender.dto.calendar.response.EventInfoResponseDto;
import sharingcalender.calender.entity.Calendar;
import sharingcalender.calender.entity.Event;
import sharingcalender.calender.entity.User;
import sharingcalender.calender.repository.CalendarEventIdRepository;
import sharingcalender.calender.repository.EventRepository;
import sharingcalender.calender.service.ResourceValidator;
import sharingcalender.calender.service.calendar.EventService;

// refactoring
@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl  implements EventService{


    private final EventRepository eventRepository;
    private final CalendarEventIdRepository eventIdRepository;
    private final ResourceValidator resourceValidator;

    @Override
    public List<EventInfoResponseDto> getEventsInCalendar(long calendarGroupId, String username,String start, String end) {
        LocalDateTime startDateTime = convertStringToLocalDateTIme(start);
        LocalDateTime endDateTime = convertStringToLocalDateTIme(end);

        return getEventsInCalendar(calendarGroupId, startDateTime, endDateTime);
    }

    private List<EventInfoResponseDto> getEventsInCalendar(long calendarGroupId, LocalDateTime start,LocalDateTime end) {
        List<Long> eventIds = eventIdRepository.readEventIds(calendarGroupId, start);

        return eventIds.isEmpty()?
            getEventsFromDbAndSaveIdsInRedis(calendarGroupId, start, end)
            : eventRepository.getAllEventsInCalendarByEventId(eventIds);

    }

    private List<EventInfoResponseDto> getEventsFromDbAndSaveIdsInRedis(long calendarGroupId,
        LocalDateTime start, LocalDateTime end) {

        List<EventInfoResponseDto> events = eventRepository.getAllEventsInCalendarByCalendarGroupId(
            calendarGroupId, start, end);

        eventIdRepository.add(calendarGroupId, start, events);
        return events;
    }

    @Override
    public long registerEvent(EventRegisterRequestDto eventRegisterReq, String username) {

        Calendar calendarEntity = resourceValidator.validateCalendar(eventRegisterReq.calendarId());

        User userEntity = resourceValidator.validateUser(username);

        Event savedEvent = registerEventInDb(eventRegisterReq, calendarEntity, userEntity);

        return savedEvent.getEventId();

    }

    private Event registerEventInDb(EventRegisterRequestDto eventRegisterReq, Calendar calendarEntity,
        User userEntity) {

        Event eventEntity = Event.create(
            calendarEntity, userEntity,
            eventRegisterReq.title(),
            eventRegisterReq.writer(), eventRegisterReq.start(), eventRegisterReq.end(),
            eventRegisterReq.backgroundColor(), eventRegisterReq.borderColor(),
            eventRegisterReq.description()
        );

        Event savedEvent = eventRepository.save(eventEntity);

        deleteGroupEventIdsInRedis(eventRegisterReq.calendarGroupId());

        return savedEvent;
    }


    @Override
    public void deleteEvent(EventDeleteRequestDto eventDeleteReq) {
        eventRepository.deleteById(eventDeleteReq.eventId());

        deleteGroupEventIdsInRedis(eventDeleteReq.calendarGroupId());
    }

    private void deleteGroupEventIdsInRedis(long eventRegisterReq) {
        eventIdRepository.delete(eventRegisterReq);
    }

    public void modifyEvent(EventModifyRequestDto eventModifyReq) {

        Event event = resourceValidator.validateEvent(eventModifyReq.eventId());

        event.setTitle(eventModifyReq.title());
        event.setStart(eventModifyReq.start());
        event.setEnd(eventModifyReq.end());
        event.setDescription(eventModifyReq.description());
    }

    @Override
    public void changeEventColor(EventChangeColorRequestDto eventChangeColorReq) {
        Event event = resourceValidator.validateEvent(eventChangeColorReq.eventId());

        event.setBackgroundColor(eventChangeColorReq.backgroundColor());
        event.setBorderColor(eventChangeColorReq.borderColor());
    }

    private LocalDateTime convertStringToLocalDateTIme(String dateTime) {
        String[] dateTimeArr = dateTime.split("T")[0].split("-");

        return LocalDateTime.of(Integer.parseInt(dateTimeArr[0]), Integer.parseInt(dateTimeArr[1]),
            Integer.parseInt(dateTimeArr[2]), 0, 0, 0);
    }

}
