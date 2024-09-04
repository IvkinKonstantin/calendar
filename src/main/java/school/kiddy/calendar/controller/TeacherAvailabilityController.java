package school.kiddy.calendar.controller;

import school.kiddy.calendar.model.TeacherAvailability;
import school.kiddy.calendar.service.TeacherAvailabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/availability")
public class TeacherAvailabilityController {

    private final TeacherAvailabilityService availabilityService;

    public TeacherAvailabilityController(TeacherAvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @PostMapping
    public ResponseEntity<TeacherAvailability> addAvailability(@RequestBody TeacherAvailability availability) {
        return ResponseEntity.ok(availabilityService.addAvailability(availability));
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<TeacherAvailability>> getAvailableSlots(@PathVariable UUID teacherId) {
        return ResponseEntity.ok(availabilityService.getAvailableSlots(teacherId));
    }

    @GetMapping("/teacher/{teacherId}/range")
    public ResponseEntity<List<TeacherAvailability>> getAvailableSlotsInRange(@PathVariable UUID teacherId,
                                                                              @RequestParam LocalDateTime startTime,
                                                                              @RequestParam LocalDateTime endTime) {
        return ResponseEntity.ok(availabilityService.getAvailableSlots(teacherId, startTime, endTime));
    }

    @DeleteMapping("/teacher/{teacherId}/range")
    public ResponseEntity<Void> deleteAvailability(@PathVariable UUID teacherId,
                                                   @RequestParam LocalDateTime startTime,
                                                   @RequestParam LocalDateTime endTime) {
        availabilityService.deleteAvailability(teacherId, startTime, endTime);
        return ResponseEntity.noContent().build();
    }
}