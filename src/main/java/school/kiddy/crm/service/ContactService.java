package school.kiddy.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import school.kiddy.crm.dto.ContactDTO;
import school.kiddy.crm.entity.Client;
import school.kiddy.crm.entity.Contact;
import school.kiddy.crm.entity.Operator;
import school.kiddy.crm.entity.Teacher;
import school.kiddy.crm.entity.CreatorType;
import school.kiddy.crm.repository.ClientRepository;
import school.kiddy.crm.repository.ContactRepository;
import school.kiddy.crm.repository.OperatorRepository;
import school.kiddy.crm.repository.TeacherRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    public List<ContactDTO> getContactsByClientId(Long clientId) {
        return contactRepository.findByClientId(clientId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ContactDTO getContactById(Long id) {
        return contactRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public void saveContact(ContactDTO contactDTO) {
        Contact contact = convertToEntity(contactDTO);
        contactRepository.save(contact);
    }

    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }

    private ContactDTO convertToDTO(Contact contact) {
        return new ContactDTO(
                contact.getId(),
                contact.getClient().getId(),
                contact.getOperator() != null ? contact.getOperator().getId() : null,
                contact.getTeacher() != null ? contact.getTeacher().getId() : null,
                contact.getCreatorType(),
                contact.getType(),
                contact.getStatus(),
                contact.getDate(),
                contact.getDescription(),
                contact.isPublic()
        );
    }

    private Contact convertToEntity(ContactDTO dto) {
        Contact contact = new Contact();
        contact.setId(dto.getId());

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + dto.getClientId()));
        contact.setClient(client);

        // Обрабатываем тип создателя контакта
        CreatorType creatorType = dto.getCreatorType();
        contact.setCreatorType(creatorType);

        switch (creatorType) {
            case OPERATOR:
                if (dto.getOperatorId() != null) {
                    Operator operator = operatorRepository.findById(dto.getOperatorId())
                            .orElseThrow(() -> new IllegalArgumentException("Operator not found: " + dto.getOperatorId()));
                    contact.setOperator(operator);
                    contact.setTeacher(null); // Обнуляем поле teacher
                } else {
                    throw new IllegalArgumentException("Operator ID is required for creator type OPERATOR");
                }
                break;
            case TEACHER:
                if (dto.getTeacherId() != null) {
                    Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                            .orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + dto.getTeacherId()));
                    contact.setTeacher(teacher);
                    contact.setOperator(null); // Обнуляем поле operator
                } else {
                    throw new IllegalArgumentException("Teacher ID is required for creator type TEACHER");
                }
                break;
            case CLIENT:
                // Если тип создателя — клиент, оба поля должны быть null
                contact.setOperator(null);
                contact.setTeacher(null);
                break;
            default:
                throw new IllegalArgumentException("Unsupported creator type: " + creatorType);
        }

        contact.setType(dto.getType());
        contact.setStatus(dto.getStatus());
        contact.setDate(dto.getDate());
        contact.setDescription(dto.getDescription());
        contact.setPublic(dto.isPublic());

        return contact;
    }
}