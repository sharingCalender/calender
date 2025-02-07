package sharingcalender.calender.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import sharingcalender.calender.entity.CalendarGroup;

public interface CalendarGroupRepository extends JpaRepository<CalendarGroup, Long> {


}
