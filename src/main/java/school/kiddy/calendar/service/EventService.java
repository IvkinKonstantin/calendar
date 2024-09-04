package school.kiddy.calendar.service;
import org.springframework.stereotype.Service;
import school.kiddy.calendar.model.Event;
import school.kiddy.calendar.model.TeacherAvailability;
import school.kiddy.calendar.repository.EventRepository;
import school.kiddy.calendar.repository.TeacherAvailabilityRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final TeacherAvailabilityRepository availabilityRepository;

    public EventService(EventRepository eventRepository, TeacherAvailabilityRepository availabilityRepository) {
        this.eventRepository = eventRepository;
        this.availabilityRepository = availabilityRepository;
    }

    public Event createEvent(Event event) {
        // Проверка доступности временного интервала
        if (!isTimeSlotAvailable(event.getTeacherId(), event.getStartTime(), event.getEndTime())) {
            throw new IllegalArgumentException("The teacher is not available at the specified time.");
        }
        return eventRepository.save(event);
    }

    public Event rescheduleEvent(UUID eventId, Event newEventDetails) {
        // Проверка доступности временного интервала
        if (!isTimeSlotAvailable(newEventDetails.getTeacherId(), newEventDetails.getStartTime(), newEventDetails.getEndTime())) {
            throw new IllegalArgumentException("The teacher is not available at the specified time.");
        }
        cancelEvent(eventId);
        return createEvent(newEventDetails);
    }

    public void cancelEvent(UUID eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        event.setStatus("canceled");
        eventRepository.save(event);
    }

    public List<Event> getEventsByStudent(UUID studentId) {
        return eventRepository.findByStudentIdsContains(studentId);
    }

    public List<Event> getEventsByTeacher(UUID teacherId) {
        return eventRepository.findByTeacherId(teacherId);
    }

    // Проверка доступности временного интервала
    private boolean isTimeSlotAvailable(UUID teacherId, LocalDateTime startTime, LocalDateTime endTime) {
        List<TeacherAvailability> availableSlots = availabilityRepository.findByTeacherIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                teacherId, startTime, endTime);

     //   List<TeacherAvailability> availableSlots = availabilityRepository.findByTeacherId(
      //          teacherId);
        return !availableSlots.isEmpty();
    }
}