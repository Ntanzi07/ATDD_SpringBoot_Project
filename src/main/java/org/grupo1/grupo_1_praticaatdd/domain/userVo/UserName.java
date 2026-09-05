package org.grupo1.grupo_1_praticaatdd.domain.userVo;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class UserName {
    @Column(name = "name", nullable = false)
    private String name;

    protected UserName() {
    }

    public UserName(String name) {
        String normalization = name == null ? null : name.trim();

        if (normalization == null || normalization.isBlank())
            throw new IllegalArgumentException("name is required");

        this.name = normalization;
    }

    public String getValue() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserName userName = (UserName) o;
        return Objects.equals(name, userName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
