package org.grupo1.grupo_1_praticaatdd.domain;

import jakarta.persistence.*;
import org.grupo1.grupo_1_praticaatdd.domain.userVo.EncryptedPassword;
import org.grupo1.grupo_1_praticaatdd.domain.userVo.UserEmail;
import org.grupo1.grupo_1_praticaatdd.domain.userVo.UserName;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private UserName name;

    @Embedded
    private UserEmail email;

    @Embedded
    private EncryptedPassword password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Signature signature;


    //CONSTRUCTORS
    public User(String name, String email, String encryptedPassword) {
        this.name = new UserName(name);
        this.email = new UserEmail(email);
        this.password = new EncryptedPassword(encryptedPassword);
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

    public EncryptedPassword getPassword() {
        return password;
    }

    public Signature getSignature() {
        return signature;
    }


    public void modifyName (String name) {
        this.name = new UserName(name);
    }

    public void modifyEmail(String email) {
        this.email = new UserEmail(email);
    }

    public void changeEncryptedPassword(String encryptedPassword){
        this.password = new EncryptedPassword(encryptedPassword);
    }

    public void linkSignature(Signature signature) {
        this.signature = signature;
        if (signature != null && signature.getUser() != this) {
            signature.setUser(this);
        }
    }
}
