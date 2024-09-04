package school.kiddy.crm.service;

import org.springframework.stereotype.Service;
import school.kiddy.crm.entity.Event;
import school.kiddy.crm.entity.Teacher;
import school.kiddy.crm.entity.TeacherAvailability;
import school.kiddy.crm.repository.EventRepository;
import school.kiddy.crm.repository.TeacherAvailabilityRepository;

import java.time.LocalDateTime;
import java.util.List;

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
    public List<TeacherAvailability> getAvailableSlots(Teacher teacher) {
        return availabilityRepository.findByTeacher(teacher);
    }

    // Получение доступных слотов для учителя в определенном диапазоне времени
    public List<TeacherAvailability> getAvailableSlots(Teacher teacher, LocalDateTime startTime, LocalDateTime endTime) {
        return availabilityRepository.findByTeacherAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(teacher, startTime, endTime);
    }

    // Удаление доступности для учителя в определенном диапазоне времени
    public void deleteAvailability(Teacher teacher, LocalDateTime startTime, LocalDateTime endTime) {
        // Проверка на наличие событий, пересекающихся с интервалом доступности
        List<Event> overlappingEvents = eventRepository.findByTeacherAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                teacher, endTime, startTime);

        if (!overlappingEvents.isEmpty()) {
            throw new IllegalArgumentException("Cannot delete availability because there are events scheduled in this time range.");
        }

        List<TeacherAvailability> slots = availabilityRepository.findByTeacherAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                teacher, startTime, endTime);
        availabilityRepository.deleteAll(slots);
    }
}