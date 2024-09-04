package school.kiddy.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import school.kiddy.crm.dto.ChildDTO;
import school.kiddy.crm.entity.Child;
import school.kiddy.crm.entity.Client;
import school.kiddy.crm.repository.ChildRepository;
import school.kiddy.crm.repository.ClientRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChildService {

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private ClientRepository clientRepository;

    public List<ChildDTO> getChildrenByClientId(Long clientId) {
        return childRepository.findByClientId(clientId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ChildDTO getChildById(Long id) {
        return childRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    public void saveChild(ChildDTO childDTO) {
        Child child = convertToEntity(childDTO);
        childRepository.save(child);
    }

    public void deleteChild(Long id) {
        childRepository.deleteById(id);
    }

    private ChildDTO convertToDTO(Child child) {
        ChildDTO dto = new ChildDTO();
        dto.setId(child.getId());
        dto.setClientId(child.getClient().getId());
        dto.setName(child.getName());
        dto.setGender(child.getGender());
        dto.setAge(child.getAge());
        dto.setPreferredCharacter(child.getPreferredCharacter());
        return dto;
    }

    private Child convertToEntity(ChildDTO dto) {
        if (dto.getClientId() == null) {
            throw new IllegalArgumentException("Client ID must not be null");
        }

        Child child = new Child();
        child.setId(dto.getId());

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + dto.getClientId()));
        child.setClient(client);

        child.setName(dto.getName());
        child.setGender(dto.getGender());
        child.setAge(dto.getAge());
        child.setPreferredCharacter(dto.getPreferredCharacter());

        return child;
    }
}