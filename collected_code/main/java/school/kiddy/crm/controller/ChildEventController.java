package school.kiddy.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import school.kiddy.crm.dto.ChildDTO;
import school.kiddy.crm.dto.EventDTO;
import school.kiddy.crm.dto.TeacherDTO;
import school.kiddy.crm.entity.Child;
import school.kiddy.crm.entity.Teacher;
import school.kiddy.crm.entity.TeacherAvailability;
import school.kiddy.crm.service.ChildService;
import school.kiddy.crm.service.EventService;
import school.kiddy.crm.service.TeacherAvailabilityService;
import school.kiddy.crm.service.TeacherService;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/children/{childId}/events")
public class ChildEventController {

    private final EventService eventService;
    private final TeacherService teacherService;
    private final ChildService childService;
    private final TeacherAvailabilityService teacherAvailabilityService;

    @Autowired
    public ChildEventController(EventService eventService, TeacherService teacherService, ChildService childService, TeacherAvailabilityService teacherAvailabilityService) {
        this.eventService = eventService;
        this.teacherService = teacherService;
        this.childService = childService;
        this.teacherAvailabilityService = teacherAvailabilityService;
    }

    @GetMapping
    public String getEventForm(@PathVariable Long childId, Model model) {
        List<TeacherDTO> teachers = teacherService.getAllTeachers();
        ChildDTO child = childService.getChildById(childId);

        model.addAttribute("teachers", teachers);
        model.addAttribute("child", child);
        return "child-event-form";
    }

    @GetMapping("/availability/{teacherId}")
    @ResponseBody
    public List<TeacherAvailability> getTeacherAvailability(@PathVariable Long teacherId) {
        // Найдите учителя по его ID
        Teacher teacher = teacherService.getTeacherById(teacherId);

        // Получите доступные слоты через TeacherAvailabilityService
        return teacherAvailabilityService.getAvailableSlots(teacher);
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<String> createEvent(@PathVariable Long childId, @RequestBody EventDTO eventDTO) {
        eventDTO.setChildrenIds(Set.of(childId));

        // Найдите учителя по его ID
        Teacher teacher = teacherService.getTeacherById(eventDTO.getTeacherId());

        eventDTO.setTeacherId(teacher.getId());

        eventService.createEventForChild(eventDTO);

        return ResponseEntity.ok("Event created successfully");
    }
}