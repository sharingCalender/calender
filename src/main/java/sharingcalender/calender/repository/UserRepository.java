package sharingcalender.calender.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import sharingcalender.calender.entity.User;
import sharingcalender.calender.repository.qdsl.UserQueryDSLRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserQueryDSLRepository {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

}
