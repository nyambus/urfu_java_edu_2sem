package ru.education.lab8.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.education.lab8.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
