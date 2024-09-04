package school.kiddy.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import school.kiddy.crm.dto.ChildDTO;
import school.kiddy.crm.dto.TeacherChildLinkDTO;
import school.kiddy.crm.entity.Child;
import school.kiddy.crm.service.ChildService;
import school.kiddy.crm.service.TeacherChildLinkService;
import school.kiddy.crm.service.TeacherService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/children")
public class ChildController {

    @Autowired
    private ChildService childService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherChildLinkService teacherChildLinkService;

    @GetMapping("/{id}")
    public String viewChild(@PathVariable Long id, Model model) {
        ChildDTO child = childService.getChildById(id);
        List<TeacherChildLinkDTO> links = teacherChildLinkService.getLinksByChildId(id);

        model.addAttribute("child", child);
        model.addAttribute("links", links);
        model.addAttribute("teachers", teacherService.getAllTeachers());
        model.addAttribute("newLink", new TeacherChildLinkDTO()); // Для новой привязки

        return "child-view";
    }

    @PostMapping("/{id}/add-teacher")
    public String addTeacherToChild(@PathVariable Long id, @ModelAttribute("newLink") TeacherChildLinkDTO newLink) {
        newLink.setChildId(id);
        newLink.setBindingDate(LocalDateTime.now()); // Устанавливаем текущую дату как дату привязки
        teacherChildLinkService.saveLink(newLink);
        return "redirect:/children/" + id;
    }

    @PostMapping("/remove-link/{linkId}")
    public String removeTeacherFromChild(@PathVariable Long linkId) {
        teacherChildLinkService.deleteLink(linkId);
        return "redirect:/children";
    }
}