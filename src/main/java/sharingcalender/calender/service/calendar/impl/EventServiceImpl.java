package sharingcalender.calender.service.calendar.impl;



import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import sharingcalender.calender.applicationevent.EvictCalendarCacheEvent;
import sharingcalender.calender.applicationevent.PutCalendarCacheEvent;
import sharingcalender.calender.dto.calendar.request.EventChangeColorRequestDto;
import sharingcalender.calender.dto.calendar.request.EventDeleteRequestDto;
import sharingcalender.calender.dto.calendar.request.EventModifyRequestDto;
import sharingcalender.calender.dto.calendar.request.EventRegisterRequestDto;
import sharingcalender.calender.dto.calendar.response.EventListResponseDto;
import sharingcalender.calender.entity.Calendar;
import sharingcalender.calender.entity.Event;
import sharingcalender.calender.entity.User;
import sharingcalender.calender.repository.EventRepository;
import sharingcalender.calender.service.ResourceValidator;
import sharingcalender.calender.service.calendar.EventService;

// refactoring
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EventServiceImpl  implements EventService{

    //TODO @Transactional 클래스 레벨에 붙이는 거 별로인 듯

    private final EventRepository eventRepository;
    private final ResourceValidator resourceValidator;
    private final CalendarEventRedisCacheService calendarEventCacheService;
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public EventListResponseDto getEventsInCalendar(long calendarGroupId, String username,
        String start, String end){

        LocalDateTime startDateTime = convertStringToLocalDateTIme(start);
        LocalDateTime endDateTime = convertStringToLocalDateTIme(end);

        return getEventsInCalendar(calendarGroupId, username, startDateTime, endDateTime);
    }

    public EventListResponseDto getEventsInCalendar(long calendarGroupId, String username,
        LocalDateTime start, LocalDateTime end) {

        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("calendar-event");
        System.out.println("[getEventsInCalendar] circuitBreaker.getState() = " + circuitBreaker.getState());

        EventListResponseDto eventList = calendarEventCacheService.getEventListInCache(calendarGroupId, start,end);

        return Objects.isNull(eventList) ? getEventsFromDbAndSaveInCache(calendarGroupId, start, end) : eventList;
    }



    private EventListResponseDto getEventsFromDbAndSaveInCache(long calendarGroupId,
        LocalDateTime start, LocalDateTime end) {

        EventListResponseDto eventList = EventListResponseDto.create(
            eventRepository.getAllEventsInCalendarByCalendarGroupId(calendarGroupId, start, end));

        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("calendar-event");
        System.out.println("[getEventsFromDbAndSaveInCache before] circuitBreaker.getState() = " + circuitBreaker.getState());

//        calendarEventCacheService.cachingInRedis(calendarGroupId, start, eventList);
        applicationEventPublisher.publishEvent(
            PutCalendarCacheEvent.create(calendarGroupId, start, eventList));

        System.out.println("[getEventsFromDbAndSaveInCache after] circuitBreaker.getState() = " + circuitBreaker.getState());

        return eventList;
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

//        calendarEventCacheService.deleteGroupEventsInCache(eventRegisterReq.calendarGroupId());

        applicationEventPublisher.publishEvent(
            EvictCalendarCacheEvent.create(eventRegisterReq.calendarGroupId()));

        return savedEvent;
    }

    @Override
    public void deleteEvent(EventDeleteRequestDto eventDeleteReq) {

        System.out.println("TransactionSynchronizationManager.isSynchronizationActive() = "
            + TransactionSynchronizationManager.isSynchronizationActive());
        System.out.println("TransactionSynchronizationManager.isActualTransactionActive() = "
            + TransactionSynchronizationManager.isActualTransactionActive());

        eventRepository.deleteById(eventDeleteReq.eventId());

//        calendarEventCacheService.deleteGroupEventsInCache(eventDeleteReq.calendarGroupId());

        applicationEventPublisher.publishEvent(
            EvictCalendarCacheEvent.create(eventDeleteReq.calendarGroupId()));
    }


    @Override
    public void modifyEvent(EventModifyRequestDto eventModifyReq) {

        Event event = resourceValidator.validateEvent(eventModifyReq.eventId());

        event.setTitle(eventModifyReq.title());
        event.setStart(eventModifyReq.start());
        event.setEnd(eventModifyReq.end());
        event.setDescription(eventModifyReq.description());

//        calendarEventCacheService.deleteGroupEventsInCache(eventModifyReq.calendarGroupId());

        applicationEventPublisher.publishEvent(
            EvictCalendarCacheEvent.create(eventModifyReq.calendarGroupId()));
    }

    @Override
    public void changeEventColor(EventChangeColorRequestDto eventChangeColorReq) {
        Event event = resourceValidator.validateEvent(eventChangeColorReq.eventId());

        event.setBackgroundColor(eventChangeColorReq.backgroundColor());
        event.setBorderColor(eventChangeColorReq.borderColor());

//        calendarEventCacheService.deleteGroupEventsInCache(eventChangeColorReq.calendarGroupId());
        applicationEventPublisher.publishEvent(
            EvictCalendarCacheEvent.create(eventChangeColorReq.calendarGroupId()));
    }



    private LocalDateTime convertStringToLocalDateTIme(String dateTime) {
        String[] dateTimeArr = dateTime.split("T")[0].split("-");

        return LocalDateTime.of(Integer.parseInt(dateTimeArr[0]), Integer.parseInt(dateTimeArr[1]),
            Integer.parseInt(dateTimeArr[2]), 0, 0, 0);
    }

}
