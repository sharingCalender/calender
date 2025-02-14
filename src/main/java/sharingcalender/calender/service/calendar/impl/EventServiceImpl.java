package sharingcalender.calender.service.calendar.impl;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
import sharingcalender.calender.exception.ResourceNotFoundException;
import sharingcalender.calender.repository.CalendarRepository;
import sharingcalender.calender.repository.EventRepository;
import sharingcalender.calender.repository.UserRepository;
import sharingcalender.calender.service.calendar.EventService;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl  implements EventService{


    private final EventRepository eventRepository;
    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;

    @Override
    public List<EventInfoResponseDto> getAllEventsInCalendar(long calendarGroupId, String username,String start, String end) {
        LocalDateTime startDateTime = convertStringToLocalDateTIme(start);
        LocalDateTime endDateTime = convertStringToLocalDateTIme(end);

        return eventRepository.getAllEventsInCalendarByCalendarGroupId(calendarGroupId, username,
            startDateTime, endDateTime);
    }

    @Override
    public long registerEvent(EventRegisterRequestDto eventRegisterReq, String username) {

        Optional<Calendar> calendarEntity = calendarRepository.findById(
            eventRegisterReq.calendarId());

        if (calendarEntity.isEmpty()) {
            throw new ResourceNotFoundException("Calendar Is Not Found");
        }

        Optional<User> userEntity = userRepository.findByUsername(username);

        if (userEntity.isEmpty()) {
            throw new ResourceNotFoundException("User Is Not Found");
        }

        Event eventEntity = new Event(calendarEntity.get(), userEntity.get(), eventRegisterReq.title(),
            eventRegisterReq.writer(), eventRegisterReq.start(), eventRegisterReq.end(),
            eventRegisterReq.backgroundColor(), eventRegisterReq.borderColor(),
            eventRegisterReq.description());

        Event savedEvent = eventRepository.save(eventEntity);

        return savedEvent.getEventId();

    }

    public void modifyEvent(EventModifyRequestDto eventModifyReq) {
        Optional<Event> eventEntity = eventRepository.findById(eventModifyReq.eventId());

        if (eventEntity.isEmpty()) {
            throw new ResourceNotFoundException("Can Not Find Event");
        }
        Event event = eventEntity.get();
        event.setTitle(eventModifyReq.title());
        event.setStart(eventModifyReq.start());
        event.setEnd(eventModifyReq.end());
        event.setDescription(eventModifyReq.description());

    }

    public void changeEventColor(EventChangeColorRequestDto eventChangeColorReq) {
        Optional<Event> eventEntity = eventRepository.findById(eventChangeColorReq.eventId());

        if (eventEntity.isEmpty()) {
            throw new ResourceNotFoundException("Can Not Find Event");
        }
        Event event = eventEntity.get();

        event.setBackgroundColor(eventChangeColorReq.backgroundColor());
        event.setBorderColor(eventChangeColorReq.borderColor());
    }

    public void deleteEvent(EventDeleteRequestDto eventDeleteReq) {
        eventRepository.deleteById(eventDeleteReq.eventId());

    }

    private LocalDateTime convertStringToLocalDateTIme(String dateTime) {
        String[] dateTimeArr = dateTime.split("T")[0].split("-");

        return LocalDateTime.of(Integer.parseInt(dateTimeArr[0]), Integer.parseInt(dateTimeArr[1]),
            Integer.parseInt(dateTimeArr[2]), 0, 0, 0);
    }

}
