package school.kiddy.calendar.controller;

import school.kiddy.calendar.model.Event;
import school.kiddy.calendar.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventService.createEvent(event));
    }

    @PutMapping("/{id}/reschedule")
    public ResponseEntity<Event> rescheduleEvent(@PathVariable UUID id, @RequestBody Event newEventDetails) {
        return ResponseEntity.ok(eventService.rescheduleEvent(id, newEventDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelEvent(@PathVariable UUID id) {
        eventService.cancelEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Event>> getEventsByStudent(@PathVariable UUID studentId) {
        return ResponseEntity.ok(eventService.getEventsByStudent(studentId));
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Event>> getEventsByTeacher(@PathVariable UUID teacherId) {
        return ResponseEntity.ok(eventService.getEventsByTeacher(teacherId));
    }
}