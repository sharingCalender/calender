package sharingcalender.calender.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import sharingcalender.calender.entity.CalendarGroup;

public interface CalendarGroupRepository extends JpaRepository<CalendarGroup, Long> {

    Optional<CalendarGroup> findByGroupName(String groupName);
}
