package school.kiddy.calendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.kiddy.calendar.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    List<Event> findByTeacherId(UUID teacherId);

    List<Event> findByStudentIdsContains(UUID studentId);


    List<Event> findByTeacherIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            UUID teacherId, LocalDateTime endTime, LocalDateTime startTime);
}