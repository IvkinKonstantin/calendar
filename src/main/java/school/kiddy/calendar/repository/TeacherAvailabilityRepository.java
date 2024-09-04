package school.kiddy.calendar.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import school.kiddy.calendar.model.TeacherAvailability;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TeacherAvailabilityRepository extends JpaRepository<TeacherAvailability, UUID> {

    List<TeacherAvailability> findByTeacherId(UUID teacherId);

    List<TeacherAvailability> findByTeacherIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            UUID teacherId, LocalDateTime startTime, LocalDateTime endTime);
}