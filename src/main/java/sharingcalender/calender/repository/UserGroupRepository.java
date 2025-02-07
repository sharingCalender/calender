package sharingcalender.calender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sharingcalender.calender.entity.CalendarGroup;
import sharingcalender.calender.entity.UserGroup;
import sharingcalender.calender.repository.qdsl.UserGroupQueryDSLRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long>,
    UserGroupQueryDSLRepository {

}
