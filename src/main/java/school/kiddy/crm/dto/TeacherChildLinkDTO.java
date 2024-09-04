package school.kiddy.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherChildLinkDTO {
    private Long id;
    private Long teacherId;
    private String teacherName; // Для отображения имени учителя
    private Long childId;
    private String bindingType;
    private LocalDateTime bindingDate;
    private LocalDateTime unbindingDate;


}