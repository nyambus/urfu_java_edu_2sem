package ru.education.lab6.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.education.lab6.entity.Subject;

public interface SubjectDAO extends JpaRepository<Subject, Long> {
}
