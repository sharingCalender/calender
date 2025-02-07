package sharingcalender.calender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sharingcalender.calender.entity.Event;
import sharingcalender.calender.repository.qdsl.EventQueryDSLRepository;

public interface EventRepository extends JpaRepository<Event, Long>, EventQueryDSLRepository {


}
