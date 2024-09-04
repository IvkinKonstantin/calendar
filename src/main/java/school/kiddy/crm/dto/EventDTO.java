package school.kiddy.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {

    private Long id;
    private Long teacherId;
    private Set<Long> childrenIds;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private String eventType;
    private String status;
    private String createdByType;
    private Long createdById;
}