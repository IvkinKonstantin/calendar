package school.kiddy.crm.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import school.kiddy.crm.entity.Teacher;
import school.kiddy.crm.entity.TeacherAvailability;
import school.kiddy.crm.service.TeacherAvailabilityService;
import school.kiddy.crm.service.TeacherService;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/teacher-availability")
public class TeacherAvailabilityController {

    private final TeacherAvailabilityService availabilityService;
    private final TeacherService teacherService;

    @Autowired
    public TeacherAvailabilityController(TeacherAvailabilityService availabilityService, TeacherService teacherService) {
        this.availabilityService = availabilityService;
        this.teacherService = teacherService;
    }

    // Отображение страницы с календарем доступности
    @GetMapping("/{teacherId}")
    public String getTeacherAvailability(@PathVariable Long teacherId, Model model) {
        Teacher teacher = teacherService.getTeacherById(teacherId);
        model.addAttribute("teacher", teacher);
        return "teacher-availability-view"; // Имя шаблона для отображения календаря
    }

    // Получение списка доступности для преподавателя (для загрузки в календарь)
    @GetMapping("/{teacherId}/events")
    @ResponseBody
    public List<TeacherAvailability> getTeacherAvailabilityEvents(@PathVariable Long teacherId) {
        Teacher teacher = teacherService.getTeacherById(teacherId);
        return availabilityService.getAvailableSlots(teacher);
    }

    // Сохранение нового временного слота доступности
    @PostMapping("/save")
    @ResponseBody
    public String saveTeacherAvailability(@RequestBody TeacherAvailabilityRequest request) {
        Teacher teacher = teacherService.getTeacherById(request.getTeacherId());

        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        OffsetDateTime startDateTime = OffsetDateTime.parse(request.getStartTime(), formatter);
        OffsetDateTime endDateTime = OffsetDateTime.parse(request.getEndTime(), formatter);

        TeacherAvailability availability = new TeacherAvailability();
        availability.setTeacher(teacher);
        availability.setStartTime(startDateTime.toLocalDateTime());
        availability.setEndTime(endDateTime.toLocalDateTime());

        availabilityService.addAvailability(availability);
        return "success";
    }

    // Удаление временного слота доступности
    @PostMapping("/delete")
    @ResponseBody
    public String deleteTeacherAvailability(@RequestBody TeacherAvailabilityRequest request) {
        Teacher teacher = teacherService.getTeacherById(request.getTeacherId());

        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        OffsetDateTime startDateTime = OffsetDateTime.parse(request.getStartTime(), formatter);
        OffsetDateTime endDateTime = OffsetDateTime.parse(request.getEndTime(), formatter);

        availabilityService.deleteAvailability(teacher, startDateTime.toLocalDateTime(), endDateTime.toLocalDateTime());
        return "success";
    }

    // Вспомогательный класс для обработки JSON-запросов
    @Setter
    @Getter
    public static class TeacherAvailabilityRequest {
        // Getters и Setters
        private Long teacherId;
        private String startTime;
        private String endTime;

    }
}