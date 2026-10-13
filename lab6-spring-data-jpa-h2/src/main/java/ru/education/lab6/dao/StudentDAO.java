package ru.education.lab6.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.education.lab6.entity.Student;

public interface StudentDAO extends JpaRepository<Student, Long> {
}
