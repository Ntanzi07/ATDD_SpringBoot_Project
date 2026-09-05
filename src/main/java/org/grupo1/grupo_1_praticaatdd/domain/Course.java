package org.grupo1.grupo_1_praticaatdd.domain;

import jakarta.persistence.*;
import org.grupo1.grupo_1_praticaatdd.domain.courseVo.CourseDescription;
import org.grupo1.grupo_1_praticaatdd.domain.courseVo.CourseTitle;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private CourseTitle title;

    @Embedded
    private CourseDescription description;

    public Course(String title, String description) {
        this.title = new CourseTitle(title);
        this.description = new CourseDescription(description);
    }

    public Long getId() {
        return id;
    }

    public CourseTitle getTitle() {
        return title;
    }

    public CourseDescription getDescription() {
        return description;
    }

    public void changeTitle(String title) {
        this.title = new CourseTitle(title);
    }

    public void changeDescription(String description) {
        this.description = new CourseDescription(description);
    }
}
