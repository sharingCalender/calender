package sharingcalender.calender.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    private String title;

    private String writer;

    @Setter
    private LocalDateTime startDate;

    @Setter
    private LocalDateTime endDate;

    @Setter
    private String backgroundColor;

    @Setter
    private String borderColor;

    @Setter
    private String description;

    public Event(Calendar calendar, User user, String title, String writer, LocalDateTime startDate,
        LocalDateTime endDate, String backgroundColor, String borderColor, String description) {
        this.calendar = calendar;
        this.user = user;
        this.title = title;
        this.writer = writer;
        this.startDate = startDate;
        this.endDate = endDate;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.description = description;
    }
}
