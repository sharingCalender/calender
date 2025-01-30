package sharingcalender.calender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sharingcalender.calender.entity.User;
import sharingcalender.calender.repository.qdsl.UserQueryDSLRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserQueryDSLRepository {

    boolean existsByUsername(String username);


}
