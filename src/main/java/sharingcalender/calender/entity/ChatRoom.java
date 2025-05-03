package sharingcalender.calender.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class ChatRoom {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_group_id")
    private CalendarGroup calendarGroup;

    private ChatRoom(CalendarGroup calendarGroup) {
        this.calendarGroup = calendarGroup;
    }

    public static ChatRoom create(CalendarGroup calendarGroup) {
        return new ChatRoom(calendarGroup);
    }

}
