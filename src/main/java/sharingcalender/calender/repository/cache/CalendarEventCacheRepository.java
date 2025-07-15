package sharingcalender.calender.repository.cache;


import com.fasterxml.jackson.core.type.TypeReference;
import java.time.Duration;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import sharingcalender.calender.applicationevent.EvictCalendarCacheEvent;
import sharingcalender.calender.dto.calendar.response.EventListResponseDto;
import sharingcalender.calender.util.serializer.DataSerializer;

@Repository
public class CalendarEventCacheRepository {


    private final StringRedisTemplate redisWriteTemplate;
    private final StringRedisTemplate redisReadTemplate;


    public CalendarEventCacheRepository(
        @Qualifier("redisWriteTemplate") StringRedisTemplate redisWriteTemplate,
        @Qualifier("redisReadTemplate") StringRedisTemplate redisReadTemplate) {

        this.redisWriteTemplate = redisWriteTemplate;
        this.redisReadTemplate = redisReadTemplate;
    }

    private static final String KEY_PREFIX = "calendar-group";
    private static final String DELIMITER = "::";

    public void add(String key, String date, EventListResponseDto events) {

        redisWriteTemplate.opsForHash().put(key, date, DataSerializer.serialize(events));
        redisWriteTemplate.expire(key, Duration.ofMinutes(15));

    }

    public void add(long calendarGroupId, LocalDateTime start, EventListResponseDto events) {

        add(generateKey(calendarGroupId), getDate(start), events);
    }

    public void delete(EvictCalendarCacheEvent evictCalendarCache) {
        redisWriteTemplate.delete(generateKey(evictCalendarCache.calendarGroupId()));
    }



    public EventListResponseDto readEvents(String key, String date) {

        if (isExpired(key)) {
            redisReadTemplate.expire(key, Duration.ofMinutes(15));
        }
        return DataSerializer.deserialize((String) redisReadTemplate.opsForHash().get(key, date),
            new TypeReference<EventListResponseDto>() {});
    }

    public EventListResponseDto readEvents(long calendarGroupId, LocalDateTime start) {
        String key = generateKey(calendarGroupId);
        String date = getDate(start);

        if (hasKey(key,date)) {
            return readEvents(key, date);
        }

        return null;
    }




    private String getDate(LocalDateTime start) {
        String[] date = start.toString().split("T")[0].split("-");

        String year = date[0];
        String month = date[1];
        String day = date[2];

        if (Integer.parseInt(day) != 1) {
            int monthValue = Integer.parseInt(month) + 1;
            month = monthValue > 12 ? String.valueOf(monthValue % 12) : String.valueOf(monthValue);
        }
        month = String.format("%02d", Integer.parseInt(month));

        return year + month;
    }

    private boolean isExpired(String key) {
        return redisReadTemplate.getExpire(key) < 120;
    }

    private boolean hasKey(String key,String date) {
        return redisReadTemplate.opsForHash().hasKey(key, date);
    }

    private String generateKey(long calendarGroupId) {
        return KEY_PREFIX + DELIMITER + calendarGroupId;
    }



}
