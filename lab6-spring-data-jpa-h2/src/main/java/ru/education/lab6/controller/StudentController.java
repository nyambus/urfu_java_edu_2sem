package ru.education.lab6.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.education.lab6.entity.Student;
import ru.education.lab6.service.StudentService;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Student> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable long id) {
        return service.findById(id).<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(new ApiError("Студент не найден: " + id)));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Student student) {
        if (student.getId() != null) {
            return ResponseEntity.badRequest().body(new ApiError("При создании студента id не указывается"));
        }
        Student saved = service.create(student);
        return ResponseEntity.created(URI.create("/api/students/" + saved.getId())).body(saved);
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody Student student) {
        if (student.getId() == null) {
            return ResponseEntity.badRequest().body(new ApiError("Для изменения студента необходим id"));
        }
        return service.update(student).<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(new ApiError("Студент не найден: " + student.getId())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        if (!service.delete(id)) {
            return ResponseEntity.status(404).body(new ApiError("Студент не найден: " + id));
        }
        return ResponseEntity.ok(new ApiMessage("Студент удалён: " + id));
    }
}
