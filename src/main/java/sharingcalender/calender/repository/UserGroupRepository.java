package sharingcalender.calender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sharingcalender.calender.entity.UserGroup;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {


}
