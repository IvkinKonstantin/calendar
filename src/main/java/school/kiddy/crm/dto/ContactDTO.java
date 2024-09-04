package school.kiddy.crm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import school.kiddy.crm.entity.CreatorType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO {

    private Long id;
    private Long clientId;
    private Long operatorId;
    private Long teacherId;
    private CreatorType creatorType;
    private String type;
    private String status;
    private LocalDateTime date;
    private String description;
    private boolean isPublic;

    public boolean isPublic() {
        return isPublic;
    }


    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }



    public boolean getIsPublic() {
        return isPublic;
    }


    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
}