package school.kiddy.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import school.kiddy.crm.dto.TeacherChildLinkDTO;
import school.kiddy.crm.entity.Child;
import school.kiddy.crm.entity.Teacher;
import school.kiddy.crm.entity.TeacherChildLink;
import school.kiddy.crm.repository.ChildRepository;
import school.kiddy.crm.repository.TeacherChildLinkRepository;
import school.kiddy.crm.repository.TeacherRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherChildLinkService {

    @Autowired
    private TeacherChildLinkRepository repository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ChildRepository childRepository;

    public List<TeacherChildLinkDTO> getLinksByChildId(Long childId) {
        return repository.findByChildId(childId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TeacherChildLinkDTO getLinkById(Long id) {
        return repository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public void saveLink(TeacherChildLinkDTO dto) {
        TeacherChildLink link = convertToEntity(dto);
        repository.save(link);
    }

    public void deleteLink(Long id) {
        repository.deleteById(id);
    }

    private TeacherChildLinkDTO convertToDTO(TeacherChildLink link) {
        TeacherChildLinkDTO dto = new TeacherChildLinkDTO();
        dto.setId(link.getId());
        dto.setChildId(link.getChild().getId());
        dto.setTeacherId(link.getTeacher().getId());
        dto.setTeacherName(link.getTeacher().getFirstName() + " " + link.getTeacher().getLastName());
        dto.setBindingType(link.getBindingType());
        dto.setBindingDate(link.getBindingDate());
        dto.setUnbindingDate(link.getUnbindingDate());
        return dto;
    }

    private TeacherChildLink convertToEntity(TeacherChildLinkDTO dto) {
        TeacherChildLink link = new TeacherChildLink();

        if (dto.getId() != null) {
            link.setId(dto.getId());
        }

        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + dto.getTeacherId()));
        link.setTeacher(teacher);

        Child child = childRepository.findById(dto.getChildId())
                .orElseThrow(() -> new IllegalArgumentException("Child not found: " + dto.getChildId()));
        link.setChild(child);

        link.setBindingType(dto.getBindingType());
        link.setBindingDate(dto.getBindingDate());
        link.setUnbindingDate(dto.getUnbindingDate());

        return link;
    }
}