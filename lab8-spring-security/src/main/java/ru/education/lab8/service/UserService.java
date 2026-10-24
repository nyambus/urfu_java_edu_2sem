package ru.education.lab8.service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.education.lab8.dto.RegistrationForm;
import ru.education.lab8.entity.Role;
import ru.education.lab8.entity.User;
import ru.education.lab8.repository.RoleRepository;
import ru.education.lab8.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository users, RoleRepository roles, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.roles = roles;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void ensureDefaultRole() {
        if (!roles.findByName("ROLE_USER").isPresent()) {
            roles.save(new Role("ROLE_USER"));
        }
    }

    @Transactional
    public void register(RegistrationForm form) {
        String email = form.getEmail().trim().toLowerCase(Locale.ROOT);
        if (users.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }
        Role role = roles.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("Роль ROLE_USER не создана"));
        User user = new User();
        user.setFirstName(form.getFirstName().trim());
        user.setLastName(form.getLastName().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.getRoles().add(role);
        users.saveAndFlush(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = users.findByEmail(email.trim().toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    @Transactional(readOnly = true)
    public List<User> listUsers() {
        return users.findAll();
    }

    public static class DuplicateEmailException extends RuntimeException {
    }
}
