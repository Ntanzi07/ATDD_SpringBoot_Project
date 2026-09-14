package org.grupo1.grupo_1_praticaatdd.domain;

import jakarta.persistence.*;
import org.grupo1.grupo_1_praticaatdd.domain.userVo.UserEncryptedPassword;
import org.grupo1.grupo_1_praticaatdd.domain.userVo.UserEmail;
import org.grupo1.grupo_1_praticaatdd.domain.userVo.UserName;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private UserName name;

    @Embedded
    private UserEmail email;

    @Embedded
    private UserEncryptedPassword password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Signature signature;

    //CONSTRUCTORS
    protected User() {
    }

    public User(String name, String email, String encryptedPassword) {
        this.name = new UserName(name);
        this.email = new UserEmail(email);
        this.password = new UserEncryptedPassword(encryptedPassword);
        this.signature = new Signature(this);
    }

    //GETTERS
    public Long getId() {
        return id;
    }

    public UserName getName() {
        return name;
    }

    public UserEmail getEmail() {
        return email;
    }

    public UserEncryptedPassword getPassword() {
        return password;
    }

    public Signature getSignature() {
        return signature;
    }

    //SETTERS
    public void modifyName(String name) {
        this.name = new UserName(name);
    }

    public void modifyEmail(String email) {
        this.email = new UserEmail(email);
    }

    public void changePassword(String encryptedPassword) {
        this.password = new UserEncryptedPassword(encryptedPassword);
    }
}
