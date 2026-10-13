package ru.education.lab7.entity;

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
@Table(name = "EDUCATIONAL_GROUPS")
@Getter
@Setter
@NoArgsConstructor
public class EducationalGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Column(name = "group_number", length = 20)
    private String groupNumber;

    @NotBlank
    @Size(max = 20)
    @Column(length = 20)
    private String faculty;

    @NotNull
    @Min(1)
    @Max(6)
    private Integer course;
}
