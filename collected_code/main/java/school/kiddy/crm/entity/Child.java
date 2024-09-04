package school.kiddy.crm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String gender;
    private int age;
    private String preferredCharacter;

    @ManyToOne
    private Client client; // Связь с клиентом

    @OneToMany(mappedBy = "child")
    private List<TeacherChildLink> teacherChildLinks;

}