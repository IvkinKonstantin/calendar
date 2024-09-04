package school.kiddy.crm.repository;

import school.kiddy.crm.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}