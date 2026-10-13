package ru.education.lab7.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.education.lab7.entity.EducationalGroup;

public interface EducationalGroupRepository extends JpaRepository<EducationalGroup, Long> {
}
