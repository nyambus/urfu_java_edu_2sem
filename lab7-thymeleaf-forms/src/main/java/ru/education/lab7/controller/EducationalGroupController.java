package ru.education.lab7.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import ru.education.lab7.entity.EducationalGroup;
import ru.education.lab7.repository.EducationalGroupRepository;

@Controller
@RequestMapping("/groups")
public class EducationalGroupController {
    private final EducationalGroupRepository groups;

    public EducationalGroupController(EducationalGroupRepository groups) {
        this.groups = groups;
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("groups", groups.findAll());
        return "list-groups";
    }

    @GetMapping("/add-form")
    public String addForm(Model model) {
        model.addAttribute("group", new EducationalGroup());
        return "add-group-form";
    }

    @GetMapping("/update-form")
    public String updateForm(@RequestParam("groupId") Long id, Model model) {
        model.addAttribute("group", findGroup(id));
        return "add-group-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("group") EducationalGroup form,
                       BindingResult errors) {
        if (errors.hasErrors()) {
            return "add-group-form";
        }
        EducationalGroup group = form.getId() == null ? new EducationalGroup() : findGroup(form.getId());
        group.setGroupNumber(form.getGroupNumber());
        group.setFaculty(form.getFaculty());
        group.setCourse(form.getCourse());
        groups.save(group);
        return "redirect:/groups/list";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("groupId") Long id) {
        groups.delete(findGroup(id));
        return "redirect:/groups/list";
    }

    private EducationalGroup findGroup(Long id) {
        return groups.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Учебная группа не найдена"));
    }
}
