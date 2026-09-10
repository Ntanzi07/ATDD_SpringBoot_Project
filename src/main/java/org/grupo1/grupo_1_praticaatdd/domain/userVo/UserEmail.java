package org.grupo1.grupo_1_praticaatdd.domain.userVo;

import jakarta.persistence.Column;

import java.util.Objects;
import java.util.regex.Pattern;

public class UserEmail {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    protected UserEmail() {
    }

    public UserEmail(String email) {
        String normalization = email == null ? null : email.trim().toLowerCase();
        if (normalization == null || normalization.isBlank())
            throw new IllegalArgumentException("E-mail is required");
        if (!EMAIL_PATTERN.matcher(normalization).matches())
            throw new IllegalArgumentException("E-mail in wrong format");

        this.email = normalization;
    }


    public String getValue() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserEmail userEmail = (UserEmail) o;
        return Objects.equals(email, userEmail.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }
}
