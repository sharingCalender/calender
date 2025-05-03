package sharingcalender.calender.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sharingcalender.calender.entity.Calendar;
import sharingcalender.calender.entity.CalendarGroup;
import sharingcalender.calender.entity.ChatRoom;
import sharingcalender.calender.entity.Event;
import sharingcalender.calender.entity.User;
import sharingcalender.calender.exception.ResourceNotFoundException;
import sharingcalender.calender.repository.CalendarGroupRepository;
import sharingcalender.calender.repository.CalendarRepository;
import sharingcalender.calender.repository.ChatRoomRepository;
import sharingcalender.calender.repository.EventRepository;
import sharingcalender.calender.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ResourceValidator {

    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
    private final EventRepository eventRepository;
    private final CalendarGroupRepository calendarGroupRepository;
    private final ChatRoomRepository chatRoomRepository;




    public User validateUser(String username) {
        Optional<User> userEntity = userRepository.findByUsername(username);

        if (userEntity.isEmpty()) {
            throw new ResourceNotFoundException("User Is Not Found");
        }
        return userEntity.get();
    }

    public Calendar validateCalendar(long calendarId) {
        Optional<Calendar> calendarEntity = calendarRepository.findById(calendarId);

        if (calendarEntity.isEmpty()) {
            throw new ResourceNotFoundException("Calendar Is Not Found");
        }
        return calendarEntity.get();
    }

    public Event validateEvent(long eventId) {
        Optional<Event> eventEntity = eventRepository.findById(eventId);

        if (eventEntity.isEmpty()) {
            throw new ResourceNotFoundException("Event Is Not Found");
        }
        return eventEntity.get();
    }

    public CalendarGroup validateCalendarGroup(long calendarGroupId) {
        Optional<CalendarGroup> calendarGroup = calendarGroupRepository.findById(calendarGroupId);

        if (calendarGroup.isEmpty()) {
            throw new ResourceNotFoundException("CalendarGroup Is Not Found");
        }
        return calendarGroup.get();
    }

    public ChatRoom validateChatRoom(long calendarGroupId) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findByCalendarGroup_CalendarGroupId(
            calendarGroupId);

        if (chatRoom.isEmpty()) {
            throw new ResourceNotFoundException("ChatRoom Is Not Found");
        }

        return chatRoom.get();
    }
}
