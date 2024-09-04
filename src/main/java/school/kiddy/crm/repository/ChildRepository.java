package school.kiddy.crm.repository;

import school.kiddy.crm.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChildRepository extends JpaRepository<Child, Long> {
    List<Child> findByClientId(Long clientId);
}