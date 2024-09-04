package school.kiddy.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.kiddy.crm.entity.Event;
import school.kiddy.crm.entity.Teacher;
import school.kiddy.crm.entity.Child;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByTeacher(Teacher teacher);

    List<Event> findByChildrenContaining(Child child);

    List<Event> findByTeacherAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            Teacher teacher, LocalDateTime endTime, LocalDateTime startTime);
}