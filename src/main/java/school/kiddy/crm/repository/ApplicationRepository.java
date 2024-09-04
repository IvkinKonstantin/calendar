package school.kiddy.crm.repository;

import school.kiddy.crm.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByClientId(Long clientId);
}