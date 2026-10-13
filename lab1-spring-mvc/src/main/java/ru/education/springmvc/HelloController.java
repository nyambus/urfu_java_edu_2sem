package ru.education.springmvc;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class HelloController {
    @GetMapping(value = "/hello", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
    public String hello(@RequestParam(name = "name", defaultValue = "World") String name) {
        return "Hello, " + name + "!";
    }
}
