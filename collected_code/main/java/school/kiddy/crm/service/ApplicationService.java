package school.kiddy.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import school.kiddy.crm.dto.ApplicationDTO;
import school.kiddy.crm.entity.Application;
import school.kiddy.crm.entity.Client;
import school.kiddy.crm.repository.ApplicationRepository;
import school.kiddy.crm.repository.ClientRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ClientRepository clientRepository;

    public List<ApplicationDTO> getAllApplications() {
        return applicationRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ApplicationDTO getApplicationById(Long id) {
        return applicationRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    public void saveApplication(ApplicationDTO applicationDTO) {
        Application application = convertToEntity(applicationDTO);
        applicationRepository.save(application);
    }

    public void updateApplication(ApplicationDTO applicationDTO) {
        if (applicationRepository.existsById(applicationDTO.getId())) {
            Application application = convertToEntity(applicationDTO);
            applicationRepository.save(application);
        }
    }

    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }

    private ApplicationDTO convertToDTO(Application application) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(application.getId());
        dto.setClientId(application.getClient().getId());
        dto.setClientName(application.getClient().getName());
        dto.setClientEmail(application.getClient().getEmail());
        dto.setClientPhone(application.getClient().getPhone());
        dto.setStatus(application.getStatus());
        dto.setSource(application.getSource());
        dto.setDetails(application.getDetails());
        dto.setDate(application.getDate());
        return dto;
    }

    private Application convertToEntity(ApplicationDTO dto) {
        Application application = new Application();
        application.setId(dto.getId());

        if (application.getId() == null) { // Новая заявка
            application.setDate(LocalDateTime.now()); // Устанавливаем текущую дату
        }


        Client client;
        if (dto.getClientId() != null) {
            client = clientRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new IllegalArgumentException("Client not found: " + dto.getClientId()));
            // Обновляем данные клиента
            client.setName(dto.getClientName());
            client.setEmail(dto.getClientEmail());
            client.setPhone(dto.getClientPhone());
        } else {
            client = new Client();
            client.setName(dto.getClientName());
            client.setEmail(dto.getClientEmail());
            client.setPhone(dto.getClientPhone());
        }

        client = clientRepository.save(client); // Сохраняем клиента (либо обновленного, либо нового)

        application.setClient(client);
        application.setStatus(dto.getStatus());
        application.setSource(dto.getSource());
        application.setDetails(dto.getDetails());

        return application;
    }
}