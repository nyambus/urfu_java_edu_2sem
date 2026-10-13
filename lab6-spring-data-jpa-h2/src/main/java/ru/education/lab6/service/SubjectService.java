package ru.education.lab6.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.education.lab6.dao.SubjectDAO;
import ru.education.lab6.entity.Subject;

@Service
public class SubjectService {
    private final SubjectDAO dao;

    public SubjectService(SubjectDAO dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public List<Subject> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Subject> findById(long id) {
        return dao.findById(id);
    }

    @Transactional
    public Subject create(Subject subject) {
        return dao.save(subject);
    }

    @Transactional
    public Optional<Subject> update(Subject changes) {
        return dao.findById(changes.getId()).map(subject -> {
            subject.setName(changes.getName());
            subject.setTeacher(changes.getTeacher());
            subject.setHours(changes.getHours());
            return subject;
        });
    }

    @Transactional
    public boolean delete(long id) {
        return dao.findById(id).map(subject -> {
            dao.delete(subject);
            return true;
        }).orElse(false);
    }
}
