package school.kiddy.crm.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class TeacherChildLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "child_id")
    private Child child;

    private String bindingType; // Тип привязки

    private LocalDateTime bindingDate; // Дата привязки

    private LocalDateTime unbindingDate; // Дата закрытия привязки
}