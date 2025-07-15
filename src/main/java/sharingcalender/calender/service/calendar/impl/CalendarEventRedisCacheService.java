package sharingcalender.calender.service.calendar.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import sharingcalender.calender.applicationevent.EvictCalendarCacheEvent;
import sharingcalender.calender.applicationevent.PutCalendarCacheEvent;
import sharingcalender.calender.dto.calendar.response.EventListResponseDto;
import sharingcalender.calender.repository.EventRepository;
import sharingcalender.calender.repository.cache.CalendarEventCacheRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalendarEventRedisCacheService {


    private final CalendarEventCacheRepository calendarEventCacheRepository;
    private final EventRepository eventRepository;

    @CircuitBreaker(name = "calendar-event", fallbackMethod = "getEventListInCacheFallback")
    public EventListResponseDto getEventListInCache(long calendarGroupId, LocalDateTime start,
        LocalDateTime end) {
        return calendarEventCacheRepository.readEvents(calendarGroupId, start);
    }

    public EventListResponseDto getEventListInCacheFallback(long calendarGroupId, LocalDateTime start,
        LocalDateTime end, Exception e) {
        log.warn("[EventServiceImpl.getEventListInCache] " ,e);

        return EventListResponseDto.create(
            eventRepository.getAllEventsInCalendarByCalendarGroupId(calendarGroupId, start, end));
    }


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @CircuitBreaker(name = "calendar-event",fallbackMethod = "cachingInRedisFallback")
    public void cachingInRedis(PutCalendarCacheEvent putCalendarCache) {
        calendarEventCacheRepository.add(putCalendarCache.calendarGroupId(),
            putCalendarCache.start(), putCalendarCache.eventList());
    }

    public void cachingInRedisFallback(PutCalendarCacheEvent putCalendarCache, Exception e) {

        log.warn("[EventServiceImpl.cachingInRedis] ", e);
    }


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @CircuitBreaker(name = "calendar-event",fallbackMethod = "evictFromCacheFallback")
    public void evictGroupEventsFromCache(EvictCalendarCacheEvent evictCalendarCache) {
        calendarEventCacheRepository.delete(evictCalendarCache);
    }

    public void evictFromCacheFallback(EvictCalendarCacheEvent evictCalendarCache,Exception e) {
        log.warn("[EventServiceImpl.deleteGroupEventsInCache] ",e);
    }
}
