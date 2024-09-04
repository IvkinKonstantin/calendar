package school.kiddy.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import school.kiddy.crm.dto.EventDTO;
import school.kiddy.crm.entity.Child;
import school.kiddy.crm.entity.Event;
import school.kiddy.crm.entity.Teacher;
import school.kiddy.crm.repository.ChildRepository;
import school.kiddy.crm.repository.EventRepository;
import school.kiddy.crm.repository.TeacherRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ChildRepository childRepository;

    public EventDTO createEventForChild(EventDTO eventDTO) {
        Teacher teacher = teacherRepository.findById(eventDTO.getTeacherId()).orElseThrow();
        Set<Child> children = new HashSet<>();
        for (Long childId : eventDTO.getChildrenIds()) {
            children.add(childRepository.findById(childId).orElseThrow());
        }

        Event event = new Event();
        event.setTeacher(teacher);
        event.setChildren(children);
        event.setStartTime(eventDTO.getStartTime().toLocalDateTime());
        event.setEndTime(eventDTO.getEndTime().toLocalDateTime());
        event.setEventType(eventDTO.getEventType());
        event.setStatus(eventDTO.getStatus());
        event.setCreatedByType(eventDTO.getCreatedByType());
        event.setCreatedById(eventDTO.getCreatedById());

        Event savedEvent = eventRepository.save(event);
        return convertToDTO(savedEvent);
    }

    private EventDTO convertToDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setTeacherId(event.getTeacher().getId());
        dto.setChildrenIds(event.getChildren().stream().map(Child::getId).collect(Collectors.toSet()));
        dto.setStartTime(event.getStartTime().atZone(ZoneId.systemDefault()));
        dto.setEndTime(event.getEndTime().atZone(ZoneId.systemDefault()));
        dto.setEventType(event.getEventType());
        dto.setStatus(event.getStatus());
        dto.setCreatedByType(event.getCreatedByType());
        dto.setCreatedById(event.getCreatedById());
        return dto;
    }
}