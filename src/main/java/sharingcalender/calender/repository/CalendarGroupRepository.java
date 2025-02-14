package sharingcalender.calender.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import sharingcalender.calender.entity.CalendarGroup;
import sharingcalender.calender.repository.qdsl.CalendarGroupQueryDSLRepository;

public interface CalendarGroupRepository extends JpaRepository<CalendarGroup, Long>,
    CalendarGroupQueryDSLRepository {
}
