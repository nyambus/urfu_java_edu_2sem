package ru.education.lab7.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.education.lab7.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
