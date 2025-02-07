package sharingcalender.calender.service.calendar.impl;


import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class EventServiceImpl implements EventService {


    private final EventRepository eventRepository;
    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;

    public List<EventInfoResponseDto> getAllEventsInCalendar(long calendarGroupId, String username) {

        return eventRepository.getAllEventsInCalendarByCalendarGroupId(calendarGroupId, username);
    }

    public void registerEvent(EventRegisterRequestDto eventRegisterReq, String username) {

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
            eventRegisterReq.writer(), eventRegisterReq.startDate(), eventRegisterReq.endDate(),
            eventRegisterReq.backgroundColor(), eventRegisterReq.borderColor(),
            eventRegisterReq.description());

        eventRepository.save(eventEntity);
    }

    public void modifyEvent(EventModifyRequestDto eventModifyReq) {
        Optional<Event> eventEntity = eventRepository.findById(eventModifyReq.eventId());

        if (eventEntity.isEmpty()) {
            throw new ResourceNotFoundException("Can Not Find Event");
        }
        Event event = eventEntity.get();
        event.setTitle(eventModifyReq.title());
        event.setStartDate(eventModifyReq.startDate());
        event.setEndDate(eventModifyReq.endDate());
        event.setDescription(eventModifyReq.description());

    }

    public void deleteEvent(EventDeleteRequestDto eventDeleteReq) {
        eventRepository.deleteById(eventDeleteReq.eventId());

    }

}
