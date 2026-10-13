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
import ru.education.lab6.entity.Subject;
import ru.education.lab6.service.SubjectService;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    private final SubjectService service;

    public SubjectController(SubjectService service) {
        this.service = service;
    }

    @GetMapping
    public List<Subject> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable long id) {
        return service.findById(id).<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(new ApiError("Дисциплина не найдена: " + id)));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Subject subject) {
        if (subject.getId() != null) {
            return ResponseEntity.badRequest().body(new ApiError("При создании дисциплины id не указывается"));
        }
        Subject saved = service.create(subject);
        return ResponseEntity.created(URI.create("/api/subjects/" + saved.getId())).body(saved);
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody Subject subject) {
        if (subject.getId() == null) {
            return ResponseEntity.badRequest().body(new ApiError("Для изменения дисциплины необходим id"));
        }
        return service.update(subject).<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(new ApiError("Дисциплина не найдена: " + subject.getId())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        if (!service.delete(id)) {
            return ResponseEntity.status(404).body(new ApiError("Дисциплина не найдена: " + id));
        }
        return ResponseEntity.ok(new ApiMessage("Дисциплина удалена: " + id));
    }
}
