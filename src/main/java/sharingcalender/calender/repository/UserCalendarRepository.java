package sharingcalender.calender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sharingcalender.calender.entity.UserCalendar;

public interface UserCalendarRepository extends JpaRepository<UserCalendar, Long> {


}
