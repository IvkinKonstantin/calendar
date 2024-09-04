package school.kiddy.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.kiddy.crm.entity.TeacherChildLink;

import java.util.List;

public interface TeacherChildLinkRepository extends JpaRepository<TeacherChildLink, Long> {
    List<TeacherChildLink> findByChildId(Long childId);
    List<TeacherChildLink> findByTeacherId(Long teacherId);
}