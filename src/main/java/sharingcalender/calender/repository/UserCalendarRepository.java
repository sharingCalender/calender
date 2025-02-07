package sharingcalender.calender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sharingcalender.calender.entity.UserCalendar;
import sharingcalender.calender.repository.qdsl.UserCalendarQueryDSLRepository;

public interface UserCalendarRepository extends JpaRepository<UserCalendar, Long>,
    UserCalendarQueryDSLRepository {



}
