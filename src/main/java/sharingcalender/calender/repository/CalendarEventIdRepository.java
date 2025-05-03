package sharingcalender.calender.repository;


import com.fasterxml.jackson.core.type.TypeReference;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import sharingcalender.calender.dto.calendar.response.EventInfoResponseDto;
import sharingcalender.calender.util.serializer.DataSerializer;

@Repository
public class CalendarEventIdRepository {


    private final StringRedisTemplate redisWriteTemplate;
    private final StringRedisTemplate redisReadTemplate;


    public CalendarEventIdRepository(
        @Qualifier("redisWriteTemplate") StringRedisTemplate redisWriteTemplate,
        @Qualifier("redisReadTemplate") StringRedisTemplate redisReadTemplate) {

        this.redisWriteTemplate = redisWriteTemplate;
        this.redisReadTemplate = redisReadTemplate;
    }

    private static final String KEY_PREFIX = "calendar-group";
    private static final String DELIMITER = "::";

    public void add(String key, String date, List<Long> eventIds) {

        if (eventIds.isEmpty()) {
            return;
        }

        redisWriteTemplate.opsForHash().put(key, date, DataSerializer.serialize(eventIds));
        redisWriteTemplate.expire(key, Duration.ofHours(1L));

    }

    public void add(long calendarGroupId, LocalDateTime start, List<EventInfoResponseDto> events) {

        List<Long> eventIds = events.stream().map(EventInfoResponseDto::id).toList();

        if (eventIds.isEmpty()) {
            return;
        }
        add(generateKey(calendarGroupId), getDate(start), eventIds);
    }

    public void delete(long calendarGroupId) {
        redisWriteTemplate.delete(generateKey(calendarGroupId));
    }



    public List<Long> readEventIds(String key, String date) {

        if (isExpired(key)) {
            redisReadTemplate.expire(key, Duration.ofHours(1L));
        }
        return DataSerializer.deserialize((String) redisReadTemplate.opsForHash().get(key, date),
            new TypeReference<List<Long>>() {});
    }

    public List<Long> readEventIds(long calendarGroupId, LocalDateTime start) {
        String key = generateKey(calendarGroupId);
        String date = getDate(start);

        if (hasKey(key,date)) {
            return readEventIds(key, date);
        }

        return List.of();
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
