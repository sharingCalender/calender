package sharingcalender.calender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sharingcalender.calender.entity.GroupInvitation;
import sharingcalender.calender.repository.qdsl.GroupInvitationQueryDSLRepository;

public interface GroupInvitationRepository extends JpaRepository<GroupInvitation, Long>,
    GroupInvitationQueryDSLRepository {

}
