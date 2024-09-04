package school.kiddy.crm.dto;

import lombok.Data;

@Data
public class TeacherDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    
    public String getName() {
        return firstName + " " + lastName;
    }
}