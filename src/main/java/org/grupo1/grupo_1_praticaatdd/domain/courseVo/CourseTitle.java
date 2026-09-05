package org.grupo1.grupo_1_praticaatdd.domain.courseVo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class CourseTitle {

    @Column(name = "title", nullable = false)
    private String title;

    //CONSTRUCTORS
    protected CourseTitle() {
    }

    public CourseTitle(String title) {
        String normalization = title == null ? null : title.trim();
        if (normalization == null || normalization.isBlank())
            throw new IllegalArgumentException("Title is required");

        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CourseTitle that = (CourseTitle) o;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(title);
    }
}
