package sharingcalender.calender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sharingcalender.calender.entity.Calendar;

public interface CalendarRepository extends JpaRepository<Calendar,Long> {

}
