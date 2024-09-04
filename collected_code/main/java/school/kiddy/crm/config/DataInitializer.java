package school.kiddy.crm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import school.kiddy.crm.entity.*;
import school.kiddy.crm.repository.*;

@Configuration
public class DataInitializer {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ChildRepository childRepository;

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            // Создание учителей
            teacherRepository.save(new Teacher(null, "John", "Doe", "john.doe@example.com", "+123456789", null));
            teacherRepository.save(new Teacher(null, "Jane", "Smith", "jane.smith@example.com", "+987654321", null));
            teacherRepository.save(new Teacher(null, "Emily", "Jones", "emily.jones@example.com", "+192837465", null));

            // Создание операторов
            operatorRepository.save(new Operator(null, "Alice", "Brown", "alice.brown@example.com", "+564738291"));
            operatorRepository.save(new Operator(null, "Bob", "Johnson", "bob.johnson@example.com", "+847362915"));

            // Создание клиента
            Client client = new Client();
            client.setName("Michael");
            client.setEmail("michael.jordan@example.com");
            client.setPhone("+1029384756");
            clientRepository.save(client);

            // Создание заявки
            Application application = new Application();
            application.setClient(client);
            application.setStatus("NEW"); // Предположим, что статус заявки — это строка
            application.setSource("Web");
            application.setDetails("Initial application details.");
            applicationRepository.save(application);

            // Создание ребенка и привязка его к клиенту
            Child child1 = new Child();
            child1.setName("Jordan Jr.");
            child1.setGender("Male");
            child1.setAge(10);
            child1.setPreferredCharacter("Spider-Man");
            child1.setClient(client); // Устанавливаем клиента у ребенка
            childRepository.save(child1);

            // Создание еще одного ребенка и привязка его к тому же клиенту
            Child child2 = new Child();
            child2.setName("Lisa Jordan");
            child2.setGender("Female");
            child2.setAge(8);
            child2.setPreferredCharacter("Wonder Woman");
            child2.setClient(client); // Устанавливаем клиента у ребенка
            childRepository.save(child2);
        };
    }
}