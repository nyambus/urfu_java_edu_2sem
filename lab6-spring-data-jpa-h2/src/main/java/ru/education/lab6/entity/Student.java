package ru.education.lab6.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "STUDENTS")
@Getter
@Setter
@NoArgsConstructor
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
    @Max(150)
    @Column(nullable = false)
    private Integer age;
}
