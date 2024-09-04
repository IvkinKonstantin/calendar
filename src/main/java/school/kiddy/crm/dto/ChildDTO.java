package school.kiddy.crm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChildDTO {

    private Long id;
    private Long clientId;
    private String name;
    private String gender;
    private int age;
    private String preferredCharacter;
}