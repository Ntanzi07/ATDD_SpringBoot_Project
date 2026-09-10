package org.grupo1.grupo_1_praticaatdd.domain.userVo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class UserEncryptedPassword {

    @Column(name = "password", nullable = false)
    private String hash;

    protected UserEncryptedPassword() {
    }

    public UserEncryptedPassword(String hash) {
        String normalization = hash == null ? null : hash.trim();

        if (normalization == null || normalization.isBlank())
            throw new IllegalArgumentException("password is required");

        this.hash = normalization;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserEncryptedPassword that = (UserEncryptedPassword) o;
        return Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(hash);
    }

}
