package ru.education.lab6.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.education.lab6.dao.StudentDAO;
import ru.education.lab6.entity.Student;

@Service
public class StudentService {
    private final StudentDAO dao;

    public StudentService(StudentDAO dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Student> findById(long id) {
        return dao.findById(id);
    }

    @Transactional
    public Student create(Student student) {
        return dao.save(student);
    }

    @Transactional
    public Optional<Student> update(Student changes) {
        return dao.findById(changes.getId()).map(student -> {
            student.setName(changes.getName());
            student.setSurname(changes.getSurname());
            student.setFaculty(changes.getFaculty());
            student.setAge(changes.getAge());
            return student;
        });
    }

    @Transactional
    public boolean delete(long id) {
        return dao.findById(id).map(student -> {
            dao.delete(student);
            return true;
        }).orElse(false);
    }
}
