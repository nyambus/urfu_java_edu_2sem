package ru.education.lab8.controller;

import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import ru.education.lab8.entity.Student;
import ru.education.lab8.repository.StudentRepository;

@Controller
public class StudentController {
    private final StudentRepository students;

    public StudentController(StudentRepository students) {
        this.students = students;
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("students", students.findAll());
        return "list-students";
    }

    @GetMapping("/addStudentForm")
    public String addForm(Model model) {
        model.addAttribute("student", new Student());
        return "add-student-form";
    }

    @GetMapping("/showUpdateForm")
    public String updateForm(@RequestParam("studentId") Long id, Model model) {
        model.addAttribute("student", findStudent(id));
        return "add-student-form";
    }

    @PostMapping("/saveStudent")
    public String saveStudent(@Valid @ModelAttribute("student") Student form, BindingResult errors) {
        Student student = form.getId() == null ? new Student() : findStudent(form.getId());
        if (errors.hasErrors()) {
            return "add-student-form";
        }
        student.setName(form.getName().trim());
        student.setSurname(form.getSurname().trim());
        student.setFaculty(form.getFaculty().trim());
        student.setAge(form.getAge());
        students.save(student);
        return "redirect:/list";
    }

    @PostMapping("/deleteStudent")
    public String deleteStudent(@RequestParam("studentId") Long id) {
        students.delete(findStudent(id));
        return "redirect:/list";
    }

    private Student findStudent(Long id) {
        return students.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Студент не найден"));
    }
}
