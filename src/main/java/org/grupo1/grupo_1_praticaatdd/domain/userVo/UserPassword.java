package org.grupo1.grupo_1_praticaatdd.domain.userVo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class UserPassword {

    private static final Pattern PATERN_PASSWORD = Pattern.compile("^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$");

    @Column(name = "password", nullable = false)
    private String password;

    protected UserPassword() {
    }

    public UserPassword(String password) {
        String normalization = password == null ? null : password.trim();

        if (normalization == null || normalization.isBlank())
            throw new IllegalArgumentException("password is required");

        this.password = normalization;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserPassword that = (UserPassword) o;
        return Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(password);
    }

}
