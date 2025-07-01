package sharingcalender.calender.outbox;


import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepository extends JpaRepository<Outbox,Long> {

    List<Outbox> findOutboxesByCreatedAtLessThanEqualOrderByCreatedAtAsc(LocalDateTime localDateTime, Pageable pageable);
}
