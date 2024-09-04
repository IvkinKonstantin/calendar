package school.kiddy.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.kiddy.crm.entity.Teacher;
import school.kiddy.crm.entity.TeacherAvailability;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TeacherAvailabilityRepository extends JpaRepository<TeacherAvailability, UUID> {

    List<TeacherAvailability> findByTeacher(Teacher teacher);

    List<TeacherAvailability> findByTeacherAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            Teacher teacher, LocalDateTime startTime, LocalDateTime endTime);
}