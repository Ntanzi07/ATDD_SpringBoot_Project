package org.grupo1.grupo_1_praticaatdd.domain.courseVo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class CourseDescription {

    @Column(name = "description", length = 1000)
    private String description;

    public CourseDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CourseDescription that = (CourseDescription) o;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(description);
    }
}
