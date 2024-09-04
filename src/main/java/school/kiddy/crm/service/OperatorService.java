package school.kiddy.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import school.kiddy.crm.dto.OperatorDTO;
import school.kiddy.crm.entity.Operator;
import school.kiddy.crm.repository.OperatorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OperatorService {

    @Autowired
    private OperatorRepository operatorRepository;

    public List<OperatorDTO> getAllOperators() {
        return operatorRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private OperatorDTO convertToDTO(Operator operator) {
        OperatorDTO dto = new OperatorDTO();
        dto.setId(operator.getId());
        dto.setFirstName(operator.getFirstName());
        dto.setLastName(operator.getLastName());
        dto.setEmail(operator.getEmail());
        dto.setPhone(operator.getPhone());
        return dto;
    }

    public Operator getOperatorById(Long id) {
        return operatorRepository.findById(id).orElse(null);
    }

    public void saveOperator(Operator operator) {
        operatorRepository.save(operator);
    }

    public void deleteOperator(Long id) {
        operatorRepository.deleteById(id);
    }
}