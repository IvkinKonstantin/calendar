package school.kiddy.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.kiddy.crm.entity.Operator;

@Repository
public interface OperatorRepository extends JpaRepository<Operator, Long> {
}