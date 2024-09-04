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
public class TeacherAvailabilityService {

    private final TeacherAvailabilityRepository availabilityRepository;
    private final EventRepository eventRepository;

    public TeacherAvailabilityService(TeacherAvailabilityRepository availabilityRepository, EventRepository eventRepository) {
        this.availabilityRepository = availabilityRepository;
        this.eventRepository = eventRepository;
    }

    // Добавление доступности для учителя
    public TeacherAvailability addAvailability(TeacherAvailability availability) {
        return availabilityRepository.save(availability);
    }

    // Получение всех доступных слотов для учителя
    public List<TeacherAvailability> getAvailableSlots(UUID teacherId) {
        return availabilityRepository.findByTeacherId(teacherId);
    }

    // Получение доступных слотов для учителя в определенном диапазоне времени
    public List<TeacherAvailability> getAvailableSlots(UUID teacherId, LocalDateTime startTime, LocalDateTime endTime) {
        return availabilityRepository.findByTeacherIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(teacherId, startTime, endTime);
    }

    // Удаление доступности для учителя в определенном диапазоне времени
    public void deleteAvailability(UUID teacherId, LocalDateTime startTime, LocalDateTime endTime) {
        // Проверка на наличие событий, пересекающихся с интервалом доступности
        List<Event> overlappingEvents = eventRepository.findByTeacherIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                teacherId, endTime, startTime);

        if (!overlappingEvents.isEmpty()) {
            throw new IllegalArgumentException("Cannot delete availability because there are events scheduled in this time range.");
        }

        List<TeacherAvailability> slots = availabilityRepository.findByTeacherIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                teacherId, startTime, endTime);
        availabilityRepository.deleteAll(slots);
    }
}