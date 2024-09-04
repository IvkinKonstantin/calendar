package school.kiddy.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import school.kiddy.crm.dto.ContactDTO;
import school.kiddy.crm.dto.TeacherDTO;
import school.kiddy.crm.dto.OperatorDTO;
import school.kiddy.crm.service.ContactService;
import school.kiddy.crm.service.TeacherService;
import school.kiddy.crm.service.OperatorService;

import java.time.format.DateTimeFormatter;

import java.util.List;

@Controller
@RequestMapping("/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private OperatorService operatorService;

    @GetMapping("/create/{clientId}")
    public String createContactForm(@PathVariable Long clientId, Model model) {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setClientId(clientId);

        List<TeacherDTO> teachers = teacherService.getAllTeachers();
        List<OperatorDTO> operators = operatorService.getAllOperators();

        model.addAttribute("contact", contactDTO);
        model.addAttribute("teachers", teachers);
        model.addAttribute("operators", operators);
        return "contact-form";
    }

    @GetMapping("/edit/{contactId}")
    public String editContactForm(@PathVariable Long contactId, Model model) {
        ContactDTO contactDTO = contactService.getContactById(contactId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String formattedDate = contactDTO.getDate().format(formatter);
        model.addAttribute("formattedDate", formattedDate);

        List<TeacherDTO> teachers = teacherService.getAllTeachers();
        List<OperatorDTO> operators = operatorService.getAllOperators();

        model.addAttribute("contact", contactDTO);
        model.addAttribute("teachers", teachers);
        model.addAttribute("operators", operators);
        return "contact-form";
    }

    @PostMapping("/save")
    public String saveContact(@ModelAttribute("contact") ContactDTO contactDTO) {
        contactService.saveContact(contactDTO);
        return "redirect:/applications/" + contactDTO.getClientId();
    }

    @PostMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable Long contactId, @RequestParam Long clientId) {
        contactService.deleteContact(contactId);
        return "redirect:/applications/" + clientId;
    }
}