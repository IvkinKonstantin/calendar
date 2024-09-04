package school.kiddy.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import school.kiddy.crm.dto.ApplicationDTO;
import school.kiddy.crm.dto.ChildDTO;
import school.kiddy.crm.service.ApplicationService;
import school.kiddy.crm.service.ChildService;
import school.kiddy.crm.service.ContactService;

import java.util.List;

@Controller
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ChildService childService;

    @Autowired
    private ContactService contactService;

    @GetMapping
    public String listApplications(Model model) {
        List<ApplicationDTO> applications = applicationService.getAllApplications();
        for (ApplicationDTO app : applications) {
            List<ChildDTO> children = childService.getChildrenByClientId(app.getClientId());
            app.setChildren(children);  // Убедитесь, что ApplicationDTO имеет поле children
        }
        model.addAttribute("apps", applications);
        return "application-list";
    }

    @GetMapping("/create")
    public String createApplicationForm(Model model) {
        model.addAttribute("app", new ApplicationDTO());
        return "application-form";
    }

    @PostMapping("/save")
    public String saveApplication(@ModelAttribute("app") ApplicationDTO applicationDTO) {
        applicationService.saveApplication(applicationDTO);
        return "redirect:/applications";
    }

    @GetMapping("/edit/{id}")
    public String editApplicationForm(@PathVariable Long id, Model model) {
        ApplicationDTO applicationDTO = applicationService.getApplicationById(id);
        model.addAttribute("app", applicationDTO);
        model.addAttribute("children", childService.getChildrenByClientId(applicationDTO.getClientId()));
        return "application-form";
    }

    @PostMapping("/update")
    public String updateApplication(@ModelAttribute("app") ApplicationDTO applicationDTO) {
        applicationService.updateApplication(applicationDTO);
        return "redirect:/applications";
    }

    @PostMapping("/delete/{id}")
    public String deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return "redirect:/applications";
    }

    @GetMapping("/{id}")
    public String viewApplication(@PathVariable Long id, Model model) {
        ApplicationDTO application = applicationService.getApplicationById(id);
        model.addAttribute("app", application);
        model.addAttribute("children", childService.getChildrenByClientId(application.getClientId()));
        model.addAttribute("contacts", contactService.getContactsByClientId(application.getClientId()));
        return "application-view";
    }

    @PostMapping("/children/save")
    public String saveChild(@ModelAttribute("child") ChildDTO childDTO) {
        childService.saveChild(childDTO);
        return "redirect:/applications/edit/" + childDTO.getClientId();
    }

    @GetMapping("/children/edit/{id}")
    public String editChildForm(@PathVariable Long id, @RequestParam(required = false) Long clientId, Model model) {
        ChildDTO childDTO;

        if (id == 0) {
            childDTO = new ChildDTO();
            childDTO.setClientId(clientId); // Устанавливаем clientId для нового ребенка
        } else {
            childDTO = childService.getChildById(id);
        }

        model.addAttribute("child", childDTO);
        return "child-form";
    }

    @PostMapping("/children/delete/{id}")
    public String deleteChild(@PathVariable Long id, @RequestParam Long clientId) {
        childService.deleteChild(id);
        return "redirect:/applications/edit/" + clientId;
    }
}