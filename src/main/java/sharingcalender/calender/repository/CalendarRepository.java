package sharingcalender.calender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sharingcalender.calender.entity.Calendar;
import sharingcalender.calender.repository.qdsl.CalendarQueryDSLRepository;

public interface CalendarRepository extends JpaRepository<Calendar,Long>,
    CalendarQueryDSLRepository {



}
