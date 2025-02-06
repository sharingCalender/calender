package sharingcalender.calender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sharingcalender.calender.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {


}
