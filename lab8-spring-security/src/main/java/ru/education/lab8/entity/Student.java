package ru.education.lab8.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "STUDENTS")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 15)
    @Column(nullable = false, length = 15)
    private String name;

    @NotBlank
    @Size(max = 25)
    @Column(nullable = false, length = 25)
    private String surname;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String faculty;

    @NotNull
    @Min(1)
    @Max(120)
    @Column(nullable = false)
    private Integer age;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    public String getFaculty() { return faculty; }
    public void setFaculty(String faculty) { this.faculty = faculty; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
}
