package school.kiddy.calendar.model;
import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Event {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID teacherId;

    @ElementCollection
    private List<UUID> studentIds;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false, length = 50)
    private String eventType;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(nullable = false, length = 50)
    private String createdByType;

    @Column(nullable = false)
    private UUID createdById;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}