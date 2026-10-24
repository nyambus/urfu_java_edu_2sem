package ru.education.lab8.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.education.lab8.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
