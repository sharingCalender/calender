package sharingcalender.calender.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class CalendarGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calendarGroupId;

    @Column(length = 30)
    private String groupName;

    public CalendarGroup(String groupName) {
        this.groupName = groupName;
    }
}
