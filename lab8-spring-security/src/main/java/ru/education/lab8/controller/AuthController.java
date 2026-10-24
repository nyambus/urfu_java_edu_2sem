package ru.education.lab8.controller;

import javax.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.education.lab8.dto.RegistrationForm;
import ru.education.lab8.service.UserService;
import ru.education.lab8.service.UserService.DuplicateEmailException;

@Controller
public class AuthController {
    private final UserService users;

    public AuthController(UserService users) {
        this.users = users;
    }

    @GetMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute("user") RegistrationForm form, BindingResult errors) {
        if (!errors.hasErrors()) {
            try {
                users.register(form);
            } catch (DuplicateEmailException | DataIntegrityViolationException exception) {
                errors.rejectValue("email", "duplicate", "Этот адрес электронной почты уже зарегистрирован");
            }
        }
        if (errors.hasErrors()) {
            form.setPassword(null);
            return "register";
        }
        return "redirect:/register?success";
    }

    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("users", users.listUsers());
        return "users";
    }
}
